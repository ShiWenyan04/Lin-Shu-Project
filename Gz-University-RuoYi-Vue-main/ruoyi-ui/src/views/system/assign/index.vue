<template>
  <div class="app-container">

    <el-card shadow="never" class="mb10" style="background-color: #f8f8f9; border: none; margin-bottom: 10px;">
      <div style="display: flex; align-items: center;">
        <i class="el-icon-user-solid" style="margin-right: 8px; font-size: 16px;"></i>
        <span style="font-weight: bold; margin-right: 15px;">我的当前身份：</span>

        <el-tag
          v-for="role in userRoles"
          :key="role.roleId"
          :type="getRoleTagType(role.roleKey)"
          :closable="isRoleClosable(role.roleKey)"
          @close="handleCloseRole(role)"
          style="margin-right: 10px;"
          size="medium"
          effect="light"
        >
          {{ role.roleName }}
        </el-tag>

        <span style="font-size: 12px; color: #909399; margin-left: 10px;">
          (点击 × 可卸任负责人身份，恢复为普通学生)
        </span>
      </div>
    </el-card>

    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部任务 (已通过)" name="public"></el-tab-pane>
      <el-tab-pane label="我的申请" name="private" v-if="checkRole(['student','manager'])"></el-tab-pane>
      <el-tab-pane label="审核管理" name="audit" v-if="checkRole(['teacher',  'admin'])"></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="分工任务名称" prop="taskName">
        <el-input v-model="queryParams.taskName" placeholder="请输入任务名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status" v-if="activeTab !== 'audit'">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.sys_audit_status" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
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
          v-if="checkRole(['student', 'admin'])"
        >申请分工</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="assignList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务名称" align="center" prop="taskName" show-overflow-tooltip />

      <el-table-column label="申请角色" align="center" prop="roleId">
        <template slot-scope="scope">
          <span>{{ getRoleName(scope.row.roleId) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="申请人" align="center" prop="userName" />

      <el-table-column label="申请人当前角色" align="center" prop="currentUserRole" width="150">
        <template slot-scope="scope">
          <el-tag size="mini" type="info">{{ scope.row.currentUserRole || '无角色' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="任务要求" align="center" prop="requirements" show-overflow-tooltip/>

      <el-table-column label="负责时段" align="center" width="200">
        <template slot-scope="scope">
          <div v-if="scope.row.startDate">{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }} 至</div>
          <div v-if="scope.row.endDate">{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</div>
        </template>
      </el-table-column>

      <el-table-column label="状态" align="center" prop="status" width="120">
        <template slot-scope="scope">
          <div style="display: flex; align-items: center; justify-content: center;">
            <dict-tag :options="dict.type.sys_audit_status" :value="scope.row.status"/>
            <span v-if="scope.row.status == '0'" style="display:inline-block; width:8px; height:8px; border-radius:50%; background-color: #e6a23c; margin-left: 6px;" title="待审核"></span>
            <span v-if="scope.row.status == '1'" style="display:inline-block; width:8px; height:8px; border-radius:50%; background-color: #67c23a; margin-left: 6px;" title="已通过"></span>
            <el-tooltip v-if="scope.row.status == '2'" :content="scope.row.auditReason" placement="top">
              <i class="el-icon-warning" style="color: #f56c6c; cursor: pointer; margin-left: 6px; font-size: 14px;"></i>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="220"
        v-if="activeTab !== 'public'"
      >
        <template slot-scope="scope">
    <span v-if="checkRole(['student', 'manager', 'group_manager']) && (scope.row.status === '0' || scope.row.status === '2') && scope.row.userId === currentUser.userId">
      <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
      <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
    </span>

          <el-button
            v-if="checkRole(['teacher', 'admin']) && scope.row.status === '0'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
          >审核</el-button>

          <span v-if="checkRole(['admin'])">
       <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
    </span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">

        <el-form-item label="当前角色">
          <el-input :value="currentUser.roleGroup || '暂无角色'" disabled />
        </el-form-item>

        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="form.taskName" placeholder="例如：实验室财务管理" />
        </el-form-item>

        <el-form-item label="申请角色" prop="roleId">
          <el-select v-model="form.roleId" placeholder="请选择对应的系统角色" style="width: 100%">
            <el-option
              v-for="item in roleOptions"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="任务要求" prop="requirements">
          <el-input v-model="form.requirements" type="textarea" placeholder="简述负责内容" />
        </el-form-item>

        <el-form-item label="负责时段" prop="startDate">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="其他说明" />
        </el-form-item>

        <el-form-item label="驳回原因" v-if="form.status === '2'">
          <el-input v-model="form.auditReason" type="textarea" disabled :rows="2" style="color: #F56C6C; font-weight: bold;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核申请" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="任务名称">
          <el-input v-model="auditForm.taskName" disabled />
        </el-form-item>
        <el-form-item label="申请人">
          <el-input v-model="auditForm.userName" disabled />
        </el-form-item>

        <el-form-item label="当前角色">
          <el-input v-model="auditForm.currentUserRole" disabled style="color: #409EFF;" />
        </el-form-item>

        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.status">
            <el-radio label="1">通过 (赋予权限)</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" v-if="auditForm.status == '2'">
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
// 注意：这里引入了 cancelRole
import { listAssign, getAssign, delAssign, addAssign, applyAssign, updateAssign, auditAssign, optionSelectRole, cancelRole } from "@/api/system/assign"
import { checkRole } from "@/utils/permission";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Assign",
  dicts: ['sys_audit_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      assignList: [],
      title: "",
      open: false,
      activeTab: 'public',

      auditOpen: false,
      auditForm: {},
      roleOptions: [],
      dateRange: [],
      currentUser: {},

      // 【新增】存储用户完整的角色对象列表
      userRoles: [],

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskName: null,
        status: null,
      },
      form: {},
      rules: {
        taskName: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
        roleId: [{ required: true, message: "必须选择关联角色", trigger: "change" }],
        requirements: [{ required: true, message: "任务要求不能为空", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList();
    this.getRoleOptions();
    this.getCurrentUser();
  },
  methods: {
    checkRole,

    // 获取当前用户信息 (包含角色列表)
    getCurrentUser() {
      getUserProfile().then(res => {
        // 1. 获取用户基础对象
        const user = res.data.user || res.data;
        this.currentUser = user || {};

        // 2. 获取角色组名称 (用于弹窗显示字符串)
        const roleGroup = res.data.roleGroup || res.roleGroup || '暂无';
        this.$set(this.currentUser, 'roleGroup', roleGroup);

        // 3. 【新增】获取详细角色列表 (用于顶部Tag显示)
        // 若依标准版 res.data.user.roles 是一个Role对象数组
        if (this.currentUser.roles) {
          this.userRoles = this.currentUser.roles;
        } else if (res.data.roles) {
          // 兼容某些版本直接返回 roles
          this.userRoles = res.data.roles;
        }
      }).catch(err => {
        console.error("获取用户信息失败", err);
        this.currentUser = {};
      });
    },

    // 【新增】判断标签颜色
    getRoleTagType(roleKey) {
      if (roleKey === 'student') return 'info';    // 学生灰色
      if (roleKey === 'admin') return 'danger';    // 管理员红色
      return 'success';                            // 负责人绿色
    },

    // 【新增】判断是否允许卸任
    isRoleClosable(roleKey) {
      // 学生和超管不允许在此处卸任
      if (roleKey === 'student' || roleKey === 'admin') return false;
      return true;
    },

    // 【新增】处理卸任逻辑
    handleCloseRole(role) {
      this.$confirm('确认要卸任 "' + role.roleName + '" 身份吗？卸任后将失去审核权限。', "警告", {
        confirmButtonText: "确定卸任",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return cancelRole(role.roleId);
      }).then(() => {
        this.$modal.msgSuccess("卸任成功，您已恢复为普通身份");
        // 刷新用户信息，标签会自动消失
        this.getCurrentUser();
        // 刷新列表 (权限变了可能导致列表数据变化)
        this.getList();
        // 建议：刷新页面以更新左侧菜单栏
        // location.reload();
      }).catch(() => {});
    },

    getRoleOptions() {
      optionSelectRole().then(response => {
        this.roleOptions = response.data;
      });
    },

    getRoleName(roleId) {
      const role = this.roleOptions.find(r => r.roleId === roleId);
      return role ? role.roleName : roleId;
    },

    getList() {
      this.loading = true;
      if (!this.queryParams.params) this.queryParams.params = {};
      this.queryParams.params['queryMode'] = this.activeTab;

      // 注释掉或者直接删除下面这三行判断
      // if (this.activeTab === 'public') {
      //   this.queryParams.status = undefined;
      // }

      listAssign(this.queryParams).then(response => {
        this.assignList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    handleTabClick() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    cancel() { this.open = false; this.reset(); },

    reset() {
      this.form = {
        id: null,
        taskName: null,
        roleId: null,
        requirements: null,
        status: "0",
        auditReason: null,
        remark: null
      };
      this.dateRange = [];
      this.resetForm("form");
    },

    handleQuery() { this.queryParams.pageNum = 1; this.getList(); },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery(); },
    handleSelectionChange(selection) { this.ids = selection.map(item => item.id); this.single = selection.length!==1; this.multiple = !selection.length; },

    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "申请组务分工";
    },

    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getAssign(id).then(response => {
        this.form = response.data;
        if (this.form.startDate && this.form.endDate) {
          this.dateRange = [this.form.startDate, this.form.endDate];
        }
        this.open = true;
        this.title = "修改申请";
      });
    },

    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.dateRange && this.dateRange.length === 2) {
            this.form.startDate = this.dateRange[0];
            this.form.endDate = this.dateRange[1];
          }

          if (this.form.id != null) {
            updateAssign(this.form).then(response => {
              this.$modal.msgSuccess("修改成功，已重新提交审核");
              this.open = false;
              this.getList();
            });
          } else {
            applyAssign(this.form).then(response => {
              this.$modal.msgSuccess("申请提交成功，请等待审核");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    handleAuditOpen(row) {
      this.auditForm = {
        id: row.id,
        taskName: row.taskName,
        userName: row.userName,
        currentUserRole: row.currentUserRole,
        status: '1',
        auditReason: ''
      };
      this.auditOpen = true;
    },

    submitAudit() {
      if (this.auditForm.status === '2' && !this.auditForm.auditReason) {
        this.$modal.msgError("驳回时必须填写审核意见");
        return;
      }
      auditAssign(this.auditForm).then(response => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },

    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() { return delAssign(ids); }).then(() => { this.getList(); this.$modal.msgSuccess("删除成功"); }).catch(() => {});
    }
  }
}
</script>
