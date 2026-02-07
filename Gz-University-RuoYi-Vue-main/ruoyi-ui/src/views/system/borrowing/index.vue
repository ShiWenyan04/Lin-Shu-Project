<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部借用 (已通过)" name="public"></el-tab-pane>
      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="audit"
        v-if="checkRole(['teacher', 'boeeowing_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="equipmentName">
        <el-input v-model="queryParams.equipmentName" placeholder="请输入设备名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="借用人员" prop="borrower">
        <el-input v-model="queryParams.borrower" placeholder="请输入借用人员" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:borrowing:add']">新增借用</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:borrowing:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="borrowingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column label="设备名称" align="center" prop="equipmentName" />
      <el-table-column label="型号" align="center" prop="model" />
      <el-table-column label="借用人员" align="center" prop="borrower" />
      <el-table-column label="借用数量" align="center" prop="borrowQty" />

      <el-table-column label="累计归还" align="center" prop="returnCount">
        <template slot-scope="scope">
          <span v-if="scope.row.returnCount >= scope.row.borrowQty" style="color: #67C23A; font-weight: bold;">已还清</span>
          <span v-else>{{ scope.row.returnCount || 0 }}</span>
        </template>
      </el-table-column>

      <el-table-column label="借用时间" align="center" prop="borrowTime" width="120">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.borrowTime, '{y}-{m}-{d}') }}</span></template>
      </el-table-column>
      <el-table-column label="项目名称" align="center" prop="projectName" />
      <el-table-column label="地点" align="center" prop="location" />
      <el-table-column label="审核状态" align="center" prop="auditStatus" width="120">
        <template slot-scope="scope">
          <div style="display: flex; align-items: center; justify-content: center;">
            <dict-tag :options="dict.type.sys_audit_status" :value="scope.row.auditStatus"/>
            <span v-if="scope.row.auditStatus == '0'" style="display:inline-block;width:8px;height:8px;border-radius:50%;background-color:#e6a23c;margin-left:5px" title="待审核"></span>
            <span v-if="scope.row.auditStatus == '1'" style="display:inline-block;width:8px;height:8px;border-radius:50%;background-color:#67c23a;margin-left:5px" title="已通过"></span>
            <el-tooltip v-if="scope.row.auditStatus == '2'" :content="scope.row.auditReason" placement="top">
              <i class="el-icon-warning" style="color: #f56c6c; cursor: pointer; margin-left: 6px; font-size: 14px;"></i>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180"
                       v-if="activeTab !== 'public'">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-refresh-left" style="color: #67C23A; font-weight: bold;"
                     @click="handleReturn(scope.row)"
                     v-if="scope.row.auditStatus == '1' && scope.row.createBy == currentUser.userName">归还</el-button>

          <el-button size="mini" type="text" icon="el-icon-edit"
                     @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:borrowing:edit']"
                     v-if="checkRole(['boeeowing_manager', 'admin']) || (checkRole(['student']) && scope.row.auditStatus != '1' && scope.row.createBy == currentUser.userName)">修改</el-button>

          <el-button size="mini" type="text" icon="el-icon-delete"
                     @click="handleDelete(scope.row)"
                     v-hasPermi="['system:borrowing:remove']"
                     v-if="checkRole(['boeeowing_manager', 'admin'])">删除</el-button>

          <el-button size="mini" type="text" icon="el-icon-check"
                     @click="handleAuditOpen(scope.row)"
                     v-hasPermi="['system:borrowing:audit']"
                     v-if="checkRole(['teacher', 'admin']) && scope.row.auditStatus == '0'">审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>

      <el-card v-if="isReturn" shadow="never" style="margin-bottom: 20px; background-color: #f8f9fa; border: 1px solid #e4e7ed;">
        <div slot="header" class="clearfix" style="padding: 5px 0;">
          <span style="font-weight: bold; color: #303133;"><i class="el-icon-s-data"></i> 归还进度概览</span>

          <el-tag v-if="remainingQty === 0" type="success" size="small" style="float: right;">
            <i class="el-icon-circle-check"></i> 归还数量已达标
          </el-tag>
          <el-tag v-else type="warning" size="small" style="float: right;">
            待归还
          </el-tag>
        </div>

        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="借用总数">
            <strong>{{ form.borrowTotalQty }}</strong>
          </el-descriptions-item>
          <el-descriptions-item label="历史归还">
            <span style="color: #409EFF;">{{ form.historyReturnQty }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="剩余需还">
            <span :style="{color: remainingQty === 0 ? '#67C23A' : '#F56C6C', fontWeight: 'bold'}">
              {{ remainingQty }}
            </span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <transition name="el-fade-in">
          <div v-if="selectedDevice && !isReturn" style="background-color: #f8f8f9; padding: 10px 15px; margin: 0 0 20px 20px; border-radius: 4px; border-left: 4px solid #409EFF;">
            <div style="font-size: 12px; color: #606266; margin-bottom: 5px; font-weight: bold;">
              <i class="el-icon-s-data"></i> 当前设备库存概况：
            </div>
            <el-row :gutter="10" style="text-align: center;">
              <el-col :span="6">
                <div style="font-size: 12px; color: #909399;">可用</div>
                <div style="font-size: 16px; color: #67C23A; font-weight: bold;">{{ selectedDevice.availableQty }}</div>
              </el-col>
              <el-col :span="6">
                <div style="font-size: 12px; color: #909399;">外借</div>
                <div style="font-size: 16px; color: #409EFF; font-weight: bold;">{{ selectedDevice.borrowedQty }}</div>
              </el-col>
            </el-row>
          </div>
        </transition>

        <el-row>
          <el-col :span="12">
            <el-form-item label="选择设备" prop="equipmentId">
              <el-select v-model="form.equipmentId" placeholder="请选择设备" filterable style="width: 100%" @change="handleEquipmentChange" :disabled="isReturn">
                <el-option v-for="item in equipmentOptions" :key="item.id" :label="item.equipmentName" :value="item.id">
                  <span style="float: left">{{ item.equipmentName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">剩余:{{ item.availableQty }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input v-model="form.model" disabled placeholder="自动填充" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="所属项目" prop="projectId">
          <el-select v-model="form.projectId" placeholder="请选择项目" filterable style="width: 100%" @change="handleProjectChange" :disabled="isReturn">
            <el-option v-for="item in projectOptions" :key="item.projectId" :label="item.projectName" :value="item.projectId" />
          </el-select>
        </el-form-item>

        <el-input v-model="form.equipmentName" v-show="false" />
        <el-input v-model="form.projectName" v-show="false" />
        <el-input v-model="form.borrowId" v-show="false" />

        <el-row>
          <el-col :span="12">
            <el-form-item label="借用人员" prop="borrower" v-if="!isReturn">
              <el-input v-model="form.borrower" placeholder="请输入借用人员" />
            </el-form-item>
            <el-form-item label="归还人员" prop="returner" v-if="isReturn">
              <el-input v-model="form.returner" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地点" prop="location">
              <el-input v-model="form.location" placeholder="请输入地点" :disabled="isReturn"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="导师姓名" prop="tutorName">
              <el-input v-model="form.tutorName" placeholder="请输入导师姓名" :disabled="isReturn"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="isReturn ? '归还时间' : '借用时间'" prop="operationTime">
              <el-date-picker v-if="!isReturn" clearable style="width: 100%" v-model="form.borrowTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择借用时间"></el-date-picker>
              <el-date-picker v-else clearable style="width: 100%" v-model="form.returnTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择归还时间"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <div v-if="!isReturn">
          <el-form-item label="借用数量" prop="borrowQty">
            <el-input-number v-model="form.borrowQty" :min="1" controls-position="right" style="width: 100%"/>
          </el-form-item>
        </div>

        <div v-if="isReturn" style="background-color: #fdf6ec; padding: 15px; border-radius: 5px; margin-bottom: 22px;">
          <el-divider content-position="left"><i class="el-icon-s-order"></i> 归还数量结算 (待审核)</el-divider>

          <div style="font-size:12px; color:#E6A23C; margin-bottom:10px; padding-left:10px;">
            提示：仅“归还数量”受借用总数限制，其他数量（安装、回收等）可根据实际情况填写。
          </div>

          <el-row :gutter="10">
            <el-col :span="8">
              <el-form-item label="归还数量" prop="returnQty" label-width="80px">
                <el-input-number
                  v-model="form.returnQty"
                  :min="0"
                  :max="remainingQty"
                  :disabled="remainingQty === 0"
                  controls-position="right"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="安装数量" prop="installAddQty" label-width="80px">
                <el-input-number v-model="form.installAddQty" :min="0" controls-position="right" style="width: 100%"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="回收数量" prop="recoveredQty" label-width="80px">
                <el-input-number v-model="form.recoveredQty" :min="0" controls-position="right" style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="8">
              <el-form-item label="报废数量" prop="scrappedQty" label-width="80px">
                <el-input-number v-model="form.scrappedQty" :min="0" controls-position="right" style="width: 100%"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="被盗数量" prop="stolenQty" label-width="80px">
                <el-input-number v-model="form.stolenQty" :min="0" controls-position="right" style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <el-form-item label="驳回原因" v-if="form.auditStatus == '2' && !isReturn">
          <el-input v-model="form.auditReason" type="textarea" disabled />
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

    <el-dialog title="借用审核" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="借用设备"><el-input v-model="auditForm.equipmentName" disabled /></el-form-item>
        <el-form-item label="借用人"><el-input v-model="auditForm.borrower" disabled /></el-form-item>
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" v-if="auditForm.auditStatus == '2'">
          <el-input type="textarea" v-model="auditForm.auditReason" placeholder="请输入驳回原因" />
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
import { listBorrowing, getBorrowing, delBorrowing, addBorrowing, updateBorrowing, auditBorrowing } from "@/api/system/borrowing"
import { addReturn } from "@/api/system/return"
import { listProject } from "@/api/system/project"
import { listSurvey } from "@/api/system/survey"
import { checkRole } from "@/utils/permission";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Borrowing",
  dicts: ['sys_audit_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      borrowingList: [],
      title: "",
      open: false,
      selectedDevice: null,
      isReturn: false,
      auditOpen: false,
      activeTab: 'public',
      currentUser: {},
      auditForm: {},
      projectOptions: [],
      equipmentOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipmentName: null,
        borrower: null,
        projectName: null,
        createBy: null,
        params: { queryMode: 'public' }
      },
      form: {},
      rules: {
        equipmentId: [{ required: true, message: "关联设备不能为空", trigger: "change" }],
        projectId: [{ required: true, message: "关联项目不能为空", trigger: "change" }],
        borrower: [{ required: true, message: "借用人员不能为空", trigger: "blur" }],
        returner: [{ required: true, message: "归还人员不能为空", trigger: "blur" }],
        borrowQty: [{ required: false, message: "借用数量不能为空", trigger: "blur" }]
      }
    }
  },
  computed: {
    // ✨✨✨ 计算逻辑修改 ✨✨✨
    // 只计算：借用总数 - 历史已还 = 剩余可归还
    // 不再关心安装、回收等数量
    remainingQty() {
      if (!this.form.borrowTotalQty) return 0;
      let left = this.form.borrowTotalQty - (this.form.historyReturnQty || 0);
      return left < 0 ? 0 : left;
    }
  },
  created() {
    this.getCurrentUser();
    this.getProjectList();
    this.getEquipmentList();
  },
  methods: {
    checkRole,
    getCurrentUser() {
      getUserProfile().then(response => {
        this.currentUser = response.data.user || response.data;
        this.getList();
      });
    },
    getList() {
      this.loading = true
      if (!this.queryParams.params) { this.queryParams.params = {}; }
      this.queryParams.params['queryMode'] = this.activeTab;

      if (this.activeTab === 'private') {
        this.queryParams.createBy = this.currentUser.userName;
        this.queryParams.auditStatus = undefined;
      } else if (this.activeTab === 'public') {
        this.queryParams.createBy = null;
        this.queryParams.auditStatus = undefined;
      } else {
        this.queryParams.createBy = null;
      }

      listBorrowing(this.queryParams).then(response => {
        this.borrowingList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleTabClick(tab, event) {
      this.queryParams.pageNum = 1;
      this.resetForm("queryForm");
      this.getList();
    },
    getProjectList() {
      listProject({ pageNum: 1, pageSize: 1000, auditStatus: '1' }).then(response => {
        this.projectOptions = response.rows;
      });
    },
    getEquipmentList() {
      listSurvey({ pageNum: 1, pageSize: 1000, auditStatus: '1' }).then(response => {
        this.equipmentOptions = response.rows;
      });
    },
    handleEquipmentChange(val) {
      const equip = this.equipmentOptions.find(item => item.id === val);
      if (equip) {
        this.form.equipmentName = equip.equipmentName;
        this.form.model = equip.model;
        this.selectedDevice = equip;
      } else {
        this.selectedDevice = null;
      }
    },
    handleProjectChange(val) {
      const project = this.projectOptions.find(item => item.projectId === val);
      if (project) {
        this.form.projectName = project.projectName;
      }
    },
    handleAuditOpen(row) {
      this.auditForm = {
        id: row.id,
        equipmentName: row.equipmentName,
        borrower: row.borrower,
        auditStatus: '1',
        auditReason: ''
      };
      this.auditOpen = true;
    },
    submitAudit() {
      if (this.auditForm.auditStatus === '2' && !this.auditForm.auditReason) {
        this.$modal.msgError("驳回时必须填写原因");
        return;
      }
      auditBorrowing(this.auditForm).then(res => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        equipmentId: null,
        equipmentName: null,
        model: null,
        borrower: null,
        returner: null,
        borrowQty: 1,
        borrowTime: null,
        returnTime: null,
        location: null,
        projectId: null,
        projectName: null,
        tutorName: null,
        auditStatus: "0",
        auditReason: null,
        remark: null,
        returnQty: 0,
        installAddQty: 0,
        recoveredQty: 0,
        scrappedQty: 0,
        stolenQty: 0,
        borrowTotalQty: 0,
        historyReturnQty: 0
      }
      this.isReturn = false;
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
      this.isReturn = false;
      this.open = true
      this.title = "添加外业调查设备借用"
    },
    handleUpdate(row) {
      this.reset()
      this.isReturn = false;
      const id = row.id || this.ids
      getBorrowing(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改外业调查设备借用"
        if (this.equipmentOptions.length > 0) {
          this.selectedDevice = this.equipmentOptions.find(item => item.id === this.form.equipmentId);
        }
      })
    },
    handleReturn(row) {
      this.reset();
      this.isReturn = true;
      this.title = "设备归还登记 (新增待审核)";
      this.form.borrowId = row.id;
      this.form.equipmentId = row.equipmentId;
      this.form.equipmentName = row.equipmentName;
      this.form.model = row.model;
      this.form.projectId = row.projectId;
      this.form.projectName = row.projectName;
      this.form.location = row.location;
      this.form.tutorName = row.tutorName;
      this.form.returner = row.borrower;

      this.form.borrowTotalQty = row.borrowQty;
      // 后端计算的 returnCount
      this.form.historyReturnQty = row.returnCount || 0;
      this.form.returnQty = 0;
      this.form.returnTime = new Date();

      if (this.equipmentOptions.length > 0) {
        this.selectedDevice = this.equipmentOptions.find(item => item.id === this.form.equipmentId);
      }
      this.open = true;
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

          // ✨✨✨ 修改点：只校验“归还数量” ✨✨✨
          if (this.isReturn) {
            if (this.form.returnQty > this.remainingQty) {
              this.$modal.msgError(`本次归还数量(${this.form.returnQty}) 不能大于剩余未还借用量(${this.remainingQty})`);
              return;
            }
            // 其他的安装、回收等可以随便填，不做限制
          }

          if (this.isReturn) {
            addReturn(this.form).then(response => {
              this.$modal.msgSuccess("归还信息已提交，请等待审核");
              this.open = false;
              this.getList();
            });
          } else {
            if (this.form.id != null) {
              this.form.auditStatus = "0";
              this.form.auditReason = null;
              updateBorrowing(this.form).then(response => {
                this.$modal.msgSuccess("修改成功，已重新提交审核")
                this.open = false
                this.getList()
              })
            } else {
              addBorrowing(this.form).then(response => {
                this.$modal.msgSuccess("新增成功")
                this.open = false
                this.getList()
              })
            }
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(function() {
        return delBorrowing(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('system/borrowing/export', {
        ...this.queryParams
      }, `borrowing_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
