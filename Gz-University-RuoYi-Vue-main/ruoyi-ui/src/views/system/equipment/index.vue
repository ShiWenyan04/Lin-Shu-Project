<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部设备 (已通过)" name="public"></el-tab-pane>

      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="audit"
        v-if="checkRole(['teacher', 'experimental_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
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
          v-hasPermi="['system:equipment:add']"
          v-if="checkRole(['student', 'experimental_manager', 'admin'])"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:equipment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="equipmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="型号" align="center" prop="model" />
      <el-table-column label="资产编号" align="center" prop="assetNo" />
      <el-table-column label="存放位置" align="center" prop="location" />
      <el-table-column label="购买时间" align="center" prop="purchaseTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.purchaseTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="设备状态" align="center" prop="equipmentStatus" />

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
            v-hasPermi="['system:equipment:edit']"
            v-if="checkRole(['experimental_manager', 'admin']) || (checkRole(['student']) && activeTab === 'private' && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:equipment:remove']"
            v-if="checkRole(['experimental_manager', 'admin']) || (checkRole(['student']) && activeTab === 'private' && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:equipment:audit']"
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

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="form.model" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="资产编号" prop="assetNo">
          <el-input v-model="form.assetNo" placeholder="请输入资产编号" />
        </el-form-item>
        <el-form-item label="存放位置" prop="location">
          <el-input v-model="form.location" placeholder="请输入存放位置" />
        </el-form-item>
        <el-form-item label="购买时间" prop="purchaseTime">
          <el-date-picker clearable
                          v-model="form.purchaseTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择购买时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="设备状态" prop="equipmentStatus">
          <el-input v-model="form.equipmentStatus" placeholder="请输入设备状态" />
        </el-form-item>

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
          <el-input v-model="auditForm.deviceName" disabled />
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
import { listEquipment, getEquipment, delEquipment, addEquipment, updateEquipment, auditEquipment } from "@/api/system/equipment"
import { checkRole } from "@/utils/permission";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Equipment",
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
      // 实验设备信息表格数据
      equipmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      // --- 新增变量 ---
      auditOpen: false,
      activeTab: 'public', // 默认选中
      currentUser: {},
      auditForm: {},

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceName: null,
        // 这里必须初始化 params 对象
        params: {
          queryMode: 'public'
        }
      },

      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceName: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
        assetNo: [
          { required: true, message: "资产编号不能为空", trigger: "blur" }
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
    getCurrentUser() {
      getUserProfile().then(response => {
        this.currentUser = response.data.user || response.data;
      });
    },
    getList() {
      this.loading = true;
      if (!this.queryParams.params) this.queryParams.params = {};

      this.queryParams.params['queryMode'] = this.activeTab;

      if (this.activeTab === 'public') {
        this.queryParams.auditStatus = undefined;
      }
      listEquipment(this.queryParams).then(response => {
        this.equipmentList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleTabClick(tab, event) {
      this.queryParams.pageNum = 1;
      this.resetForm("queryForm");
      this.getList();
    },
    cancel() { this.open = false; this.reset(); },
    reset() {
      this.form = { id: null, deviceName: null, model: null, assetNo: null, location: null, purchaseTime: null, equipmentStatus: null, auditStatus: null, auditReason: null, remark: null };
      this.resetForm("form");
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList(); },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery(); },
    handleSelectionChange(selection) { this.ids = selection.map(item => item.id); this.single = selection.length!==1; this.multiple = !selection.length; },
    handleAdd() { this.reset(); this.open = true; this.title = "添加实验设备信息"; },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getEquipment(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改实验设备信息";
      });
    },
    handleAuditOpen(row) {
      this.auditForm = { id: row.id, deviceName: row.deviceName, auditStatus: '1', auditReason: '' };
      this.auditOpen = true;
    },
    submitAudit() {
      if (this.auditForm.auditStatus === '2' && !this.auditForm.auditReason) {
        this.$modal.msgError("驳回时必须填写审核意见"); return;
      }
      auditEquipment(this.auditForm).then(response => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateEquipment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功"); this.open = false; this.getList();
            });
          } else {
            addEquipment(this.form).then(response => {
              this.$modal.msgSuccess("新增成功"); this.open = false; this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() { return delEquipment(ids); }).then(() => { this.getList(); this.$modal.msgSuccess("删除成功"); }).catch(() => {});
    },
    handleExport() { this.download('system/equipment/export', { ...this.queryParams }, `equipment_${new Date().getTime()}.xlsx`); }
  }
}
</script>
