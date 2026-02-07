<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部记录 (已通过)" name="ALL"></el-tab-pane>
      <el-tab-pane label="我的提交" name="MINE" v-if="!checkRole(['teacher'])"></el-tab-pane>
      <el-tab-pane label="审核管理" name="AUDIT" v-if="checkRole(['teacher', 'assistant_manager'])"></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="年级" prop="grade">
        <el-input v-model="queryParams.grade" placeholder="请输入年级" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:teaching:add']">新增安排</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:teaching:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="teachingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="姓名" align="center" prop="studentName" width="100"/>
      <el-table-column label="年级" align="center" prop="grade" width="100"/>
      <el-table-column label="课程名称" align="center" prop="courseName" min-width="150" show-overflow-tooltip/>

      <el-table-column label="授课时间" align="center" prop="teachingTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.teachingTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="授课班级" align="center" prop="teachingClass" width="120" show-overflow-tooltip/>
      <el-table-column label="周数" align="center" prop="teachingWeeks" width="60"/>

      <el-table-column label="教案PPT" align="center" width="100">
        <template slot-scope="scope">
          <el-button v-if="scope.row.pptFile" size="mini" type="text" icon="el-icon-download" @click="$download.resource(scope.row.pptFile)">下载</el-button>
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
            v-hasPermi="['system:teaching:edit']"
            v-if="checkRole(['assistant_manager']) || (scope.row.createBy == currentUser.userId && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:teaching:remove']"
            v-if="checkRole(['assistant_manager']) || (scope.row.createBy == currentUser.userId && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:teaching:audit']"
            v-if="checkRole(['teacher', 'assistant_manager']) && scope.row.auditStatus == '0'"
          >审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="studentName">
              <el-input v-model="form.studentName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年级" prop="grade">
              <el-input v-model="form.grade" placeholder="如: 2023级硕士" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="授课时间" prop="teachingTime">
              <el-date-picker clearable style="width: 100%"
                              v-model="form.teachingTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="教学周数" prop="teachingWeeks">
              <el-input-number v-model="form.teachingWeeks" :min="1" controls-position="right" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="授课班级" prop="teachingClass">
          <el-input v-model="form.teachingClass" placeholder="请输入授课班级" />
        </el-form-item>

        <el-form-item label="授课内容" prop="teachingContent">
          <el-input v-model="form.teachingContent" type="textarea" :rows="4" placeholder="简述授课内容..." />
        </el-form-item>

        <el-form-item label="教案PPT" prop="pptFile">
          <file-upload v-model="form.pptFile" :limit="1"/>
        </el-form-item>

        <div v-if="form.auditStatus == '2'" style="margin-top: 10px; padding: 10px; background: #fef0f0; border-radius: 4px;">
          <el-form-item label="驳回原因" style="margin-bottom: 0;">
            <span style="color: #f56c6c; font-weight: bold;">{{ form.auditReason }}</span>
          </el-form-item>
        </div>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="课程详情" :visible.sync="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="姓名">{{ viewForm.studentName }}</el-descriptions-item>
        <el-descriptions-item label="年级">{{ viewForm.grade }}</el-descriptions-item>
        <el-descriptions-item label="课程名称">{{ viewForm.courseName }}</el-descriptions-item>
        <el-descriptions-item label="授课班级">{{ viewForm.teachingClass }}</el-descriptions-item>
        <el-descriptions-item label="授课时间">{{ parseTime(viewForm.teachingTime, '{y}-{m}-{d} {h}:{i}') }}</el-descriptions-item>
        <el-descriptions-item label="教学周数">{{ viewForm.teachingWeeks }} 周</el-descriptions-item>

        <el-descriptions-item label="授课内容">
          <div style="white-space: pre-wrap;">{{ viewForm.teachingContent }}</div>
        </el-descriptions-item>

        <el-descriptions-item label="教案PPT">
          <div v-if="viewForm.pptFile">
            <el-link type="primary" icon="el-icon-download" :href="viewForm.pptFile" target="_blank">点击下载</el-link>
          </div>
          <div v-else>无</div>
        </el-descriptions-item>

        <el-descriptions-item label="审核状态">
          <dict-tag :options="dict.type.sys_audit_status" :value="viewForm.auditStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="审核意见" v-if="viewForm.auditStatus == '2'">
          <span style="color:red">{{ viewForm.auditReason }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewForm.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核安排" :visible.sync="auditOpen" width="500px" append-to-body>
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
// 1. 记得引入 auditTeaching
import { listTeaching, getTeaching, delTeaching, addTeaching, updateTeaching, auditTeaching } from "@/api/system/teaching"
import { checkRole } from "@/utils/permission"
import { getUserProfile } from "@/api/system/user"

export default {
  name: "Teaching",
  dicts: ['sys_audit_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      teachingList: [],
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
        studentName: null,
        grade: null,
        tabType: 'ALL'
      },
      form: {},
      rules: {
        studentName: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
        courseName: [{ required: true, message: "课程名称不能为空", trigger: "blur" }],
        teachingTime: [{ required: true, message: "授课时间不能为空", trigger: "change" }]
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
      listTeaching(this.queryParams).then(response => {
        this.teachingList = response.rows;
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
        studentName: null,
        grade: null,
        courseName: null,
        teachingTime: null,
        teachingClass: null,
        teachingWeeks: null,
        teachingContent: null,
        pptFile: null,
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
      this.title = "添加研究生助教安排";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getTeaching(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改研究生助教安排";
      });
    },

    // 详情
    handleView(row) {
      this.viewForm = row;
      this.viewOpen = true;
    },

    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTeaching(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTeaching(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    // 审核
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
      auditTeaching(this.auditForm).then(res => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },

    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() {
        return delTeaching(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('system/teaching/export', {
        ...this.queryParams
      }, `teaching_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
