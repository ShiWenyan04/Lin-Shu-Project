<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="外业负责人" prop="leader">
        <el-input
          v-model="queryParams.leader"
          placeholder="请输入外业负责人"
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
          v-hasPermi="['system:work:add']"
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
          v-hasPermi="['system:work:edit']"
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
          v-hasPermi="['system:work:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:work:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="workList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60" v-if="columns[0].visible">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="统计月份" align="center" prop="statMonth" v-if="columns[1].visible" />
      <el-table-column label="出发日期" align="center" prop="startDate" width="180" v-if="columns[2].visible">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="返校日期" align="center" prop="endDate" width="180" v-if="columns[3].visible">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="外业地点" align="center" prop="location" v-if="columns[4].visible" />
      <el-table-column label="外业内容" align="center" prop="content" v-if="columns[5].visible" show-overflow-tooltip />
      <el-table-column label="外业负责人" align="center" prop="leader" v-if="columns[6].visible" />
      <el-table-column label="参与人员" align="center" prop="participants" v-if="columns[7].visible" show-overflow-tooltip />
      <el-table-column label="外业天数" align="center" prop="fieldDays" v-if="columns[8].visible" />
      <el-table-column label="共计天数" align="center" prop="totalDays" v-if="columns[9].visible" />
      <el-table-column label="备注" align="center" prop="remarks" v-if="columns[10].visible" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-if="columns[11].visible">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:work:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:work:remove']"
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

    <!-- 添加或修改外业信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="统计月份" prop="statMonth">
              <el-date-picker
                v-model="form.statMonth"
                type="month"
                value-format="yyyy-MM"
                placeholder="请选择统计月份"
                style="width: 100%"
                :clearable="false"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="外业天数" prop="fieldDays">
              <el-input v-model="form.fieldDays" placeholder="请输入外业天数" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="出发日期" prop="startDate">
              <el-date-picker clearable
                              v-model="form.startDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择出发日期"
                              style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="共计天数" prop="totalDays">
              <el-input v-model="form.totalDays" placeholder="请输入共计天数" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="返校日期" prop="endDate">
              <el-date-picker clearable
                              v-model="form.endDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择返校日期"
                              style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="外业地点" prop="location">
              <el-input v-model="form.location" placeholder="请输入外业地点" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="外业负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入外业负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参与人员" prop="participants">
              <el-input v-model="form.participants" placeholder="请输入参与人员" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="外业内容">
              <editor v-model="form.content" :min-height="192"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容" />
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
import { listWork, getWork, delWork, addWork, updateWork } from "@/api/system/work"

export default {
  name: "Work",
  data() {
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
      // 外业信息表格数据
      workList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        leader: null,
      },
      // 列信息
      columns: [
        { key: 0, label: '序号', visible: true },
        { key: 1, label: '统计月份', visible: true },
        { key: 2, label: '出发日期', visible: true },
        { key: 3, label: '返校日期', visible: true },
        { key: 4, label: '外业地点', visible: true },
        { key: 5, label: '外业内容', visible: true },
        { key: 6, label: '外业负责人', visible: true },
        { key: 7, label: '参与人员', visible: true },
        { key: 8, label: '外业天数', visible: true },
        { key: 9, label: '共计天数', visible: true },
        { key: 10, label: '备注', visible: true },
        { key: 11, label: '操作', visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        statMonth: [
          { required: true, message: "统计月份不能为空", trigger: "change" }
        ],
        startDate: [
          { required: true, message: "出发日期不能为空", trigger: "change" }
        ],
        endDate: [
          { required: true, message: "返校日期不能为空", trigger: "change" }
        ],
        location: [
          { required: true, message: "外业地点不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "外业内容不能为空", trigger: "blur" }
        ],
        leader: [
          { required: true, message: "外业负责人不能为空", trigger: "blur" }
        ],
        participants: [
          { required: true, message: "参与人员不能为空", trigger: "blur" }
        ],
        fieldDays: [
          { required: true, message: "外业天数不能为空", trigger: "blur" },
          { type: 'number', message: '外业天数必须为数字值' }
        ],
        totalDays: [
          { required: true, message: "共计天数不能为空", trigger: "blur" },
          { type: 'number', message: '共计天数必须为数字值' }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询外业信息列表 */
    getList() {
      this.loading = true
      listWork(this.queryParams).then(response => {
        this.workList = response.rows
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
        sequenceNo: null,
        statMonth: null,
        startDate: null,
        endDate: null,
        location: null,
        content: null,
        leader: null,
        participants: null,
        fieldDays: null,
        totalDays: null,
        remarks: null
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
      this.title = "添加外业信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids[0]
      getWork(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改外业信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWork(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWork(this.form).then(response => {
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
      const names = row.leader || this.selectionRows.map(item => item.leader).join('、')

      this.$modal.confirm('是否确认删除外业负责人为"' + names + '"的数据项？').then(function() {
        return delWork(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/work/export', {
        ...this.queryParams
      }, `work_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
