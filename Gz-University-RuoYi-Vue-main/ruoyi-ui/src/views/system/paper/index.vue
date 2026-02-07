<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="studentName">
        <el-input
          v-model="queryParams.studentName"
          placeholder="请输入姓名"
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
          v-hasPermi="['system:paper:add']"
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
          v-hasPermi="['system:paper:edit']"
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
          v-hasPermi="['system:paper:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:paper:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paperList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="studentName" />
      <el-table-column label="学位类别" align="center" prop="degreeType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.xuewei" :value="scope.row.degreeType"/>
        </template>
      </el-table-column>
      <el-table-column label="指导人" align="center" prop="supervisorName" />
      <el-table-column label="定题名称" align="center" prop="paperTitle" />
      <el-table-column label="定题时间" align="center" prop="topicConfirmationDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.topicConfirmationDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划完成时间" align="center" prop="plannedCompletionDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.plannedCompletionDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="初稿提交时间" align="center" prop="firstDraftSubmitDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.firstDraftSubmitDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="写作状态" align="center" prop="writingStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.writing" :value="scope.row.writingStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:paper:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:paper:remove']"
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

    <!-- 添加或修改学术论文写作对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="studentName">
          <el-input v-model="form.studentName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="学位类别" prop="degreeType">
          <el-select v-model="form.degreeType" placeholder="请选择学位类别">
            <el-option
              v-for="dict in dict.type.xuewei"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="指导人" prop="supervisorName">
          <el-input v-model="form.supervisorName" placeholder="请输入指导人" />
        </el-form-item>
        <el-form-item label="定题名称" prop="paperTitle">
          <el-input v-model="form.paperTitle" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="定题时间" prop="topicConfirmationDate">
          <el-date-picker clearable
            v-model="form.topicConfirmationDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择定题时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="计划完成时间" prop="plannedCompletionDate">
          <el-date-picker clearable
            v-model="form.plannedCompletionDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择计划完成时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="初稿提交时间" prop="firstDraftSubmitDate">
          <el-date-picker clearable
            v-model="form.firstDraftSubmitDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择初稿提交时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="写作状态" prop="writingStatus">
          <el-select v-model="form.writingStatus" placeholder="请选择写作状态">
            <el-option
              v-for="dict in dict.type.writing"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
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
import { listPaper, getPaper, delPaper, addPaper, updatePaper } from "@/api/system/paper"

export default {
  name: "Paper",
  dicts: ['writing', 'xuewei'],
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
      // 学术论文写作表格数据
      paperList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        studentName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        degreeType: [
          { required: true, message: "学位类别不能为空", trigger: "change" }
        ],
        supervisorName: [
          { required: true, message: "指导人不能为空", trigger: "blur" }
        ],
        paperTitle: [
          { required: true, message: "定题名称不能为空", trigger: "blur" }
        ],
        topicConfirmationDate: [
          { required: true, message: "定题时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询学术论文写作列表 */
    getList() {
      this.loading = true
      listPaper(this.queryParams).then(response => {
        this.paperList = response.rows
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
        studentName: null,
        degreeType: null,
        supervisorName: null,
        paperTitle: null,
        topicConfirmationDate: null,
        plannedCompletionDate: null,
        firstDraftSubmitDate: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        writingStatus: null
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加学术论文写作"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPaper(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改学术论文写作"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePaper(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPaper(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除学术论文写作编号为"' + ids + '"的数据项？').then(function() {
        return delPaper(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/paper/export', {
        ...this.queryParams
      }, `paper_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
