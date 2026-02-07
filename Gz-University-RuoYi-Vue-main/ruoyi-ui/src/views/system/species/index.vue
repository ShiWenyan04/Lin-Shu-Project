<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物种名称" prop="speciesName">
        <el-input
          v-model="queryParams.speciesName"
          placeholder="请输入物种名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物种类别" prop="objectCategory">
        <el-select v-model="queryParams.objectCategory" placeholder="请选择物种类别" clearable>
          <el-option
            v-for="dict in dict.type.specimen_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="拍摄地点" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入拍摄地点"
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
          v-hasPermi="['system:species:add']"
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
          v-hasPermi="['system:species:edit']"
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
          v-hasPermi="['system:species:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:species:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 照片墙布局 -->
    <div class="photo-wall-container">
      <div v-loading="loading" class="photo-wall">
        <div v-for="item in speciesList" :key="item.id" class="photo-item">
          <div class="photo-wrapper" @click="previewPhoto(item)">
            <image-preview :src="item.photoPath" class="species-photo" />
            <div class="photo-overlay">
              <div class="photo-info">
                <div class="species-name">{{ item.speciesName }}</div>
                <div class="species-category">
                  <dict-tag :options="dict.type.specimen_category" :value="item.objectCategory" size="small" />
                </div>
              </div>
              <div class="photo-actions">
                <el-button
                  size="mini"
                  type="primary"
                  icon="el-icon-edit"
                  circle
                  @click.stop="handleUpdate(item)"
                  v-hasPermi="['system:species:edit']"
                ></el-button>
                <el-button
                  size="mini"
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  @click.stop="handleDelete(item)"
                  v-hasPermi="['system:species:remove']"
                ></el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 照片预览对话框 -->
    <el-dialog :visible.sync="photoPreviewVisible" width="60%" top="5vh" append-to-body>
      <div class="photo-preview-container">
        <el-image :src="currentPhoto" fit="contain" class="preview-image">
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
        <div class="preview-info" v-if="currentItem">
          <h3>{{ currentItem.speciesName }}</h3>
          <div class="info-row">
            <span class="label">物种类别：</span>
            <dict-tag :options="dict.type.specimen_category" :value="currentItem.objectCategory" />
          </div>
          <div class="info-row">
            <span class="label">拍摄地点：</span>
            <span>{{ currentItem.location }}</span>
          </div>
          <div class="info-row">
            <span class="label">拍摄时间：</span>
            <span>{{ parseTime(currentItem.photoTime, '{y}-{m}-{d}') }}</span>
          </div>
          <div class="info-row">
            <span class="label">备注：</span>
            <span>{{ currentItem.remarks || '无' }}</span>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="photoPreviewVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改野生动物图鉴对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="序号" prop="sequenceNo">
          <el-input v-model="form.sequenceNo" placeholder="请输入序号" />
        </el-form-item>
        <el-form-item label="物种名称" prop="speciesName">
          <el-input v-model="form.speciesName" placeholder="请输入物种名称" />
        </el-form-item>
        <el-form-item label="物种类别" prop="objectCategory">
          <el-select v-model="form.objectCategory" placeholder="请选择物种类别">
            <el-option
              v-for="dict in dict.type.specimen_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="拍摄地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入拍摄地点" />
        </el-form-item>
        <el-form-item label="拍摄时间" prop="photoTime">
          <el-date-picker clearable
            v-model="form.photoTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择拍摄时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="照片" prop="photoPath">
          <image-upload v-model="form.photoPath"/>
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
import { listSpecies, getSpecies, delSpecies, addSpecies, updateSpecies } from "@/api/system/species"

export default {
  name: "Species",
  dicts: ['specimen_category'],
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
      // 野生动物图鉴表格数据
      speciesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        speciesName: null,
        objectCategory: null,
        location: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        speciesName: [
          { required: true, message: "物种名称不能为空", trigger: "blur" }
        ],
        objectCategory: [
          { required: true, message: "物种类别不能为空", trigger: "change" }
        ],
        location: [
          { required: true, message: "拍摄地点不能为空", trigger: "blur" }
        ],
        photoTime: [
          { required: true, message: "拍摄时间不能为空", trigger: "blur" }
        ],
        photoPath: [
          { required: true, message: "照片不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    // 从路由参数获取物种名称
    const speciesName = this.$route.query.speciesName;
    if (speciesName) {
      // 设置物种名称字段值
      this.form.speciesName = speciesName;

      // 可选：显示提示信息
      this.$modal.msgSuccess(`已自动填充物种名称: ${speciesName}`);
    }
    this.getList()
  },
  methods: {
    /** 查询野生动物图鉴列表 */
    getList() {
      this.loading = true
      listSpecies(this.queryParams).then(response => {
        this.speciesList = response.rows
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
        speciesName: null,
        objectCategory: null,
        location: null,
        photoTime: null,
        photoPath: null,
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
      this.title = "添加野生动物图鉴"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getSpecies(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改野生动物图鉴"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSpecies(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSpecies(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除野生动物图鉴编号为"' + ids + '"的数据项？').then(function() {
        return delSpecies(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/species/export', {
        ...this.queryParams
      }, `species_${new Date().getTime()}.xlsx`)
    }
  }
}

</script>
<style scoped>
/* 照片墙容器 */
.photo-wall-container {
  margin: 0 -10px;
}

.photo-wall {
  display: flex;
  flex-wrap: wrap;
}

.photo-item {
  width: 20%;
  padding: 10px;
  box-sizing: border-box;
}

.photo-wrapper {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
  height: 250px;
}

.photo-wrapper:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.species-photo {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.photo-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  padding: 15px;
  color: white;
  transition: all 0.3s ease;
}

.photo-info {
  margin-bottom: 10px;
}

.species-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}

.species-category {
  font-size: 12px;
}

.photo-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  opacity: 0;
  transform: translateY(10px);
  transition: all 0.3s ease;
}

.photo-wrapper:hover .photo-actions {
  opacity: 1;
  transform: translateY(0);
}

/* 照片预览样式 */
.photo-preview-container {
  display: flex;
  gap: 20px;
}

.preview-image {
  flex: 1;
  height: 60vh;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f5f7fa;
}

.preview-info {
  flex: 1;
  padding: 15px;
}

.preview-info h3 {
  margin-top: 0;
  color: #303133;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 10px;
}

.info-row {
  margin-bottom: 15px;
  display: flex;
}

.label {
  font-weight: bold;
  min-width: 80px;
  color: #606266;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .photo-item {
    width: 25%;
  }
}

@media (max-width: 992px) {
  .photo-item {
    width: 33.33%;
  }
}

@media (max-width: 768px) {
  .photo-item {
    width: 50%;
  }

  .photo-preview-container {
    flex-direction: column;
  }
}

@media (max-width: 480px) {
  .photo-item {
    width: 100%;
  }
}
</style>
