<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane
        v-if="checkRole(['student'])"
        label="我的开题"
        name="my"
      >
      </el-tab-pane>
      <el-tab-pane
        v-if="checkRole(['teacher', 'manager'])"
        label="全部开题"
        name="all"
      >
      </el-tab-pane>
    </el-tabs>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5" v-if="checkRole(['student'])">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:proposal:add']"
        >新增</el-button>
      </el-col>

      <el-col :span="1.5" v-if="checkRole(['student'])">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:proposal:edit']"
        >修改</el-button>
      </el-col>

      <el-col :span="1.5" v-if="checkRole(['student'])">
        <el-button
          type="warning"
          plain
          icon="el-icon-upload"
          size="mini"
          :disabled="single"
          @click="handleUpload"
          v-hasPermi="['system:proposal:upload']"
        >上传报告</el-button>
      </el-col>

      <el-col :span="1.5" v-if="checkRole(['teacher', 'manager'])">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:proposal:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="proposalList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="学生姓名" align="center" prop="studentName" />
      <el-table-column label="类别" align="center" prop="degreeType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.xuewei" :value="scope.row.degreeType"/>
        </template>
      </el-table-column>
      <el-table-column label="专业" align="center" prop="major">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_major" :value="scope.row.major"/>
        </template>
      </el-table-column>
      <el-table-column label="入学年级" align="center" prop="enrollmentYear" />
      <el-table-column label="开题题目" align="center" prop="thesisTitle" show-overflow-tooltip/>
      <el-table-column label="方向类别" align="center" prop="researchDirection" />
      <el-table-column label="计划开题日期" align="center" prop="plannedProposalDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.plannedProposalDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开题答辩日期" align="center" prop="actualProposalDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actualProposalDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开题报告" align="center" prop="proposalReportUrl">
        <template slot-scope="scope">
          <span v-if="scope.row.proposalReportUrl">
            <a :href="scope.row.proposalReportUrl" target="_blank" style="color: #409EFF">查看报告</a>
          </span>
          <span v-else>未上传</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="checkRole(['student'])"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:proposal:edit']"
          >修改</el-button>

          <el-button
            v-if="checkRole(['student'])"
            size="mini"
            type="text"
            icon="el-icon-upload"
            @click="handleUpload(scope.row)"
            v-hasPermi="['system:proposal:upload']"
          >上传</el-button>

          <el-button
            v-if="checkRole(['teacher', 'manager'])"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >查看</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="学生姓名" prop="studentName">
              <el-input
                v-model="form.studentName"
                placeholder="请输入学生姓名"
                :disabled="checkRole(['student'])"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类别" prop="degreeType">
              <el-select v-model="form.degreeType" placeholder="请选择类别" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.xuewei"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-select v-model="form.major" placeholder="请选择专业" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.sys_major"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入学年级" prop="enrollmentYear">
              <el-input v-model="form.enrollmentYear" placeholder="请输入入学年级" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="方向类别" prop="researchDirection">
              <el-input v-model="form.researchDirection" placeholder="请输入方向类别" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划开题" prop="plannedProposalDate">
              <el-date-picker clearable
                              style="width: 100%"
                              v-model="form.plannedProposalDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="开题答辩" prop="actualProposalDate">
              <el-date-picker clearable
                              style="width: 100%"
                              v-model="form.actualProposalDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="中期检查" prop="midtermCheckDate">
              <el-date-picker clearable
                              style="width: 100%"
                              v-model="form.midtermCheckDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="中期答辩" prop="midtermDefenseDate">
              <el-date-picker clearable
                              style="width: 100%"
                              v-model="form.midtermDefenseDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="开题题目" prop="thesisTitle">
              <el-input v-model="form.thesisTitle" type="textarea" :rows="3" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="开题报告" prop="proposalReportUrl" v-if="!checkRole(['teacher', 'manager'])">
              <file-upload v-model="form.proposalReportUrl"/>
            </el-form-item>
            <el-form-item label="开题报告" prop="proposalReportUrl" v-else>
              <span v-if="form.proposalReportUrl">
                <a :href="form.proposalReportUrl" target="_blank">查看报告</a>
              </span>
              <span v-else>未上传</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="上传开题报告" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :action="upload.url"
        :auto-upload="false"
        :data="upload.data"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        :show-file-list="true"
        :headers="upload.headers"
      >
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <div slot="tip" class="el-upload__tip">
          只能上传doc/docx/pdf文件，且不超过10MB
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="upload.open = false">取消</el-button>
        <el-button type="primary" @click="submitUpload">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProposal, getProposal, delProposal, addProposal, updateProposal, uploadProposalReport } from "@/api/system/proposal"
import { checkRole } from "@/utils/permission"
import { getToken } from "@/utils/auth";

export default {
  name: "Proposal",
  dicts: ['sys_major', 'xuewei'],
  data() {
    return {
      // 选项卡
      activeTab: 'my',
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
      // 开题与学位论文表格数据
      proposalList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        queryMode: 'my'
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        studentName: [
          { required: true, message: "学生姓名不能为空", trigger: "blur" }
        ],
        degreeType: [
          { required: true, message: "类别不能为空", trigger: "change" }
        ],
        major: [
          { required: true, message: "专业不能为空", trigger: "change" }
        ],
        enrollmentYear: [
          { required: true, message: "入学年级不能为空", trigger: "blur" }
        ]
        // 修改点5：移除了 thesisTitle 的必填校验，其他日期本身就没有配置校验
      },
      // 上传相关
      upload: {
        open: false,
        url: process.env.VUE_APP_BASE_API + "/common/upload",
        headers: { Authorization: "Bearer " + getToken() },
        data: {}
      }
    }
  },
  created() {
    if (this.checkRole(['student'])) {
      this.queryParams.queryMode = 'my'
      this.activeTab = 'my'
    } else {
      this.queryParams.queryMode = 'all'
      this.activeTab = 'all'
    }
    this.getList()
  },
  methods: {
    checkRole,

    /** 查询列表 */
    getList() {
      this.loading = true
      // 根据选项卡设置查询模式
      if (this.checkRole(['student'])) {
        this.queryParams.queryMode = this.activeTab
      } else {
        this.queryParams.queryMode = 'all'
      }

      // 注意：虽然移除了搜索栏，但查询接口可能还需要空参数，这里直接传 current queryParams 即可
      listProposal(this.queryParams).then(response => {
        this.proposalList = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    /** 选项卡切换 */
    handleTabClick(tab) {
      this.activeTab = tab.name
      this.getList()
    },

    /** 查看详情 */
    handleView(row) {
      this.reset()
      const id = row.id
      getProposal(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "查看开题与学位论文"
      })
    },

    cancel() {
      this.open = false
      this.reset()
    },

    reset() {
      this.form = {
        id: null,
        studentName: null,
        degreeType: null,
        major: null,
        enrollmentYear: null,
        thesisTitle: null,
        researchDirection: null,
        plannedProposalDate: null,
        actualProposalDate: null,
        midtermCheckDate: null,
        midtermDefenseDate: null,
        proposalReportUrl: null,
        submitStatus: '0'
      }
      this.resetForm("form")
    },

    // 删除了 handleQuery 和 resetQuery，因为搜索栏移除了

    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    handleAdd() {
      this.reset()
      if (this.checkRole(['student'])) {
        this.form.studentName = this.$store.getters.name
      }
      this.open = true
      this.title = "添加开题与学位论文"
    },

    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getProposal(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改开题与学位论文"
      })
    },

    handleUpload(row) {
      this.upload.data.thesisId = row.id;
      this.upload.open = true;
      this.$nextTick(() => {
        this.$refs.uploadRef.clearFiles();
      });
    },

    submitUpload() {
      this.$refs.uploadRef.submit()
    },

    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        const fileUrl = response.url;
        const updateData = {
          id: this.upload.data.thesisId,
          proposalReportUrl: fileUrl
        };
        updateProposal(updateData).then(res => {
          this.$modal.msgSuccess("上传成功");
          this.upload.open = false;
          this.getList();
        });
      } else {
        this.$modal.msgError(response.msg);
      }
    },

    handleUploadError(err, file, fileList) {
      this.$modal.msgError("上传失败：" + err.message)
    },

    beforeUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      const fileExtension = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase()
      const allowedExtensions = ['doc', 'docx', 'pdf']
      if (!allowedExtensions.includes(fileExtension)) {
        this.$message.error('只允许上传doc, docx, pdf格式的文件!')
        return false
      }
      return true
    },

    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProposal(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addProposal(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },

    handleExport() {
      this.download('system/proposal/export', {
        ...this.queryParams
      }, `proposal_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
