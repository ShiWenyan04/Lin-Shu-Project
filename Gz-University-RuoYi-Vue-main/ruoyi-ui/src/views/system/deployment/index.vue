<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件名" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名"
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
          v-hasPermi="['system:data:add']"
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
          v-hasPermi="['system:data:edit']"
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
          v-hasPermi="['system:data:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:data:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange" class="no-wrap-table">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="80" v-if="columns[0].visible">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="文件名" align="center" prop="fileName" v-if="columns[1].visible" show-overflow-tooltip/>
      <el-table-column label="所属成员" align="center" prop="student" v-if="columns[2].visible" show-overflow-tooltip/>
      <el-table-column label="上传人" align="center" prop="createdBy" v-if="columns[5].visible" show-overflow-tooltip/>
      <el-table-column label="上传时间" align="center" prop="createdTime" width="180" v-if="columns[6].visible">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="文件类型" align="center" prop="fileType" v-if="columns[3].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.file_type" :value="scope.row.fileType"/>
        </template>
      </el-table-column>
      <el-table-column label="文件大小(M)" align="center" prop="fileSize" v-if="columns[4].visible" width="120">
        <template slot-scope="scope">
          <span>{{ formatFileSize(scope.row.fileSize) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改时间" align="center" prop="updatedTime" width="180" v-if="columns[7].visible">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remarks" v-if="columns[9].visible" show-overflow-tooltip/>
      <el-table-column label="下载" align="center" prop="filePath" v-if="columns[8].visible">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)">
            下载
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-if="columns[10].visible">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:data:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:data:remove']"
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

    <!-- 添加或修改毕业生资料管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件名" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件名" />
        </el-form-item>
        <el-form-item label="所属成员" prop="student">
          <el-input v-model="form.student" placeholder="请输入所属成员" />
        </el-form-item>
        <el-form-item label="文件上传" prop="filePath">
          <el-upload
            ref="upload"
            class="upload-demo"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :on-remove="handleUploadRemove"
            :before-upload="beforeUpload"
            :file-list="fileList"
            :limit="1"
            :on-exceed="handleExceed"
            accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.zip,.rar,.7z,.txt,.jpg,.jpeg,.png,.gif">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">支持文档、图片、压缩文件等格式，且不超过100MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="文件类型" prop="fileType">
          <el-input v-model="form.fileType" placeholder="自动识别文件类型" readonly />
        </el-form-item>
        <el-form-item label="文件大小(M)" prop="fileSize">
          <el-input :value="formatFileSizeDisplay(form.fileSize)" placeholder="自动计算文件大小" readonly />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容" />
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
import { listData, getData, delData, addData, updateData } from "@/api/system/data"
import { getToken } from "@/utils/auth"

export default {
  name: "Data",
  dicts: ['file_type'],
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
      // 毕业生资料管理表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 上传文件列表
      fileList: [],
      // 上传地址
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      // 上传请求头
      uploadHeaders: { Authorization: "Bearer " + getToken() },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sequenceNo: null,
        fileName: null,
        student: null,
        fileType: null,
        createdBy: null,
        remarks: null,
        createdTime: null,
        updatedTime: null,
        filePath: null
      },
      // 列信息
      columns: [
        { key: 0, label: '序号', visible: true },
        { key: 1, label: '文件名', visible: true },
        { key: 2, label: '所属成员', visible: true },
        { key: 3, label: '文件类型', visible: true },
        { key: 4, label: '文件大小(M)', visible: true },
        { key: 5, label: '上传人', visible: true },
        { key: 6, label: '上传时间', visible: true },
        { key: 7, label: '修改时间', visible: true },
        { key: 8, label: '文件', visible: true },
        { key: 9, label: '备注', visible: true },
        { key: 10, label: '操作', visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fileName: [
          { required: true, message: "文件名不能为空", trigger: "blur" }
        ],
        student: [
          { required: true, message: "所属成员不能为空", trigger: "blur" }
        ],
        filePath: [
          { required: true, message: "请上传文件", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询毕业生资料管理列表 */
    getList() {
      this.loading = true
      listData(this.queryParams).then(response => {
        console.log('获取到的数据:', response.rows) // 调试用，查看实际返回的数据
        this.dataList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(error => {
        console.error('获取数据失败:', error)
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
        fileName: null,
        student: null,
        fileType: null,
        fileSize: null,
        createdBy: null,
        remarks: null,
        createdTime: null,
        updatedTime: null,
        filePath: null
      }
      this.fileList = []
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
      this.title = "添加毕业生资料管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids[0]
      getData(id).then(response => {
        this.form = response.data
        // 如果已有文件路径，初始化文件列表
        if (this.form.filePath) {
          const fileName = this.form.filePath.split('/').pop()
          this.fileList = [{
            name: fileName,
            url: this.form.filePath
          }]
        }
        this.open = true
        this.title = "修改毕业生资料管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateData(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addData(this.form).then(response => {
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
      const names = row.fileName || this.selectionRows.map(item => item.fileName).join('、')

      this.$modal.confirm('是否确认删除"' + names + '"?').then(function() {
        return delData(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/data/export', {
        ...this.queryParams
      }, `data_${new Date().getTime()}.xlsx`)
    },
    // 上传前校验
    beforeUpload(file) {
      const isLt100M = file.size / 1024 / 1024 < 100
      if (!isLt100M) {
        this.$message.error('上传文件大小不能超过100MB!')
        return false
      }
      return true
    },
    // 上传成功回调
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.form.filePath = response.fileName
        // 自动获取文件类型
        const fileName = file.name
        const fileExtension = fileName.split('.').pop().toLowerCase()
        this.form.fileType = this.getFileTypeByExtension(fileExtension)

        // 自动计算文件大小（转换为MB，保留2位小数）
        const fileSizeMB = (file.size / 1024 / 1024).toFixed(2)
        this.form.fileSize = parseFloat(fileSizeMB) // 转换为数字类型

        this.$message.success('文件上传成功')
      } else {
        this.$message.error(response.msg || '文件上传失败')
      }
    },
    // 上传失败回调
    handleUploadError(err, file, fileList) {
      this.$message.error('文件上传失败')
    },
    // 文件移除回调
    handleUploadRemove(file, fileList) {
      this.form.filePath = null
      this.form.fileType = null
      this.form.fileSize = null
    },
    // 文件超出限制
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，请删除后重新上传`)
    },
    // 根据文件扩展名获取文件类型
    getFileTypeByExtension(extension) {
      const typeMap = {
        // 文档类型
        'pdf': 'pdf',
        'doc': 'doc',
        'docx': 'doc',
        'xls': 'xls',
        'xlsx': 'xls',
        'ppt': 'ppt',
        'pptx': 'ppt',
        'txt': 'txt',
        // 图片类型
        'jpg': 'image',
        'jpeg': 'image',
        'png': 'image',
        'gif': 'image',
        'bmp': 'image',
        // 压缩文件类型
        'zip': 'zip',
        'rar': 'zip',
        '7z': 'zip',
        'tar': 'zip',
        'gz': 'zip'
      }
      return typeMap[extension] || 'other'
    },
    /** 格式化文件大小显示 */
    formatFileSize(fileSize) {
      if (!fileSize && fileSize !== 0) return '-'

      // 确保是数字类型
      const size = Number(fileSize)

      // 直接显示MB，保留2位小数
      return size.toFixed(2)
    },
    /** 格式化文件大小显示（用于表单显示） */
    formatFileSizeDisplay(fileSize) {
      if (!fileSize && fileSize !== 0) return ''

      // 确保是数字类型
      const size = Number(fileSize)

      // 直接显示MB，保留2位小数
      return size.toFixed(2)
    },
    /** 处理文件下载 */
    handleDownload(row) {
      if (row.filePath) {
        this.$download.resource(row.filePath, false)
      } else {
        this.$modal.msgError("文件路径不存在")
      }
    }
  }
}
</script>

<style scoped>
.upload-demo {
  width: 100%;
}

/* 设置表格单元格不换行 */
::v-deep .no-wrap-table .el-table .cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 确保表格整体布局合理 */
::v-deep .no-wrap-table .el-table {
  table-layout: fixed;
}

/* 为表格列设置合适的宽度分配 */
::v-deep .no-wrap-table .el-table th,
::v-deep .no-wrap-table .el-table td {
  padding: 8px 0;
}

/* 调整操作列按钮间距 */
::v-deep .no-wrap-table .el-table .small-padding .cell .el-button {
  margin: 0 2px;
  padding: 4px 8px;
}
</style>
