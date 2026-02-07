<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="年份" prop="year">
        <el-input v-model="queryParams.year" placeholder="请输入年份" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="会议名称" prop="meetingName">
        <el-input v-model="queryParams.meetingName" placeholder="请输入会议名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="召开时间" prop="holdTime">
        <el-date-picker clearable v-model="queryParams.holdTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择召开时间"></el-date-picker>
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
          v-hasPermi="['system:meeting:add']"
          v-if="checkRole(['academicmeeting_manager', 'admin'])"
        >新增</el-button>
      </el-col> <el-col :span="1.5">
      <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:meeting:edit']">修改</el-button>
    </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:meeting:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:meeting:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="meetingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="年份" align="center" prop="year" width="80"/>
      <el-table-column label="会议名称" align="center" prop="meetingName" show-overflow-tooltip/>
      <el-table-column label="举办单位" align="center" prop="hostUnit" show-overflow-tooltip/>
      <el-table-column label="召开时间" align="center" prop="holdTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.holdTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地点" align="center" prop="location" show-overflow-tooltip/>

      <el-table-column label="会议通知" align="center" prop="meetingNotice">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.meetingNotice"
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row.meetingNotice)"
          >下载通知</el-button>
          <span v-else>暂无</span>
        </template>
      </el-table-column>

      <el-table-column label="报名时间" align="center" width="180">
        <template slot-scope="scope">
          <div style="font-size: 12px">起：{{ parseTime(scope.row.signUpStart, '{y}-{m}-{d}') }}</div>
          <div style="font-size: 12px">止：{{ parseTime(scope.row.signUpEnd, '{y}-{m}-{d}') }}</div>
        </template>
      </el-table-column>
      <el-table-column label="注册费用" align="center" prop="registerFee" />
      <el-table-column label="参会人员" align="center" prop="participants" show-overflow-tooltip/>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="240">
        <template slot-scope="scope">
          <span v-if="checkRole(['academicmeeting_manager', 'admin'])">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:meeting:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:meeting:remove']">删除</el-button>
            <el-button size="mini" type="text" icon="el-icon-user" @click="handleCheckSignUp(scope.row)">名单</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button v-if="!scope.row.signedUp" size="mini" type="text" icon="el-icon-circle-plus-outline" @click="handleStudentSignup(scope.row)">报名</el-button>
            <el-button v-else size="mini" type="text" icon="el-icon-circle-close" style="color: #F56C6C;" @click="handleStudentCancel(scope.row)">取消</el-button>
          </span>

          <span v-else-if="checkRole(['student'])">
            <el-button v-if="!scope.row.signedUp" size="mini" type="text" icon="el-icon-circle-plus-outline" @click="handleStudentSignup(scope.row)">报名</el-button>
            <el-button v-else size="mini" type="text" icon="el-icon-circle-close" style="color: #F56C6C;" @click="handleStudentCancel(scope.row)">取消报名</el-button>
          </span>

          <span v-else-if="checkRole(['teacher', 'academicmeeting_manager'])">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleCheckSignUp(scope.row)">报名名单</el-button>
          </span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年份" prop="year">
              <el-input v-model="form.year" placeholder="请输入年份" type="number" :min="currentYear" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会议名称" prop="meetingName">
              <el-input v-model="form.meetingName" placeholder="请输入会议名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="举办单位" prop="hostUnit">
              <el-input v-model="form.hostUnit" placeholder="请输入举办单位" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地点" prop="location">
              <el-input v-model="form.location" placeholder="请输入地点" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="召开时间" prop="holdTime">
              <el-date-picker
                style="width: 100%"
                clearable
                v-model="form.holdTime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择召开时间"
                :picker-options="holdTimeOptions"
                @change="handleHoldTimeChange">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注册费用" prop="registerFee">
              <el-input v-model="form.registerFee" placeholder="请输入注册费用" type="number" :min="0" step="0.01">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报名开始" prop="signUpStart">
              <el-date-picker
                style="width: 100%"
                clearable
                v-model="form.signUpStart"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="开始时间"
                :picker-options="signUpStartOptions"
                @change="handleSignUpStartChange">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报名结束" prop="signUpEnd">
              <el-date-picker
                style="width: 100%"
                clearable
                v-model="form.signUpEnd"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="结束时间"
                :picker-options="signUpEndOptions"
                @change="handleSignUpEndChange">
              </el-date-picker>
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
            <el-form-item label="会议通知" prop="meetingNotice">
              <el-upload
                class="upload-demo"
                :action="uploadUrl"
                :headers="headers"
                :on-success="handleUploadSuccess"
                :on-remove="handleRemove"
                :limit="1"
                :file-list="fileList">
                <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="参会人员" prop="participants">
              <el-input
                v-model="form.participants"
                type="textarea"
                :rows="3"
                placeholder="请手动添加参会人员名单..."
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="报名人员列表" :visible.sync="signupListOpen" width="500px" append-to-body>
      <el-table :data="signupList" border stripe>
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="姓名" align="center" prop="name">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="联系方式" align="center" prop="phone">
          <template slot-scope="scope">
            <span>{{ scope.row.phone }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="signupListCancel">关闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listMeeting, getMeeting, delMeeting, getSignupList, addMeeting, updateMeeting, studentSignup, studentCancelSignup} from "@/api/system/meeting"
import { getToken } from "@/utils/auth"; // 需要引入Token供上传使用

export default {
  name: "Meeting",
  data() {
    const currentYear = new Date().getFullYear()

    return {
      // 上传相关配置
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 若依标准上传接口
      headers: { Authorization: "Bearer " + getToken() }, // 携带Token
      fileList: [], // 上传文件列表回显

      signupListOpen: false,
      signupList: [],

      currentYear: currentYear,
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
        year: null,
        meetingName: null,
        holdTime: null,
        participants: null,
      },
      form: {},
      holdTimeOptions: {
        disabledDate: (time) => {
          const today = new Date()
          today.setHours(0, 0, 0, 0)
          return time.getTime() <= today.getTime()
        }
      },
      signUpStartOptions: {
        disabledDate: (time) => {
          const today = new Date()
          today.setHours(0, 0, 0, 0)
          return time.getTime() < today.getTime()
        }
      },
      signUpEndOptions: {
        disabledDate: (time) => {
          const today = new Date()
          today.setHours(0, 0, 0, 0)
          return time.getTime() <= today.getTime()
        }
      },
      rules: {
        year: [
          { required: true, message: "年份不能为空", trigger: "blur" },
          { type: 'number', min: currentYear, message: `年份必须大于等于${currentYear}`, trigger: "blur", transform: value => Number(value) }
        ],
        meetingName: [
          { required: true, message: "会议名称不能为空", trigger: "blur" }
        ],
        hostUnit: [
          { required: true, message: "举办单位不能为空", trigger: "blur" }
        ],
        holdTime: [
          { required: true, message: "召开时间不能为空", trigger: "change" }
        ],
        location: [
          { required: true, message: "地点不能为空", trigger: "blur" }
        ],
        meetingNotice: [
          { required: true, message: "请上传会议通知文件", trigger: "change" }
        ],
        signUpStart: [
          { required: true, message: "报名开始时间不能为空", trigger: "change" }
        ],
        signUpEnd: [
          { required: true, message: "报名结束时间不能为空", trigger: "change" }
        ],
        registerFee: [
          { required: true, message: "注册费用不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  watch: {
    'form.holdTime': function(newVal) {
      if (newVal) {
        const year = new Date(newVal).getFullYear()
        this.form.year = year
        this.updateSignUpEndOptions()
        this.updateSignUpStartOptions()
      }
    },
    'form.signUpStart': function(newVal) {
      if (newVal) {
        this.updateSignUpEndOptions()
        this.updateSignUpStartOptions()
      }
    }
  },
  methods: {
    checkRole(roles) {
      const userRoles = this.$store.getters && this.$store.getters.roles
      if (userRoles && userRoles.length > 0) {
        return roles.some(role => userRoles.includes(role))
      }
      return false
    },
    getList() {
      this.loading = true
      listMeeting(this.queryParams).then(response => {
        this.meetingList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        year: null,
        meetingName: null,
        hostUnit: null,
        holdTime: null,
        location: null,
        meetingNotice: null,
        signUpStart: null,
        signUpEnd: null,
        registerFee: null,
        participants: null,
        reporter: null
      }
      this.fileList = []; // 重置文件列表
      this.resetForm("form")
      this.updateSignUpEndOptions()
      this.updateSignUpStartOptions()
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
      this.title = "添加学术会议信息"
      this.form.year = this.currentYear
    },

    // 上传成功回调
    handleUploadSuccess(res, file) {
      // 若依上传接口通常返回 { url: '/profile/upload/...', fileName: '...' }
      // 这里取 url 字段，请确保后端返回格式一致
      this.form.meetingNotice = res.url;
      this.$message.success("上传成功");
    },
    // 移除文件回调
    handleRemove(file, fileList) {
      this.form.meetingNotice = null;
    },
    // 下载/预览通知
    handleDownload(url) {
      if (!url) return;
      if (url.startsWith("http")) {
        window.open(url);
      } else {
        window.open(process.env.VUE_APP_BASE_API + url);
      }
    },

    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMeeting(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改学术会议信息"
        // 回显文件
        if (this.form.meetingNotice) {
          // 截取文件名用于显示
          const fileName = this.form.meetingNotice.substring(this.form.meetingNotice.lastIndexOf("/") + 1);
          this.fileList = [{ name: fileName, url: this.form.meetingNotice }];
        }
        this.updateSignUpEndOptions()
        this.updateSignUpStartOptions()
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const validationError = this.validateForm()
          if (validationError) {
            this.$modal.msgError(validationError)
            return
          }
          if (this.form.id != null) {
            updateMeeting(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            }).catch(error => {
              this.$modal.msgError(error.response?.data?.msg || '操作失败')
            })
          } else {
            addMeeting(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            }).catch(error => {
              this.$modal.msgError(error.response?.data?.msg || '操作失败')
            })
          }
        }
      })
    },
    handleStudentSignup(row) {
      const now = new Date();
      now.setHours(0,0,0,0);
      if (row.signUpStart) {
        const start = new Date(row.signUpStart);
        start.setHours(0,0,0,0);
        if (now < start) {
          this.$modal.msgWarning(`报名尚未开始，开始时间：${this.parseTime(row.signUpStart, '{y}-{m}-{d}')}`);
          return;
        }
      }
      if (row.signUpEnd) {
        const end = new Date(row.signUpEnd);
        end.setHours(0,0,0,0);
        if (now > end) {
          this.$modal.msgWarning(`报名已结束，结束时间：${this.parseTime(row.signUpEnd, '{y}-{m}-{d}')}`);
          return;
        }
      }
      this.$modal.confirm(`确认要报名参加 "${row.meetingName}" 吗？系统将自动填入您的信息。`).then(() => {
        return studentSignup(row.id);
      }).then(() => {
        this.$modal.msgSuccess("报名成功");
        this.getList();
      }).catch(() => {});
    },
    handleStudentCancel(row) {
      this.$modal.confirm(`确认要取消 "${row.meetingName}" 的报名吗？`).then(() => {
        return studentCancelSignup(row.id);
      }).then(() => {
        this.$modal.msgSuccess("已取消报名");
        this.getList();
      }).catch(() => {});
    },
    validateForm() {
      const { signUpStart, signUpEnd, holdTime } = this.form
      if (signUpStart && signUpEnd) {
        if (new Date(signUpEnd) <= new Date(signUpStart)) {
          return '报名结束时间必须大于报名开始时间'
        }
      }
      if (signUpEnd && holdTime) {
        if (new Date(signUpEnd) >= new Date(holdTime)) {
          return '报名结束时间必须小于召开时间'
        }
      }
      return null
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除学术会议信息编号为"' + ids + '"的数据项？').then(function() {
        return delMeeting(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('system/meeting/export', {
        ...this.queryParams
      }, `meeting_${new Date().getTime()}.xlsx`)
    },
    handleCheckSignUp(row) {
      this.signupListOpen = true
      this.getSignupListData(row.id)
    },
    getSignupListData(meetingId) {
      getSignupList(meetingId).then(response => {
        this.signupList = response.rows
      }).catch(error => {
        this.$modal.msgError('获取报名列表失败')
      })
    },
    signupListCancel() {
      this.signupListOpen = false
      this.signupList = []
    },
    updateSignUpEndOptions() {
      this.signUpEndOptions = {
        disabledDate: (time) => {
          const today = new Date(); today.setHours(0, 0, 0, 0)
          if (time.getTime() <= today.getTime()) return true
          if (this.form.signUpStart) {
            const start = new Date(this.form.signUpStart); start.setHours(0,0,0,0)
            if (time.getTime() <= start.getTime()) return true
          }
          if (this.form.holdTime) {
            const hold = new Date(this.form.holdTime); hold.setHours(0,0,0,0)
            if (time.getTime() >= hold.getTime()) return true
          }
          return false
        }
      }
    },
    updateSignUpStartOptions() {
      this.signUpStartOptions = {
        disabledDate: (time) => {
          const today = new Date(); today.setHours(0, 0, 0, 0)
          if (time.getTime() < today.getTime()) return true
          if (this.form.signUpEnd) {
            const end = new Date(this.form.signUpEnd); end.setHours(0,0,0,0)
            if (time.getTime() >= end.getTime()) return true
          }
          if (this.form.holdTime) {
            const holdTime = new Date(this.form.holdTime); holdTime.setHours(0,0,0,0)
            const holdTimePrevDay = new Date(holdTime.getTime() - 24 * 60 * 60 * 1000)
            if (time.getTime() >= holdTimePrevDay.getTime()) return true
          }
          return false
        }
      }
    },
    handleHoldTimeChange(value) {
      if (value) {
        this.$nextTick(() => {
          this.$refs.form.validateField('holdTime')
          if (this.form.signUpEnd && new Date(this.form.signUpEnd) > new Date(value)) {
            this.form.signUpEnd = null
          }
        })
      }
    },
    handleSignUpStartChange(value) {
      if (value) {
        this.$nextTick(() => {
          this.$refs.form.validateField('signUpStart')
          if (this.form.signUpEnd && new Date(this.form.signUpEnd) <= new Date(value)) {
            this.form.signUpEnd = null
          }
        })
      }
    },
    handleSignUpEndChange(value) {
      if (value) {
        this.$nextTick(() => {
          this.$refs.form.validateField('signUpEnd')
        })
      }
    },
  }
}
</script>
