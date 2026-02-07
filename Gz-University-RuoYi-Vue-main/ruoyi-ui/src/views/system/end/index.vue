<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部记录 (已通过)" name="ALL"></el-tab-pane>
      <el-tab-pane label="我的提交" name="MINE" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="AUDIT"
        v-if="checkRole(['teacher', 'fieldend_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:end:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="endList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="关联项目" align="center" prop="projectName" min-width="150" show-overflow-tooltip/>
      <el-table-column label="外业负责人" align="center" prop="leaderName" width="100"/>
      <el-table-column label="实际时间" align="center" width="180">
        <template slot-scope="scope">
          {{ parseTime(scope.row.actStartDate, '{y}-{m}-{d}') }} 至 {{ parseTime(scope.row.actEndDate, '{y}-{m}-{d}') }}
        </template>
      </el-table-column>
      <el-table-column label="天数" align="center" prop="actDays" width="60"/>
      <el-table-column label="小结附件" align="center" width="80">
        <template slot-scope="scope">
          <el-button v-if="scope.row.summaryFile" size="mini" type="text" icon="el-icon-download" @click="$download.resource(scope.row.summaryFile)">下载</el-button>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="auditStatus" width="100">
        <template slot-scope="scope">
          <div style="display: flex; align-items: center; justify-content: center;">
            <dict-tag :options="dict.type.sys_audit_status" :value="scope.row.auditStatus"/>
            <span v-if="scope.row.auditStatus == '0'" style="display:inline-block; width:6px; height:6px; border-radius:50%; background-color: #e6a23c; margin-left: 5px;"></span>
            <span v-if="scope.row.auditStatus == '1'" style="display:inline-block; width:6px; height:6px; border-radius:50%; background-color: #67c23a; margin-left: 5px;"></span>
            <span v-if="scope.row.auditStatus == '2'" style="display:inline-block; width:6px; height:6px; border-radius:50%; background-color: #f56c6c; margin-left: 5px;"></span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:end:edit']"
            v-if="checkRole(['fieldend_manager', 'admin']) || (scope.row.createBy == currentUser.userId && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:end:remove']"
            v-if="checkRole(['fieldend_manager', 'admin']) || (scope.row.createBy == currentUser.userId && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:end:audit']"
            v-if="checkRole(['teacher', 'admin']) && scope.row.auditStatus == '0'"
          >审核</el-button>

          <span v-if="scope.row.auditStatus == '1' && scope.row.createBy == currentUser.userId">
             <el-tag v-if="scope.row.archiveCount > 0" type="success" size="mini" effect="plain" style="margin-left:5px">
               已归档
             </el-tag>

             <el-button
               v-else
               size="mini"
               type="text"
               icon="el-icon-folder-add"
               @click="handleArchive(scope.row)"
               v-hasPermi="['system:archive:add']"
               style="color: #409EFF;"
             >归档</el-button>
          </span>

        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-divider content-position="left"><i class="el-icon-s-order"></i> 关联信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="关联外业申请" prop="startId">
              <el-select v-model="form.startId" placeholder="请选择" style="width: 100%" :disabled="form.endId != null" @change="handleStartSelectChange">
                <el-option v-for="item in myStartOptions" :key="item.startId" :label="item.projectName + ' (' + item.destination + ')'" :value="item.startId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="外业负责人" prop="leaderName">
              <el-input v-model="form.leaderName" disabled placeholder="自动读取" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="外业总天数" prop="actDays">
              <el-input-number v-model="form.actDays" :min="1" controls-position="right" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left"><i class="el-icon-date"></i> 时间与凭证</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="实际出发时间" prop="actStartDate">
              <el-date-picker v-model="form.actStartDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际结束时间" prop="actEndDate">
              <el-date-picker v-model="form.actEndDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="有无报销明细" prop="isReimburse">
              <el-radio-group v-model="form.isReimburse">
                <el-radio v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票是否提交" prop="isInvoice">
              <el-radio-group v-model="form.isInvoice">
                <el-radio v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left"><i class="el-icon-document"></i> 成果汇报</el-divider>
        <el-form-item label="任务完成情况" prop="taskResult">
          <el-input v-model="form.taskResult" type="textarea" :rows="3" placeholder="简述任务完成情况..." />
        </el-form-item>
        <el-form-item label="外业小结附件" prop="summaryFile">
          <file-upload v-model="form.summaryFile" :limit="1"/>
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

    <el-dialog title="数据归档" :visible.sync="archiveOpen" width="800px" append-to-body>
      <el-form ref="archiveForm" :model="archiveForm" :rules="archiveRules" label-width="120px">

        <el-divider content-position="left"><i class="el-icon-s-home"></i> 基础信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="关联项目">
              <el-input v-model="archiveForm.projectName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="外业负责人">
              <el-input v-model="archiveForm.leaderName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归档人" prop="archiverName">
              <el-input v-model="archiveForm.archiverName" placeholder="默认为当前用户" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left"><i class="el-icon-folder"></i> 资料路径归档 (手动填写)</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="工作照路径" prop="workPhotoUrl">
              <el-input v-model="archiveForm.workPhotoUrl" type="textarea" :rows="2" placeholder="请输入工作照存放路径..." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="物种照路径" prop="speciesPhotoUrl">
              <el-input v-model="archiveForm.speciesPhotoUrl" type="textarea" :rows="2" placeholder="请输入物种照存放路径..." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="生境照路径" prop="habitatPhotoUrl">
              <el-input v-model="archiveForm.habitatPhotoUrl" type="textarea" :rows="2" placeholder="请输入生境照存放路径..." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="奥维数据路径" prop="ovitalDataUrl">
              <el-input v-model="archiveForm.ovitalDataUrl" type="textarea" :rows="2" placeholder="请输入奥维数据存放路径..." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="红外数据路径" prop="cameraDataUrl">
              <el-input v-model="archiveForm.cameraDataUrl" type="textarea" :rows="2" placeholder="请输入红外相机数据存放路径..." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="信息表路径" prop="infoSheetUrl">
              <el-input v-model="archiveForm.infoSheetUrl" type="textarea" :rows="2" placeholder="请输入信息表存放路径..." />
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitArchiveForm">提交归档</el-button>
        <el-button @click="archiveOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核行程" :visible.sync="auditOpen" width="500px" append-to-body>
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
import { listEnd, getEnd, delEnd, addEnd, updateEnd, auditEnd, listMyAvailableStart } from "@/api/system/end"
// 🚀 引入 Archive 的新增接口
import { addArchive } from "@/api/system/archive"
import { checkRole } from "@/utils/permission"
import { getUserProfile } from "@/api/system/user"

export default {
  name: "End",
  dicts: ['sys_audit_status', 'sys_yes_no'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      endList: [],
      title: "",
      open: false,
      activeTab: 'ALL',
      currentUser: {},
      auditOpen: false,
      auditForm: {},
      myStartOptions: [],

      // 🚀 归档弹窗变量
      archiveOpen: false,
      archiveForm: {},
      archiveRules: {
        archiverName: [{ required: true, message: "请输入归档人", trigger: "blur" }]
      },

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        leaderName: null,
        tabType: 'ALL'
      },
      form: {},
      rules: {
        startId: [{ required: true, message: "请选择关联的外业申请", trigger: "change" }],
        actStartDate: [{ required: true, message: "请选择时间", trigger: "change" }],
        actDays: [{ required: true, message: "请输入天数", trigger: "blur" }]
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
      listEnd(this.queryParams).then(response => {
        this.endList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(() => this.loading = false);
    },

    getMyStartOptions() {
      listMyAvailableStart().then(res => {
        this.myStartOptions = res.data;
      });
    },

    handleStartSelectChange(selectedStartId) {
      const selectedItem = this.myStartOptions.find(item => item.startId === selectedStartId);
      if (selectedItem) {
        this.form.leaderName = selectedItem.leaderName;
      }
    },

    /** 🚀 打开归档弹窗 */
    handleArchive(row) {
      this.archiveForm = {
        startId: row.startId,       // 关联ID
        projectName: row.projectName, // 回显
        leaderName: row.leaderName,   // 回显
        archiverName: this.currentUser.nickName, // 默认当前用户
        // 其他字段置空
        workPhotoUrl: null,
        speciesPhotoUrl: null,
        habitatPhotoUrl: null,
        ovitalDataUrl: null,
        cameraDataUrl: null,
        infoSheetUrl: null,
        auditStatus: '0'
      };
      this.archiveOpen = true;
    },

    /** 🚀 提交归档 */
    submitArchiveForm() {
      this.$refs["archiveForm"].validate(valid => {
        if (valid) {
          addArchive(this.archiveForm).then(response => {
            this.$modal.msgSuccess("归档申请提交成功");
            this.archiveOpen = false;
            this.getList(); // 刷新列表，更新状态
          });
        }
      });
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
        endId: null,
        startId: null,
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
      this.ids = selection.map(item => item.endId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "填写外业行程结束";
      this.getMyStartOptions();
    },

    handleUpdate(row) {
      this.reset();
      const endId = row.endId || this.ids;
      getEnd(endId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改外业行程结束";
        this.getMyStartOptions();
      });
    },

    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.endId != null) {
            updateEnd(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEnd(this.form).then(response => {
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
        endId: row.endId,
        auditStatus: '1',
        auditReason: ''
      };
      this.auditOpen = true;
    },
    submitAudit() {
      if(this.auditForm.auditStatus === '2' && !this.auditForm.auditReason){
        this.$modal.msgError("请填写驳回原因"); return;
      }
      auditEnd(this.auditForm).then(res => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },

    handleDelete(row) {
      const endIds = row.endId || this.ids;
      this.$modal.confirm('确认删除？').then(function() {
        return delEnd(endIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('system/end/export', {
        ...this.queryParams
      }, `end_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
