<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="布设区域" prop="area">
        <el-input
          v-model="queryParams.area"
          placeholder="请输入布设区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="对象类别" prop="objectCategory">
        <el-input
          v-model="queryParams.objectCategory"
          placeholder="请输入对象类别"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物种名称" prop="speciesName">
        <el-input
          v-model="queryParams.speciesName"
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
          v-hasPermi="['system:statistics:add']"
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
          v-hasPermi="['system:statistics:edit']"
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
          v-hasPermi="['system:statistics:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['system:statistics:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:statistics:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 修改表格样式，添加文本溢出处理 -->
    <el-table
      v-loading="loading"
      :data="statisticsList"
      @selection-change="handleSelectionChange"
      style="width: 100%"
      :cell-style="{overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap'}"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="80">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
        label="布设区域"
        align="center"
        prop="area"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="存储路径"
        align="center"
        prop="storagePath"
        :show-overflow-tooltip="true"
        min-width="150"
      />
      <el-table-column
        label="文件编号"
        align="center"
        prop="fileNo"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="原始文件编号"
        align="center"
        prop="originalFileNo"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="文件格式"
        align="center"
        prop="fileFormat"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="文件夹编号"
        align="center"
        prop="folderNo"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="相机编号"
        align="center"
        prop="cameraNo"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="布设位点编号"
        align="center"
        prop="siteNo"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="拍摄日期"
        align="center"
        prop="photoDate"
        :show-overflow-tooltip="true"
        min-width="130"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.photoDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="拍摄时间"
        align="center"
        prop="photoTime"
        :show-overflow-tooltip="true"
        min-width="130"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.photoTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="工作天数"
        align="center"
        prop="workingDays"
        :show-overflow-tooltip="true"
        min-width="100"
      />
      <el-table-column
        label="对象类别"
        align="center"
        prop="objectCategory"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="物种名称"
        align="center"
        prop="speciesName"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="动物数量"
        align="center"
        prop="animalCount"
        :show-overflow-tooltip="true"
        min-width="100"
      />
      <el-table-column
        label="性别"
        align="center"
        prop="gender"
        :show-overflow-tooltip="true"
        min-width="80"
      />
      <el-table-column
        label="独立探测首张"
        align="center"
        prop="firstPhoto"
        :show-overflow-tooltip="true"
        min-width="120"
      />
      <el-table-column
        label="有效照片"
        align="center"
        prop="validPhotos"
        :show-overflow-tooltip="true"
        min-width="100"
      />
      <el-table-column
        label="温度"
        align="center"
        prop="temperature"
        :show-overflow-tooltip="true"
        min-width="100"
      />
      <el-table-column
        label="备注"
        align="center"
        prop="remarks"
        :show-overflow-tooltip="true"
        min-width="150"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="150"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:statistics:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:statistics:remove']"
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
        <el-checkbox v-model="upload.updateSupport">是否更新已经存在的数据</el-checkbox>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改红外相机数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <div class="form-container">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="布设区域" prop="area">
                <el-input v-model="form.area" placeholder="请输入布设区域" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="存储路径" prop="storagePath">
                <el-input v-model="form.storagePath" placeholder="请输入存储路径" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="文件编号" prop="fileNo">
                <el-input v-model="form.fileNo" placeholder="请输入文件编号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="原始文件编号" prop="originalFileNo">
                <el-input v-model="form.originalFileNo" placeholder="请输入原始文件编号" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="文件格式" prop="fileFormat">
                <el-input v-model="form.fileFormat" placeholder="请输入文件格式" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="文件夹编号" prop="folderNo">
                <el-input v-model="form.folderNo" placeholder="请输入文件夹编号" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="相机编号" prop="cameraNo">
                <el-input v-model="form.cameraNo" placeholder="请输入相机编号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="布设位点编号" prop="siteNo">
                <el-input v-model="form.siteNo" placeholder="请输入布设位点编号" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="拍摄日期" prop="photoDate">
                <el-date-picker clearable
                                v-model="form.photoDate"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="请选择拍摄日期"
                                style="width: 100%">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="拍摄时间" prop="photoTime">
                <el-time-picker clearable
                                v-model="form.photoTime"
                                value-format="HH:mm:ss"
                                placeholder="请选择拍摄时间"
                                style="width: 100%">
                </el-time-picker>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="工作天数" prop="workingDays">
                <el-input v-model="form.workingDays" placeholder="请输入工作天数" type="number" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="对象类别" prop="objectCategory">
                <el-input v-model="form.objectCategory" placeholder="请输入对象类别" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="物种名称" prop="speciesName">
                <el-input v-model="form.speciesName" placeholder="请输入物种名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="动物数量" prop="animalCount">
                <el-input v-model="form.animalCount" placeholder="请输入动物数量" type="number" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="性别" prop="gender">
                <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
                  <el-option label="雄性" value="male"></el-option>
                  <el-option label="雌性" value="female"></el-option>
                  <el-option label="未知" value="unknown"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="独立探测首张" prop="firstPhoto">
                <el-input v-model="form.firstPhoto" placeholder="请输入独立探测首张" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="有效照片" prop="validPhotos">
                <el-input v-model="form.validPhotos" placeholder="请输入有效照片" type="number" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="温度" prop="temperature">
                <el-input v-model="form.temperature" placeholder="请输入温度" type="number">
                  <template slot="append">°C</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="remarks">
                <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容" :rows="3" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStatistics, getStatistics, delStatistics, addStatistics, updateStatistics } from "@/api/system/statistics"

export default {
  name: "Statistics",
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
      // 红外相机数据表格数据
      statisticsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        area: null,
        objectCategory: null,
        speciesName: null,
      },
      // 导入参数
      upload: {
        open: false,
        title: "",
        isUploading: false,
        updateSupport: 0,
        headers: { Authorization: "Bearer " + this.$store.getters.token },
        url: process.env.VUE_APP_BASE_API + "/system/statistics/importData"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        area: [
          { required: true, message: "布设区域不能为空", trigger: "blur" }
        ],
        fileNo: [
          { required: true, message: "文件编号不能为空", trigger: "blur" }
        ],
        originalFileNo: [
          { required: true, message: "原始文件编号不能为空", trigger: "blur" }
        ],
        fileFormat: [
          { required: true, message: "文件格式不能为空", trigger: "blur" }
        ],
        folderNo: [
          { required: true, message: "文件夹编号不能为空", trigger: "blur" }
        ],
        cameraNo: [
          { required: true, message: "相机编号不能为空", trigger: "blur" }
        ],
        siteNo: [
          { required: true, message: "布设位点编号不能为空", trigger: "blur" }
        ],
        photoDate: [
          { required: true, message: "拍摄日期不能为空", trigger: "blur" }
        ],
        photoTime: [
          { required: true, message: "拍摄时间不能为空", trigger: "blur" }
        ],
        workingDays: [
          { required: true, message: "工作天数不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询红外相机数据列表 */
    getList() {
      this.loading = true
      listStatistics(this.queryParams).then(response => {
        this.statisticsList = response.rows
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
        area: null,
        storagePath: null,
        fileNo: null,
        originalFileNo: null,
        fileFormat: null,
        folderNo: null,
        cameraNo: null,
        siteNo: null,
        photoDate: null,
        photoTime: null,
        workingDays: null,
        objectCategory: null,
        speciesName: null,
        animalCount: null,
        gender: null,
        firstPhoto: null,
        validPhotos: null,
        temperature: null,
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加红外相机数据"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getStatistics(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改红外相机数据"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 判断是添加还是修改操作
          if (this.title === "添加红外相机数据") {
            this.handleAddSubmit();
          } else if (this.title === "修改红外相机数据") {
            this.handleUpdateSubmit();
          }
        }
      });
    },

    /** 处理添加提交 */
    handleAddSubmit() {
      addStatistics(this.form).then(response => {
        this.$modal.msgSuccess("新增成功");
        this.open = false; // 关闭对话框
        this.getList();    // 刷新数据列表
      }).catch(this.handleSubmitError);
    },

    /** 处理修改提交 */
    handleUpdateSubmit() {
      updateStatistics(this.form).then(response => {
        this.$modal.msgSuccess("修改成功");
        this.open = false; // 关闭对话框
        this.getList();    // 刷新数据列表
      }).catch(this.handleSubmitError);
    },

    /** 统一处理提交错误 */
    handleSubmitError(error) {
      // 处理物种不存在的情况
      if (error.response?.data?.code === 600) {
        const speciesName = error.response.data.speciesName || this.form.speciesName;
        this.$confirm(error.response.data.msg, "提示", {
          confirmButtonText: "去添加",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          // 保存当前表单数据到本地存储
          localStorage.setItem('cameraStatisticsForm', JSON.stringify(this.form));

          this.$router.push({
            path: "/system/species/add",
            query: { speciesName: speciesName }
          });
        }).catch(() => {});
      } else {
        // 处理其他错误
        let errorMessage = "操作失败";

        if (error.response?.data?.msg) {
          errorMessage = error.response.data.msg;
        } else if (error.message) {
          errorMessage = error.message;
        }

        this.$modal.msgError(errorMessage);
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除红外相机数据编号为"' + ids + '"的数据项？').then(function() {
        return delStatistics(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/statistics/export', {
        ...this.queryParams
      }, `statistics_${new Date().getTime()}.xlsx`)
    },
    handleImport() {
      this.upload.title = "红外相机数据导入";
      this.upload.updateSupport = 0;
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('/system/statistics/importTemplate', {}, `红外相机数据导入模板_${new Date().getTime()}.xlsx`);
    },

    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },

    // 提交上传文件
    submitFileForm() {
      if (this.$refs.upload.uploadFiles.length === 0) {
        this.$message.warning("请先选择文件");
        return;
      }

      this.$refs.upload.submit();
    },

    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();

      // 无论成功还是失败，都显示消息
      if (response.code === 200) {
        // 使用v-html指令渲染HTML内容
        this.$alert(response.msg, "导入结果", {
          dangerouslyUseHTMLString: true,
          customClass: 'import-result-alert',
          confirmButtonText: '确定',
          callback: action => {
            this.getList();
          }
        });
      } else {
        this.$alert(response.msg, "导入失败", {
          dangerouslyUseHTMLString: true
        });
      }
    },
  },
  mounted() {
    // 添加路由变化监听器
    this.$watch(
      () => this.$route,
      (to, from) => {
        // 如果是从物种添加页面返回
        if (from.path === "/system/species/add" && to.path === "/system/statistics") {
          // 恢复保存的表单数据
          const savedForm = localStorage.getItem('cameraStatisticsForm');
          if (savedForm) {
            this.form = JSON.parse(savedForm);
            this.open = true;
            this.title = "添加红外相机数据";
            localStorage.removeItem('cameraStatisticsForm');

            // 自动重新提交表单
            setTimeout(() => {
              this.submitForm();
            }, 500);
          }
        }
      }
    )
  }
}
</script>
<style scoped>
/* 添加样式 */
.import-result-alert {
  max-width: 80%;
}
</style>
