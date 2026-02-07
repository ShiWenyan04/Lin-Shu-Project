<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部设备 (已通过)" name="public"></el-tab-pane>
      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher'])"></el-tab-pane>
      <el-tab-pane
        label="审核管理"
        name="audit"
        v-if="checkRole(['teacher', 'fildsurvey_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="equipmentName">
        <el-input
          v-model="queryParams.equipmentName"
          placeholder="请输入设备名称"
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
          v-hasPermi="['system:survey:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:survey:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="surveyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="设备名称" align="center" prop="equipmentName" />
      <el-table-column label="型号" align="center" prop="model" />
      <el-table-column label="单价" align="center" prop="price" />
      <el-table-column label="销售商" align="center" prop="vendor" />
      <el-table-column label="采购时间" align="center" prop="purchaseTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.purchaseTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总数量" align="center" prop="totalQty" />
      <el-table-column label="可用" align="center" prop="availableQty" />
      <el-table-column label="外借" align="center" prop="borrowedQty" />
      <el-table-column label="安装" align="center" prop="installedQty" />
      <el-table-column label="报废" align="center" prop="scrappedQty" />

      <el-table-column label="审核状态" align="center" prop="auditStatus" width="120">
        <template slot-scope="scope">
          <div style="display: flex; align-items: center; justify-content: center;">
            <dict-tag :options="dict.type.sys_audit_status" :value="scope.row.auditStatus"/>
            <span v-if="scope.row.auditStatus == '0'" style="display:inline-block; width:8px; height:8px; border-radius:50%; background-color: #e6a23c; margin-left: 6px;" title="待审核"></span>
            <span v-if="scope.row.auditStatus == '1'" style="display:inline-block; width:8px; height:8px; border-radius:50%; background-color: #67c23a; margin-left: 6px;" title="已通过"></span>
            <el-tooltip v-if="scope.row.auditStatus == '2'" :content="scope.row.auditReason" placement="top">
              <i class="el-icon-warning" style="color: #f56c6c; cursor: pointer; margin-left: 6px; font-size: 14px;"></i>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip/>

      <<el-table-column
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
          v-hasPermi="['system:survey:edit']"
          v-if="checkRole(['fildsurvey_manager', 'admin']) || (checkRole(['student']) && scope.row.auditStatus != '1')"
        >修改</el-button>

        <el-button
          size="mini"
          type="text"
          icon="el-icon-delete"
          @click="handleDelete(scope.row)"
          v-hasPermi="['system:survey:remove']"
          v-if="checkRole(['fildsurvey_manager', 'admin']) || (checkRole(['student']) && scope.row.auditStatus != '1')"
        >删除</el-button>

        <el-button
          size="mini"
          type="text"
          icon="el-icon-check"
          @click="handleAuditOpen(scope.row)"
          v-hasPermi="['system:survey:audit']"
          v-if="checkRole(['teacher', 'admin']) && scope.row.auditStatus == '0'"
        >审核</el-button>

      </template>
    </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备名称" prop="equipmentName">
              <el-input v-model="form.equipmentName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入型号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="单价" prop="price">
              <el-input v-model="form.price" placeholder="请输入单价" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购时间" prop="purchaseTime">
              <el-date-picker clearable style="width: 100%" v-model="form.purchaseTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择采购时间"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="销售商" prop="vendor">
          <el-input v-model="form.vendor" placeholder="请输入销售商" />
        </el-form-item>

        <el-form-item label="总数量" prop="totalQty">
          <el-input v-model="form.totalQty" type="number" placeholder="请输入总数量" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="可用数量" prop="availableQty">
              <el-input-number v-model="form.availableQty" :min="0" :disabled="!!form.id" controls-position="right" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="外借数量" prop="borrowedQty">
              <el-input-number v-model="form.borrowedQty" :min="0" :disabled="!!form.id" controls-position="right" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="安装数量" prop="installedQty">
              <el-input-number v-model="form.installedQty" :min="0" :disabled="!!form.id" controls-position="right" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报废数量" prop="scrappedQty">
              <el-input-number v-model="form.scrappedQty" :min="0" :disabled="!!form.id" controls-position="right" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>

        <div v-if="!form.id" style="color: #909399; font-size: 12px; margin-left: 80px; margin-bottom: 20px;">
          <i class="el-icon-info"></i> 提示：此处为期初建档数据，后续变动请通过业务流程操作。
        </div>
        <el-form-item label="驳回原因" v-if="form.auditStatus == '2'">
          <el-input v-model="form.auditReason" type="textarea" disabled />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核设备" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="设备名称">
          <el-input v-model="auditForm.equipmentName" disabled />
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
import { listSurvey, getSurvey, delSurvey, addSurvey, updateSurvey, auditSurvey } from "@/api/system/survey"
import { checkRole } from "@/utils/permission";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Survey",
  dicts: ['sys_audit_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      surveyList: [],
      title: "",
      open: false,
      auditOpen: false,
      activeTab: 'public',
      currentUser: {}, // 存储当前用户信息
      auditForm: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipmentName: null,
        // 这里需要加上 createBy 字段定义，防止报错
        createBy: null,
        params: {
          queryMode: 'public'
        }
      },
      form: {},
      rules: {
        equipmentName: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
        totalQty: [
          { required: true, message: "总数量不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    // === 修改重点：确保先获取用户信息，再获取列表 ===
    // 这样当页面加载时，如果默认Tab是private，也能正确带上用户ID
    this.getCurrentUser();
  },
  methods: {
    checkRole,

    /** 获取当前登录用户 */
    getCurrentUser() {
      getUserProfile().then(response => {
        this.currentUser = response.data.user || response.data;
        // 用户信息拿到后，再加载列表，确保小明能看到数据
        this.getList();
      });
    },

    /** 查询列表 */
    getList() {
      this.loading = true;
      if (!this.queryParams.params) {
        this.queryParams.params = {};
      }
      this.queryParams.params['queryMode'] = this.activeTab;

      // === 修改重点：处理“我的提交”逻辑 ===
      if (this.activeTab === 'private') {
        // 如果是我的提交，必须把当前用户名传给后端
        this.queryParams.createBy = this.currentUser.userName;
        this.queryParams.auditStatus = undefined; // 我的提交里看所有状态
      } else if (this.activeTab === 'public') {
        this.queryParams.auditStatus = undefined; // 后端控制只查通过的
        this.queryParams.createBy = null; // 清空用户过滤
      } else {
        // audit tab
        this.queryParams.createBy = null;
      }

      listSurvey(this.queryParams).then(response => {
        this.surveyList = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    /** Tab 切换 */
    handleTabClick(tab, event) {
      this.queryParams.pageNum = 1;
      // 切换Tab时，不要重置整个表单，否则 queryParams.params 里的配置会丢
      // 只需要重置搜索框的内容，或者简单地刷新列表
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
        equipmentName: null,
        model: null,
        price: null,
        vendor: null,
        purchaseTime: null,
        totalQty: 0,
        availableQty: 0, // 初始化
        borrowedQty: 0,
        installedQty: 0,
        scrappedQty: 0,
        auditStatus: "0",
        auditReason: null,
        remark: null
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加外业调查设备"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getSurvey(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改外业调查设备"
      })
    },
    handleAuditOpen(row) {
      this.auditForm = {
        id: row.id,
        equipmentName: row.equipmentName,
        auditStatus: '1',
        auditReason: ''
      };
      this.auditOpen = true;
    },
    submitAudit() {
      if (this.auditForm.auditStatus === '2' && !this.auditForm.auditReason) {
        this.$modal.msgError("驳回时必须填写审核意见");
        return;
      }
      auditSurvey(this.auditForm).then(response => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            // ================= 关键修改开始 =================
            // 如果是修改操作，强制将状态改为 "0" (待审核)
            // 同时清空之前的驳回原因，方便下一次审核
            this.form.auditStatus = "0";
            this.form.auditReason = null;
            // ================= 关键修改结束 =================

            updateSurvey(this.form).then(response => {
              this.$modal.msgSuccess("修改成功，已重新提交审核")
              this.open = false
              this.getList()
            })
          } else {
            addSurvey(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(function() {
        return delSurvey(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('system/survey/export', {
        ...this.queryParams
      }, `survey_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
