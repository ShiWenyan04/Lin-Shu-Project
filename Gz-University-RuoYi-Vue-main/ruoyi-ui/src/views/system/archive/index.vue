<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部归档 (已通过)" name="ALL"></el-tab-pane>
      <el-tab-pane label="我的提交" name="MINE" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="AUDIT"
        v-if="checkRole(['teacher', 'fieldAchive_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="负责人" prop="leaderName">
        <el-input v-model="queryParams.leaderName" placeholder="请输入负责人" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="归档人" prop="archiverName">
        <el-input v-model="queryParams.archiverName" placeholder="请输入归档人" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:archive:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="archiveList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="项目名称" align="center" prop="projectName" min-width="120" show-overflow-tooltip/>
      <el-table-column label="地点" align="center" prop="location" width="100" show-overflow-tooltip/>
      <el-table-column label="负责人" align="center" prop="leaderName" width="80" show-overflow-tooltip/>
      <el-table-column label="归档人" align="center" prop="archiverName" width="80" show-overflow-tooltip/>

      <el-table-column label="工作照路径" align="center" prop="workPhotoUrl" width="120" show-overflow-tooltip/>
      <el-table-column label="物种照路径" align="center" prop="speciesPhotoUrl" width="120" show-overflow-tooltip/>
      <el-table-column label="生境照路径" align="center" prop="habitatPhotoUrl" width="120" show-overflow-tooltip/>
      <el-table-column label="奥维数据路径" align="center" prop="ovitalDataUrl" width="120" show-overflow-tooltip/>
      <el-table-column label="红外数据路径" align="center" prop="cameraDataUrl" width="120" show-overflow-tooltip/>
      <el-table-column label="信息表路径" align="center" prop="infoSheetUrl" width="120" show-overflow-tooltip/>

      <el-table-column label="状态" align="center" prop="auditStatus" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_audit_status" :value="scope.row.auditStatus"/>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200"
                       v-if="checkRole(['teacher', 'fieldAchive_manager', 'admin']) || activeTab === 'MINE'">
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
            v-hasPermi="['system:archive:edit']"
            v-if="checkRole(['fieldAchive_manager', 'admin']) || (scope.row.createBy == currentUser.userId && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:archive:remove']"
            v-if="checkRole(['fieldAchive_manager', 'admin']) || (scope.row.createBy == currentUser.userId && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:archive:audit']"
            v-if="checkRole(['teacher', 'admin']) && scope.row.auditStatus == '0'"
          >审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">

        <el-divider content-position="left"><i class="el-icon-s-home"></i> 基础信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="关联外业任务" prop="startId">
              <el-select
                v-model="form.startId"
                placeholder="请选择已完成的外业任务"
                style="width: 100%"
                :disabled="form.archiveId != null"
                @change="handleStartSelectChange"
                filterable
              >
                <el-option
                  v-for="item in myFinishOptions"
                  :key="item.startId"
                  :label="item.projectName + ' (' + item.destination + ')'"
                  :value="item.startId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="form.projectName" disabled placeholder="自动读取" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地点" prop="location">
              <el-input v-model="form.location" disabled placeholder="自动读取" />
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
            <el-form-item label="归档人" prop="archiverName">
              <el-input v-model="form.archiverName" placeholder="默认当前用户" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left"><i class="el-icon-folder"></i> 资料路径归档 (手动填写)</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="工作照路径" prop="workPhotoUrl">
              <el-input v-model="form.workPhotoUrl" type="textarea" :rows="2" placeholder="请输入工作照存放路径..." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="物种照路径" prop="speciesPhotoUrl">
              <el-input v-model="form.speciesPhotoUrl" type="textarea" :rows="2" placeholder="请输入物种照存放路径..." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="生境照路径" prop="habitatPhotoUrl">
              <el-input v-model="form.habitatPhotoUrl" type="textarea" :rows="2" placeholder="请输入生境照存放路径..." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="奥维数据路径" prop="ovitalDataUrl">
              <el-input v-model="form.ovitalDataUrl" type="textarea" :rows="2" placeholder="请输入奥维数据存放路径..." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="红外数据路径" prop="cameraDataUrl">
              <el-input v-model="form.cameraDataUrl" type="textarea" :rows="2" placeholder="请输入红外相机数据存放路径..." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="信息表路径" prop="infoSheetUrl">
              <el-input v-model="form.infoSheetUrl" type="textarea" :rows="2" placeholder="请输入信息表存放路径..." />
            </el-form-item>
          </el-col>
        </el-row>

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

    <el-dialog title="审核归档" :visible.sync="auditOpen" width="500px" append-to-body>
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

    <el-dialog title="归档详情信息" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="1" border size="medium">
        <el-descriptions-item label="项目名称">{{ viewForm.projectName }}</el-descriptions-item>
        <el-descriptions-item label="地点">{{ viewForm.location }}</el-descriptions-item>
        <el-descriptions-item label="外业负责人">{{ viewForm.leaderName }}</el-descriptions-item>
        <el-descriptions-item label="归档人">{{ viewForm.archiverName }}</el-descriptions-item>

        <el-descriptions-item label="工作照路径">{{ viewForm.workPhotoUrl || '无' }}</el-descriptions-item>
        <el-descriptions-item label="物种照路径">{{ viewForm.speciesPhotoUrl || '无' }}</el-descriptions-item>
        <el-descriptions-item label="生境照路径">{{ viewForm.habitatPhotoUrl || '无' }}</el-descriptions-item>
        <el-descriptions-item label="奥维数据路径">{{ viewForm.ovitalDataUrl || '无' }}</el-descriptions-item>
        <el-descriptions-item label="红外数据路径">{{ viewForm.cameraDataUrl || '无' }}</el-descriptions-item>
        <el-descriptions-item label="信息表路径">{{ viewForm.infoSheetUrl || '无' }}</el-descriptions-item>

        <el-descriptions-item label="审核状态">
          <dict-tag :options="dict.type.sys_audit_status" :value="viewForm.auditStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="审核意见" v-if="viewForm.auditStatus == '2'">{{ viewForm.auditReason }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listArchive, getArchive, delArchive, addArchive, updateArchive, auditArchive, listMyFinishedStarts } from "@/api/system/archive"
import { checkRole } from "@/utils/permission"
import { getUserProfile } from "@/api/system/user"

export default {
  name: "Archive",
  dicts: ['sys_audit_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      archiveList: [],
      title: "",
      open: false,

      // 核心
      activeTab: 'ALL',
      currentUser: {},
      myFinishOptions: [],

      // 审核
      auditOpen: false,
      auditForm: {},

      // 详情
      viewOpen: false,
      viewForm: {},

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectName: null,
        leaderName: null,
        archiverName: null,
        tabType: 'ALL'
      },
      form: {},
      rules: {
        startId: [{ required: true, message: "请选择外业任务", trigger: "change" }],
        archiverName: [{ required: true, message: "归档人不能为空", trigger: "blur" }]
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
      listArchive(this.queryParams).then(response => {
        this.archiveList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(() => this.loading = false);
    },

    getMyFinishOptions() {
      listMyFinishedStarts().then(res => {
        this.myFinishOptions = res.data;
      });
    },

    handleStartSelectChange(selectedStartId) {
      const item = this.myFinishOptions.find(i => i.startId === selectedStartId);
      if (item) {
        this.form.projectName = item.projectName;
        this.form.location = item.destination;
        this.form.leaderName = item.leaderName;
      }
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
        archiveId: null,
        startId: null,
        projectName: null,
        location: null,
        leaderName: null,
        archiverName: this.currentUser.nickName,
        workPhotoUrl: null,
        speciesPhotoUrl: null,
        habitatPhotoUrl: null,
        ovitalDataUrl: null,
        cameraDataUrl: null,
        infoSheetUrl: null,
        auditStatus: '0',
        auditReason: null
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
      this.ids = selection.map(item => item.archiveId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增外业归档";
      this.getMyFinishOptions();
    },
    handleUpdate(row) {
      this.reset();
      const archiveId = row.archiveId || this.ids;
      getArchive(archiveId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改外业归档";
        this.getMyFinishOptions();
      });
    },

    /** 点击详情按钮 */
    handleView(row) {
      this.viewForm = row;
      this.viewOpen = true;
    },

    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.archiveId != null) {
            updateArchive(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addArchive(this.form).then(response => {
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
        archiveId: row.archiveId,
        auditStatus: '1',
        auditReason: ''
      };
      this.auditOpen = true;
    },
    submitAudit() {
      if(this.auditForm.auditStatus === '2' && !this.auditForm.auditReason){
        this.$modal.msgError("请填写驳回原因"); return;
      }
      auditArchive(this.auditForm).then(res => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },

    handleDelete(row) {
      const archiveIds = row.archiveId || this.ids;
      this.$modal.confirm('确认删除？').then(function() {
        return delArchive(archiveIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('system/archive/export', {
        ...this.queryParams
      }, `archive_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
