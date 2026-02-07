<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="参数名称" prop="configName">
        <el-input
          v-model="queryParams.configName"
          placeholder="请输入参数名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="参数键名" prop="configKey">
        <el-input
          v-model="queryParams.configKey"
          placeholder="请输入参数键名"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="系统内置" prop="configType">
        <el-select v-model="queryParams.configType" placeholder="系统内置" clearable>
          <el-option
            v-for="dict in dict.type.sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['system:config:add']"
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
          v-hasPermi="['system:config:edit']"
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
          v-hasPermi="['system:config:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:config:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleRefreshCache"
          v-hasPermi="['system:config:remove']"
        >刷新缓存</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="参数主键" align="center" prop="configId" />
      <el-table-column label="参数名称" align="center" prop="configName" :show-overflow-tooltip="true" />
      <el-table-column label="参数键名" align="center" prop="configKey" :show-overflow-tooltip="true" />
      <el-table-column label="参数键值" align="center" prop="configValue" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <div v-if="scope.row.configKey === 'sys.login.background_image'">
            <div v-if="scope.row.configValue" class="cell-image-preview">
              <img
                :src="getImageUrl(scope.row.configValue)"
                class="table-preview-image"
                @error="handleTableImageError"
              />
              <div class="image-path">{{ scope.row.configValue }}</div>
            </div>
            <div v-else class="cell-image-preview">
              <div class="no-image">未设置背景图</div>
            </div>
          </div>
          <div v-else class="config-value-text">
            {{ scope.row.configValue }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="系统内置" align="center" prop="configType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.configType"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:config:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:config:remove']"
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

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body @close="handleDialogClose">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="form.configName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input v-model="form.configKey" placeholder="请输入参数键名" />
        </el-form-item>
        <el-form-item label="参数键值" prop="configValue">
          <!-- 如果是登录背景图配置，显示上传组件 -->
          <template v-if="form.configKey === 'sys.login.background_image'">
            <div class="login-bg-upload">
              <!-- 图片预览区域 -->
              <div v-if="form.configValue" class="image-preview-wrapper">
                <div class="preview-title">当前背景图预览：</div>
                <div class="image-preview">
                  <img
                    :src="getImageUrl(form.configValue)"
                    class="preview-image"
                    @error="handleImageError"
                  />
                  <div class="image-info">
                    <span>路径：{{ form.configValue }}</span>
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-delete"
                      @click="clearBackgroundImage"
                    >
                      清除
                    </el-button>
                  </div>
                </div>
              </div>

              <!-- 上传区域 -->
              <div class="upload-area">
                <div class="upload-section">
                  <div class="upload-title">方式一：上传图片</div>
                  <el-upload
                    ref="upload"
                    class="upload-demo"
                    :action="uploadUrl"
                    :headers="headers"
                    :on-success="handleUploadSuccess"
                    :on-error="handleUploadError"
                    :before-upload="beforeUpload"
                    :show-file-list="false"
                    :limit="1"
                    accept=".jpg,.jpeg,.png,.gif"
                    :disabled="uploadLoading"
                  >
                    <el-button
                      type="primary"
                      icon="el-icon-upload"
                      :loading="uploadLoading"
                    >
                      {{ uploadLoading ? '上传中...' : '选择图片上传' }}
                    </el-button>
                    <div slot="tip" class="el-upload__tip">
                      支持 JPG、PNG、GIF 格式，建议尺寸 1920×1080，大小不超过 2MB
                    </div>
                  </el-upload>
                </div>

                <div class="or-divider">或</div>

                <!-- 手动输入URL -->
                <div class="url-section">
                  <div class="upload-title">方式二：输入URL</div>
                  <el-input
                    v-model="form.configValue"
                    placeholder="请输入图片URL或服务器路径"
                    clearable
                  >
                    <template slot="append">
                      <el-button @click="testImageUrl" :loading="testingUrl">
                        {{ testingUrl ? '测试中...' : '测试链接' }}
                      </el-button>
                    </template>
                  </el-input>
                  <div class="el-upload__tip">
                    不需要改动背景就填“无”
                  </div>
                </div>
              </div>
            </div>
          </template>
          <template v-else-if="form.configKey === 'sys.login.title'">
            <el-input
              v-model="form.configValue"
              placeholder="请输入登录页标题"
              clearable
            >
              <template slot="prepend">标题：</template>
            </el-input>
          </template>
          <template v-else>
            <el-input
              v-model="form.configValue"
              placeholder="请输入参数键值"
              :rows="form.configType === 'Y' ? 4 : 2"
              :type="form.configType === 'Y' ? 'textarea' : 'text'"
            />
          </template>
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form.configType">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listConfig,
  getConfig,
  delConfig,
  addConfig,
  updateConfig,
  refreshCache
} from "@/api/system/config";
import { getToken } from "@/utils/auth";

export default {
  name: "Config",
  dicts: ['sys_yes_no'],
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
      // 参数表格数据
      configList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        configName: undefined,
        configKey: undefined,
        configType: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        configName: [
          { required: true, message: "参数名称不能为空", trigger: "blur" }
        ],
        configKey: [
          { required: true, message: "参数键名不能为空", trigger: "blur" }
        ],
        configValue: [
          { required: true, message: "参数键值不能为空", trigger: "blur" }
        ]
      },
      // 上传相关
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      uploadLoading: false,
      testingUrl: false,
      submitLoading: false
    }
  },
  created() {
    console.log('listConfig is:', typeof listConfig);
    this.getList();
  },
  methods: {
    /** 查询参数列表 */
    getList() {
      this.loading = true;
      listConfig(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.configList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(error => {
        this.loading = false;
        console.error('获取配置列表失败:', error);
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
      this.uploadLoading = false;
      this.testingUrl = false;
    },

    // 表单重置
    reset() {
      this.form = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: "Y",
        remark: undefined
      };
      this.resetForm("form");
      this.uploadLoading = false;
      this.testingUrl = false;
    },

    // 获取完整图片URL
    getImageUrl(path) {
      if (!path) return '';
      if (path.startsWith('http://') || path.startsWith('https://')) {
        return path;
      }
      if (path.startsWith('/')) {
        return process.env.VUE_APP_BASE_API + path + '?t=' + new Date().getTime();
      }
      return path;
    },

    // 图片加载失败处理（表格中）
    handleTableImageError(event) {
      console.warn('表格图片加载失败:', event.target.src);
      event.target.style.display = 'none';
    },

    // 图片加载失败处理（对话框中）
    handleImageError(event) {
      console.warn('对话框图片加载失败:', event.target.src);
      event.target.style.display = 'none';
    },

    // 清除背景图
    clearBackgroundImage() {
      this.form.configValue = '';
      this.$modal.msgSuccess("已清除背景图");
    },

    // 上传前校验
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$modal.msgError('只能上传图片文件！');
        return false;
      }
      if (!isLt2M) {
        this.$modal.msgError('图片大小不能超过 2MB！');
        return false;
      }

      this.uploadLoading = true;
      return true;
    },

    // 上传成功回调
    handleUploadSuccess(response, file, fileList) {
      this.uploadLoading = false;

      if (response.code === 200) {
        // 获取返回的文件路径
        let filePath = '';
        if (response.data && response.data.url) {
          filePath = response.data.url;
        } else if (response.data && response.data.fileName) {
          filePath = response.data.fileName;
        } else if (response.fileName) {
          filePath = response.fileName;
        } else if (response.url) {
          filePath = response.url;
        }

        if (filePath) {
          this.form.configValue = filePath;
          this.$modal.msgSuccess("上传成功");
        } else {
          this.$modal.msgError("上传失败：未获取到文件路径");
        }
      } else {
        this.$modal.msgError(response.msg || "上传失败");
      }
    },

    // 上传失败回调
    handleUploadError(error, file, fileList) {
      this.uploadLoading = false;
      console.error('上传失败:', error);
      this.$modal.msgError("上传失败，请重试");
    },

    // 测试图片URL
    testImageUrl() {
      const url = this.form.configValue;
      if (!url) {
        this.$modal.msgWarning("请输入图片URL");
        return;
      }

      this.testingUrl = true;

      const img = new Image();
      img.onload = () => {
        this.testingUrl = false;
        this.$modal.msgSuccess("图片URL有效");
      };
      img.onerror = () => {
        this.testingUrl = false;
        this.$modal.msgError("图片URL无效或无法访问");
      };
      img.src = this.getImageUrl(url);

      // 设置超时
      setTimeout(() => {
        if (!img.complete) {
          this.testingUrl = false;
          this.$modal.msgError("图片加载超时");
        }
      }, 5000);
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加参数";
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.configId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const configId = row.configId || this.ids;
      getConfig(configId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改参数";
      }).catch(error => {
        console.error('获取配置详情失败:', error);
      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          const promise = this.form.configId != undefined
            ? updateConfig(this.form)
            : addConfig(this.form);

          promise.then(response => {
            this.submitLoading = false;
            this.$modal.msgSuccess(this.form.configId != undefined ? "修改成功" : "新增成功");
            this.open = false;
            this.getList();
          }).catch(error => {
            this.submitLoading = false;
            console.error('保存配置失败:', error);
          });
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const configIds = row.configId || this.ids;
      this.$modal.confirm('是否确认删除参数编号为"' + configIds + '"的数据项？').then(function() {
        return delConfig(configIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('system/config/export', {
        ...this.queryParams
      }, `config_${new Date().getTime()}.xlsx`);
    },

    /** 刷新缓存按钮操作 */
    handleRefreshCache() {
      refreshCache().then(() => {
        this.$modal.msgSuccess("刷新成功");
      }).catch(error => {
        console.error('刷新缓存失败:', error);
      });
    },

    // 对话框关闭时清理状态
    handleDialogClose() {
      this.uploadLoading = false;
      this.testingUrl = false;
      this.submitLoading = false;
    }
  }
}
</script>

<style scoped>
/* 表格中的图片预览 */
.cell-image-preview {
  padding: 5px 0;
}

.table-preview-image {
  max-width: 80px;
  max-height: 50px;
  border-radius: 4px;
  border: 1px solid #ebeef5;
  object-fit: cover;
}

.image-path {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  word-break: break-all;
  line-height: 1.4;
}

.no-image {
  color: #c0c4cc;
  font-size: 12px;
}

.config-value-text {
  word-break: break-word;
}

/* 对话框中的上传样式 */
.login-bg-upload {
  width: 100%;
}

.image-preview-wrapper {
  margin-bottom: 20px;
}

.preview-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
  font-weight: 500;
}

.image-preview {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  background-color: #f5f7fa;
}

.preview-image {
  width: 100%;
  max-height: 200px;
  object-fit: contain;
  border-radius: 4px;
}

.image-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  padding: 5px 0;
  border-top: 1px solid #e4e7ed;
  font-size: 12px;
  color: #666;
}

.upload-area {
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.upload-section,
.url-section {
  margin-bottom: 15px;
}

.upload-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
  font-weight: 500;
}

.or-divider {
  text-align: center;
  margin: 15px 0;
  color: #909399;
  font-size: 13px;
  position: relative;
}

.or-divider::before,
.or-divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 45%;
  height: 1px;
  background-color: #dcdfe6;
}

.or-divider::before {
  left: 0;
}

.or-divider::after {
  right: 0;
}

.el-upload__tip {
  font-size: 12px;
  color: #909399;
  margin-top: 7px;
  line-height: 1.4;
}
</style>
