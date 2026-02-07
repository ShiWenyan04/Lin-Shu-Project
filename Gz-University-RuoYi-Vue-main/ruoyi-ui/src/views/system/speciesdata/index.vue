<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="归属项目ID" prop="installId">
        <el-input
          v-model="queryParams.installId"
          placeholder="请输入归属项目ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联的具体位点ID(可选)" prop="pointId">
        <el-input
          v-model="queryParams.pointId"
          placeholder="请输入关联的具体位点ID(可选)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="Excel中的原始编号(如H21)" prop="siteCodeRef">
        <el-input
          v-model="queryParams.siteCodeRef"
          placeholder="请输入Excel中的原始编号(如H21)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物种名称(如豹猫)" prop="speciesName">
        <el-input
          v-model="queryParams.speciesName"
          placeholder="请输入物种名称(如豹猫)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="对象类别(如兽类)" prop="className">
        <el-input
          v-model="queryParams.className"
          placeholder="请输入对象类别(如兽类)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="拍摄次数(预留)" prop="captureCount">
        <el-input
          v-model="queryParams.captureCount"
          placeholder="请输入拍摄次数(预留)"
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
          v-hasPermi="['system:speciesdata:add']"
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
          v-hasPermi="['system:speciesdata:edit']"
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
          v-hasPermi="['system:speciesdata:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:speciesdata:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="speciesdataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="记录ID" align="center" prop="id" />
      <el-table-column label="归属项目ID" align="center" prop="installId" />
      <el-table-column label="关联的具体位点ID(可选)" align="center" prop="pointId" />
      <el-table-column label="Excel中的原始编号(如H21)" align="center" prop="siteCodeRef" />
      <el-table-column label="物种名称(如豹猫)" align="center" prop="speciesName" />
      <el-table-column label="对象类别(如兽类)" align="center" prop="className" />
      <el-table-column label="拍摄次数(预留)" align="center" prop="captureCount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:speciesdata:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:speciesdata:remove']"
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

    <!-- 添加或修改物种监测数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="归属项目ID" prop="installId">
          <el-input v-model="form.installId" placeholder="请输入归属项目ID" />
        </el-form-item>
        <el-form-item label="关联的具体位点ID(可选)" prop="pointId">
          <el-input v-model="form.pointId" placeholder="请输入关联的具体位点ID(可选)" />
        </el-form-item>
        <el-form-item label="Excel中的原始编号(如H21)" prop="siteCodeRef">
          <el-input v-model="form.siteCodeRef" placeholder="请输入Excel中的原始编号(如H21)" />
        </el-form-item>
        <el-form-item label="物种名称(如豹猫)" prop="speciesName">
          <el-input v-model="form.speciesName" placeholder="请输入物种名称(如豹猫)" />
        </el-form-item>
        <el-form-item label="对象类别(如兽类)" prop="className">
          <el-input v-model="form.className" placeholder="请输入对象类别(如兽类)" />
        </el-form-item>
        <el-form-item label="拍摄次数(预留)" prop="captureCount">
          <el-input v-model="form.captureCount" placeholder="请输入拍摄次数(预留)" />
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
import { listSpeciesdata, getSpeciesdata, delSpeciesdata, addSpeciesdata, updateSpeciesdata } from "@/api/system/speciesdata"

export default {
  name: "Speciesdata",
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
      // 物种监测数据表格数据
      speciesdataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        installId: null,
        pointId: null,
        siteCodeRef: null,
        speciesName: null,
        className: null,
        captureCount: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        installId: [
          { required: true, message: "归属项目ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询物种监测数据列表 */
    getList() {
      this.loading = true
      listSpeciesdata(this.queryParams).then(response => {
        this.speciesdataList = response.rows
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
        installId: null,
        pointId: null,
        siteCodeRef: null,
        speciesName: null,
        className: null,
        captureCount: null,
        createTime: null
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
      this.title = "添加物种监测数据"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getSpeciesdata(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改物种监测数据"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSpeciesdata(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSpeciesdata(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除物种监测数据编号为"' + ids + '"的数据项？').then(function() {
        return delSpeciesdata(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/speciesdata/export', {
        ...this.queryParams
      }, `speciesdata_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
