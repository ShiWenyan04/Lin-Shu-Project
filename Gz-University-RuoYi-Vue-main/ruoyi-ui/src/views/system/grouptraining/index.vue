<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="类别" prop="activityType">
        <el-select v-model="queryParams.activityType" placeholder="请选择类别" clearable>
          <el-option
            v-for="dict in dict.type.activity_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="内容" prop="activityContent">
        <el-select v-model="queryParams.activityContent" placeholder="请选择内容" clearable>
          <el-option
            v-for="dict in dict.type.activity_content"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="负责人" prop="leaderName">
        <el-input
          v-model="queryParams.leaderName"
          placeholder="请输入负责人"
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
          v-hasPermi="['system:grouptraining:add']"
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
          v-hasPermi="['system:grouptraining:edit']"
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
          v-hasPermi="['system:grouptraining:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:grouptraining:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="grouptrainingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="活动时间" align="center" prop="activityTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.activityTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类别" align="center" prop="activityType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.activity_type" :value="scope.row.activityType"/>
        </template>
      </el-table-column>
      <el-table-column label="内容" align="center" prop="activityContent">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.activity_content" :value="scope.row.activityContent"/>
        </template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="leaderName" />
      <el-table-column label="参与人" align="center" prop="participants" />
      <el-table-column label="资料存储" align="center" prop="materials" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:grouptraining:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:grouptraining:remove']"
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

    <!-- 添加或修改实训与竞赛活动对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="活动时间" prop="activityTime">
          <el-date-picker clearable
            v-model="form.activityTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择活动时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="类别" prop="activityType">
          <el-select v-model="form.activityType" placeholder="请选择类别">
            <el-option
              v-for="dict in dict.type.activity_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="activityContent">
          <el-select v-model="form.activityContent" placeholder="请选择内容">
            <el-option
              v-for="dict in dict.type.activity_content"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="leaderName">
          <el-input v-model="form.leaderName" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="参与人" prop="participants">
          <el-input v-model="form.participants" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="资料存储" prop="materials">
          <file-upload v-model="form.materials"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listGrouptraining, getGrouptraining, delGrouptraining, addGrouptraining, updateGrouptraining } from "@/api/system/grouptraining"

export default {
  name: "Grouptraining",
  dicts: ['activity_content', 'activity_type'],
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
      // 实训与竞赛活动表格数据
      grouptrainingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        activityType: null,
        activityContent: null,
        leaderName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        activityTime: [
          { required: true, message: "活动时间不能为空", trigger: "blur" }
        ],
        activityType: [
          { required: true, message: "类别不能为空", trigger: "change" }
        ],
        activityContent: [
          { required: true, message: "内容不能为空", trigger: "change" }
        ],
        leaderName: [
          { required: true, message: "负责人不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询实训与竞赛活动列表 */
    getList() {
      this.loading = true
      listGrouptraining(this.queryParams).then(response => {
        this.grouptrainingList = response.rows
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
        activityTime: null,
        activityType: null,
        activityContent: null,
        leaderName: null,
        participants: null,
        materials: null,
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
      this.title = "添加实训与竞赛活动"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getGrouptraining(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改实训与竞赛活动"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateGrouptraining(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addGrouptraining(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除实训与竞赛活动编号为"' + ids + '"的数据项？').then(function() {
        return delGrouptraining(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/grouptraining/export', {
        ...this.queryParams
      }, `grouptraining_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
