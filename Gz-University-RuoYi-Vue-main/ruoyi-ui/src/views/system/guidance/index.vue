<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部记录 (已通过)" name="ALL"></el-tab-pane>
      <el-tab-pane label="我的提交" name="MINE" v-if="!checkRole(['teacher'])"></el-tab-pane>
      <el-tab-pane label="审核管理" name="AUDIT" v-if="checkRole(['teacher', 'assistant_manager'])"></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="年级" prop="grade">
        <el-input v-model="queryParams.grade" placeholder="请输入年级" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="指导人" prop="supervisor">
        <el-input v-model="queryParams.supervisor" placeholder="请输入指导人" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="论文题目" prop="thesisTitle">
        <el-input v-model="queryParams.thesisTitle" placeholder="请输入题目" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:guidance:add']">新增填报</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:guidance:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="guidanceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="年级" align="center" prop="grade" width="80"/>
      <el-table-column label="姓名" align="center" prop="studentName" width="100"/>
      <el-table-column label="学号" align="center" prop="studentId" width="120"/>
      <el-table-column label="指导人" align="center" prop="supervisor" width="100"/>
      <el-table-column label="论文题目" align="center" prop="thesisTitle" min-width="150" show-overflow-tooltip/>

      <el-table-column label="评价附件" align="center" width="100">
        <template slot-scope="scope">
          <el-button v-if="scope.row.evaluationFile" size="mini" type="text" icon="el-icon-download" @click="$download.resource(scope.row.evaluationFile)">下载</el-button>
          <span v-else style="color: #909399">-</span>
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

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220" v-if="checkRole(['teacher', 'assistant_manager']) || activeTab === 'MINE'">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >详情</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:guidance:edit']"
            v-if="checkRole(['assistant_manager']) || (scope.row.createBy == currentUser.userId && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:guidance:remove']"
            v-if="checkRole(['assistant_manager']) || (scope.row.createBy == currentUser.userId && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:guidance:audit']"
            v-if="checkRole(['teacher', 'assistant_manager']) && scope.row.auditStatus == '0'"
          >审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">

        <el-divider content-position="left"><i class="el-icon-user"></i> 基础信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年级" prop="grade">
              <el-input v-model="form.grade" placeholder="如: 2021级" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级" prop="className">
              <el-input v-model="form.className" placeholder="请输入班级" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="studentName">
              <el-input v-model="form.studentName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学号" prop="studentId">
              <el-input v-model="form.studentId" placeholder="请输入学号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left"><i class="el-icon-document"></i> 论文信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="指导人" prop="supervisor">
              <el-input v-model="form.supervisor" placeholder="请输入指导人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否指导费" prop="hasFee">
              <el-radio-group v-model="form.hasFee">
                <el-radio v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="论文题目" prop="thesisTitle">
          <el-input v-model="form.thesisTitle" type="textarea" :rows="2" placeholder="请输入论文题目" />
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="非必填" />
        </el-form-item>

        <el-divider content-position="left"><i class="el-icon-date"></i> 进度与评价</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="开题时间" prop="proposalDate">
              <el-date-picker v-model="form.proposalDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="答辩时间" prop="defenseDate">
              <el-date-picker v-model="form.defenseDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="答辩结果" prop="defenseResult">
              <el-select v-model="form.defenseResult" placeholder="请选择" style="width: 100%">
                <el-option v-for="dict in dict.type.edu_defense_result" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="记录及评价" prop="evaluationFile">
          <file-upload v-model="form.evaluationFile" :limit="1"/>
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

    <el-dialog title="详情信息" :visible.sync="viewOpen" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="年级">{{ viewForm.grade }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ viewForm.className }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ viewForm.studentName }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ viewForm.studentId }}</el-descriptions-item>

        <el-descriptions-item label="指导人">{{ viewForm.supervisor }}</el-descriptions-item>
        <el-descriptions-item label="指导费用">
          <dict-tag :options="dict.type.sys_yes_no" :value="viewForm.hasFee"/>
        </el-descriptions-item>

        <el-descriptions-item label="论文题目" :span="2">{{ viewForm.thesisTitle }}</el-descriptions-item>
        <el-descriptions-item label="项目名称" :span="2">{{ viewForm.projectName || '无' }}</el-descriptions-item>

        <el-descriptions-item label="开题时间">{{ parseTime(viewForm.proposalDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="答辩时间">{{ parseTime(viewForm.defenseDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="答辩结果">
          <dict-tag :options="dict.type.edu_defense_result" :value="viewForm.defenseResult"/>
        </el-descriptions-item>

        <el-descriptions-item label="评价附件">
          <div v-if="viewForm.evaluationFile">
            <el-link type="primary" icon="el-icon-download" :href="viewForm.evaluationFile" target="_blank">点击下载</el-link>
          </div>
          <div v-else>无</div>
        </el-descriptions-item>

        <el-descriptions-item label="审核状态">
          <dict-tag :options="dict.type.sys_audit_status" :value="viewForm.auditStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="审核意见" :span="2" v-if="viewForm.auditStatus == '2'">
          <span style="color:red">{{ viewForm.auditReason }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewForm.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核申请" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
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
// 1. 引入 auditGuidance 方法
import { listGuidance, getGuidance, delGuidance, addGuidance, updateGuidance, auditGuidance } from "@/api/system/guidance"
import { checkRole } from "@/utils/permission"
import { getUserProfile } from "@/api/system/user"

export default {
  name: "Guidance",
  dicts: ['edu_defense_result', 'sys_audit_status', 'sys_yes_no'],
  data() {
    return {
      // 遮罩层
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      guidanceList: [],
      title: "",
      open: false,

      // 核心
      activeTab: 'ALL',
      currentUser: {},

      // 详情
      viewOpen: false,
      viewForm: {},

      // 审核
      auditOpen: false,
      auditForm: {},

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        grade: null,
        supervisor: null,
        studentName: null,
        thesisTitle: null,
        tabType: 'ALL'
      },
      form: {},
      // 校验规则
      rules: {
        grade: [{ required: true, message: "年级不能为空", trigger: "blur" }],
        studentName: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
        studentId: [{ required: true, message: "学号不能为空", trigger: "blur" }],
        supervisor: [{ required: true, message: "指导人不能为空", trigger: "blur" }],
        thesisTitle: [{ required: true, message: "论文题目不能为空", trigger: "blur" }],
        // 项目名称非必填，无需配置
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
      this.queryParams.tabType = this.activeTab;
      listGuidance(this.queryParams).then(response => {
        this.guidanceList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(() => this.loading = false);
    },

    handleTabClick(tab) {
      this.queryParams.pageNum = 1;
      this.resetQuery();
    },

    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        grade: null,
        className: null,
        studentName: null,
        studentId: null,
        supervisor: null,
        thesisTitle: null,
        projectName: null,
        proposalDate: null,
        defenseDate: null,
        defenseResult: null,
        hasFee: 'N',
        evaluationFile: null,
        auditStatus: '0',
        auditReason: null,
        remark: null
      };
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加本科论文指导";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getGuidance(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改本科论文指导";
      });
    },

    /** 详情按钮 */
    handleView(row) {
      this.viewForm = row;
      this.viewOpen = true;
    },

    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateGuidance(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGuidance(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 审核相关 */
    handleAuditOpen(row) {
      this.auditForm = {
        id: row.id,
        auditStatus: '1',
        auditReason: ''
      };
      this.auditOpen = true;
    },
    submitAudit() {
      if(this.auditForm.auditStatus === '2' && !this.auditForm.auditReason){
        this.$modal.msgError("请填写驳回原因"); return;
      }
      auditGuidance(this.auditForm).then(res => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },

    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() {
        return delGuidance(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('system/guidance/export', {
        ...this.queryParams
      }, `guidance_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
