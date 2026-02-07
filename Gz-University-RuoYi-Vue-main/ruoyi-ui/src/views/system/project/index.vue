<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目编号" prop="projectCode">
        <el-input
          v-model="queryParams.projectCode"
          placeholder="请输入项目编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="queryParams.projectName"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目负责人" prop="leaderName">
        <el-input
          v-model="queryParams.leaderName"
          placeholder="请输入项目负责人"
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
          v-hasPermi="['system:project:add']"
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
          v-hasPermi="['system:project:edit']"
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
          v-hasPermi="['system:project:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:project:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="项目编号" align="center" prop="projectCode" />
      <el-table-column label="项目名称" align="center" prop="projectName" />
      <el-table-column label="项目地点" align="center" prop="location" />
      <el-table-column label="项目负责人" align="center" prop="leaderName" />
      <el-table-column label="项目来源" align="center" prop="source" />

      <el-table-column label="执行周期" align="center" width="180">
        <template slot-scope="scope">
          <span v-if="scope.row.startDate">
            {{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }} 至 {{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}
          </span>
        </template>
      </el-table-column>

      <el-table-column label="合同信息" align="center" prop="contractInfo">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.contractInfo"
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownloadFile(scope.row.contractInfo)"
          >下载附件</el-button>
          <span v-else>-</span>
        </template>
      </el-table-column>

      <el-table-column label="备注" align="center" prop="remark" />
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
            v-hasPermi="['system:project:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:project:remove']"
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="项目编号" prop="projectCode">
          <el-input v-model="form.projectCode" placeholder="请输入项目编号" :disabled="isView" />
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" :disabled="isView" />
        </el-form-item>
        <el-form-item label="项目地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入项目地点" :disabled="isView" />
        </el-form-item>
        <el-form-item label="项目负责人" prop="leaderName">
          <el-input v-model="form.leaderName" placeholder="请输入项目负责人" :disabled="isView" />
        </el-form-item>
        <el-form-item label="项目来源" prop="source">
          <el-input v-model="form.source" placeholder="请输入项目来源" :disabled="isView" />
        </el-form-item>

        <el-form-item label="执行期限" prop="execPeriodRange">
          <el-date-picker
            v-model="daterangeExecPeriod"
            style="width: 100%"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :disabled="isView"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="合同信息" prop="contractInfo">
          <file-upload v-model="form.contractInfo" :disabled="isView"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" :disabled="isView" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" v-if="!isView">确 定</el-button>
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProject, getProject, delProject, addProject, updateProject } from "@/api/system/project"

export default {
  name: "Project",
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
      // 项目信息表格数据
      projectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 控制查看模式
      isView: false,
      // 【修改点4】日期范围数组，用于绑定 el-date-picker
      daterangeExecPeriod: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectCode: null,
        projectName: null,
        leaderName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        projectCode: [
          { required: true, message: "项目编号不能为空", trigger: "blur" }
        ],
        projectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询项目信息列表 */
    getList() {
      this.loading = true
      listProject(this.queryParams).then(response => {
        this.projectList = response.rows
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
        projectId: null,
        projectCode: null,
        projectName: null,
        location: null,
        leaderName: null,
        source: null,
        // 【修改点5】增加新字段的重置，删除旧字段
        startDate: null,
        endDate: null,
        contractInfo: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.daterangeExecPeriod = []; // 清空时间组件
      this.resetForm("form")
      this.isView = false;
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
      this.ids = selection.map(item => item.projectId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加项目信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const projectId = row.projectId || this.ids
      getProject(projectId).then(response => {
        this.form = response.data

        // 【修改点6】后端返回的 startDate 和 endDate，需要拼成数组给前端组件回显
        if (this.form.startDate && this.form.endDate) {
          this.daterangeExecPeriod = [this.form.startDate, this.form.endDate];
        } else {
          this.daterangeExecPeriod = [];
        }

        this.open = true
        this.title = "修改项目信息"
      })
    },
    /** 查看详情按钮操作 */
    handleView(row) {
      this.reset()
      const projectId = row.projectId || this.ids
      getProject(projectId).then(response => {
        this.form = response.data

        // 【修改点7】详情时也需要回显时间
        if (this.form.startDate && this.form.endDate) {
          this.daterangeExecPeriod = [this.form.startDate, this.form.endDate];
        } else {
          this.daterangeExecPeriod = [];
        }

        this.open = true
        this.title = "查看项目详情"
        this.isView = true;
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 【修改点8】在提交前，把数组拆分为两个字段
          if (this.daterangeExecPeriod != null && this.daterangeExecPeriod.length === 2) {
            this.form.startDate = this.daterangeExecPeriod[0];
            this.form.endDate = this.daterangeExecPeriod[1];
          } else {
            this.form.startDate = null;
            this.form.endDate = null;
          }

          if (this.form.projectId != null) {
            updateProject(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addProject(this.form).then(response => {
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
      const projectIds = row.projectId || this.ids
      this.$modal.confirm('是否确认删除项目信息编号为"' + projectIds + '"的数据项？').then(function() {
        return delProject(projectIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/project/export', {
        ...this.queryParams
      }, `project_${new Date().getTime()}.xlsx`)
    },
    /** 【修改点9】文件下载通用方法 */
    handleDownloadFile(url) {
      // 若依的标准文件服务地址前缀，如果你的文件存的是相对路径，需要拼接
      // 通常 file-upload 组件存的是 "/profile/upload/..."
      // 此处使用若依自带的通用下载方法
      // 或者直接打开新窗口
      const baseUrl = process.env.VUE_APP_BASE_API;
      window.open(baseUrl + url);
    }
  }
}
</script>
