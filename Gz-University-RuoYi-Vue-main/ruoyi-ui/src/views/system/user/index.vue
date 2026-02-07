<template>
  <div class="app-container">
    <el-row :gutter="20">
      <splitpanes :horizontal="this.$store.getters.device === 'mobile'" class="default-theme">
        <pane size="100">
          <el-col>
            <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
              <el-form-item label="姓名" prop="nickName">
                <el-input
                  v-model="queryParams.nickName"
                  placeholder="请输入姓名"
                  clearable
                  style="width: 150px"
                  @keyup.enter.native="handleQuery"
                />
              </el-form-item>

              <el-form-item label="入学年份" prop="enrollmentYear">
                <el-date-picker
                  v-model="queryParams.enrollmentYear"
                  type="year"
                  value-format="yyyy"
                  placeholder="选择年份"
                  style="width: 150px"
                  @change="handleQuery"
                />
              </el-form-item>

              <el-form-item label="学位" prop="degreeType">
                <el-select
                  v-model="queryParams.degreeType"
                  placeholder="学位类别"
                  clearable
                  style="width: 150px"
                  @change="handleQuery"
                >
                  <el-option
                    v-for="dict in dict.type.xuewei"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="专业" prop="major">
                <el-select
                  v-model="queryParams.major"
                  placeholder="选择专业"
                  clearable
                  style="width: 150px"
                  @change="handleQuery"
                >
                  <el-option
                    v-for="dict in dict.type.sys_major"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="当前状态" prop="currentStatus">
                <el-select
                  v-model="queryParams.currentStatus"
                  placeholder="当前状态"
                  clearable
                  style="width: 150px"
                  @change="handleQuery"
                >
                  <el-option
                    v-for="dict in dict.type.current_status"
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
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:user:add']">新增</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:user:edit']">修改</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:user:remove']">删除</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport" v-hasPermi="['system:user:import']">导入</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:user:export']">导出</el-button>
              </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </el-row>

            <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="50" align="center" />

              <el-table-column label="序号" align="center" width="50">
                <template slot-scope="scope">
                  {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
                </template>
              </el-table-column>

              <el-table-column label="性别" align="center" prop="sex" width="60">
                <template slot-scope="scope">
                  <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.sex"/>
                </template>
              </el-table-column>

              <el-table-column label="专业" align="center" prop="major" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                  <dict-tag :options="dict.type.sys_major" :value="scope.row.major"/>
                </template>
              </el-table-column>

              <el-table-column label="用户账号名" align="center" key="userName" prop="userName" v-if="columns[1].visible" :show-overflow-tooltip="true" />

              <el-table-column label="姓名" align="center" key="nickName" prop="nickName" v-if="columns[2].visible" :show-overflow-tooltip="true" />

              <el-table-column label="学号" align="center" prop="studentId" width="120" />

              <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber" v-if="columns[4].visible" width="120" />

              <el-table-column label="审核状态" align="center" width="100">
                <template slot-scope="scope">
                  <span v-if="scope.row.auditStatus == '0'" style="color: #E6A23C; font-weight: bold;">待审核</span>
                  <span v-else-if="scope.row.auditStatus == '1'" style="color: #67C23A;">已通过</span>
                  <span v-else-if="scope.row.auditStatus == '2'" style="color: #F56C6C;">已拒绝</span>
                  <span v-else>-</span>
                </template>
              </el-table-column>

              <el-table-column label="状态" align="center" key="status" v-if="columns[5].visible" width="80">
                <template slot-scope="scope">
                  <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)" :disabled="scope.row.auditStatus == '0'"></el-switch>
                </template>
              </el-table-column>

              <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
                <template slot-scope="scope" v-if="scope.row.userId !== 1">
                  <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">详情</el-button>

                  <span v-if="scope.row.auditStatus == '0'">
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-check"
                      style="color: #67C23A;"
                      @click="handleAudit(scope.row)"
                      v-hasPermi="['system:user:audit']"
                    >通过</el-button>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-close"
                      style="color: #F56C6C;"
                      @click="handleReject(scope.row)"
                      v-hasPermi="['system:user:audit']"
                    >拒绝</el-button>
                  </span>

                  <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:user:edit']">修改</el-button>
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:user:remove']">删除</el-button>

                  <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['system:user:resetPwd', 'system:user:edit']">
                    <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="handleResetPwd" icon="el-icon-key" v-hasPermi="['system:user:resetPwd']">重置密码</el-dropdown-item>
                      <el-dropdown-item command="handleAuthRole" icon="el-icon-circle-check" v-hasPermi="['system:user:edit']">分配角色</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>

            <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
          </el-col>
        </pane>
      </splitpanes>
    </el-row>

    <el-dialog title="用户详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions border :column="2" size="medium">
        <el-descriptions-item label="账号名">{{ form.userName }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ form.nickName }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ form.studentId }}</el-descriptions-item>
        <el-descriptions-item label="手机号码">{{ form.phonenumber }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ form.email }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <dict-tag :options="dict.type.sys_user_sex" :value="form.sex"/>
        </el-descriptions-item>
        <el-descriptions-item label="民族">{{ form.ethnicity }}</el-descriptions-item>
        <el-descriptions-item label="专业">
          <dict-tag :options="dict.type.sys_major" :value="form.major"/>
        </el-descriptions-item>
        <el-descriptions-item label="学位类别">
          <dict-tag :options="dict.type.xuewei" :value="form.degreeType"/>
        </el-descriptions-item>
        <el-descriptions-item label="入学年份">{{ form.enrollmentYear }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <dict-tag :options="dict.type.current_status" :value="form.currentStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="本科院校">{{ form.undergradUniv }}</el-descriptions-item>
        <el-descriptions-item label="硕士院校">{{ form.masterUniv }}</el-descriptions-item>
        <el-descriptions-item label="就业单位">{{ form.employer }}</el-descriptions-item>
        <el-descriptions-item label="家庭住址" :span="2">{{ form.address }}</el-descriptions-item>

        <el-descriptions-item label="紧急联系人">{{ form.emergencyContact }}</el-descriptions-item>
        <el-descriptions-item label="关系">{{ form.emergencyRelation }}</el-descriptions-item>
        <el-descriptions-item label="联系人电话">{{ form.emergencyPhone }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <span v-if="form.auditStatus == '0'">待审核</span>
          <span v-else-if="form.auditStatus == '1'">已通过</span>
          <span v-else-if="form.auditStatus == '2'">已拒绝</span>
        </el-descriptions-item>

        <el-descriptions-item label="备注" :span="2">{{ form.remark }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(form.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="最后登录">{{ parseTime(form.loginDate) }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入真实姓名" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学号" prop="studentId">
              <el-input v-model="form.studentId" placeholder="请输入学号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phonenumber">
              <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="账号" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户账号" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="用户密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入用户密码" type="password" maxlength="20" show-password />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-select v-model="form.sex" placeholder="请选择性别">
                <el-option v-for="dict in dict.type.sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="民族">
              <el-input v-model="form.ethnicity" placeholder="请输入民族" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-select v-model="form.major" placeholder="请选择专业">
                <el-option v-for="dict in dict.type.sys_major" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学位类别" prop="degreeType">
              <el-select v-model="form.degreeType" placeholder="请选择">
                <el-option v-for="dict in dict.type.xuewei" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="入学年份">
              <el-date-picker v-model="form.enrollmentYear" type="year" value-format="yyyy" placeholder="选择年份" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前状态">
              <el-select v-model="form.currentStatus" placeholder="请选择">
                <el-option v-for="dict in dict.type.current_status" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="本科院校">
              <el-input v-model="form.undergradUniv" placeholder="请输入本科院校" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="硕士院校">
              <el-input v-model="form.masterUniv" placeholder="请输入硕士院校" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="家庭住址">
              <el-input v-model="form.address" placeholder="请输入家庭住址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="就业单位">
              <el-input v-model="form.employer" placeholder="请输入就业单位" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="紧急联系人">
              <el-input v-model="form.emergencyContact" placeholder="姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="关系">
              <el-input v-model="form.emergencyRelation" placeholder="关系" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="电话">
              <el-input v-model="form.emergencyPhone" placeholder="电话" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select v-model="form.roleIds" multiple placeholder="请选择角色">
                <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId" :disabled="item.status == 1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="审核状态">
              <el-radio-group v-model="form.auditStatus">
                <el-radio label="0">待审核</el-radio>
                <el-radio label="1">通过</el-radio>
                <el-radio label="2">拒绝</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核拒绝" :visible.sync="rejectOpen" width="500px" append-to-body>
      <el-form ref="rejectForm" :model="rejectForm" label-width="80px">
        <el-form-item label="拒绝原因" prop="remark" :rules="[{ required: true, message: '请填写拒绝原因', trigger: 'blur' }]">
          <el-input
            v-model="rejectForm.remark"
            type="textarea"
            placeholder="请填写拒绝原因，以便学生修改（例如：学号填写错误）"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReject">确 定</el-button>
        <el-button @click="rejectOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 注意：请确保 auditUser 已经添加到 api 文件中
import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus, deptTreeSelect, auditUser } from "@/api/system/user"
import { getToken } from "@/utils/auth"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import { Splitpanes, Pane } from "splitpanes"
import "splitpanes/dist/splitpanes.css"

export default {
  name: "User",
  dicts: ['sys_normal_disable', 'sys_user_sex', 'xuewei', 'sys_major', 'current_status'], // 增加新的字典
  components: { Treeselect, Splitpanes, Pane },
  data() {
    return {
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
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 详情弹窗
      viewOpen: false,
      // 拒绝弹窗
      rejectOpen: false,
      rejectForm: {},

      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      // 用户导入参数
      upload: {
        open: false,
        title: "",
        isUploading: false,
        updateSupport: 0,
        headers: { Authorization: "Bearer " + getToken() },
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        nickName: undefined,
        enrollmentYear: undefined, // 入学年份 (新增)
        degreeType: undefined,     // 学位类别 (新增)
        major: undefined,          // 专业 (新增)
        currentStatus: undefined,  // 当前状态 (新增)
        phonenumber: undefined,
        studentId: undefined, // 新增
        status: undefined,
        auditStatus: undefined // 新增
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `用户名称`, visible: true },
        { key: 2, label: `真实姓名`, visible: true },
        { key: 3, label: `部门`, visible: true },
        { key: 4, label: `手机号码`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true }
      ],
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
          { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: "真实姓名不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
          { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' },
          { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
        ],
        email: [
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }
        ],
        phonenumber: [
          { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }
        ],
        studentId: [
          { required: true, message: "学号不能为空", trigger: "blur" } // 如果学号必填
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg
    })
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    // 用户状态修改
    handleStatusChange(row) {
      if (row.auditStatus == '0') {
        this.$modal.msgWarning("待审核用户无法修改状态");
        row.status = "1"; // 强制回退为停用
        return;
      }
      let text = row.status === "0" ? "启用" : "停用"
      this.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗？').then(function() {
        return changeUserStatus(row.userId, row.status)
      }).then(() => {
        this.$modal.msgSuccess(text + "成功")
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0"
      })
    },

    // 查看详情
    handleView(row) {
      this.reset();
      const userId = row.userId;
      getUser(userId).then(response => {
        this.form = response.data;
        this.viewOpen = true; // 打开详情弹窗
      });
    },

    // 审核通过
    handleAudit(row) {
      this.$modal.confirm('确认通过用户 "' + row.nickName + '" 的注册申请？').then(() => {
        const data = {
          userId: row.userId,
          status: '0', // 启用
          auditStatus: '1', // 审核通过
        };
        return auditUser(data); // 使用 auditUser 接口
      }).then(() => {
        this.$modal.msgSuccess("审核通过");
        this.getList();
      });
    },

    // 打开拒绝弹窗
    handleReject(row) {
      this.rejectForm = {
        userId: row.userId,
        remark: ""
      };
      this.rejectOpen = true;
    },

    // 提交拒绝
    submitReject() {
      this.$refs["rejectForm"].validate(valid => {
        if (valid) {
          const data = {
            userId: this.rejectForm.userId,
            status: '0',       // 保持启用，让学生能登录看原因
            auditStatus: '2',  // 2=拒绝
            remark: this.rejectForm.remark // 拒绝原因存入备注
          };

          auditUser(data).then(response => {
            this.$modal.msgSuccess("已拒绝该申请");
            this.rejectOpen = false;
            this.getList(); // 刷新列表
          });
        }
      });
    },

    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        auditStatus: "1", // 默认审核状态为通过（后台添加）
        remark: undefined,
        postIds: [],
        roleIds: [],
        // 重置新增字段
        studentId: undefined,
        ethnicity: undefined,
        major: undefined,
        degreeType: undefined,
        enrollmentYear: undefined,
        currentStatus: undefined,
        undergradUniv: undefined,
        masterUniv: undefined,
        employer: undefined,
        address: undefined,
        emergencyContact: undefined,
        emergencyRelation: undefined,
        emergencyPhone: undefined
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row)
          break
        case "handleAuthRole":
          this.handleAuthRole(row)
          break
        default:
          break
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      getUser().then(response => {
        this.postOptions = response.posts
        this.roleOptions = response.roles
        this.open = true
        this.title = "添加用户"
        this.form.password = this.initPassword
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const userId = row.userId || this.ids
      getUser(userId).then(response => {
        this.form = response.data
        this.postOptions = response.posts
        this.roleOptions = response.roles
        this.$set(this.form, "postIds", response.postIds)
        this.$set(this.form, "roleIds", response.roleIds)
        this.open = true
        this.title = "修改用户"
        this.form.password = ""
      })
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: "用户密码长度必须介于 5 和 20 之间",
        inputValidator: (value) => {
          if (/<|>|"|'|\||\\/.test(value)) {
            return "不能包含非法字符：< > \" ' \\\ |"
          }
        },
      }).then(({ value }) => {
        resetUserPwd(row.userId, value).then(response => {
          this.$modal.msgSuccess("修改成功，新密码是：" + value)
        })
      }).catch(() => {})
    },
    /** 分配角色操作 */
    handleAuthRole: function(row) {
      const userId = row.userId
      this.$router.push("/system/user-auth/role/" + userId)
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids
      this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function() {
        return delUser(userIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入"
      this.upload.open = true
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/user/importTemplate', {
      }, `user_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
      this.getList()
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit()
    }
  }
}
</script>
