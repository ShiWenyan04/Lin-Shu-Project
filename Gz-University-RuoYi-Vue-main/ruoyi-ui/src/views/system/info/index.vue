<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物种类别" prop="specimenCategory">
        <el-select v-model="queryParams.specimenCategory" placeholder="请选择物种类别" clearable>
          <el-option
            v-for="dict in dict.type.specimen_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="物种名称" prop="specimenName">
        <el-input
          v-model="queryParams.specimenName"
          placeholder="请输入物种名称"
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
          v-hasPermi="['system:info:add']"
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
          v-hasPermi="['system:info:edit']"
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
          v-hasPermi="['system:info:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:info:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60" v-if="columns[0].visible">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="物种名称" align="center" prop="specimenName" v-if="columns[1].visible" />
      <el-table-column label="物种类别" align="center" prop="specimenCategory" v-if="columns[2].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.specimen_category" :value="scope.row.specimenCategory"/>
        </template>
      </el-table-column>
      <el-table-column label="数量" align="center" prop="quantity" v-if="columns[3].visible" />
      <el-table-column label="制作日期" align="center" prop="productionDate" width="180" v-if="columns[4].visible">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.productionDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="制作人" align="center" prop="producer" v-if="columns[5].visible" />
      <!-- 添加状态列 -->
      <el-table-column label="状态" align="center" prop="status" v-if="columns[6].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.specimen_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <!-- 添加来源列 -->
      <el-table-column label="来源" align="center" prop="source" v-if="columns[7].visible" />
      <!-- 添加采集地列 -->
      <el-table-column label="采集地" align="center" prop="collectionPlace" v-if="columns[8].visible" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-if="columns[9].visible">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:info:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:info:remove']"
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

    <!-- 添加或修改标本信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物种类别" prop="specimenCategory">
          <el-select v-model="form.specimenCategory" placeholder="请选择物种类别">
            <el-option
              v-for="dict in dict.type.specimen_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="物种名称" prop="specimenName">
          <el-input v-model="form.specimenName" placeholder="请输入物种名称" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input v-model="form.quantity" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="制作日期" prop="productionDate">
          <el-date-picker clearable
                          v-model="form.productionDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择制作日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="制作人" prop="producer">
          <el-input v-model="form.producer" placeholder="请输入制作人" />
        </el-form-item>
        <!-- 添加状态字段 -->
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option
              v-for="dict in dict.type.specimen_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- 添加来源字段 -->
        <el-form-item label="来源" prop="source">
          <el-input v-model="form.source" placeholder="请输入来源" />
        </el-form-item>
        <!-- 添加采集地字段 -->
        <el-form-item label="采集地" prop="collectionPlace">
          <el-input v-model="form.collectionPlace" placeholder="请输入采集地" />
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
import { listInfo, getInfo, delInfo, addInfo, updateInfo } from "@/api/system/info"

export default {
  name: "Info",
  dicts: ['specimen_category', 'specimen_status'],
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
      // 标本信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sequenceNo: null,
        specimenCategory: null,
        specimenName: null,
        quantity: null,
        productionDate: null,
        producer: null,
        // 添加新字段
        status: null,
        source: null,
        collectionPlace: null
      },
      // 列信息
      columns: [
        { key: 0, label: '序号', visible: true },
        { key: 1, label: '标本名称', visible: true },
        { key: 2, label: '标本类别', visible: true },
        { key: 3, label: '标本数量', visible: true },
        { key: 4, label: '制作日期', visible: true },
        { key: 5, label: '制作人', visible: true },
        { key: 6, label: '状态', visible: true },
        { key: 7, label: '来源', visible: true },
        { key: 8, label: '采集地', visible: true },
        { key: 9, label: '操作', visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        specimenCategory: [
          { required: true, message: "标本类别不能为空", trigger: "change" }
        ],
        specimenName: [
          { required: true, message: "标本名称不能为空", trigger: "blur" }
        ],
        // 添加新字段验证规则（可根据需要调整）
        status: [
          { required: false, message: "请选择状态", trigger: "change" }
        ],
        source: [
          { required: false, message: "请输入来源", trigger: "blur" },
          { max: 200, message: "长度不能超过200个字符", trigger: "blur" }
        ],
        collectionPlace: [
          { required: false, message: "请输入采集地", trigger: "blur" },
          { max: 200, message: "长度不能超过200个字符", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询标本信息列表 */
    getList() {
      this.loading = true
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows
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
        specimenCategory: null,
        specimenName: null,
        quantity: null,
        productionDate: null,
        producer: null,
        // 添加新字段
        status: null,
        source: null,
        collectionPlace: null
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
      this.title = "添加标本信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids[0]
      getInfo(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改标本信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addInfo(this.form).then(response => {
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
      const names = row.specimenName || this.selectionRows.map(item => item.specimenName).join('、')

      this.$modal.confirm('是否确认删除标本信息"' + names + '"?').then(function() {
        return delInfo(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/info/export', {
        ...this.queryParams
      }, `info_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
