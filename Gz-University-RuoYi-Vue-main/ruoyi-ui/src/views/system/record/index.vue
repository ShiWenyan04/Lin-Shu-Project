<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="宿主" prop="host">
        <el-input
          v-model="queryParams.host"
          placeholder="请输入宿主"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="样本类型" prop="sampleType">
        <el-input
          v-model="queryParams.sampleType"
          placeholder="请输入样本类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采样人员" prop="samplingPerson">
        <el-input
          v-model="queryParams.sampler"
          placeholder="请输入采样人员"
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
          v-hasPermi="['system:record:add']"
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
          v-hasPermi="['system:record:edit']"
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
          v-hasPermi="['system:record:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <!-- 修改表格样式，添加文本溢出处理 -->
    <el-table
      v-loading="loading"
      :data="recordList"
      @selection-change="handleSelectionChange"
      style="width: 100%"
      :cell-style="{overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap'}"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="80" v-if="columns[0].visible">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
        label="样本编号"
        align="center"
        prop="sampleCode"
        v-if="columns[1].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="宿主"
        align="center"
        prop="host"
        v-if="columns[2].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="样本类型"
        align="center"
        prop="sampleType"
        v-if="columns[3].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="采样地点"
        align="center"
        prop="samplingLocation"
        v-if="columns[4].visible"
        :show-overflow-tooltip="true"
        min-width="150"
      />
      <el-table-column
        label="生境类型"
        align="center"
        prop="habitatType"
        v-if="columns[5].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="经度"
        align="center"
        prop="longitude"
        v-if="columns[6].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="纬度"
        align="center"
        prop="latitude"
        v-if="columns[7].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="采样时间"
        align="center"
        prop="samplingTime"
        v-if="columns[8].visible"
        :show-overflow-tooltip="true"
        min-width="130"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.samplingTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="采样人员"
        align="center"
        prop="sampler"
        v-if="columns[9].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="保存位置"
        align="center"
        prop="storageLocation"
        v-if="columns[10].visible"
        :show-overflow-tooltip="true"
        min-width="150"
      />
      <el-table-column
        label="使用人员"
        align="center"
        prop="usedBy"
        v-if="columns[11].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="用样时间"
        align="center"
        prop="usedTime"
        v-if="columns[12].visible"
        :show-overflow-tooltip="true"
        min-width="130"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.usedTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="用途"
        align="center"
        prop="purpose"
        v-if="columns[13].visible"
        :show-overflow-tooltip="true"
        min-width="200"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        v-if="columns[14].visible"
        width="180"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:record:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:record:remove']"
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

    <!-- 添加或修改样本使用记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="样本ID" prop="sampleId">
          <el-input v-model="form.sampleId" placeholder="请输入样本ID" />
        </el-form-item>
        <el-form-item label="使用人员" prop="usedBy">
          <el-input v-model="form.usedBy" placeholder="请输入使用人员" />
        </el-form-item>
        <el-form-item label="用样时间" prop="usedTime">
          <el-date-picker clearable
                          v-model="form.usedTime"
                          type="datetime"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择用样时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="用途" prop="purpose">
          <el-input v-model="form.purpose" type="textarea" placeholder="请输入用途" />
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
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/system/record"

export default {
  name: "Record",
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
      // 样本使用记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        host: null,           // 添加宿主
        sampleType: null,     // 添加样本类型
        sampler: null, // 添加采样人员
      },
      // 列信息
      columns: [
        { key: 0, label: '序号', visible: true },
        { key: 1, label: '样本编号', visible: true },
        { key: 2, label: '宿主', visible: true },
        { key: 3, label: '样本类型', visible: true },
        { key: 4, label: '采样地点', visible: true },
        { key: 5, label: '生境类型', visible: true },
        { key: 6, label: '经度', visible: true },
        { key: 7, label: '纬度', visible: true },
        { key: 8, label: '采样时间', visible: true },
        { key: 9, label: '采样人员', visible: true },
        { key: 10, label: '保存位置', visible: true },
        { key: 11, label: '使用人员', visible: true },
        { key: 12, label: '用样时间', visible: true },
        { key: 13, label: '用途', visible: true },
        { key: 14, label: '操作', visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sampleId: [
          { required: true, message: "样本ID不能为空", trigger: "blur" }
        ],
        usedBy: [
          { required: true, message: "使用人员不能为空", trigger: "blur" }
        ],
        usedTime: [
          { required: true, message: "用样时间不能为空", trigger: "change" }
        ],
        purpose: [
          { required: true, message: "用途不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询样本使用记录列表 */
    getList() {
      this.loading = true
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows
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
        sampleId: null,
        usedBy: null,
        usedTime: null,
        purpose: null
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
      this.title = "添加样本使用记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids[0]
      getRecord(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改样本使用记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRecord(this.form).then(response => {
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
      const names = row.sampleCode || this.selectionRows.map(item => item.sampleCode).join('、')

      this.$modal.confirm('是否确认删除样本使用记录"' + names + '"?').then(function() {
        return delRecord(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
