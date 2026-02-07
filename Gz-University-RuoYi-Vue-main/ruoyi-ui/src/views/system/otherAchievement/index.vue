<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部成果 (已通过)" name="public"></el-tab-pane>

      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="audit"
        v-if="checkRole(['teacher', 'otherAchievement_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="成果类型" prop="achievementType">
        <el-select v-model="queryParams.achievementType" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.achievement_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="成果名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
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
          v-hasPermi="['system:otherAchievement:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:otherAchievement:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="otherAchievementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序" align="center" width="50">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="成果名称" align="center" prop="name" show-overflow-tooltip min-width="150" />

      <el-table-column label="类型" align="center" prop="achievementType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.achievement_type" :value="scope.row.achievementType"/>
        </template>
      </el-table-column>

      <el-table-column label="主完成人" align="center" prop="mainPerson" width="100"/>
      <el-table-column label="获奖等次" align="center" prop="awardLevel" show-overflow-tooltip/>

      <el-table-column label="获得时间" align="center" prop="obtainTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.obtainTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>

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
            v-hasPermi="['system:otherAchievement:edit']"
            v-if="checkRole(['otherAchievement_manager', 'admin']) || (checkRole(['student']) && activeTab === 'private' && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:otherAchievement:remove']"
            v-if="checkRole(['otherAchievement_manager', 'admin']) || (checkRole(['student']) && activeTab === 'private' && scope.row.auditStatus != '1')"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:otherAchievement:audit']"
            v-if="checkRole(['teacher', 'otherAchievement_manager', 'admin']) && scope.row.auditStatus == '0'"
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

    <el-dialog title="成果详情" :visible.sync="detailOpen" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="成果名称" :span="2">{{ form.name }}</el-descriptions-item>

        <el-descriptions-item label="成果类型">
          <dict-tag :options="dict.type.achievement_type" :value="form.achievementType"/>
        </el-descriptions-item>
        <el-descriptions-item label="获得时间">{{ parseTime(form.obtainTime, '{y}-{m}-{d}') }}</el-descriptions-item>

        <el-descriptions-item label="主完成人">{{ form.mainPerson }}</el-descriptions-item>
        <el-descriptions-item label="共同完成人">{{ form.coPerson }}</el-descriptions-item>

        <el-descriptions-item label="颁发单位">{{ form.issueUnit }}</el-descriptions-item>
        <el-descriptions-item label="获奖等次">{{ form.awardLevel }}</el-descriptions-item>

        <template v-if="form.rewardStatus == 'Y' || form.rewardStatus == '1'"> <el-descriptions-item label="奖励额度">{{ form.rewardAmount }} 元</el-descriptions-item>
          <el-descriptions-item label="奖励时间">{{ parseTime(form.rewardTime, '{y}-{m}-{d}') }}</el-descriptions-item>
        </template>
        <template v-else>
          <el-descriptions-item label="奖励情况">未发放</el-descriptions-item>
        </template>

        <el-descriptions-item label="审核状态">
          <dict-tag :options="dict.type.sys_audit_status" :value="form.auditStatus"/>
        </el-descriptions-item>

        <el-descriptions-item label="审核意见" v-if="form.auditStatus == '2'" :span="2">
          <span style="color: red">{{ form.auditOpinion }}</span>
        </el-descriptions-item>

        <el-descriptions-item label="备注" :span="2">{{ form.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="成果类型" prop="achievementType">
              <el-select v-model="form.achievementType" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.achievement_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="获得时间" prop="obtainTime">
              <el-date-picker clearable style="width: 100%"
                              v-model="form.obtainTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="成果名称" prop="name">
          <el-input v-model="form.name" type="textarea" :rows="2" placeholder="请输入成果名称" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="主完成人" prop="mainPerson">
              <el-input v-model="form.mainPerson" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="获奖等次" prop="awardLevel">
              <el-input v-model="form.awardLevel" placeholder="如：一等奖,没有等次划分则填无" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="共同完成人" prop="coPerson">
          <el-input v-model="form.coPerson" placeholder="多个人员请用逗号分隔" />
        </el-form-item>

        <el-form-item label="颁发单位" prop="issueUnit">
          <el-input v-model="form.issueUnit" placeholder="请输入颁发单位" />
        </el-form-item>

        <el-row>
          <el-col :span="24">
            <el-form-item label="奖励发放" prop="rewardStatus">
              <el-radio-group v-model="form.rewardStatus">
                <el-radio
                  v-for="dict in dict.type.sys_isnot"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="form.rewardStatus == 'Y' || form.rewardStatus == '1'">
          <el-col :span="12">
            <el-form-item label="奖励额度" prop="rewardAmount">
              <el-input v-model="form.rewardAmount" placeholder="请输入金额" type="number">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="奖励时间" prop="rewardTime">
              <el-date-picker clearable style="width: 100%"
                              v-model="form.rewardTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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

    <el-dialog title="审核成果" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="成果名称">
          <el-input v-model="auditForm.name" disabled />
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
// 注意：请确保 api 文件中已添加 auditOtherAchievement 方法 (如果没有，可以复用 update，但建议单独写)
import { listOtherAchievement, getOtherAchievement, delOtherAchievement, addOtherAchievement, updateOtherAchievement, auditOtherAchievement } from "@/api/system/otherAchievement"
import { checkRole } from "@/utils/permission";

export default {
  name: "OtherAchievement",
  dicts: ['sys_isnot', 'sys_audit_status', 'achievement_type'],
  data() {
    return {
      // --- Tab 状态 ---
      activeTab: 'public',

      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      otherAchievementList: [],
      title: "",
      open: false,
      detailOpen: false, // 详情弹窗

      // 审核相关
      auditOpen: false,
      auditForm: {},

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        achievementType: null,
        name: null,
        // 传递查询模式
        params: {
          queryMode: 'public'
        }
      },
      form: {},
      rules: {
        achievementType: [
          { required: true, message: "成果类型不能为空", trigger: "change" }
        ],
        name: [
          { required: true, message: "成果名称不能为空", trigger: "blur" }
        ],
        mainPerson: [
          { required: true, message: "主完成人不能为空", trigger: "blur" }
        ],
        issueUnit: [
          { required: true, message: "颁发单位不能为空", trigger: "blur" }
        ],
        obtainTime: [
          { required: true, message: "获得时间不能为空", trigger: "blur" }
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
      this.loading = true;
      if (!this.queryParams.params) this.queryParams.params = {};

      // 核心逻辑：传递 queryMode 给后端
      // 后端 Controller 需要根据这个值判断是否强制加上 create_by = 当前用户
      this.queryParams.params['queryMode'] = this.activeTab;

      listOtherAchievement(this.queryParams).then(response => {
        this.otherAchievementList = response.rows
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
      getOtherAchievement(row.id).then(response => {
        this.form = response.data;
        this.detailOpen = true;
      });
    },

    /** 打开审核 */
    handleAuditOpen(row) {
      this.auditForm = {
        id: row.id,
        name: row.name,
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
      // 记得去 api 文件里加这个方法
      auditOtherAchievement(this.auditForm).then(response => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },

    cancel() { this.open = false; this.reset(); },
    reset() {
      this.form = {
        id: null,
        achievementType: null,
        name: null,
        mainPerson: null,
        coPerson: null,
        issueUnit: null,
        awardLevel: null,
        obtainTime: null,
        rewardStatus: 'N', // 默认否
        rewardAmount: null,
        rewardTime: null,
        auditStatus: '0', // 默认待审核
        auditOpinion: null,
        remark: null
      }
      this.resetForm("form")
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList(); },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery(); },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() { this.reset(); this.open = true; this.title = "添加其他成果"; },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getOtherAchievement(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改其他成果";
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOtherAchievement(this.form).then(response => {
              this.$modal.msgSuccess("修改成功，请等待重新审核")
              this.open = false
              this.getList()
            })
          } else {
            addOtherAchievement(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除？').then(function() { return delOtherAchievement(ids); }).then(() => { this.getList(); this.$modal.msgSuccess("删除成功"); }).catch(() => {})
    },
    handleExport() { this.download('system/otherAchievement/export', { ...this.queryParams }, `otherAchievement_${new Date().getTime()}.xlsx`) }
  }
}
</script>
