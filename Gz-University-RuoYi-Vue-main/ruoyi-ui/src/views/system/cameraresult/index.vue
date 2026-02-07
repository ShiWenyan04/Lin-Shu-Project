<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick" style="margin-bottom: 10px;">
      <el-tab-pane label="公开成果" name="public"><span slot="label"><i class="el-icon-share"></i> 公开成果</span></el-tab-pane>
      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher', 'admin'])"><span slot="label"><i class="el-icon-user"></i> 我的提交</span></el-tab-pane>
      <el-tab-pane label="审核管理" name="audit" v-if="checkRole(['teacher', 'cameraResults_manager', 'admin'])"><span slot="label"><i class="el-icon-s-check"></i> 审核管理</span></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="地点" prop="regionName">
        <el-input v-model="queryParams.regionName" placeholder="请输入地点" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="撰写人" prop="writerName">
        <el-input v-model="queryParams.writerName" placeholder="请输入撰写人" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5" v-if="activeTab !== 'public' || checkRole(['admin'])">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:cameraresult:add']">新增成果</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cameraresultList" @selection-change="handleSelectionChange" border>
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="地点" align="center" prop="regionName" />
      <el-table-column label="监测时间" align="center" width="200">
        <template slot-scope="scope">
          {{ parseTime(scope.row.periodStart, '{y}-{m}-{d}') }} 至 {{ parseTime(scope.row.periodEnd, '{y}-{m}-{d}') }}
        </template>
      </el-table-column>

      <el-table-column label="关联项目" align="center" prop="relatedIds" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="!scope.row.relatedIds">无关联</span>
          <span v-else>
            {{ formatRelatedProjects(scope.row.relatedIds) }}
          </span>
        </template>
      </el-table-column>

      <el-table-column label="撰写人" align="center" prop="writerName" />

      <el-table-column label="审核状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.auditStatus == '0'" type="warning">待审核</el-tag>
          <el-tag v-else-if="scope.row.auditStatus == '1'" type="success">已通过</el-tag>
          <el-tooltip v-else-if="scope.row.auditStatus == '2'" :content="scope.row.auditReason" placement="top">
            <el-tag type="danger" style="cursor:pointer">已驳回 <i class="el-icon-question"></i></el-tag>
          </el-tooltip>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini" type="text" icon="el-icon-view"
            @click="handleView(scope.row)"
          >详情</el-button>

          <el-button
            v-if="checkRole(['admin', 'teacher', 'cameraResults_manager']) || (scope.row.createBy == currentUser.userName && scope.row.auditStatus != '1')"
            size="mini" type="text" icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>

          <el-button
            v-if="checkRole(['admin', 'teacher', 'cameraResults_manager']) || (scope.row.createBy == currentUser.userName && scope.row.auditStatus != '1')"
            size="mini" type="text" icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>

          <el-button
            v-if="checkRole(['teacher', 'cameraResults_manager', 'admin']) && scope.row.auditStatus == '0'"
            size="mini" type="text" icon="el-icon-check" style="color: #E6A23C"
            @click="handleAuditOpen(scope.row)"
          >审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-alert v-if="form.auditStatus == '2'" :title="'被驳回原因：' + form.auditReason" type="error" show-icon :closable="false" style="margin-bottom:15px;"/>

        <el-form-item label="地点(区位)" prop="regionName">
          <el-input v-model="form.regionName" placeholder="如：秦岭保护区核心区" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="periodStart">
              <el-date-picker clearable v-model="form.periodStart" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="periodEnd">
              <el-date-picker clearable v-model="form.periodEnd" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="关联项目" prop="relatedIds">
          <el-select v-model="relatedIdsArray" multiple placeholder="请选择关联的安装项目(可多选)" style="width: 100%" filterable>
            <el-option
              v-for="item in projectOptions"
              :key="item.id"
              :label="item.projectName + ' (' + item.regionName + ')'"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-row>
          <el-col :span="12"><el-form-item label="撰写人" prop="writerName"><el-input v-model="form.writerName" /></el-form-item></el-col>
        </el-row>

        <el-form-item label="监测方案" prop="monitorPlanFile"><file-upload v-model="form.monitorPlanFile" :limit="1"/></el-form-item>
        <el-form-item label="中期报告" prop="interimRptFile"><file-upload v-model="form.interimRptFile" :limit="1"/></el-form-item>
        <el-form-item label="结题报告" prop="finalRptFile"><file-upload v-model="form.finalRptFile" :limit="1"/></el-form-item>
        <el-form-item label="汇报PPT" prop="pptFile"><file-upload v-model="form.pptFile" :limit="1"/></el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="成果详情" :visible.sync="detailOpen" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="地点">{{ detailForm.regionName }}</el-descriptions-item>
        <el-descriptions-item label="撰写人">{{ detailForm.writerName }}</el-descriptions-item>
        <el-descriptions-item label="监测时间" :span="2">
          {{ parseTime(detailForm.periodStart, '{y}-{m}-{d}') }} 至 {{ parseTime(detailForm.periodEnd, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item label="关联项目" :span="2">
          {{ formatRelatedProjects(detailForm.relatedIds) || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag v-if="detailForm.auditStatus == '0'" type="warning">待审核</el-tag>
          <el-tag v-else-if="detailForm.auditStatus == '1'" type="success">已通过</el-tag>
          <el-tag v-else type="danger">已驳回</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="驳回原因" v-if="detailForm.auditStatus == '2'">{{ detailForm.auditReason }}</el-descriptions-item>

        <el-descriptions-item label="监测方案" :span="2">
          <el-link v-if="detailForm.monitorPlanFile" type="primary" :href="getFileUrl(detailForm.monitorPlanFile)" target="_blank">
            <i class="el-icon-download"></i> 点击下载/预览
          </el-link>
          <span v-else style="color: #909399">未上传</span>
        </el-descriptions-item>

        <el-descriptions-item label="中期报告" :span="2">
          <el-link v-if="detailForm.interimRptFile" type="primary" :href="getFileUrl(detailForm.interimRptFile)" target="_blank">
            <i class="el-icon-download"></i> 点击下载/预览
          </el-link>
          <span v-else style="color: #909399">未上传</span>
        </el-descriptions-item>

        <el-descriptions-item label="结题报告" :span="2">
          <el-link v-if="detailForm.finalRptFile" type="primary" :href="getFileUrl(detailForm.finalRptFile)" target="_blank">
            <i class="el-icon-download"></i> 点击下载/预览
          </el-link>
          <span v-else style="color: #909399">未上传</span>
        </el-descriptions-item>

        <el-descriptions-item label="汇报PPT" :span="2">
          <el-link v-if="detailForm.pptFile" type="primary" :href="getFileUrl(detailForm.pptFile)" target="_blank">
            <i class="el-icon-download"></i> 点击下载/预览
          </el-link>
          <span v-else style="color: #909399">未上传</span>
        </el-descriptions-item>

        <el-descriptions-item label="备注" :span="2">{{ detailForm.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog title="成果审核" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="地点"><el-input v-model="auditForm.regionName" disabled /></el-form-item>
        <el-form-item label="撰写人"><el-input v-model="auditForm.writerName" disabled /></el-form-item>
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" v-if="auditForm.auditStatus == '2'">
          <el-input type="textarea" v-model="auditForm.auditReason" placeholder="请输入驳回原因" :rows="3"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">确认审核</el-button>
        <el-button @click="auditOpen = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listCameraresult, getCameraresult, delCameraresult, addCameraresult, updateCameraresult, auditCameraresult } from "@/api/system/cameraresult"
import { listCameraInstall } from "@/api/system/CameraInstall"
import { getUserProfile } from "@/api/system/user";
import { checkRole } from "@/utils/permission";

export default {
  name: "Cameraresult",
  data() {
    return {
      activeTab: 'public',
      currentUser: {},
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      cameraresultList: [],
      title: "",
      open: false,
      auditOpen: false, // 审核弹窗
      detailOpen: false, // 详情弹窗

      // 项目列表选项
      projectOptions: [],
      // 临时存储多选的ID数组
      relatedIdsArray: [],

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        regionName: null,
        writerName: null,
        auditStatus: '1', // 默认看公开（已通过）
        createBy: null
      },
      form: {},
      auditForm: {},
      detailForm: {}, // 详情数据
      rules: {
        regionName: [{ required: true, message: "地点不能为空", trigger: "blur" }],
        periodStart: [{ required: true, message: "时间不能为空", trigger: "change" }],
        relatedIds: [{ required: true, message: "请至少关联一个项目", trigger: "change" }]
      }
    }
  },
  created() {
    this.getUser();
    this.getProjectList();
    this.getList();
  },
  methods: {
    checkRole,
    getUser() {
      getUserProfile().then(response => {
        this.currentUser = response.data.user || response.data;
      });
    },
    // 获取已通过审核的安装项目供选择
    getProjectList() {
      listCameraInstall({ auditStatus: '1', pageSize: 1000 }).then(res => {
        this.projectOptions = res.rows;
      });
    },

    // 拼接完整的下载地址
    getFileUrl(url) {
      if (!url) return '';
      // 如果数据库存的是完整http路径则直接返回，如果是相对路径则拼接BaseAPI
      if (url.startsWith('http')) {
        return url;
      }
      return process.env.VUE_APP_BASE_API + url;
    },

    // Tab 切换逻辑
    handleTabClick() {
      this.resetForm("queryForm");
      if (this.activeTab === 'public') {
        this.queryParams.auditStatus = '1';
        this.queryParams.createBy = null;
      } else if (this.activeTab === 'private') {
        this.queryParams.auditStatus = null; // 全部状态
        this.queryParams.createBy = this.currentUser.userName;
      } else if (this.activeTab === 'audit') {
        this.queryParams.auditStatus = '0'; // 待审核
        this.queryParams.createBy = null;
      }
      this.handleQuery();
    },

    getList() {
      this.loading = true;
      listCameraresult(this.queryParams).then(response => {
        this.cameraresultList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 格式化显示关联项目名称
    formatRelatedProjects(idsStr) {
      if (!idsStr) return '';
      const ids = idsStr.split(',');
      const names = [];
      ids.forEach(id => {
        const found = this.projectOptions.find(p => p.id == id);
        if (found) names.push(found.projectName);
        else names.push(id);
      });
      return names.join(', ');
    },

    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        regionName: null,
        periodStart: null,
        periodEnd: null,
        relatedIds: null,
        monitorPlanFile: null,
        interimRptFile: null,
        finalRptFile: null,
        pptFile: null,
        writerName: this.currentUser.nickName, // 默认填当前人
        auditStatus: "0",
        remark: null
      };
      this.relatedIdsArray = []; // 重置多选数组
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
      this.ids = selection.map(item => item.id);
      this.single = selection.length!==1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增成果 (提交后需审核)";
    },
    // 【修改】详情点击逻辑
    handleView(row) {
      this.detailForm = row;
      this.detailOpen = true;
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getCameraresult(id).then(response => {
        this.form = response.data;
        // 回显多选
        if (this.form.relatedIds) {
          this.relatedIdsArray = this.form.relatedIds.split(',').map(Number);
        }
        this.open = true;
        this.title = "修改成果";
      });
    },
    submitForm() {
      if (this.relatedIdsArray.length === 0) {
        this.$modal.msgError("请至少选择一个关联项目");
        return;
      }
      this.form.relatedIds = this.relatedIdsArray.join(',');

      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCameraresult(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCameraresult(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() {
        return delCameraresult(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    // 打开审核
    handleAuditOpen(row) {
      this.auditForm = {
        id: row.id,
        regionName: row.regionName,
        writerName: row.writerName,
        auditStatus: '1',
        auditReason: ''
      };
      this.auditOpen = true;
    },
    // 提交审核
    submitAudit() {
      if (this.auditForm.auditStatus === '2' && !this.auditForm.auditReason) {
        this.$modal.msgError("驳回时必须填写审核意见");
        return;
      }
      auditCameraresult(this.auditForm).then(res => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    }
  }
}
</script>
