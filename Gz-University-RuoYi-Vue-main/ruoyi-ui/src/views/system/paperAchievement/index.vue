<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部成果 (已通过)" name="public"></el-tab-pane>

      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="audit"
        v-if="checkRole(['teacher', 'papeAachievement_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item label="中科院" prop="casPartition">
        <el-input
          v-model="queryParams.casPartition"
          placeholder="分区"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="题目" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入题目"
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
          v-hasPermi="['system:paperAchievement:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:paperAchievement:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paperAchievementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序" align="center" width="50">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="题目" align="center" prop="title" show-overflow-tooltip min-width="150"/>
      <el-table-column label="第一作者" align="center" prop="firstAuthor" width="100"/>
      <el-table-column label="期刊" align="center" prop="journal" show-overflow-tooltip/>

      <el-table-column label="学位" align="center" prop="degreeType" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.current_education" :value="scope.row.degreeType"/>
        </template>
      </el-table-column>

      <el-table-column label="发表时间" align="center" prop="publicationDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.publicationDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="审核状态" align="center" prop="auditStatus" width="100">
        <template slot-scope="scope">
          <div style="display: flex; align-items: center; justify-content: center;">
            <dict-tag :options="dict.type.sys_audit_status" :value="scope.row.auditStatus"/>
            <span v-if="scope.row.auditStatus == '0'" style="display:inline-block;width:8px;height:8px;border-radius:50%;background-color:#e6a23c;margin-left:6px;" title="待审核"></span>
            <span v-if="scope.row.auditStatus == '1'" style="display:inline-block;width:8px;height:8px;border-radius:50%;background-color:#67c23a;margin-left:6px;" title="已通过"></span>
            <el-tooltip v-if="scope.row.auditStatus == '2'" :content="scope.row.remark" placement="top">
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
            v-hasPermi="['system:paperAchievement:edit']"
            v-if="checkRole(['papeAachievement_manager', 'admin']) || (checkRole(['student']) && activeTab === 'private' && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:paperAchievement:remove']"
            v-if="checkRole(['papeAachievement_manager', 'admin']) || (checkRole(['student']) && activeTab === 'private' && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:paperAchievement:audit']"
            v-if="checkRole(['teacher', 'papeAachievement_manager', 'admin']) && scope.row.auditStatus == '0'"
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

    <el-dialog title="论文成果详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="题目" :span="2">{{ form.title }}</el-descriptions-item>

        <el-descriptions-item label="第一作者">{{ form.firstAuthor }}</el-descriptions-item>
        <el-descriptions-item label="其他作者">{{ form.otherAuthors }}</el-descriptions-item>

        <el-descriptions-item label="指导人">{{ form.supervisor }}</el-descriptions-item>
        <el-descriptions-item label="方向类别">{{ form.directionCategory }}</el-descriptions-item>

        <el-descriptions-item label="期刊名称">{{ form.journal }}</el-descriptions-item>
        <el-descriptions-item label="DOI">{{ form.doi }}</el-descriptions-item>

        <el-descriptions-item label="中科院分区">{{ form.casPartition }}</el-descriptions-item>
        <el-descriptions-item label="影响因子">{{ form.impactFactor }}</el-descriptions-item>

        <el-descriptions-item label="是否TOP">
          <dict-tag :options="dict.type.sys_isnot" :value="form.isTop"/>
        </el-descriptions-item>
        <el-descriptions-item label="自然指数">
          <dict-tag :options="dict.type.sys_isnot" :value="form.isNaturalIndex"/>
        </el-descriptions-item>

        <el-descriptions-item label="投稿时间">{{ parseTime(form.submissionDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="接收时间">{{ parseTime(form.acceptanceDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="发表时间">{{ parseTime(form.publicationDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="版面费">{{ form.pageFee }} 元</el-descriptions-item>

        <el-descriptions-item label="摘要" :span="2">{{ form.paperAbstract }}</el-descriptions-item>
        <el-descriptions-item label="APA引用" :span="2">{{ form.apaCitation }}</el-descriptions-item>

        <el-descriptions-item label="收录证明" :span="2">
          <div v-if="form.inclusionProof">
            <el-link
              type="primary"
              icon="el-icon-download"
              @click="handleDownloadFile(form.inclusionProof)"
            >
              点击下载 / 预览文件
            </el-link>
          </div>
          <div v-else>暂无文件</div>
        </el-descriptions-item>

        <template v-if="form.rewardStatus == 'Y'">
          <el-descriptions-item label="奖励额度">{{ form.rewardAmount }}</el-descriptions-item>
          <el-descriptions-item label="奖励时间">{{ parseTime(form.rewardDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        </template>

        <el-descriptions-item label="审核状态">
          <dict-tag :options="dict.type.sys_audit_status" :value="form.auditStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="审核意见" v-if="form.auditStatus == '2'" :span="2">
          <span style="color: red">{{ form.remark }}</span>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="8">
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
          <el-col :span="8">
            <el-form-item label="年级" prop="grade">
              <el-input v-model="form.grade" placeholder="2023级" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="第一作者" prop="firstAuthor">
              <el-input v-model="form.firstAuthor" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="指导人" prop="supervisor">
              <el-input v-model="form.supervisor" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向类别" prop="directionCategory">
              <el-input v-model="form.directionCategory" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="其他作者" prop="otherAuthors">
          <el-input v-model="form.otherAuthors" placeholder="请输入其他作者" />
        </el-form-item>

        <el-form-item label="题目" prop="title">
          <el-input v-model="form.title" type="textarea" :rows="2" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="期刊名称" prop="journal">
              <el-input v-model="form.journal" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="DOI" prop="doi">
              <el-input v-model="form.doi" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="中科院分区" prop="casPartition">
              <el-input v-model="form.casPartition" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="影响因子" prop="impactFactor">
              <el-input v-model="form.impactFactor" type="number"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="版面费" prop="pageFee">
              <el-input v-model="form.pageFee" type="number">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="TOP期刊" prop="isTop">
              <el-radio-group v-model="form.isTop">
                <el-radio v-for="dict in dict.type.sys_isnot" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="自然指数" prop="isNaturalIndex">
              <el-radio-group v-model="form.isNaturalIndex">
                <el-radio v-for="dict in dict.type.sys_isnot" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="奖励发放" prop="rewardStatus">
              <el-radio-group v-model="form.rewardStatus">
                <el-radio v-for="dict in dict.type.sys_isnot" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="投稿时间" prop="submissionDate">
              <el-date-picker clearable style="width: 100%" v-model="form.submissionDate" type="date" value-format="yyyy-MM-dd"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="接收时间" prop="acceptanceDate">
              <el-date-picker clearable style="width: 100%" v-model="form.acceptanceDate" type="date" value-format="yyyy-MM-dd"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="发表时间" prop="publicationDate">
              <el-date-picker clearable style="width: 100%" v-model="form.publicationDate" type="date" value-format="yyyy-MM-dd"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="form.rewardStatus == 'Y'">
          <el-col :span="12">
            <el-form-item label="奖励额度" prop="rewardAmount">
              <el-input v-model="form.rewardAmount" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="奖励时间" prop="rewardDate">
              <el-date-picker clearable style="width: 100%" v-model="form.rewardDate" type="date" value-format="yyyy-MM-dd"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="摘要" prop="paperAbstract">
          <el-input v-model="form.paperAbstract" type="textarea" :rows="3" />
        </el-form-item>

        <el-form-item label="APA引用" prop="apaCitation">
          <el-input v-model="form.apaCitation" type="textarea" :rows="3" />
        </el-form-item>

        <el-form-item label="收录证明" prop="inclusionProof">
          <file-upload v-model="form.inclusionProof"/>
        </el-form-item>

        <el-form-item label="驳回原因" v-if="form.auditStatus == '2'" style="color: #f56c6c;">
          <span>{{ form.remark }}</span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核论文成果" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="论文题目">
          <el-input v-model="auditForm.title" disabled />
        </el-form-item>
        <el-form-item label="审核结果" prop="auditStatus">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="remark" v-if="auditForm.auditStatus == '2'">
          <el-input type="textarea" v-model="auditForm.remark" placeholder="请输入驳回原因（必填）" />
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
import { listPaperAchievement, getPaperAchievement, delPaperAchievement, addPaperAchievement, updatePaperAchievement, auditPaperAchievement } from "@/api/system/paperAchievement"
import { checkRole } from "@/utils/permission";

export default {
  name: "PaperAchievement",
  dicts: ['sys_isnot', 'sys_audit_status', 'current_education'],
  data() {
    return {
      activeTab: 'public',
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      paperAchievementList: [],
      title: "",
      open: false,
      // 详情弹窗开关
      detailOpen: false,

      // 审核相关
      auditOpen: false,
      auditForm: {},

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        degreeType: null,
        casPartition: null,
        publicationDate: null,
        title: null,
        params: {
          queryMode: 'public'
        }
      },
      form: {},
      rules: {
        firstAuthor: [{ required: true, message: "第一作者不能为空", trigger: "blur" }],
        degreeType: [{ required: true, message: "学位类别不能为空", trigger: "change" }],
        grade: [{ required: true, message: "年级不能为空", trigger: "blur" }],
        supervisor: [{ required: true, message: "指导人不能为空", trigger: "blur" }],
        title: [{ required: true, message: "题目不能为空", trigger: "blur" }],
        journal: [{ required: true, message: "期刊不能为空", trigger: "blur" }],
        publicationDate: [{ required: true, message: "发表时间不能为空", trigger: "blur" }],
        paperAbstract: [{ required: true, message: "摘要不能为空", trigger: "blur" }]
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
      this.loading = true;
      if (!this.queryParams.params) this.queryParams.params = {};

      // 核心逻辑：告诉后端当前在哪个Tab
      // 后端检测到 'private' 时，会强制 create_by = 当前用户
      // 这样负责人(manager)在 private Tab 也只能看到自己的数据
      this.queryParams.params['queryMode'] = this.activeTab;

      listPaperAchievement(this.queryParams).then(response => {
        this.paperAchievementList = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    /** 切换 Tab */
    handleTabClick(tab, event) {
      this.queryParams.pageNum = 1;
      this.resetForm("queryForm");
      this.getList();
    },

    /** 打开详情弹窗 */
    handleView(row) {
      this.reset();
      const id = row.id;
      getPaperAchievement(id).then(response => {
        this.form = response.data;
        this.detailOpen = true; // 打开详情页
      });
    },

    /** 下载文件 */
    handleDownloadFile(url) {
      // 如果 url 是相对路径，拼接上 baseURL
      // 如果 url 已经是 http 开头，直接使用
      let fullUrl = url;
      if (url && !url.startsWith("http") && !url.startsWith("//")) {
        fullUrl = process.env.VUE_APP_BASE_API + url;
      }
      window.open(fullUrl, '_blank');
    },

    /** 打开审核 */
    handleAuditOpen(row) {
      this.auditForm = {
        id: row.id,
        title: row.title,
        auditStatus: '1',
        remark: ''
      };
      this.auditOpen = true;
    },

    /** 提交审核 */
    submitAudit() {
      if (this.auditForm.auditStatus === '2' && !this.auditForm.remark) {
        this.$modal.msgError("驳回时必须填写审核意见");
        return;
      }
      auditPaperAchievement(this.auditForm).then(response => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },

    cancel() { this.open = false; this.reset(); },
    reset() {
      this.form = {
        id: null,
        serialNumber: null,
        firstAuthor: null,
        degreeType: null,
        grade: null,
        supervisor: null,
        otherAuthors: null,
        title: null,
        journal: null,
        casPartition: null,
        isTop: 'N',
        isNaturalIndex: 'N',
        impactFactor: null,
        doi: null,
        submissionDate: null,
        acceptanceDate: null,
        publicationDate: null,
        paperAbstract: null,
        directionCategory: null,
        apaCitation: null,
        pageFee: null,
        inclusionProof: null,
        rewardStatus: 'N',
        rewardAmount: null,
        rewardDate: null,
        auditStatus: '0',
        remark: null
      };
      this.resetForm("form");
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList(); },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery(); },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length!==1;
      this.multiple = !selection.length;
    },
    handleAdd() { this.reset(); this.open = true; this.title = "添加论文成果"; },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getPaperAchievement(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改论文成果";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePaperAchievement(this.form).then(response => {
              this.$modal.msgSuccess("修改成功，请等待重新审核");
              this.open = false;
              this.getList();
            });
          } else {
            addPaperAchievement(this.form).then(response => {
              this.$modal.msgSuccess("新增成功，请等待审核");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() { return delPaperAchievement(ids); }).then(() => { this.getList(); this.$modal.msgSuccess("删除成功"); }).catch(() => {});
    },
    handleExport() { this.download('system/paperAchievement/export', { ...this.queryParams }, `paperAchievement_${new Date().getTime()}.xlsx`); }
  }
}
</script>
