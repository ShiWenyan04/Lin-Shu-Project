<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部项目 (已通过)" name="ALL"></el-tab-pane>
      <el-tab-pane label="我的提交" name="MINE" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="AUDIT"
        v-if="checkRole(['teacher', 'fildstart_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="负责人" prop="leaderName">
        <el-input v-model="queryParams.leaderName" placeholder="请输入负责人" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:start:add']">新增申请</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:start:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="startList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="项目名称" align="center" prop="projectName" show-overflow-tooltip/>
      <el-table-column label="负责人" align="center" prop="leaderName" width="100"/>
      <el-table-column label="目的地" align="center" prop="destination" />
      <el-table-column label="出发时间" align="center" prop="startDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="参与人员" align="center" prop="membersInnerNames" show-overflow-tooltip/>
      <el-table-column label="租车公司" align="center" prop="carCompany" show-overflow-tooltip />

      <el-table-column label="调查方案" align="center" width="100">
        <template slot-scope="scope">
          <el-button v-if="scope.row.planFile" size="mini" type="text" icon="el-icon-download" @click="handleDownloadFile(scope.row.planFile)">下载</el-button>
          <span v-else>-</span>
        </template>
      </el-table-column>

      <el-table-column label="是否保险" align="center" prop="isInsurance" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isInsurance"/>
        </template>
      </el-table-column>

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

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220"
                       v-if="checkRole(['teacher', 'fildstart_manager', 'admin']) || activeTab === 'MINE'">
        <template slot-scope="scope">

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:start:edit']"
            v-if="checkRole(['fildstart_manager', 'admin']) || (scope.row.createBy == currentUser.userId && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:start:remove']"
            v-if="checkRole(['fildstart_manager', 'admin']) || (scope.row.createBy == currentUser.userId && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:start:audit']"
            v-if="checkRole(['teacher', 'admin']) && scope.row.auditStatus == '0'"
          >审核</el-button>

          <span v-if="scope.row.auditStatus == '1' && scope.row.createBy == currentUser.userId">
             <el-tag v-if="scope.row.endCount > 0" type="info" size="mini" effect="plain" style="margin-left:5px">
               <i class="el-icon-finished"></i> 已申请结束
             </el-tag>

             <el-button
               v-else
               size="mini"
               type="text"
               icon="el-icon-finished"
               @click="handleEndWork(scope.row)"
               v-hasPermi="['system:end:add']"
               style="color: #67c23a;"
             >结束外业</el-button>
          </span>

        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-divider content-position="left"><i class="el-icon-s-order"></i> 基础信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="关联项目" prop="projectId">
              <el-select v-model="form.projectId" placeholder="请选择项目" filterable style="width: 100%">
                <el-option v-for="item in projectOptions" :key="item.projectId" :label="item.projectName" :value="item.projectId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="外业负责人" prop="leaderName">
              <el-input v-model="form.leaderName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目的地" prop="destination">
              <el-input v-model="form.destination" placeholder="请输入目的地" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出发时间" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划天数" prop="planDays">
              <el-input-number v-model="form.planDays" :min="1" controls-position="right" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left"><i class="el-icon-user-solid"></i> 人员与后勤</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="本组人员" prop="membersInner">
              <el-select v-model="currentMembers" multiple placeholder="请选择组员 (可多选)" style="width: 100%">
                <el-option v-for="user in userOptions" :key="user.userId" :label="user.nickName" :value="user.userId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="组外人员" prop="membersOuter">
              <el-input v-model="form.membersOuter" placeholder="手动输入姓名，用逗号分隔" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="租车公司" prop="carCompany">
              <el-input v-model="form.carCompany" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="购买保险" prop="isInsurance">
              <el-radio-group v-model="form.isInsurance">
                <el-radio v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left"><i class="el-icon-document"></i> 详细内容</el-divider>
        <el-form-item label="外业事由" prop="reason">
          <el-input v-model="form.reason" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="调查方案" prop="planFile">
          <file-upload v-model="form.planFile" :limit="1"/>
        </el-form-item>
        <div v-if="form.auditStatus == '2'" style="margin-top: 10px; padding: 10px; background: #fef0f0; border-radius: 4px;">
          <el-form-item label="驳回原因" style="margin-bottom: 0;">
            <span style="color: #f56c6c; font-weight: bold;">{{ form.auditReason }}</span>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="endTitle" :visible.sync="endOpen" width="700px" append-to-body>
      <el-form ref="endForm" :model="endForm" :rules="endRules" label-width="110px">

        <el-divider content-position="left"><i class="el-icon-s-order"></i> 关联信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="关联项目">
              <el-input v-model="endForm.projectName" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="外业负责人">
              <el-input v-model="endForm.leaderName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="外业总天数" prop="actDays">
              <el-input-number v-model="endForm.actDays" :min="1" controls-position="right" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left"><i class="el-icon-date"></i> 时间与凭证</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="实际出发时间" prop="actStartDate">
              <el-date-picker v-model="endForm.actStartDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际结束时间" prop="actEndDate">
              <el-date-picker v-model="endForm.actEndDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="有无报销明细" prop="isReimburse">
              <el-radio-group v-model="endForm.isReimburse">
                <el-radio v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票是否提交" prop="isInvoice">
              <el-radio-group v-model="endForm.isInvoice">
                <el-radio v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left"><i class="el-icon-document"></i> 成果汇报</el-divider>
        <el-form-item label="任务完成情况" prop="taskResult">
          <el-input v-model="endForm.taskResult" type="textarea" :rows="3" placeholder="简述任务完成情况..." />
        </el-form-item>

        <el-form-item label="外业小结附件" prop="summaryFile">
          <file-upload v-model="endForm.summaryFile" :limit="1"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEndForm">提交填报</el-button>
        <el-button @click="endOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核申请" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="项目名称">
          <el-input v-model="auditForm.projectName" disabled />
        </el-form-item>
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" v-if="auditForm.auditStatus == '2'">
          <el-input v-model="auditForm.auditReason" type="textarea" placeholder="请输入驳回原因..." />
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
// 1. 引入必要的方法
import { listStart, getStart, delStart, addStart, updateStart, auditStart } from "@/api/system/start"
// 🚀 引入 End 的新增接口
import { addEnd } from "@/api/system/end"
import { listUser } from "@/api/system/user"
import { checkRole } from "@/utils/permission"
import { getUserProfile } from "@/api/system/user"
import { listProject } from "@/api/system/project"

export default {
  name: "Start",
  dicts: ['sys_audit_status', 'sys_yes_no'],
  data() {
    return {
      // 遮罩层
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      startList: [],
      title: "",
      open: false,

      // --- 核心逻辑变量 ---
      activeTab: 'ALL',
      currentUser: {},

      // Start 审核弹窗
      auditOpen: false,
      auditForm: {},

      // 🚀 End 填报弹窗
      endOpen: false,
      endTitle: "填写外业行程结束",
      endForm: {},
      endRules: {
        actStartDate: [{ required: true, message: "请选择时间", trigger: "change" }],
        actDays: [{ required: true, message: "请输入天数", trigger: "blur" }]
      },

      // 下拉框数据
      projectOptions: [],
      userOptions: [],
      currentMembers: [],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        destination: null,
        projectName: null,
        leaderName: null,
        tabType: 'ALL'
      },
      form: {},
      rules: {
        projectId: [{ required: true, message: "请选择项目", trigger: "change" }],
        leaderName: [{ required: true, message: "请填写负责人", trigger: "blur" }],
        startDate: [{ required: true, message: "请选择时间", trigger: "change" }],
        planDays: [{ required: true, message: "请输入天数", trigger: "blur" }],
        isInsurance: [{ required: true, message: "请选择保险", trigger: "change" }]
      }
    }
  },
  created() {
    this.getCurrentUser();
    this.getUserList();
    this.getProjectList();
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
      this.queryParams.tabType = this.activeTab;

      listStart(this.queryParams).then(response => {
        this.startList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(err => {
        this.loading = false;
        console.error("查询列表失败:", err);
      });
    },

    /** 🚀 打开结束填报弹窗 */
    handleEndWork(row) {
      this.resetEndForm();
      // 自动关联
      this.endForm.startId = row.startId;
      this.endForm.projectName = row.projectName; // 用于回显
      this.endForm.leaderName = row.leaderName;   // 用于回显
      this.endOpen = true;
    },

    /** 🚀 重置结束填报表单 */
    resetEndForm() {
      this.endForm = {
        startId: null,
        projectName: null,
        leaderName: null,
        summaryFile: null,
        taskResult: null,
        actStartDate: null,
        actEndDate: null,
        actDays: null,
        isReimburse: 'N',
        isInvoice: 'N',
        auditStatus: '0',
      };
      this.resetForm("endForm");
    },

    /** 🚀 提交结束填报 */
    submitEndForm() {
      this.$refs["endForm"].validate(valid => {
        if (valid) {
          addEnd(this.endForm).then(response => {
            this.$modal.msgSuccess("填报成功，已提交审核");
            this.endOpen = false;
            // 提交成功后，刷新一下列表，让“已申请结束”的标签显示出来
            this.getList();
          });
        }
      });
    },

    handleTabClick(tab) {
      this.queryParams.pageNum = 1;
      this.resetQuery();
    },

    getProjectList() {
      listProject({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.projectOptions = response.rows;
      });
    },
    getUserList() {
      listUser({ pageNum: 1, pageSize: 1000 }).then(res => {
        this.userOptions = res.rows;
      }).catch(() => {});
    },

    handleDownloadFile(url) {
      this.$download.resource(url);
    },

    cancel() {
      this.open = false;
      this.reset();
    },

    reset() {
      this.form = {
        startId: null,
        projectId: null,
        leaderName: null,
        reason: null,
        destination: null,
        startDate: null,
        planDays: 1,
        membersInner: null,
        membersOuter: null,
        isInsurance: 'N',
        auditStatus: '0',
        carCompany: null,
        planFile: null
      };
      this.currentMembers = [];
      this.resetForm("form");
    },

    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.startId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "填写外业申请";
    },

    handleUpdate(row) {
      this.reset();
      const startId = row.startId || this.ids;
      getStart(startId).then(response => {
        this.form = response.data;
        if (this.form.membersInner) {
          this.currentMembers = this.form.membersInner.split(',').map(Number);
        }
        this.open = true;
        this.title = "修改外业申请";
      });
    },

    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.currentMembers && this.currentMembers.length > 0) {
            this.form.membersInner = this.currentMembers.join(',');
          } else {
            this.form.membersInner = '';
          }

          if (this.form.startId != null) {
            updateStart(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStart(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    handleAuditOpen(row) {
      this.auditForm = {
        startId: row.startId,
        projectName: row.projectName, // 展示用
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
      auditStart(this.auditForm).then(res => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },

    handleDelete(row) {
      const startIds = row.startId || this.ids;
      this.$modal.confirm('确认删除？').then(function() {
        return delStart(startIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    handleExport() {
      this.download('system/start/export', {
        ...this.queryParams
      }, `start_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
