<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:submission:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:submission:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:submission:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:submission:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="submissionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="关联写作表ID" align="center" prop="paperId" />
      <el-table-column label="姓名" align="center" prop="studentName" />
      <el-table-column label="学位类别" align="center" prop="degreeType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.xuewei" :value="scope.row.degreeType"/>
        </template>
      </el-table-column>
      <el-table-column label="指导人" align="center" prop="supervisorName" />
      <el-table-column label="定题名称" align="center" prop="paperTitle" show-overflow-tooltip/>
      <el-table-column label="投稿时间" align="center" prop="submissionDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.submissionDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="投稿期刊" align="center" prop="journalName" show-overflow-tooltip/>
      <el-table-column label="投稿状态" align="center" prop="submissionStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sub_status" :value="scope.row.submissionStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:submission:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:submission:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">

        <el-divider content-position="left"><i class="el-icon-link"></i> 关联论文信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="选择论文" prop="paperId">
              <el-select v-model="form.paperId" placeholder="请选择您的论文" style="width: 100%" filterable @change="handlePaperChange">
                <el-option v-for="item in paperList" :key="item.id" :label="item.paperTitle" :value="item.id">
                  <span style="float: left">{{ item.paperTitle }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.studentName }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="定题名称" prop="paperTitle">
              <el-input v-model="form.paperTitle" type="textarea" :rows="2" placeholder="选择论文后自动填充" disabled />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="姓名" prop="studentName">
              <el-input v-model="form.studentName" placeholder="自动填充" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="指导人" prop="supervisorName">
              <el-input v-model="form.supervisorName" placeholder="自动填充" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学位类别" prop="degreeType">
              <el-select v-model="form.degreeType" placeholder="自动填充" disabled style="width: 100%">
                <el-option v-for="dict in dict.type.xuewei" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left"><i class="el-icon-s-promotion"></i> 投稿详细信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="投稿期刊" prop="journalName">
              <el-input v-model="form.journalName" placeholder="请输入投稿期刊" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投稿时间" prop="submissionDate">
              <el-date-picker clearable v-model="form.submissionDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投稿状态" prop="submissionStatus">
              <el-select v-model="form.submissionStatus" placeholder="请选择状态" style="width: 100%">
                <el-option v-for="dict in dict.type.sub_status" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="接收时间" prop="receiveDate">
              <el-date-picker clearable v-model="form.receiveDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发表时间" prop="publicationDate">
              <el-date-picker clearable v-model="form.publicationDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left"><i class="el-icon-warning-outline"></i> 拒稿记录 (选填)</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="拒稿时间" prop="rejectionDate">
              <el-date-picker clearable v-model="form.rejectionDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="拒稿再投时间" prop="resubmissionDate">
              <el-date-picker clearable v-model="form.resubmissionDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"></el-date-picker>
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
import { listSubmission, getSubmission, delSubmission, addSubmission, updateSubmission } from "@/api/system/submission"
import { listPaper } from "@/api/system/paper"

export default {
  name: "Submission",
  dicts: ['sub_status', 'xuewei'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      submissionList: [],
      paperList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentName: null,
      },
      form: {},
      rules: {
        paperId: [{ required: true, message: "必须选择关联的论文", trigger: "change" }],
        studentName: [{ required: true, message: "不能为空", trigger: "blur" }],
        degreeType: [{ required: true, message: "不能为空", trigger: "change" }],
        supervisorName: [{ required: true, message: "不能为空", trigger: "blur" }],
        paperTitle: [{ required: true, message: "不能为空", trigger: "blur" }],
        submissionDate: [{ required: true, message: "不能为空", trigger: "blur" }],
        journalName: [{ required: true, message: "不能为空", trigger: "blur" }],
      }
    }
  },
  created() {
    this.getList();
    this.getMyPaperList();
  },
  methods: {
    getMyPaperList() {
      listPaper().then(response => {
        this.paperList = response.rows;
      });
    },
    handlePaperChange(paperId) {
      const selectedPaper = this.paperList.find(item => item.id === paperId);
      if (selectedPaper) {
        this.form.studentName = selectedPaper.studentName;
        this.form.degreeType = selectedPaper.degreeType;
        this.form.supervisorName = selectedPaper.supervisorName;
        this.form.paperTitle = selectedPaper.paperTitle;
      }
    },
    getList() {
      this.loading = true
      listSubmission(this.queryParams).then(response => {
        this.submissionList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        paperId: null,
        studentName: null,
        degreeType: null,
        supervisorName: null,
        paperTitle: null,
        submissionDate: null,
        journalName: null,
        receiveDate: null,
        rejectionDate: null,
        resubmissionDate: null,
        publicationDate: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        submissionStatus: null
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加学术论文投稿"
      this.getMyPaperList();
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      this.getMyPaperList();
      getSubmission(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改学术论文投稿"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSubmission(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSubmission(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('确认删除？').then(function() {
        return delSubmission(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('system/submission/export', {
        ...this.queryParams
      }, `submission_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
