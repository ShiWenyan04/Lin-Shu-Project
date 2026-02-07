<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物种名称(关联键)" prop="speciesName">
        <el-input
          v-model="queryParams.speciesName"
          placeholder="请输入物种名称(关联键)"
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
          v-hasPermi="['system:library:add']"
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
          v-hasPermi="['system:library:edit']"
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
          v-hasPermi="['system:library:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:library:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="libraryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="物种名称(关联键)" align="center" prop="speciesName" />
      <el-table-column label="拉丁学名" align="center" prop="scientificName" />
      <el-table-column label="别名/俗名" align="center" prop="alias" />
      <el-table-column label="国家保护级别(一级/二级/三有)" align="center" prop="protectionLevel" />
      <el-table-column label="食性(食肉/食草/杂食)" align="center" prop="dietType" />
      <el-table-column label="活动节律(夜行/昼行/晨昏)" align="center" prop="activityPattern" />
      <el-table-column label="典型生境" align="center" prop="habitat" />
      <el-table-column label="形态特征与习性描述" align="center" prop="description" />
      <el-table-column label="物种封面图路径" align="center" prop="coverImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.coverImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:library:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:library:remove']"
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

    <!-- 添加或修改物种百科资料库对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物种名称(关联键)" prop="speciesName">
          <el-input v-model="form.speciesName" placeholder="请输入物种名称(关联键)" />
        </el-form-item>
        <el-form-item label="拉丁学名" prop="scientificName">
          <el-input v-model="form.scientificName" placeholder="请输入拉丁学名" />
        </el-form-item>
        <el-form-item label="别名/俗名" prop="alias">
          <el-input v-model="form.alias" placeholder="请输入别名/俗名" />
        </el-form-item>
        <el-form-item label="国家保护级别(一级/二级/三有)" prop="protectionLevel">
          <el-input v-model="form.protectionLevel" placeholder="请输入国家保护级别(一级/二级/三有)" />
        </el-form-item>
        <el-form-item label="活动节律(夜行/昼行/晨昏)" prop="activityPattern">
          <el-input v-model="form.activityPattern" placeholder="请输入活动节律(夜行/昼行/晨昏)" />
        </el-form-item>
        <el-form-item label="典型生境" prop="habitat">
          <el-input v-model="form.habitat" placeholder="请输入典型生境" />
        </el-form-item>
        <el-form-item label="形态特征与习性描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="物种封面图路径" prop="coverImage">
          <image-upload v-model="form.coverImage"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listLibrary, getLibrary, delLibrary, addLibrary, updateLibrary } from "@/api/system/library"

export default {
  name: "Library",
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
      // 物种百科资料库表格数据
      libraryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        speciesName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        speciesName: [
          { required: true, message: "物种名称(关联键)不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询物种百科资料库列表 */
    getList() {
      this.loading = true
      listLibrary(this.queryParams).then(response => {
        this.libraryList = response.rows
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
        speciesName: null,
        scientificName: null,
        alias: null,
        protectionLevel: null,
        dietType: null,
        activityPattern: null,
        habitat: null,
        description: null,
        coverImage: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.title = "添加物种百科资料库"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLibrary(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改物种百科资料库"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLibrary(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLibrary(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除物种百科资料库编号为"' + ids + '"的数据项？').then(function() {
        return delLibrary(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/library/export', {
        ...this.queryParams
      }, `library_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
