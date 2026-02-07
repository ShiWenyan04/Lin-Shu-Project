<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部论文 (已通过)" name="public"></el-tab-pane>

      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="audit"
        v-if="checkRole(['teacher', 'thesis_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="studentName">
        <el-input
          v-model="queryParams.studentName"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学位类别" prop="degreeType">
        <el-select v-model="queryParams.degreeType" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.current_education"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="毕业年份" prop="graduationYear">
        <el-input
          v-model="queryParams.graduationYear"
          placeholder="请输入年份"
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
          v-hasPermi="['system:thesis:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:thesis:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="thesisList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="studentName" width="100"/>
      <el-table-column label="学位" align="center" prop="degreeType" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.current_education" :value="scope.row.degreeType"/>
        </template>
      </el-table-column>
      <el-table-column label="毕业年份" align="center" prop="graduationYear" width="80"/>
      <el-table-column label="论文题目" align="center" prop="thesisTitle" show-overflow-tooltip min-width="150"/>
      <el-table-column label="研究方向" align="center" prop="researchField" show-overflow-tooltip/>

      <el-table-column label="审核状态" align="center" prop="auditStatus" width="120">
        <template slot-scope="scope">
          <div style="display: flex; align-items: center; justify-content: center;">
            <dict-tag :options="dict.type.sys_audit_status" :value="scope.row.auditStatus"/>
            <span v-if="scope.row.auditStatus == '0'" style="display:inline-block;width:8px;height:8px;border-radius:50%;background-color:#e6a23c;margin-left:6px;" title="待审核"></span>
            <span v-if="scope.row.auditStatus == '1'" style="display:inline-block;width:8px;height:8px;border-radius:50%;background-color:#67c23a;margin-left:6px;" title="已通过"></span>
            <el-tooltip v-if="scope.row.auditStatus == '2'" :content="scope.row.auditOpinion" placement="top">
              <i class="el-icon-warning" style="color: #f56c6c; cursor: pointer; margin-left: 6px; font-size: 14px;"></i>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
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
            v-hasPermi="['system:thesis:edit']"
            v-if="checkRole(['thesis_manager', 'admin']) || (checkRole(['student']) && activeTab === 'private' && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:thesis:remove']"
            v-if="checkRole(['thesis_manager', 'admin']) || (checkRole(['student']) && activeTab === 'private' && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:thesis:audit']"
            v-if="checkRole(['teacher', 'thesis_manager', 'admin']) && scope.row.auditStatus == '0'"
          >审核</el-button>
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

    <el-dialog title="学位论文详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ form.studentName }}</el-descriptions-item>
        <el-descriptions-item label="毕业年份">{{ form.graduationYear }}</el-descriptions-item>

        <el-descriptions-item label="学位类别">
          <dict-tag :options="dict.type.current_education" :value="form.degreeType"/>
        </el-descriptions-item>
        <el-descriptions-item label="校外导师">{{ form.externalSupervisor }}</el-descriptions-item>

        <el-descriptions-item label="论文题目" :span="2">{{ form.thesisTitle }}</el-descriptions-item>

        <el-descriptions-item label="研究方向">{{ form.researchField }}</el-descriptions-item>
        <el-descriptions-item label="研究地点">{{ form.researchLocation }}</el-descriptions-item>

        <el-descriptions-item label="研究对象" :span="2">{{ form.researchSubject }}</el-descriptions-item>
        <el-descriptions-item label="论文发表情况" :span="2">{{ form.publicationInfo }}</el-descriptions-item>

        <el-descriptions-item label="审核状态">
          <dict-tag :options="dict.type.sys_audit_status" :value="form.auditStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="审核意见" v-if="form.auditStatus == '2'">
          <span style="color: red">{{ form.auditOpinion }}</span>
        </el-descriptions-item>

        <el-descriptions-item label="备注" :span="2">{{ form.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名" prop="studentName">
              <el-input v-model="form.studentName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学位类别" prop="degreeType">
              <el-select v-model="form.degreeType" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.current_education"
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
            <el-form-item label="毕业年份" prop="graduationYear">
              <el-input v-model="form.graduationYear" placeholder="如：2023" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="校外导师" prop="externalSupervisor">
              <el-input v-model="form.externalSupervisor" placeholder="请输入校外导师,无则填无" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="论文题目" prop="thesisTitle">
          <el-input v-model="form.thesisTitle" type="textarea" :rows="2" placeholder="请输入论文题目" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="研究对象" prop="researchSubject">
              <el-input v-model="form.researchSubject" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="研究地点" prop="researchLocation">
              <el-input v-model="form.researchLocation" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="研究方向" prop="researchField">
              <el-input v-model="form.researchField" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="论文发表情况" prop="publicationInfo">
          <el-input v-model="form.publicationInfo" type="textarea" :rows="3" placeholder="请输入发表论文大致情况" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>

        <el-form-item label="驳回原因" v-if="form.auditStatus == '2'" style="color: #f56c6c;">
          <span>{{ form.auditOpinion }}</span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核学位论文" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="论文题目">
          <el-input v-model="auditForm.thesisTitle" disabled />
        </el-form-item>
        <el-form-item label="审核结果" prop="auditStatus">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditOpinion" v-if="auditForm.auditStatus == '2'">
          <el-input type="textarea" v-model="auditForm.auditOpinion" placeholder="请输入驳回原因（必填）" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">提交审核</el-button>
        <el-button @click="auditOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 注意：请确保 api 文件中已添加 auditThesis 方法
import { listThesis, getThesis, delThesis, addThesis, updateThesis, auditThesis } from "@/api/system/thesis"
import { checkRole } from "@/utils/permission";

export default {
  name: "Thesis",
  dicts: ['sys_audit_status', 'current_education'], // 确保 current_education 字典存在
  data() {
    return {
      // Tab
      activeTab: 'public',

      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      thesisList: [],
      title: "",
      open: false,
      detailOpen: false,

      // Audit
      auditOpen: false,
      auditForm: {},

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentName: null,
        degreeType: null,
        graduationYear: null,
        // 传递查询模式
        params: {
          queryMode: 'public'
        }
      },
      form: {},
      rules: {
        studentName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        degreeType: [
          { required: true, message: "学位类别不能为空", trigger: "change" }
        ],
        graduationYear: [
          { required: true, message: "毕业年份不能为空", trigger: "blur" }
        ],
        thesisTitle: [
          { required: true, message: "论文题目不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    checkRole,

    /** 查询列表 */
    getList() {
      this.loading = true
      if (!this.queryParams.params) this.queryParams.params = {};

      // 传递当前 Tab 模式给后端
      this.queryParams.params['queryMode'] = this.activeTab;

      listThesis(this.queryParams).then(response => {
        this.thesisList = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    /** Tab 切换 */
    handleTabClick(tab, event) {
      this.queryParams.pageNum = 1;
      this.resetForm("queryForm");
      this.getList();
    },

    /** 打开详情 */
    handleView(row) {
      this.reset();
      getThesis(row.id).then(response => {
        this.form = response.data;
        this.detailOpen = true;
      });
    },

    /** 打开审核 */
    handleAuditOpen(row) {
      this.auditForm = {
        id: row.id,
        thesisTitle: row.thesisTitle,
        auditStatus: '1', // 默认通过
        auditOpinion: ''
      };
      this.auditOpen = true;
    },

    /** 提交审核 */
    submitAudit() {
      if (this.auditForm.auditStatus === '2' && !this.auditForm.auditOpinion) {
        this.$modal.msgError("驳回时必须填写审核意见");
        return;
      }
      auditThesis(this.auditForm).then(response => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },

    cancel() { this.open = false; this.reset(); },
    reset() {
      this.form = {
        id: null,
        studentName: null,
        externalSupervisor: null,
        degreeType: null,
        graduationYear: null,
        thesisTitle: null,
        researchSubject: null,
        researchLocation: null,
        publicationInfo: null,
        researchField: null,
        auditStatus: '0',
        auditOpinion: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList(); },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery(); },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() { this.reset(); this.open = true; this.title = "添加学位论文"; },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getThesis(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改学位论文";
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateThesis(this.form).then(response => {
              this.$modal.msgSuccess("修改成功，请等待重新审核")
              this.open = false
              this.getList()
            })
          } else {
            addThesis(this.form).then(response => {
              this.$modal.msgSuccess("新增成功，请等待审核")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(function() { return delThesis(ids); }).then(() => { this.getList(); this.$modal.msgSuccess("删除成功"); }).catch(() => {})
    },
    handleExport() { this.download('system/thesis/export', { ...this.queryParams }, `thesis_${new Date().getTime()}.xlsx`) }
  }
}
</script>
