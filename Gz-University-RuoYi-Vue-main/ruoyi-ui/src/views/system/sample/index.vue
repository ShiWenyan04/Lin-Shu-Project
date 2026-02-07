<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item label="测序类型" prop="sequencingType">
        <el-input
          v-model="queryParams.sequencingType"
          placeholder="请输入测序类型"
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
          v-hasPermi="['system:sample:add']"
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
          v-hasPermi="['system:sample:edit']"
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
          v-hasPermi="['system:sample:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['system:sample:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:sample:export']"
        >导出</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <!-- 修改表格样式，添加文本溢出处理 -->
    <el-table
      v-loading="loading"
      :data="sampleList"
      @selection-change="handleSelectionChange"
      style="width: 100%"
      :cell-style="{overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap'}"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" v-if="false" />
      <el-table-column label="序号" align="center" width="80" v-if="columns[0].visible">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
        label="测序编号"
        align="center"
        prop="sequencingNo"
        v-if="columns[1].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="样本编号"
        align="center"
        prop="sampleName"
        v-if="columns[2].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="宿主"
        align="center"
        prop="host"
        v-if="columns[3].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="样本类型"
        align="center"
        prop="sampleType"
        v-if="columns[4].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="采样地点"
        align="center"
        prop="samplingLocation"
        v-if="columns[5].visible"
        :show-overflow-tooltip="true"
        min-width="150"
      />
      <el-table-column
        label="生境类型"
        align="center"
        prop="habitatType"
        v-if="columns[6].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="经度"
        align="center"
        prop="longitude"
        v-if="columns[7].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="纬度"
        align="center"
        prop="latitude"
        v-if="columns[8].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="采样时间"
        align="center"
        v-if="columns[9].visible"
        :show-overflow-tooltip="true"
        min-width="150"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.samplingTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="采样人员"
        align="center"
        prop="samplingPerson"
        v-if="columns[10].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="测序类型"
        align="center"
        prop="sequencingType"
        v-if="columns[11].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="测序者"
        align="center"
        prop="sequencer"
        v-if="columns[12].visible"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="测序公司"
        align="center"
        prop="sequencingCompany"
        v-if="columns[13].visible"
        :show-overflow-tooltip="true"
        min-width="150"
      />
      <el-table-column
        label="测序时间"
        align="center"
        v-if="columns[14].visible"
        :show-overflow-tooltip="true"
        min-width="150"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sequencingDate, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="存储位置"
        align="center"
        prop="storageLocation"
        v-if="columns[15].visible"
        :show-overflow-tooltip="true"
        min-width="150"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        v-if="columns[16].visible"
        width="150"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:sample:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:sample:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px">
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div style="margin-top: 15px;">
        <el-checkbox v-model="upload.updateSupport">是否更新已经存在的样本数据</el-checkbox>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改测序信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="测序编号" prop="sequencingNo">
              <el-input v-model="form.sequencingNo" placeholder="请输入测序编号" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="样本编号" prop="sampleName">
              <el-input v-model="form.sampleName" placeholder="请输入样本编号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="宿主" prop="host">
              <el-input v-model="form.host" placeholder="请输入宿主" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="样本类型" prop="sampleType">
              <el-input v-model="form.sampleType" placeholder="请输入样本类型" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="采样地点" prop="samplingLocation">
              <el-input v-model="form.samplingLocation" placeholder="请输入采样地点" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生境类型" prop="habitatType">
              <el-input v-model="form.habitatType" placeholder="请输入生境类型" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" placeholder="请输入经度" type="number" step="0.000001" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" placeholder="请输入纬度" type="number" step="0.000001" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="采样时间" prop="samplingTime">
              <el-date-picker clearable
                              v-model="form.samplingTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择采样时间"
                              style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采样人员" prop="samplingPerson">
              <el-input v-model="form.samplingPerson" placeholder="请输入采样人员" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="测序类型" prop="sequencingType">
              <el-input v-model="form.sequencingType" placeholder="请输入测序类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测序者" prop="sequencer">
              <el-input v-model="form.sequencer" placeholder="请输入测序者" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="测序公司" prop="sequencingCompany">
              <el-input v-model="form.sequencingCompany" placeholder="请输入测序公司" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测序时间" prop="sequencingDate">
              <el-date-picker clearable
                              v-model="form.sequencingDate"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择测序时间"
                              style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="存储位置" prop="storageLocation">
              <el-input v-model="form.storageLocation" placeholder="请输入存储位置" />
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
import { listSample, getSample, delSample, addSample, updateSample } from "@/api/system/sample"
import { getToken } from "@/utils/auth"

export default {
  name: "Sample",
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
      // 测序信息表格数据
      sampleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      // 导入参数
      upload: {
        open: false,
        title: "",
        isUploading: false,
        updateSupport: 0,
        headers: { Authorization: "Bearer " + getToken() },
        url: process.env.VUE_APP_BASE_API + "/system/sample/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        host: null,
        sampleType: null,
        sequencingType: null
      },
      // 列信息
      columns: [
        { key: 0, label: '序号', visible: true },
        { key: 1, label: '测序编号', visible: true },
        { key: 2, label: '样本编号', visible: true },
        { key: 3, label: '宿主', visible: true },
        { key: 4, label: '样本类型', visible: true },
        { key: 5, label: '采样地点', visible: true },
        { key: 6, label: '生境类型', visible: true },
        { key: 7, label: '经度', visible: true },
        { key: 8, label: '纬度', visible: true },
        { key: 9, label: '采样时间', visible: true },
        { key: 10, label: '采样人员', visible: true },
        { key: 11, label: '测序类型', visible: true },
        { key: 12, label: '测序者', visible: true },
        { key: 13, label: '测序公司', visible: true },
        { key: 14, label: '测序时间', visible: true },
        { key: 15, label: '存储位置', visible: true },
        { key: 16, label: '操作', visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sequencingNo: [
          { required: true, message: "测序编号不能为空", trigger: "blur" }
        ],
        sampleName: [
          { required: true, message: "样本编号不能为空", trigger: "blur" }
        ],
        host: [
          { required: true, message: "宿主不能为空", trigger: "blur" }
        ],
        sampleType: [
          { required: true, message: "样本类型不能为空", trigger: "blur" }
        ],
        sequencingType: [
          { required: true, message: "测序类型不能为空", trigger: "blur" }
        ],
        sequencer: [
          { required: true, message: "测序者不能为空", trigger: "blur" }
        ],
        sequencingCompany: [
          { required: true, message: "测序公司不能为空", trigger: "blur" }
        ],
        sequencingDate: [
          { required: true, message: "测序时间不能为空", trigger: "change" }
        ],
        storageLocation: [
          { required: true, message: "存储位置不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询测序信息列表 */
    getList() {
      this.loading = true
      listSample(this.queryParams).then(response => {
        this.sampleList = response.rows
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
        sequencingNo: null,
        sampleName: null,
        host: null,
        sampleType: null,
        samplingLocation: null,
        habitatType: null,
        longitude: null,
        latitude: null,
        samplingTime: null,
        samplingPerson: null,
        sequencingType: null,
        sequencer: null,
        sequencingCompany: null,
        sequencingDate: null,
        storageLocation: null
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
      this.title = "添加测序信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids[0]
      getSample(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改测序信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSample(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSample(this.form).then(response => {
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
      const names = row.sampleName || this.selectionRows.map(item => item.sampleName).join('、')

      this.$modal.confirm('是否确认删除测序信息"' + names + '"?').then(function() {
        return delSample(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/sample/export', {
        ...this.queryParams
      }, `sample_${new Date().getTime()}.xlsx`)
    },
    // 导入按钮操作
    handleImport() {
      this.upload.title = "测序样本导入"
      this.upload.updateSupport = 0
      this.upload.open = true
    },
    // 下载模板操作
    importTemplate() {
      this.download('system/sample/importTemplate', {}, `测序样本导入模板_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 提交上传文件
    submitFileForm() {
      if (this.$refs.upload.uploadFiles.length === 0) {
        this.$message.warning("请先选择文件")
        return
      }
      this.$refs.upload.submit()
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.isUploading = false
      if (response.code === 200) {
        this.$alert(response.msg, "导入结果", {
          dangerouslyUseHTMLString: true,
          callback: () => {
            this.upload.open = false
            this.$refs.upload.clearFiles()
            this.getList()
          }
        })
      } else {
        this.$message.error(response.msg || "导入失败")
      }
    }
  }
}
</script>
