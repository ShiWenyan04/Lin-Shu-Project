<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="全部归还 (已通过)" name="public"></el-tab-pane>
      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher'])"></el-tab-pane>

      <el-tab-pane
        label="审核管理"
        name="audit"
        v-if="checkRole(['teacher', 'return_manager', 'admin'])"
      ></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="equipmentName">
        <el-input
          v-model="queryParams.equipmentName"
          placeholder="请输入设备名称"
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:return:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="returnList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="设备名称" align="center" prop="equipmentName" />
      <el-table-column label="型号" align="center" prop="model" />
      <el-table-column label="项目名称" align="center" prop="projectName" />
      <el-table-column label="归还人员" align="center" prop="returner" />

      <el-table-column label="归还时间" align="center" prop="returnTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.returnTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="地点" align="center" prop="location" />
      <el-table-column label="导师姓名" align="center" prop="tutorName" />

      <el-table-column label="数量变动" align="center" width="280">
        <template slot-scope="scope">
          <span v-if="scope.row.returnQty > 0" style="margin-right:5px; color:#67C23A">归还:{{scope.row.returnQty}}</span>
          <span v-if="scope.row.installAddQty > 0" style="margin-right:5px; color:#E6A23C">安装:{{scope.row.installAddQty}}</span>
          <span v-if="scope.row.scrappedQty > 0" style="margin-right:5px; color:#F56C6C">报废:{{scope.row.scrappedQty}}</span>
          <span v-if="scope.row.stolenQty > 0" style="margin-right:5px; color:#909399">被盗:{{scope.row.stolenQty}}</span>
          <span v-if="scope.row.recoveredQty > 0" style="margin-right:5px; color:#409EFF">回收:{{scope.row.recoveredQty}}</span>
        </template>
      </el-table-column>

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

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width"
                       v-if="checkRole(['return_manager', 'teacher', 'admin']) || (checkRole(['student']) && activeTab === 'private')">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:return:edit']"
            v-if="checkRole(['return_manager', 'admin']) || (checkRole(['student']) && scope.row.auditStatus != '1')"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:return:remove']"
            v-if="checkRole(['return_manager', 'admin'])"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAuditOpen(scope.row)"
            v-hasPermi="['system:return:audit']"
            v-if="checkRole(['teacher', 'admin']) && scope.row.auditStatus == '0'"
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

    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">

        <el-row>
          <el-col :span="12">
            <el-form-item label="选择设备" prop="equipmentId">
              <el-select
                v-model="form.equipmentId"
                placeholder="请选择设备"
                filterable
                style="width: 100%"
                @change="handleEquipmentChange"
              >
                <el-option
                  v-for="item in equipmentOptions"
                  :key="item.id"
                  :label="item.equipmentName"
                  :value="item.id"
                >
                  <span style="float: left">{{ item.equipmentName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">余:{{ item.availableQty }}</span>
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

        <transition name="el-fade-in">
          <div v-if="selectedDevice" style="background-color: #f8f8f9; padding: 10px 15px; margin: 0 0 20px 20px; border-radius: 4px; border-left: 4px solid #409EFF;">
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
              <el-col :span="6">
                <div style="font-size: 12px; color: #909399;">安装</div>
                <div style="font-size: 16px; color: #E6A23C; font-weight: bold;">{{ selectedDevice.installedQty }}</div>
              </el-col>
              <el-col :span="6">
                <div style="font-size: 12px; color: #909399;">报废</div>
                <div style="font-size: 16px; color: #F56C6C; font-weight: bold;">{{ selectedDevice.scrappedQty }}</div>
              </el-col>
            </el-row>
          </div>
        </transition>

        <el-row>
          <el-col :span="12">
            <el-form-item label="关联项目" prop="projectId">
              <el-select
                v-model="form.projectId"
                placeholder="请选择项目"
                filterable
                style="width: 100%"
                @change="handleProjectChange"
              >
                <el-option
                  v-for="item in projectOptions"
                  :key="item.projectId"
                  :label="item.projectName"
                  :value="item.projectId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归还人员" prop="returner">
              <el-input v-model="form.returner" placeholder="请输入操作人员" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-input v-model="form.equipmentName" v-show="false" />
        <el-input v-model="form.projectName" v-show="false" />
        <el-input v-model="form.borrowId" v-show="false" />

        <el-form-item label="归还时间" prop="returnTime">
          <el-date-picker clearable style="width: 100%"
                          v-model="form.returnTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择时间">
          </el-date-picker>
        </el-form-item>

        <el-divider content-position="left">数量变动 (请按实际情况填写)</el-divider>

        <el-row>
          <el-col :span="8">
            <el-form-item label="归还数量" prop="returnQty">
              <el-input-number v-model="form.returnQty" :min="0" style="width: 100%" placeholder="归还数量" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="安装数量" prop="installAddQty">
              <el-input-number v-model="form.installAddQty" :min="0" style="width: 100%" placeholder="新增安装" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="回收数量" prop="recoveredQty">
              <el-input-number v-model="form.recoveredQty" :min="0" style="width: 100%" placeholder="回收" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="报废数量" prop="scrappedQty">
              <el-input-number v-model="form.scrappedQty" :min="0" style="width: 100%" placeholder="报废" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="被盗数量" prop="stolenQty">
              <el-input-number v-model="form.stolenQty" :min="0" style="width: 100%" placeholder="被盗" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入地点" />
        </el-form-item>
        <el-form-item label="导师姓名" prop="tutorName">
          <el-input v-model="form.tutorName" placeholder="请输入导师姓名" />
        </el-form-item>

        <el-form-item label="驳回原因" v-if="form.auditStatus == '2'">
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

    <el-dialog title="运维/归还审核" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="设备名称">
          <el-input v-model="auditForm.equipmentName" disabled />
        </el-form-item>
        <el-form-item label="操作人">
          <el-input v-model="auditForm.returner" disabled />
        </el-form-item>
        <el-form-item label="审核结果" prop="auditStatus">
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
import { listReturn, getReturn, delReturn, addReturn, updateReturn, auditReturn } from "@/api/system/return"
import { listProject } from "@/api/system/project"
import { listSurvey } from "@/api/system/survey"
import { checkRole } from "@/utils/permission";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Return",
  dicts: ['sys_audit_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      returnList: [],
      title: "",
      open: false,

      auditOpen: false,
      activeTab: 'public',
      currentUser: {},
      auditForm: {},
      projectOptions: [],
      equipmentOptions: [],
      selectedDevice: null,

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipmentName: null,
        projectName: null,
        createBy: null,
        params: {
          queryMode: 'public'
        }
      },
      form: {},
      rules: {
        equipmentId: [
          { required: true, message: "关联设备不能为空", trigger: "change" }
        ],
        projectId: [
          { required: true, message: "关联项目不能为空", trigger: "change" }
        ],
        returner: [
          { required: true, message: "归还人员不能为空", trigger: "blur" }
        ],
        returnTime: [
          { required: true, message: "归还时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getCurrentUser();
    this.getProjectList();
    this.getEquipmentList();
  },
  methods: {
    checkRole,

    // 获取当前用户
    getCurrentUser() {
      getUserProfile().then(response => {
        this.currentUser = response.data.user || response.data;
        this.getList();
      });
    },

    // 查询列表
    getList() {
      this.loading = true
      if (!this.queryParams.params) this.queryParams.params = {};
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

      listReturn(this.queryParams).then(response => {
        this.returnList = response.rows
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
        returner: row.returner,
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
      auditReturn(this.auditForm).then(res => {
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
      this.selectedDevice = null;
      this.form = {
        id: null,
        borrowId: null,
        equipmentId: null,
        equipmentName: null,
        model: null,
        projectId: null,
        projectName: null,
        returner: null,
        returnTime: null,
        returnQty: 0,
        installAddQty: 0,
        scrappedQty: 0,
        stolenQty: 0,
        recoveredQty: 0,
        location: null,
        tutorName: null,
        auditStatus: "0",
        auditReason: null,
        remark: null
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
      this.title = "添加红外相机归还"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getReturn(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改红外相机归还"
        if (this.equipmentOptions.length > 0) {
          this.selectedDevice = this.equipmentOptions.find(item => item.id === this.form.equipmentId);
        }
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.form.auditStatus = "0";
            this.form.auditReason = null;
            updateReturn(this.form).then(response => {
              this.$modal.msgSuccess("修改成功，已重新提交审核")
              this.open = false
              this.getList()
            })
          } else {
            addReturn(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除？').then(function() {
        return delReturn(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('system/return/export', {
        ...this.queryParams
      }, `return_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
