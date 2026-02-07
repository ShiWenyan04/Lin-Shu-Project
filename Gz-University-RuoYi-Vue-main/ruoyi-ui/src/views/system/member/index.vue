<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="queryParams.gender" placeholder="请选择性别">
          <el-option
            v-for="dict in dict.type.sys_user_sex"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"></el-option>
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
          v-hasPermi="['system:member:add']"
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
          v-hasPermi="['system:member:edit']"
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
          v-hasPermi="['system:member:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:member:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>


    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60" v-if="columns[0].visible">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="name" v-if="columns[1].visible" />
      <el-table-column label="性别" align="center" prop="gender" v-if="columns[2].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.gender"/>
        </template>
      </el-table-column>
      <el-table-column label="研究方向" align="center" prop="researchDirection" v-if="columns[3].visible" />
      <el-table-column label="学号" align="center" prop="studentId" v-if="columns[4].visible" show-overflow-tooltip />
      <el-table-column label="手机号" align="center" prop="phone" v-if="columns[5].visible" show-overflow-tooltip />
      <el-table-column label="邮箱" align="center" prop="email" v-if="columns[6].visible" show-overflow-tooltip />
      <el-table-column label="银行卡号" align="center" prop="bankCard" v-if="columns[7].visible" show-overflow-tooltip />
      <el-table-column label="入学时间" align="center" prop="enrollmentDate" width="180" v-if="columns[8].visible">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.enrollmentDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="当前学历" align="center" prop="currentEducation" v-if="columns[9].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.current_education" :value="scope.row.currentEducation"/>
        </template>
      </el-table-column>
      <el-table-column label="当前状态" align="center" prop="currentStatus" v-if="columns[10].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.current_status" :value="scope.row.currentStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-if="columns[11].visible">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:member:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:member:remove']"
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

    <!-- 添加或修改团队成员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
                <el-option v-for="dict in dict.type.sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="研究方向" prop="researchDirection">
              <el-input v-model="form.researchDirection" placeholder="请输入研究方向" />
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
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="银行卡号" prop="bankCard">
              <el-input v-model="form.bankCard" placeholder="请输入银行卡号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入学时间" prop="enrollmentDate">
              <el-date-picker clearable
                              v-model="form.enrollmentDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择入学时间"
                              style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="当前学历" prop="currentEducation">
              <el-select v-model="form.currentEducation" placeholder="请选择当前学历" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.current_education"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前状态" prop="currentStatus">
              <el-select v-model="form.currentStatus" placeholder="请选择当前状态" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.current_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMember, getMember, delMember, addMember, updateMember } from "@/api/system/member"

export default {
  name: "Member",
  dicts: ['sys_user_sex','current_status', 'current_education'],
  data() {
    // 手机号校验规则
    const validatePhone = (rule, value, callback) => {
      if (value && !/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号码'))
      } else {
        callback()
      }
    }
    // 邮箱校验规则
    const validateEmail = (rule, value, callback) => {
      if (value && !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
        callback(new Error('请输入正确的邮箱地址'))
      } else {
        callback()
      }
    }

    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中的行数据
      selectionRows: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 团队成员表格数据
      memberList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        gender: null,
        researchDirection: null,
        studentId: null,
        phone: null,
        email: null,
        bankCard: null,
        enrollmentDate: null,
        currentEducation: null,
        currentStatus: null
      },
      // 列信息
      columns: [
        { key: 0, label: '序号', visible: true },
        { key: 1, label: '姓名', visible: true },
        { key: 2, label: '性别', visible: true },
        { key: 3, label: '研究方向', visible: true },
        { key: 4, label: '学号', visible: true },
        { key: 5, label: '手机号', visible: true },
        { key: 6, label: '邮箱', visible: true },
        { key: 7, label: '银行卡号', visible: true },
        { key: 8, label: '入学时间', visible: true },
        { key: 9, label: '当前学历', visible: true },
        { key: 10, label: '当前状态', visible: true },
        { key: 11, label: '操作', visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        gender: [
          { required: true, message: "性别不能为空", trigger: "change" }
        ],
        researchDirection: [
          { required: true, message: "研究方向不能为空", trigger: "blur" }
        ],
        studentId: [
          { required: true, message: "学号不能为空", trigger: "blur" }
        ],
        phone: [
          { validator: validatePhone, trigger: 'blur' }
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ],
        enrollmentDate: [
          { required: true, message: "入学时间不能为空", trigger: "blur" }
        ],
        currentEducation: [
          { required: true, message: "当前学历不能为空", trigger: "change" }
        ],
        currentStatus: [
          { required: true, message: "当前状态不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询团队成员列表 */
    getList() {
      this.loading = true
      listMember(this.queryParams).then(response => {
        this.memberList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        gender: null,
        researchDirection: null,
        studentId: null,
        phone: null,
        email: null,
        bankCard: null,
        enrollmentDate: null,
        currentEducation: null,
        currentStatus: null
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
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.selectionRows = selection
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加团队成员"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids[0]
      getMember(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改团队成员"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMember(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMember(this.form).then(response => {
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
      const ids = row.id || this.ids
      const names = row.name || this.selectionRows.map(item => item.name).join('、')

      this.$modal.confirm('是否确认删除"' + names + '"?').then(function() {
        return delMember(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/member/export', {
        ...this.queryParams
      }, `member_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
