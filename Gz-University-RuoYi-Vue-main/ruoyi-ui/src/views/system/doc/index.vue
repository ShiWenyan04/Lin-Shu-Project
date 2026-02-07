<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文档名称" prop="docName">
        <el-input
          v-model="queryParams.docName"
          placeholder="请输入文档名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文档分类" prop="docCategory">
        <el-input
          v-model="queryParams.docCategory"
          placeholder="请输入文档分类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8" v-if="checkRole(['student', 'teacher', 'manager', 'admin'])">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:doc:add']"
        >新增模板</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:doc:edit']"
        >修改</el-button>
      </el-col>

      <el-col :span="1.5" v-if="!checkRole(['student'])">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:doc:remove']"
        >删除</el-button>
      </el-col>

      <el-col :span="1.5" v-if="!checkRole(['student'])">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:doc:export']"
        >导出列表</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="docList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="文档名称" align="center" prop="docName" min-width="150" show-overflow-tooltip />

      <el-table-column label="文档分类" align="center" prop="docCategory" width="120" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.docCategory">{{ scope.row.docCategory }}</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>

      <el-table-column label="模板文件" align="center" width="120">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.fileUrl"
            size="mini"
            type="primary"
            icon="el-icon-download"
            plain
            @click="handleDownload(scope.row)"
          >点击下载</el-button>
          <span v-else style="color: #909399">暂无文件</span>
        </template>
      </el-table-column>

      <el-table-column label="排序" align="center" prop="sortOrder" width="60" />

      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />

      <el-table-column label="上传时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="150"
        v-if="checkRole(['manager', 'teacher'])"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:doc:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:doc:remove']"
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

    <el-dialog :title="title" :visible.sync="open" width="550px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="文档名称" prop="docName">
          <el-input v-model="form.docName" placeholder="请输入文档名称" />
        </el-form-item>

        <el-form-item label="文档分类" prop="docCategory">
          <el-input v-model="form.docCategory" placeholder="例如：调查表格、参考资料..." />
        </el-form-item>

        <el-form-item label="上传文件" prop="fileUrl">
          <file-upload v-model="form.fileUrl" :limit="1" />
        </el-form-item>

        <el-form-item label="显示顺序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" controls-position="right" :min="0" />
        </el-form-item>

        <el-form-item label="备注说明" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="关于该文档的填写说明..." />
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
import { listDoc, getDoc, delDoc, addDoc, updateDoc } from "@/api/system/doc"
import { checkRole } from "@/utils/permission" // 1. 引入权限工具

export default {
  name: "Doc",
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
      // 表格数据
      docList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        docName: null,
        docCategory: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        docName: [
          { required: true, message: "文档名称不能为空", trigger: "blur" }
        ],
        fileUrl: [
          { required: true, message: "请上传文件", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    checkRole, // 2. 注册方法

    /** 查询列表 */
    getList() {
      this.loading = true
      listDoc(this.queryParams).then(response => {
        this.docList = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    /** 🚀 点击下载文件 */
    handleDownload(row) {
      if (!row.fileUrl) return;
      // 调用若依通用的资源下载方法
      this.$download.resource(row.fileUrl);
    },

    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        docId: null,
        docName: null,
        docCategory: null,
        fileUrl: null,
        sortOrder: 0, // 默认为0
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
      this.ids = selection.map(item => item.docId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "上传新模板"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const docId = row.docId || this.ids
      getDoc(docId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改文档信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.docId != null) {
            updateDoc(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDoc(this.form).then(response => {
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
      const docIds = row.docId || this.ids
      this.$modal.confirm('是否确认删除该文档？').then(function() {
        return delDoc(docIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/doc/export', {
        ...this.queryParams
      }, `doc_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
