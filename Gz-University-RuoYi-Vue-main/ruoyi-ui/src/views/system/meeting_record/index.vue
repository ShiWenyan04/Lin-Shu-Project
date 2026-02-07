<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部记录 (已通过)" name="public"></el-tab-pane>
      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="audit"
        v-if="checkRole(['teacher', 'meetingrecord_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="会议名称" prop="meetingName">
        <el-input v-model="queryParams.meetingName" placeholder="请输入会议名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="举办单位" prop="hostUnit">
        <el-input v-model="queryParams.hostUnit" placeholder="请输入举办单位" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:meeting_record:add']">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="meeting_recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" width="100" />
      <el-table-column label="会议名称" align="center" prop="meetingName" show-overflow-tooltip/>
      <el-table-column label="举办单位" align="center" prop="hostUnit" show-overflow-tooltip/>
      <el-table-column label="参会人员" align="center" prop="participants" show-overflow-tooltip/>
      <el-table-column label="出发日期" align="center" prop="departureDate" width="100">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.departureDate, '{y}-{m}-{d}') }}</span></template>
      </el-table-column>
      <el-table-column label="返校日期" align="center" prop="returnDate" width="100">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.returnDate, '{y}-{m}-{d}') }}</span></template>
      </el-table-column>
      <el-table-column label="汇报人" align="center" prop="reporter" />
      <el-table-column label="会议手册" align="center" prop="meetingManual">
        <template slot-scope="scope">
          <el-button v-if="scope.row.meetingManual" size="mini" type="text" @click="handleDownload(scope.row.meetingManual)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="精选照片" align="center" prop="photos" width="100">
        <template slot-scope="scope"><image-preview :src="scope.row.photos" :width="50" :height="50"/></template>
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
                       v-if="activeTab !== 'public'">
        <template slot-scope="scope">

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:meeting_record:edit']"
            v-if="checkRole(['meetingrecord_manager', 'admin']) || (checkRole(['student']) && scope.row.auditStatus != '1' && scope.row.createBy === currentUser.userName)"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:meeting_record:remove']"
            v-if="checkRole(['meetingrecord_manager', 'admin']) || (checkRole(['student']) && scope.row.auditStatus != '1' && scope.row.createBy === currentUser.userName)"
          >删除</el-button>

          <el-button
            v-if="scope.row.auditStatus === '0' && checkRole(['teacher', 'admin'])"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:meeting_record:audit']"
          >审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="会议名称" prop="meetingName">
              <el-input v-model="form.meetingName" placeholder="请输入会议名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="举办单位" prop="hostUnit">
              <el-input v-model="form.hostUnit" placeholder="请输入举办单位" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="汇报人" prop="reporter">
              <el-input v-model="form.reporter" placeholder="请输入汇报人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="汇报题目" prop="reportTopic">
              <el-input v-model="form.reportTopic" placeholder="请输入汇报题目" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出发日期" prop="departureDate">
              <el-date-picker
                clearable
                v-model="form.departureDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择出发日期"
                @change="handleDepartureDateChange"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="返校日期" prop="returnDate">
              <el-date-picker
                clearable
                v-model="form.returnDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择返校日期"
                :picker-options="returnPickerOptions"
                :disabled="!form.departureDate"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属会场" prop="venue">
              <el-input v-model="form.venue" placeholder="请输入所属会场" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="返校汇报" prop="isReport">
              <el-select v-model="form.isReport" placeholder="请选择返校汇报" style="width: 100%">
                <el-option v-for="dict in dict.type.sys_isnot" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="参会人员" prop="participantsArr">
              <el-select
                v-model="form.participantsArr"
                multiple
                filterable
                placeholder="请选择参会人员"
                style="width: 100%"
              >
                <el-option
                  v-for="item in userList"
                  :key="item.userId"
                  :label="item.nickName"
                  :value="item.nickName"
                >
                  <span style="float: left">{{ item.nickName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.dept ? item.dept.deptName : '' }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="会议手册" prop="meetingManual">
              <file-upload v-model="form.meetingManual"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="精选照片" prop="photos">
              <image-upload v-model="form.photos"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="form.auditStatus === '2'">
          <el-col :span="24">
            <el-form-item label="驳回原因">
              <el-input v-model="form.auditReason" type="textarea" disabled />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核参会记录" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="会议名称">
          <el-input v-model="auditForm.meetingName" disabled />
        </el-form-item>
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" v-if="auditForm.auditStatus == '2'">
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
import { listMeeting_record, getMeeting_record, delMeeting_record, addMeeting_record, updateMeeting_record, auditMeeting_record } from "@/api/system/meeting_record"
import { listUser } from "@/api/system/user";
import { checkRole } from "@/utils/permission";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Meeting_record",
  dicts: ['sys_audit_status', 'sys_isnot'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      meeting_recordList: [],
      title: "",
      open: false,
      activeTab: 'public',
      auditOpen: false,
      auditForm: {},
      userList: [],
      currentUser: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        meetingName: null,
        hostUnit: null,
        reporter: null,
        params: { queryMode: 'public' }
      },
      form: {
        participantsArr: []
      },
      // 修改：去掉了 departurePickerOptions 里的 disabledDate，允许选过去的时间
      departurePickerOptions: {},
      // 保留：返校日期必须大于出发日期
      returnPickerOptions: {
        disabledDate: (time) => {
          if (this.form.departureDate) {
            return time.getTime() <= new Date(this.form.departureDate).setHours(0,0,0,0);
          }
          return true;
        }
      },
      rules: {
        meetingName: [ { required: true, message: "会议名称不能为空", trigger: "blur" } ],
        hostUnit: [ { required: true, message: "举办单位不能为空", trigger: "blur" } ],
        participantsArr: [ { required: true, message: "参会人员不能为空", trigger: "change" } ],
        // 修改：去掉了自定义校验 validator
        departureDate: [ { required: true, message: "出发日期不能为空", trigger: "change" } ],
        // 修改：去掉了自定义校验 validator，保留 required
        returnDate: [ { required: true, message: "返校日期不能为空", trigger: "change" } ],
        reporter: [ { required: true, message: "汇报人不能为空", trigger: "blur" } ],
        reportTopic: [ { required: true, message: "汇报题目不能为空", trigger: "blur" } ],
        meetingManual: [ { required: true, message: "会议手册不能为空", trigger: "change" } ],
        photos: [ { required: true, message: "精选照片不能为空", trigger: "change" } ],
        isReport: [ { required: true, message: "请选择是否返校汇报", trigger: "change" } ],
      }
    }
  },
  created() {
    this.getList();
    this.getCurrentUser();
    this.getUserList();
  },
  methods: {
    checkRole,
    getCurrentUser() {
      getUserProfile().then(response => {
        this.currentUser = response.data || response.data.user;
      });
    },
    getUserList() {
      listUser({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.userList = response.rows;
      });
    },
    getList() {
      this.loading = true;
      if (!this.queryParams.params) this.queryParams.params = {};
      this.queryParams.params['queryMode'] = this.activeTab;
      if (this.activeTab === 'public') {
        this.queryParams.auditStatus = undefined;
      }
      listMeeting_record(this.queryParams).then(response => {
        this.meeting_recordList = response.rows;
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
        meetingName: null,
        hostUnit: null,
        participants: null,
        participantsArr: [],
        departureDate: null,
        returnDate: null,
        reporter: null,
        reportTopic: null,
        venue: null,
        meetingManual: null,
        photos: null,
        isReport: null,
        auditStatus: null,
        auditReason: null
      };
      this.resetForm("form");
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList(); },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery(); },
    handleSelectionChange(selection) { this.ids = selection.map(item => item.id); this.single = selection.length!==1; this.multiple = !selection.length; },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加参会记录";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getMeeting_record(id).then(response => {
        this.form = response.data;
        if (this.form.participants) {
          this.$set(this.form, 'participantsArr', this.form.participants.split(','));
        } else {
          this.$set(this.form, 'participantsArr', []);
        }
        this.open = true;
        this.title = "修改参会记录";
      });
    },
    handleDepartureDateChange(value) { if (value !== this.form.returnDate) { this.form.returnDate = null; } },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.participantsArr && this.form.participantsArr.length > 0) {
            this.form.participants = this.form.participantsArr.join(',');
          } else {
            this.form.participants = '';
          }
          if (this.form.departureDate && this.form.returnDate) {
            if (new Date(this.form.returnDate) <= new Date(this.form.departureDate)) {
              this.$modal.msgError('返校日期必须大于出发日期'); return;
            }
          }
          if (this.form.id != null) {
            updateMeeting_record(this.form).then(response => {
              this.$modal.msgSuccess("修改成功"); this.open = false; this.getList();
            });
          } else {
            addMeeting_record(this.form).then(response => {
              this.$modal.msgSuccess("新增成功"); this.open = false; this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() { return delMeeting_record(ids); }).then(() => { this.getList(); this.$modal.msgSuccess("删除成功"); }).catch(() => {});
    },
    handleExport() { this.download('system/meeting_record/export', { ...this.queryParams }, `meeting_record_${new Date().getTime()}.xlsx`); },
    handleAuditOpen(row) {
      this.auditForm = { id: row.id, meetingName: row.meetingName, auditStatus: '1', auditReason: '' };
      this.auditOpen = true;
    },
    submitAudit() {
      if (this.auditForm.auditStatus === '2' && !this.auditForm.auditReason) {
        this.$modal.msgError("驳回时必须填写审核意见"); return;
      }
      auditMeeting_record(this.auditForm).then(response => {
        this.$modal.msgSuccess("审核完成"); this.auditOpen = false; this.getList();
      });
    },
    handleDownload(url) {
      if (!url) return;
      const downloadUrl = url.startsWith("http") ? url : process.env.VUE_APP_BASE_API + url;
      window.open(downloadUrl);
    }
  }
}
</script>
