<template>
  <div class="app-container">
    <div class="view-switch" style="margin-bottom: 20px;">
      <el-radio-group v-model="activeView" size="mini">
        <el-radio-button label="table" @click.native="switchView('table')">表格视图</el-radio-button>
        <el-radio-button label="calendar" @click.native="switchView('calendar')">日历视图</el-radio-button>
        <el-radio-button label="timeline" @click.native="switchView('timeline')">时间轴视图</el-radio-button>
      </el-radio-group>
    </div>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="地点" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入地点"
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
          v-hasPermi="['system:groupmeeting:add']"
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
          v-hasPermi="['system:groupmeeting:edit']"
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
          v-hasPermi="['system:groupmeeting:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:groupmeeting:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <div v-if="activeView === 'table'">
      <el-table v-loading="loading" :data="meetingList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" align="center" width="60">
          <template slot-scope="scope">
            {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="计划时间" align="center" prop="scheduledTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.scheduledTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="地点" align="center" prop="location" />
        <el-table-column label="组会负责人" align="center" prop="responsiblePerson" />
        <el-table-column label="会议安排" align="center" prop="meetingDocument">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.meetingDocument"
              type="text"
              size="mini"
              icon="el-icon-document"
              @click="handleDownload(scope.row.meetingDocument)"
            >下载文档</el-button>
            <span v-else>无文档</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:groupmeeting:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:groupmeeting:remove']"
            >删除</el-button>
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
    </div>

    <div v-else-if="activeView === 'calendar'">
      <el-calendar v-model="calendarDate">
        <template #dateCell="{date, data}">
          <div class="calendar-date">
            <div class="date-number">{{ data.day.split('-')[2] }}</div>
            <div v-for="meeting in getMeetingsByDate(date)"
                 :key="meeting.id"
                 class="meeting-item"
                 @click="handleCalendarItemClick(meeting)">
              <div>{{ meeting.location }}</div>
              <div style="font-size: 12px; color: #666;">{{ meeting.responsiblePerson }}</div>
              <div v-if="meeting.meetingDocument" style="font-size: 10px; color: #409EFF;">
                📎 有文档
              </div>
            </div>
          </div>
        </template>
      </el-calendar>
    </div>

    <div v-else-if="activeView === 'timeline'">
      <el-timeline>
        <el-timeline-item
          v-for="meeting in timelineData"
          :key="meeting.id"
          :timestamp="formatTime(meeting.scheduledTime)"
          placement="top">
          <el-card>
            <h4>{{ meeting.location }}</h4>
            <p>负责人：{{ meeting.responsiblePerson }}</p >
            <p>时间：{{ formatTime(meeting.scheduledTime) }}</p >
            <div v-if="meeting.meetingDocument">
              <el-button
                type="text"
                icon="el-icon-document"
                @click="handleDownload(meeting.meetingDocument)"
              >下载会议文档</el-button>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="计划时间" prop="scheduledTime">
          <el-date-picker clearable
                          v-model="form.scheduledTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择计划时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入地点" />
        </el-form-item>
        <el-form-item label="组会负责人" prop="responsiblePerson">
          <el-input v-model="form.responsiblePerson" placeholder="请输入组会负责人" />
        </el-form-item>
        <el-form-item label="会议安排" prop="meetingDocument">
          <file-upload
            v-model="form.meetingDocument"
            :headers="uploadHeaders"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 🚀 修复引入：虽然你之前引入的是 listMeeting，但建议统一用 groupmeeting 相关的命名
// 请确保你的 src/api/system/groupmeeting.js 文件存在，并且里面导出的方法名正确
import { listGroupmeeting, getGroupmeeting, delGroupmeeting, addGroupmeeting, updateGroupmeeting } from "@/api/system/groupmeeting"
import { getToken } from "@/utils/auth";

export default {
  name: "GroupMeeting", // 建议改名，避免与之前的 Meeting 混淆
  data() {
    return {
      activeView: 'table',
      calendarDate: new Date(),
      timelineData: [],
      uploadHeaders: { Authorization: "Bearer " + getToken() },
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      meetingList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        location: null,
      },
      form: {},
      rules: {
        scheduledTime: [
          { required: true, message: "计划时间不能为空", trigger: "blur" }
        ],
        location: [
          { required: true, message: "地点不能为空", trigger: "blur" }
        ],
        responsiblePerson: [
          { required: true, message: "组会负责人不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    switchView(view) {
      if (event) {
        event.preventDefault();
        event.stopPropagation();
      }
      this.activeView = view;
      if (view === 'calendar') {
        this.loadCalendarData();
      } else if (view === 'timeline') {
        this.loadTimelineData();
      }
    },

    handleDownload(fileUrl) {
      if (!fileUrl) {
        this.$modal.msgError("文件路径不存在");
        return;
      }
      this.$download.resource(fileUrl);
    },

    /** 查询列表 */
    getList() {
      this.loading = true
      // 🚀 使用正确的 API 方法名
      listGroupmeeting(this.queryParams).then(response => {
        this.meetingList = response.rows
        this.total = response.total
        this.loading = false
        if (this.activeView === 'calendar') {
          this.loadCalendarData()
        } else if (this.activeView === 'timeline') {
          this.loadTimelineData()
        }
      })
    },

    loadCalendarData() {
      this.timelineData = this.meetingList
    },

    loadTimelineData() {
      this.timelineData = [...this.meetingList].sort((a, b) => {
        return new Date(a.scheduledTime) - new Date(b.scheduledTime)
      })
    },

    getMeetingsByDate(date) {
      const dateStr = this.parseTime(date, '{y}-{m}-{d}')
      return this.meetingList.filter(meeting => {
        const meetingDate = this.parseTime(meeting.scheduledTime, '{y}-{m}-{d}')
        return meetingDate === dateStr
      })
    },

    handleCalendarItemClick(meeting) {
      if (meeting.meetingDocument) {
        this.$confirm('该会议有相关文档，是否下载？', '提示', {
          confirmButtonText: '下载',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.handleDownload(meeting.meetingDocument)
        }).catch(() => {})
      }
    },

    formatTime(time) {
      return this.parseTime(time, '{y}-{m}-{d} {h}:{i}')
    },

    cancel() {
      this.open = false
      this.reset()
    },

    reset() {
      this.form = {
        id: null,
        scheduledTime: null,
        location: null,
        responsiblePerson: null,
        meetingDocument: null
      }
      this.resetForm("form")
    },

    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },

    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加大组会安排"
    },

    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      // 🚀 使用正确的 API 方法名
      getGroupmeeting(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改大组会安排"
      })
    },

    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            // 🚀 使用正确的 API 方法名
            updateGroupmeeting(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            // 🚀 使用正确的 API 方法名
            addGroupmeeting(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },

    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除大组会安排编号为"' + ids + '"的数据项？').then(function() {
        // 🚀 使用正确的 API 方法名
        return delGroupmeeting(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },

    handleExport() {
      this.download('system/groupmeeting/export', {
        ...this.queryParams
      }, `groupmeeting_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.calendar-date {
  height: 100%;
  min-height: 80px;
}
.date-number {
  font-weight: bold;
  margin-bottom: 5px;
}
.meeting-item {
  font-size: 12px;
  background: #f5f7fa;
  padding: 3px 6px;
  margin: 2px 0;
  border-radius: 3px;
  cursor: pointer;
  border-left: 3px solid #409EFF;
}
.meeting-item:hover {
  background: #e4e7ed;
}
.view-switch {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}
</style>
