<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部物品 (已通过)" name="public"></el-tab-pane>

      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="audit"
        v-if="checkRole(['teacher', 'purchase_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物品名称" prop="itemName">
        <el-input
          v-model="queryParams.itemName"
          placeholder="请输入物品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采购人员" prop="purchaser">
        <el-input
          v-model="queryParams.purchaser"
          placeholder="请输入采购人员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:purchase:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:purchase:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="purchaseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="物品名称" align="center" prop="itemName" />
      <el-table-column label="类型" align="center" prop="itemType" />
      <el-table-column label="型号" align="center" prop="model" />
      <el-table-column label="采购人员" align="center" prop="purchaser" />
      <el-table-column label="采购公司" align="center" prop="companyName" />
      <el-table-column label="数量" align="center" prop="quantity" />
      <el-table-column label="价格" align="center" prop="price" />
      <el-table-column label="用途" align="center" prop="usagePurpose" :show-overflow-tooltip="true"/>

      <el-table-column label="发票" align="center" prop="invoiceUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.invoiceUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>

      <el-table-column label="审核状态" align="center" prop="auditStatus" width="120">
        <template slot-scope="scope">
          <div style="display: flex; align-items: center; justify-content: center;">

            <dict-tag :options="dict.type.sys_audit_status" :value="scope.row.auditStatus"/>

            <span v-if="scope.row.auditStatus == '0'"
                  style="display:inline-block; width:8px; height:8px; border-radius:50%; background-color: #e6a23c; margin-left: 6px;"
                  title="待审核">
            </span>

            <span v-if="scope.row.auditStatus == '1'"
                  style="display:inline-block; width:8px; height:8px; border-radius:50%; background-color: #67c23a; margin-left: 6px;"
                  title="已通过">
            </span>

            <el-tooltip v-if="scope.row.auditStatus == '2'" :content="scope.row.auditReason" placement="top">
              <i class="el-icon-warning" style="color: #f56c6c; cursor: pointer; margin-left: 6px; font-size: 14px;"></i>
            </el-tooltip>

          </div>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        v-if="activeTab !== 'public'"
      >
        <template slot-scope="scope">

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:purchase:edit']"
            v-if="checkRole(['purchase_manager', 'admin']) || (checkRole(['student']) && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:purchase:remove']"
            v-if="checkRole(['purchase_manager', 'admin']) || (checkRole(['student']) && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:purchase:audit']"
            v-if="checkRole(['teacher', 'admin']) && scope.row.auditStatus == '0'"
          >审核</el-button>

        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="物品名称" prop="itemName">
              <el-input v-model="form.itemName" placeholder="请输入物品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型" prop="itemType">
              <el-input v-model="form.itemType" placeholder="请输入类型" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购人员" prop="purchaser">
              <el-input v-model="form.purchaser" placeholder="请输入采购人员" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="采购公司" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入采购公司" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="数量" prop="quantity">
              <el-input v-model="form.quantity" type="number" placeholder="请输入数量" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="价格" prop="price">
              <el-input v-model="form.price" type="number" placeholder="请输入价格" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="用途" prop="usagePurpose">
          <el-input v-model="form.usagePurpose" type="textarea" placeholder="请输入用途" />
        </el-form-item>

        <el-form-item label="发票上传" prop="invoiceUrl">
          <image-upload v-model="form.invoiceUrl"/>
        </el-form-item>

        <el-form-item label="驳回原因" v-if="form.auditStatus == '2'">
          <el-input v-model="form.auditReason" type="textarea" disabled />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核采购信息" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="物品名称">
          <el-input v-model="auditForm.itemName" disabled />
        </el-form-item>
        <el-form-item label="审核结果" prop="auditStatus">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditReason" v-if="auditForm.auditStatus == '2'">
          <el-input type="textarea" v-model="auditForm.auditReason" placeholder="请输入驳回原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">提交审核</el-button>
        <el-button @click="auditOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 注意：auditPurchase 是新增的，必须确保 api/system/purchase.js 里有这个方法
import { listPurchase, getPurchase, delPurchase, addPurchase, updatePurchase, auditPurchase } from "@/api/system/purchase"
import { checkRole } from "@/utils/permission";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Purchase",
  dicts: ['sys_audit_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 购买支出信息表格数据
      purchaseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      // --- 新增变量 ---
      auditOpen: false,
      activeTab: 'public',
      currentUser: {},
      auditForm: {},

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        itemName: null,
        purchaser: null,
        // 关键：初始化 params
        params: {
          queryMode: 'public'
        }
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        itemName: [
          { required: true, message: "物品名称不能为空", trigger: "blur" }
        ],
        quantity: [
          { required: true, message: "数量不能为空", trigger: "blur" }
        ],
        price: [
          { required: true, message: "价格不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getCurrentUser();
    this.getList();
  },
  methods: {
    checkRole,

    /** 获取当前登录用户 */
    getCurrentUser() {
      getUserProfile().then(response => {
        this.currentUser = response.data.user || response.data;
      });
    },

    /** 查询购买支出信息列表 */
    getList() {
      this.loading = true;
      if (!this.queryParams.params) {
        this.queryParams.params = {};
      }
      this.queryParams.params['queryMode'] = this.activeTab;

      // 如果是公共Tab，排除前端可能存在的状态干扰
      if (this.activeTab === 'public') {
        this.queryParams.auditStatus = undefined;
      }

      listPurchase(this.queryParams).then(response => {
        this.purchaseList = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    /** Tab 切换 */
    handleTabClick(tab, event) {
      this.queryParams.pageNum = 1;
      this.resetForm("queryForm");
      this.getList();
    },

    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        itemName: null,
        itemType: null,
        model: null,
        purchaser: null,
        companyName: null,
        quantity: null,
        price: null,
        usagePurpose: null,
        invoiceUrl: null,
        auditStatus: null,
        auditReason: null,
        remark: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加购买支出信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPurchase(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改购买支出信息"
      })
    },
    /** 打开审核弹窗 */
    handleAuditOpen(row) {
      this.auditForm = {
        id: row.id,
        itemName: row.itemName, // 展示用
        auditStatus: '1',
        auditReason: ''
      };
      this.auditOpen = true;
    },
    /** 提交审核 */
    submitAudit() {
      if (this.auditForm.auditStatus === '2' && !this.auditForm.auditReason) {
        this.$modal.msgError("驳回时必须填写审核意见");
        return;
      }
      auditPurchase(this.auditForm).then(response => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePurchase(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPurchase(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(function() {
        return delPurchase(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/purchase/export', {
        ...this.queryParams
      }, `purchase_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
