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
          v-model="queryParams.samplingPerson"
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
          v-hasPermi="['system:information:add']"
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
          v-hasPermi="['system:information:edit']"
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
          v-hasPermi="['system:information:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['system:information:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:information:export']"
        >导出</el-button>
      </el-col>
      <!-- 添加批量用样按钮 -->
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-s-operation"
          size="mini"
          :disabled="multiple"
          @click="handleBatchUse"
          v-hasPermi="['system:information:batchUse']"
        >批量用样</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <!-- 修改表格样式，使用自适应列宽 -->
    <el-table
      v-loading="loading"
      :data="informationList"
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
        prop="sampleName"
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
        prop="location"
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
        prop="samplingPerson"
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
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        v-if="columns[11].visible"
        width="180"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:information:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:information:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-operation"
            @click="handleUse(scope.row)"
            v-hasPermi="['system:information:use']"
            style="color: #e6a23c;"
          >用样</el-button>
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

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url "
        :data="uploadParams"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport"/> 是否更新已经存在的样本数据
          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入"xls"或"xlsx"格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加批量用样对话框 -->
    <el-dialog title="批量用样" :visible.sync="batchUseDialogVisible" width="600px" append-to-body>
      <el-form ref="batchUseForm" :model="batchUseForm" :rules="batchUseRules" label-width="100px">
        <el-form-item label="用样时间" prop="usedTime">
          <el-date-picker
            clearable
            v-model="batchUseForm.usedTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择用样时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="用途" prop="purpose">
          <el-input v-model="batchUseForm.purpose" type="textarea" placeholder="请输入用途" />
        </el-form-item>
        <el-form-item label="选中样本">
          <el-table :data="selectedSamples" border size="small" max-height="300">
            <el-table-column label="样本编号" prop="sampleName" />
            <el-table-column label="宿主" prop="host" />
            <el-table-column label="样本类型" prop="sampleType" />
          </el-table>
          <div style="margin-top: 10px; color: #909399; font-size: 12px;">
            共选中 {{ selectedSamples.length }} 个样本
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBatchUseForm">确 定</el-button>
        <el-button @click="batchUseDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改采样信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="样本编号" prop="sampleName">
          <el-input v-model="form.sampleName" placeholder="请输入样本编号" />
        </el-form-item>
        <el-form-item label="宿主" prop="host">
          <el-input v-model="form.host" placeholder="请输入宿主" />
        </el-form-item>
        <el-form-item label="样本类型" prop="sampleType">
          <el-input v-model="form.sampleType" placeholder="请输入样本类型" />
        </el-form-item>
        <el-form-item label="采样地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入采样地点" />
        </el-form-item>
        <el-form-item label="生境类型" prop="habitatType">
          <el-input v-model="form.habitatType" placeholder="请输入生境类型" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input-number
            v-model="form.longitude"
            placeholder="请输入经度"
            :precision="6"
            :step="0.000001"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input-number
            v-model="form.latitude"
            placeholder="请输入纬度"
            :precision="6"
            :step="0.000001"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="采样时间" prop="samplingTime">
          <el-date-picker
            clearable
            v-model="form.samplingTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择采样时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="采样人员" prop="samplingPerson">
          <el-input v-model="form.samplingPerson" placeholder="请输入采样人员" />
        </el-form-item>
        <el-form-item label="保存位置" prop="storageLocation">
          <el-input v-model="form.storageLocation" placeholder="请输入保存位置" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用样对话框 -->
    <el-dialog title="样本使用" :visible.sync="useDialogVisible" width="500px" append-to-body>
      <el-form ref="useForm" :model="useForm" :rules="useRules" label-width="100px">
        <el-form-item label="样本编号" prop="sampleName">
          <el-input v-model="useForm.sampleName" disabled />
        </el-form-item>
        <el-form-item label="宿主" prop="host">
          <el-input v-model="useForm.host" disabled />
        </el-form-item>
        <el-form-item label="样本类型" prop="sampleType">
          <el-input v-model="useForm.sampleType" disabled />
        </el-form-item>
        <el-form-item label="采样地点" prop="location">
          <el-input v-model="useForm.location" disabled />
        </el-form-item>
        <el-form-item label="生境类型" prop="habitatType">
          <el-input v-model="useForm.habitatType" disabled />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="useForm.longitude" disabled />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="useForm.latitude" disabled />
        </el-form-item>
        <el-form-item label="采样时间" prop="samplingTime">
          <el-input v-model="useForm.samplingTime" disabled />
        </el-form-item>
        <el-form-item label="采样人员" prop="samplingPerson">
          <el-input v-model="useForm.samplingPerson" disabled />
        </el-form-item>
        <el-form-item label="保存位置" prop="storageLocation">
          <el-input v-model="useForm.storageLocation" disabled />
        </el-form-item>
        <el-form-item label="用样时间" prop="usedTime">
          <el-date-picker
            clearable
            v-model="useForm.usedTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择用样时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="用途" prop="purpose">
          <el-input v-model="useForm.purpose" type="textarea" placeholder="请输入用途" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUseForm">确 定</el-button>
        <el-button @click="useDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInformation, getInformation, delInformation, addInformation, updateInformation, useSample, checkSampleNameUnique, batchUseSample } from "@/api/system/information"
import { getToken } from "@/utils/auth"
export default {
  name: "Information",
  data() {
    // 自定义验证规则：检查样本编号是否唯一
    const validateSampleName = (rule, value, callback) => {
      if (!value) {
        callback(new Error('样本编号不能为空'));
        return;
      }

      // 调用后端检查样本编号是否唯一
      checkSampleNameUnique({
        sampleName: value,
        id: this.form.id // 编辑时传递当前记录的ID，排除自身
      }).then(response => {
        if (response.data) {
          callback(new Error('样本编号已存在'));
        } else {
          callback();
        }
      }).catch(() => {
        callback();
      });
    };
    const that = this;
    return {
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
        { key: 11, label: '操作', visible: true }
      ],
      originalSampleName: '', // 保存编辑前的样本编号
      isChecking: false, // 防止重复验证
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中的样本数据 - 确保这个在return内部
      selectedSamples: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 采样信息表格数据
      informationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        host: null,
        sampleType: null,  // 修改为样本类型
        samplingPerson: null,  // 添加采样人员
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sampleName: [
          { required: true, message: "样本编号不能为空", trigger: "blur" },
          { validator: (rule, value, callback) => that.validateSampleName(rule, value, callback), trigger: "blur" }
        ],
        host: [
          { required: true, message: "宿主不能为空", trigger: "blur" }
        ],
        sampleType: [
          { required: true, message: "样本类型不能为空", trigger: "blur" }
        ],
        samplingPerson: [
          { required: true, message: "采样人员不能为空", trigger: "blur" }
        ],
        storageLocation: [
          { required: true, message: "保存位置不能为空", trigger: "blur" }
        ]
      },
      // 用样对话框显示状态
      useDialogVisible: false,
      // 批量用样对话框显示状态
      batchUseDialogVisible: false,
      // 用样表单数据
      useForm: {
        sampleId: null,
        sampleName: '',
        host: '',
        sampleType: '',
        location: '',
        habitatType: '',
        longitude: null,
        latitude: null,
        samplingTime: '',
        samplingPerson: '',
        storageLocation: '',
        usedTime: null,
        purpose: ''
      },
      // 批量用样表单数据
      batchUseForm: {
        usedTime: null,
        purpose: ''
      },
      // 用样表单验证规则
      useRules: {
        usedTime: [
          { required: true, message: "用样时间不能为空", trigger: "change" }
        ],
        purpose: [
          { required: true, message: "用途不能为空", trigger: "blur" }
        ]
      },
      // 批量用样表单验证规则
      batchUseRules: {
        usedTime: [
          { required: true, message: "用样时间不能为空", trigger: "change" }
        ],
        purpose: [
          { required: true, message: "用途不能为空", trigger: "blur" }
        ]
      },
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,

        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/information/importData"
      },
      uploadParams: {
        updateSupport: true
      }
    }
  },
  computed: {
    // 添加计算属性确保安全访问
    safeSelectedSamples() {
      return this.selectedSamples || [];
    }
  },
  created() {
    this.getList()
    // 双重保险，确保 selectedSamples 被初始化
    if (!this.selectedSamples) {
      this.selectedSamples = [];
    }
    // 确保 queryParams 被初始化
    if (!this.queryParams) {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        host: null,
        sampleType: null,
        samplingPerson: null,
      };
    }
  },
  methods: {
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "采样信息导入";
      this.upload.open = true;
    },

    /** 下载模板操作 */
    importTemplate() {
      this.download('system/information/importTemplate', {}, `采样信息导入模板_${new Date().getTime()}.xlsx`)
    },

    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },

    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();

      // 使用更简洁的提示方式
      if (response.code === 200) {
        this.$modal.msgSuccess(response.msg);
      } else {
        this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '确定'
        });
      }

      this.getList();
    },

    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 查询采样信息列表 */
    getList() {
      this.loading = true
      listInformation(this.queryParams).then(response => {
        this.informationList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
      this.originalSampleName = '';
      this.isChecking = false;
    },
    // 自定义验证规则：检查样本编号是否唯一
    validateSampleName(rule, value, callback) {
      if (!value) {
        callback(new Error('样本编号不能为空'));
        return;
      }

      // 防止重复验证
      if (this.isChecking) {
        callback();
        return;
      }

      this.isChecking = true;

      // 如果是编辑模式且样本编号没有变化，直接通过
      if (this.form.id && this.originalSampleName === value) {
        this.isChecking = false;
        callback();
        return;
      }

      // 调用后端检查样本编号是否唯一
      checkSampleNameUnique({
        sampleName: value,
        id: this.form.id
      }).then(response => {
        this.isChecking = false;
        if (response.data) {
          callback(new Error('样本编号已存在'));
        } else {
          callback();
        }
      }).catch(() => {
        this.isChecking = false;
        callback();
      });
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        sequenceNo: null,
        sampleName: null,
        host: null,
        sampleType: null,
        samplingTime: null,
        location: null,
        habitatType: null,
        longitude: null,
        latitude: null,
        samplingPerson: null,
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
      this.selectedSamples = selection; // 保存选中的样本数据
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },

    /** 批量用样按钮操作 */
    handleBatchUse() {
      if (this.ids.length === 0) {
        this.$modal.msgError("请先选择要使用的样本");
        return;
      }

      // 重置批量用样表单
      this.batchUseForm = {
        usedTime: null,
        purpose: ''
      };

      // 打开对话框前清除验证
      this.$nextTick(() => {
        if (this.$refs.batchUseForm) {
          this.$refs.batchUseForm.clearValidate();
        }
      });

      this.batchUseDialogVisible = true;
    },

    /** 提交批量用样表单 */
    submitBatchUseForm() {
      this.$refs["batchUseForm"].validate(valid => {
        if (valid) {
          // 确保用样时间格式正确
          if (this.batchUseForm.usedTime && this.batchUseForm.usedTime.length > 10) {
            this.batchUseForm.usedTime = this.batchUseForm.usedTime.substring(0, 10);
          }
          // 将样本ID转换为长整型（确保数据类型正确）
          const sampleIds = this.ids.map(id => Number(id));

          /// 创建批量用样请求对象
          const requestData = {
            sampleIds: sampleIds, // 使用转换后的数组
            usedTime: this.batchUseForm.usedTime,
            purpose: this.batchUseForm.purpose
          };

          console.log("提交批量用样数据:", requestData);
          console.log("样本ID类型检查:", typeof sampleIds[0], sampleIds);

          // 显示加载指示器
          const loading = this.$loading({
            lock: true,
            text: '批量处理中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });

          // 调用批量用样API
          batchUseSample(requestData)
            .then(response => {
              loading.close();
              console.log("批量用样API响应:", response);
              if (response.code === 200) {
                this.$modal.msgSuccess(`成功为 ${this.ids.length} 个样本添加用样记录`);
                this.batchUseDialogVisible = false;
                this.getList(); // 刷新列表
                // 清空选中状态
                this.ids = [];
                this.selectedSamples = [];
                this.multiple = true;
              } else {
                this.$modal.msgError(response.msg || "批量用样失败");
              }
            })
            .catch(error => {
              loading.close();
              console.error("批量用样API错误:", error);
              this.$modal.msgError("批量用样失败");
            });
        } else {
          this.$modal.msgError("请填写完整的用样信息");
        }
      });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加采样信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getInformation(id).then(response => {
        this.form = response.data;
        // 保存原始样本编号
        this.originalSampleName = response.data.sampleName;
        this.open = true;
        this.title = "修改采样信息";

        // 清除验证状态
        this.$nextTick(() => {
          if (this.$refs.form) {
            this.$refs.form.clearValidate();
          }
        });
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          this.$modal.msgError("表单验证失败，请检查输入");
          return;
        }

        // 确保采样时间格式正确
        if (this.form.samplingTime && this.form.samplingTime.length > 10) {
          this.form.samplingTime = this.form.samplingTime.substring(0, 10);
        }

        if (this.form.id != null) {
          updateInformation(this.form).then(response => {
            if (response.code === 200) {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            } else {
              this.$modal.msgError(response.msg || "修改失败");
            }
          }).catch(error => {
            this.$modal.msgError("修改失败: " + (error.msg || "未知错误"));
          });
        } else {
          addInformation(this.form).then(response => {
            if (response.code === 200) {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            } else {
              this.$modal.msgError(response.msg || "新增失败");
            }
          }).catch(error => {
            this.$modal.msgError("新增失败: " + (error.msg || "未知错误"));
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;

      // 获取样本名称用于显示
      let sampleNames = [];
      if (row.id) {
        // 单个删除
        sampleNames = [row.sampleName];
      } else {
        // 批量删除 - 从选中的样本中获取名称
        sampleNames = this.selectedSamples.map(item => item.sampleName);
      }

      let confirmMessage = '';
      if (sampleNames.length === 1) {
        confirmMessage = `是否确认删除样本编号为 "${sampleNames[0]}" 的数据项？`;
      } else if (sampleNames.length > 1) {
        const displayNames = sampleNames.slice(0, 3).join('、');
        const extraCount = sampleNames.length - 3;
        const extraText = extraCount > 0 ? ` 等 ${sampleNames.length} 个` : '';
        confirmMessage = `是否确认删除样本编号为 "${displayNames}${extraText}" 的数据项？`;
      } else {
        confirmMessage = '是否确认删除选中的数据项？';
      }

      this.$modal.confirm(confirmMessage).then(() => {
        return delInformation(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/information/export', {
        ...this.queryParams
      }, `information_${new Date().getTime()}.xlsx`)
    },
    /** 用样按钮操作 */
    handleUse(row) {
      // 重置表单并填充数据
      this.useForm = {
        sampleId: row.id,
        sampleName: row.sampleName,
        host: row.host,
        sampleType: row.sampleType,
        location: row.location,
        habitatType: row.habitatType,
        longitude: row.longitude,
        latitude: row.latitude,
        samplingTime: this.parseTime(row.samplingTime, '{y}-{m}-{d} {h}:{i}:{s}'),
        samplingPerson: row.samplingPerson,
        storageLocation: row.storageLocation,
        usedTime: null,
        purpose: ''
      };

      // 打开对话框前清除验证
      this.$nextTick(() => {
        if (this.$refs.useForm) {
          this.$refs.useForm.clearValidate();
        }
      });

      // 打开对话框
      this.useDialogVisible = true;
    },
    /** 提交用样表单 */
    submitUseForm() {
      this.$refs["useForm"].validate(valid => {
        if (valid) {
          // 确保用样时间格式正确
          if (this.useForm.usedTime && this.useForm.usedTime.length > 10) {
            this.useForm.usedTime = this.useForm.usedTime.substring(0, 10);
          }

          // 创建符合后端要求的请求对象
          const requestData = {
            sampleId: this.useForm.sampleId,
            purpose: this.useForm.purpose,
            usedTime: this.useForm.usedTime
          };

          console.log("提交用样数据:", requestData);

          // 调用API
          useSample(requestData)
            .then(response => {
              console.log("API响应:", response);
              if (response.code === 200) {
                this.$modal.msgSuccess("用样记录添加成功");
                this.useDialogVisible = false;
              } else {
                this.$modal.msgError(response.msg || "操作失败");
              }
            })
            .catch(error => {
              console.error("API错误:", error);
              this.$modal.msgError("用样失败");
            });
        } else {
          this.$modal.msgError("请填写完整的用样信息");
        }
      });
    }
  }
}
</script>
