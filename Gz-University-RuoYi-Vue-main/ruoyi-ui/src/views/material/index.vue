<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-input
          v-model="queryParams.type"
          placeholder="请输入类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="录入人员" prop="inputPerson">
        <el-input
          v-model="queryParams.inputPerson"
          placeholder="请输入录入人员"
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
          v-hasPermi="['research:material:add']"
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
          v-hasPermi="['research:material:edit']"
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
          v-hasPermi="['research:material:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['research:material:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="名称" align="center" prop="name" show-overflow-tooltip min-width="150"/>
      <el-table-column label="类型" align="center" prop="type" width="100"/>
      <el-table-column label="方向类别" align="center" prop="directionCategory" width="120"/>
      <el-table-column label="录入人员" align="center" prop="inputPerson" width="100"/>

      <el-table-column label="更新时间" align="center" prop="updateTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >详情</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['research:material:edit']"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['research:material:remove']"
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

    <el-dialog title="资料详情" :visible.sync="detailOpen" width="700px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="名称">{{ form.name }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ form.type }}</el-descriptions-item>
        <el-descriptions-item label="方向类别">{{ form.directionCategory }}</el-descriptions-item>
        <el-descriptions-item label="录入人员">{{ form.inputPerson }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(form.updateTime) }}</el-descriptions-item>

        <el-descriptions-item label="内容概述">
          <div style="white-space: pre-wrap;">{{ form.contentSummary }}</div>
        </el-descriptions-item>

        <el-descriptions-item label="储存路径">
          {{ form.storagePath }}
        </el-descriptions-item>

        <el-descriptions-item label="备注">{{ form.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入资料名称" />
        </el-form-item>

        <el-form-item label="类型" prop="type">
          <el-input v-model="form.type" placeholder="请输入类型（如：专著、文献）" />
        </el-form-item>

        <el-form-item label="方向类别" prop="directionCategory">
          <el-input v-model="form.directionCategory" placeholder="请输入方向类别" />
        </el-form-item>

        <el-form-item label="内容概述" prop="contentSummary">
          <el-input v-model="form.contentSummary" type="textarea" :rows="3" placeholder="请输入内容概述" />
        </el-form-item>

        <el-form-item label="储存路径" prop="storagePath">
          <el-input v-model="form.storagePath" placeholder="请输入储存路径" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
import { listMaterial, getMaterial, delMaterial, addMaterial, updateMaterial } from "@/api/research/material"
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Material",
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
      // 列表数据
      materialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      detailOpen: false, // 详情弹窗

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null,
        inputPerson: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "类型不能为空", trigger: "blur" }
        ],
        // 去掉 storagePath 的必填校验，或者根据需要保留
        // storagePath: [
        //   { required: true, message: "路径不能为空", trigger: "blur" }
        // ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      listMaterial(this.queryParams).then(response => {
        this.materialList = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    /** 打开详情 */
    handleView(row) {
      this.reset();
      const id = row.id;
      getMaterial(id).then(response => {
        this.form = response.data;
        this.detailOpen = true;
      });
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
        name: null,
        type: null,
        contentSummary: null,
        directionCategory: null,
        storagePath: null,
        inputPerson: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加资料清单"

      // 自动填充当前登录用户为录入人员
      getUserProfile().then(response => {
        const user = response.data.user || response.data;
        this.form.inputPerson = user.nickName;
      });
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMaterial(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改资料清单"
      })
    },
    /** 提交 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMaterial(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMaterial(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(function() {
        return delMaterial(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出 */
    handleExport() {
      this.download('research/material/export', {
        ...this.queryParams
      }, `material_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
