<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部记录 (已通过)" name="public"></el-tab-pane>

      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="audit"
        v-if="checkRole(['teacher', 'meetingImplementation_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="会议地点" prop="meetingPlace">
        <el-input
          v-model="queryParams.meetingPlace"
          placeholder="请输入会议地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主持人" prop="hostPerson">
        <el-input
          v-model="queryParams.hostPerson"
          placeholder="请输入主持人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="auditStatus" v-if="activeTab !== 'public'">
        <el-select v-model="queryParams.auditStatus" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_audit_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
          v-if="checkRole(['student', 'meetingImplementation_manager', 'admin'])"
        >新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-if="checkRole(['meetingImplementation_manager', 'admin'])"
        >修改</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-if="checkRole(['meetingImplementation_manager', 'admin'])"
        >删除</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-document-copy"
          size="mini"
          @click="handleOnlineDoc"
        >在线文档 (多人协作)</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="group_meeting_recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="会议时间" align="center" prop="meetingTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.meetingTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会议地点" align="center" prop="meetingPlace" show-overflow-tooltip/>
      <el-table-column label="主持人" align="center" prop="hostPerson" width="100"/>
      <el-table-column label="参加人员" align="center" prop="participants" show-overflow-tooltip/>
      <el-table-column label="缺席人员" align="center" prop="absentees" show-overflow-tooltip/>

      <el-table-column label="图片/文件" align="center" width="150">
        <template slot-scope="scope">
          <image-preview v-if="scope.row.imageUrls" :src="scope.row.imageUrls" :width="30" :height="30" style="margin-right: 5px"/>
          <el-button
            v-if="scope.row.fileUrls"
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="$download.resource(scope.row.fileUrls)">
            下载</el-button>
        </template>
      </el-table-column>

      <el-table-column label="审核状态" align="center" prop="auditStatus" width="100">
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

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200" v-if="activeTab !== 'public'">
        <template slot-scope="scope">

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if="checkRole(['meetingImplementation_manager', 'admin']) || (checkRole(['student']) && activeTab === 'private' && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="checkRole(['meetingImplementation_manager', 'admin']) || (checkRole(['student']) && activeTab === 'private' && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:group_meeting_record:audit']"
            v-if="checkRole(['teacher', 'meetingImplementation_manager', 'admin']) && scope.row.auditStatus == '0'"
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="会议时间" prop="meetingTime">
              <el-date-picker
                clearable
                v-model="form.meetingTime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择时间"
                :picker-options="pickerOptions"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主持人" prop="hostPerson">
              <el-input v-model="form.hostPerson" placeholder="请输入主持人" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="会议地点" prop="meetingPlace">
          <el-input v-model="form.meetingPlace" placeholder="请输入会议地点" />
        </el-form-item>

        <el-form-item label="参加人员" prop="participants">
          <el-input v-model="form.participants" type="textarea" :rows="2" placeholder="请输入参加人员" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="缺席人员" prop="absentees">
              <el-input v-model="form.absentees" placeholder="无缺席填'无'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="缺席原因" prop="absentReason">
              <el-select v-model="form.absentReason" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.absent_reasons"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="现场图片" prop="imageUrls">
          <image-upload v-model="form.imageUrls" :limit="3"/>
        </el-form-item>

        <el-form-item label="会议资料" prop="fileUrls">
          <file-upload v-model="form.fileUrls" :limit="1"/>
        </el-form-item>

        <el-form-item label="驳回原因" v-if="form.auditStatus == '2'">
          <el-input v-model="form.auditReason" type="textarea" disabled style="color: #f56c6c"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="auditTitle" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" :rules="auditRules" label-width="80px">
        <el-form-item label="会议时间">
          <span>{{ parseTime(auditForm.meetingTime, '{y}-{m}-{d}') }}</span>
        </el-form-item>
        <el-form-item label="主持人">
          <span>{{ auditForm.hostPerson }}</span>
        </el-form-item>
        <el-form-item label="审核结果" prop="auditStatus">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="驳回原因"
          prop="auditReason"
          v-if="auditForm.auditStatus === '2'"
        >
          <el-input
            v-model="auditForm.auditReason"
            type="textarea"
            placeholder="请输入驳回原因"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">提交审核</el-button>
        <el-button @click="cancelAudit">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listGroup_meeting_record, getGroup_meeting_record, delGroup_meeting_record,
  addGroup_meeting_record, updateGroup_meeting_record, auditGroup_meeting_record
} from "@/api/system/group_meeting_record"
import { checkRole } from "@/utils/permission";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Group_meeting_record",
  dicts: ['sys_audit_status', 'absent_reasons'],
  data() {
    return {
      // 🚀 替换成你自己的文档链接
      onlineDocUrl: "https://docs.qq.com/doc/DSE1OYk9WWXpNc1hq#",

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
      // 表格数据
      group_meeting_recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      // --- Tab 相关变量 ---
      activeTab: 'public', // 默认选中全部记录
      currentUser: {},

      // 审核相关
      auditOpen: false,
      auditTitle: "",
      auditForm: {},
      auditRules: {
        auditStatus: [{ required: true, message: "审核状态不能为空", trigger: "change" }],
        auditReason: [{ required: true, message: "驳回原因不能为空", trigger: "blur" }]
      },

      // 日期选择器配置
      pickerOptions: {
        disabledDate: (time) => {
          return time.getTime() > Date.now();
        }
      },

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        meetingPlace: null,
        hostPerson: null,
        auditStatus: null,
        // 初始化 params 用于后端 Tab 判断
        params: {
          queryMode: 'public'
        }
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        meetingTime: [{ required: true, message: "会议时间不能为空", trigger: "blur" }],
        meetingPlace: [{ required: true, message: "会议地点不能为空", trigger: "blur" }],
        hostPerson: [{ required: true, message: "主持人不能为空", trigger: "blur" }],
        participants: [{ required: true, message: "参加人员不能为空", trigger: "blur" }],
      }
    }
  },

  created() {
    this.getCurrentUser();
    this.getList();
  },

  methods: {
    checkRole,

    // 🚀 打开在线文档
    handleOnlineDoc() {
      window.open(this.onlineDocUrl, '_blank');
    },

    // 获取当前用户信息
    getCurrentUser() {
      getUserProfile().then(response => {
        this.currentUser = response.data.user || response.data;
      });
    },

    // 查询列表
    getList() {
      this.loading = true;
      if (!this.queryParams.params) this.queryParams.params = {};

      // 将 Tab 类型传给后端
      this.queryParams.params['queryMode'] = this.activeTab;

      // 如果是公开 Tab，清空审核状态筛选，默认查通过的
      if (this.activeTab === 'public') {
        this.queryParams.auditStatus = undefined;
      }

      listGroup_meeting_record(this.queryParams).then(response => {
        this.group_meeting_recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 切换 Tab
    handleTabClick(tab, event) {
      this.queryParams.pageNum = 1;
      this.resetForm("queryForm");
      this.getList();
    },

    // 打开审核弹窗
    handleAuditOpen(row) {
      this.auditForm = {
        id: row.id,
        meetingTime: row.meetingTime,
        hostPerson: row.hostPerson,
        auditStatus: '1', // 默认通过
        auditReason: ''
      };
      this.auditOpen = true;
      this.auditTitle = "审核大组会实施记录";
    },

    // 取消审核
    cancelAudit() {
      this.auditOpen = false;
      this.resetForm("auditForm");
    },

    // 提交审核
    submitAudit() {
      this.$refs["auditForm"].validate(valid => {
        if (valid) {
          auditGroup_meeting_record(this.auditForm).then(response => {
            this.$modal.msgSuccess("审核完成");
            this.auditOpen = false;
            this.getList();
          });
        }
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    // 表单重置
    reset() {
      this.form = {
        id: null,
        meetingTime: null,
        meetingPlace: null,
        hostPerson: null,
        participants: null,
        absentees: null,
        absentReason: null,
        imageUrls: null,
        fileUrls: null,
        auditStatus: null,
        auditReason: null
      };
      this.resetForm("form");
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加大组会实施记录";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getGroup_meeting_record(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改大组会实施记录";
      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateGroup_meeting_record(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGroup_meeting_record(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() {
        return delGroup_meeting_record(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('system/group_meeting_record/export', {
        ...this.queryParams
      }, `group_meeting_record_${new Date().getTime()}.xlsx`);
    }
  }
}
</script>
