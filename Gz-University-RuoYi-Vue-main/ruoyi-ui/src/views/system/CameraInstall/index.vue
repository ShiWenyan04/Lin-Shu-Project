<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick" style="margin-bottom: 10px;">
      <el-tab-pane label="公开数据 (已通过)" name="public"><span slot="label"><i class="el-icon-share"></i> 公开数据</span></el-tab-pane>
      <el-tab-pane label="我的提交" name="private" v-if="!checkRole(['teacher', 'admin'])"><span slot="label"><i class="el-icon-user"></i> 我的提交</span></el-tab-pane>
      <el-tab-pane label="审核管理" name="audit" v-if="checkRole(['teacher', 'cameraInstalaion_manager', 'admin'])"><span slot="label"><i class="el-icon-s-check"></i> 审核管理</span></el-tab-pane>
    </el-tabs>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="18">
        <el-card shadow="always" :body-style="{ padding: '0px' }">
          <div slot="header" class="clearfix">
            <span style="font-weight: bold; font-size: 16px"><i class="el-icon-map-location"></i> 物种分布可视化 <span style="font-size:12px; color:#909399">({{ activeTabName }})</span></span>
          </div>
          <div id="camera-map-container" style="width: 100%; height: 500px;"></div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="always" style="height: 555px; overflow-y: auto;">
          <div slot="header" class="clearfix">
            <span><i class="el-icon-collection"></i> 物种百科</span>
            <el-button v-if="selectedSpeciesName" style="float: right; padding: 3px 0; color: #909399" type="text" @click="closeRightPanel">关闭</el-button>
          </div>

          <div v-if="!selectedSpeciesName" style="text-align: center; color: #909399; margin-top: 50px;">
            <i class="el-icon-mouse" style="font-size: 48px; margin-bottom: 20px;"></i>
            <p>请在下方"叠加筛选-搜物种"列表中<br>点击某个物种查看详情</p>
          </div>

          <div v-else-if="speciesInfo.id && !isEditing">
            <div style="text-align: center; margin-bottom: 15px; position: relative;">
              <el-image
                style="width: 100%; height: 200px; border-radius: 4px;"
                :src="speciesInfo.coverImage ? (baseUrl + speciesInfo.coverImage) : require('@/assets/images/profile.jpg')"
                fit="cover">
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline" style="font-size: 30px; margin-top: 80px; color:#ccc;"></i>
                </div>
              </el-image>
              <el-button
                type="primary"
                icon="el-icon-edit"
                circle
                size="mini"
                style="position: absolute; right: 5px; bottom: 5px; z-index: 10"
                @click="handleEditLibrary"
              ></el-button>

              <h3 style="margin: 10px 0 5px 0">{{ speciesInfo.speciesName }}</h3>
              <small style="color: #666; font-style: italic;">{{ speciesInfo.scientificName || '暂无学名' }}</small>
            </div>

            <el-descriptions :column="1" border size="small">
              <el-descriptions-item label="别名">{{ speciesInfo.alias || '-' }}</el-descriptions-item>
              <el-descriptions-item label="保护级别">
                <el-tag size="mini" v-if="speciesInfo.protectionLevel">{{ speciesInfo.protectionLevel }}</el-tag>
                <span v-else>-</span>
              </el-descriptions-item>
              <el-descriptions-item label="食性">{{ speciesInfo.dietType || '-' }}</el-descriptions-item>
              <el-descriptions-item label="习性">{{ speciesInfo.activityPattern || '-' }}</el-descriptions-item>
              <el-descriptions-item label="生境">{{ speciesInfo.habitat || '-' }}</el-descriptions-item>
            </el-descriptions>

            <div style="margin-top: 15px;">
              <span style="font-weight:bold; font-size:14px;">形态与习性：</span>
              <div style="margin-top: 5px; font-size: 13px; line-height: 1.5; color: #606266; text-indent: 2em;">
                {{ speciesInfo.description || '暂无详细描述信息。' }}
              </div>
            </div>
          </div>

          <div v-else>
            <el-alert
              :title="speciesInfo.id ? '编辑物种资料' : '该物种暂无资料，请补充'"
              :type="speciesInfo.id ? 'info' : 'warning'"
              show-icon :closable="false" style="margin-bottom: 15px;">
            </el-alert>

            <el-form :model="libraryForm" size="mini" label-position="top">
              <el-form-item label="物种名称" style="margin-bottom: 10px;">
                <el-input v-model="libraryForm.speciesName" disabled />
              </el-form-item>

              <el-form-item label="封面图" style="margin-bottom: 10px;">
                <image-upload v-model="libraryForm.coverImage" :limit="1"/>
              </el-form-item>

              <el-row :gutter="10">
                <el-col :span="12">
                  <el-form-item label="拉丁学名">
                    <el-input v-model="libraryForm.scientificName" placeholder="Scientific Name"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="别名/俗名">
                    <el-input v-model="libraryForm.alias" placeholder="如：山猫"/>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="10">
                <el-col :span="12">
                  <el-form-item label="保护级别">
                    <el-select v-model="libraryForm.protectionLevel" placeholder="请选择" style="width:100%">
                      <el-option label="一级保护" value="一级保护"></el-option>
                      <el-option label="二级保护" value="二级保护"></el-option>
                      <el-option label="三有" value="三有"></el-option>
                      <el-option label="无危" value="无危"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="食性">
                    <el-select v-model="libraryForm.dietType" placeholder="请选择" style="width:100%">
                      <el-option label="食肉" value="食肉"></el-option>
                      <el-option label="食草" value="食草"></el-option>
                      <el-option label="杂食" value="杂食"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="10">
                <el-col :span="12">
                  <el-form-item label="活动习性">
                    <el-select v-model="libraryForm.activityPattern" placeholder="请选择" style="width:100%">
                      <el-option label="昼行性" value="昼行性"></el-option>
                      <el-option label="夜行性" value="夜行性"></el-option>
                      <el-option label="晨昏活动" value="晨昏活动"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="典型生境">
                    <el-input v-model="libraryForm.habitat" placeholder="如：阔叶林"/>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="形态与习性描述">
                <el-input type="textarea" v-model="libraryForm.description" rows="4" placeholder="请填写详细的形态特征、生活习性等..."></el-input>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" style="width:100%" @click="submitLibrary" :loading="libraryLoading">保存资料</el-button>
                <el-button v-if="speciesInfo.id" style="width:100%; margin: 5px 0 0 0" @click="isEditing = false">取消修改</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="never" style="margin-bottom: 20px;">
          <div slot="header">
            <span><i class="el-icon-edit-outline"></i> 数据补录</span>
            <el-tag v-if="lastClickedProject" size="mini" type="success" style="float:right">{{ lastClickedProject.projectName }}</el-tag>
            <el-tag v-else size="mini" type="info" style="float:right">未选择项目</el-tag>
          </div>
          <el-form :inline="true" size="mini">
            <el-form-item><el-input v-model="manualInput.siteCode" placeholder="点位(L17)" style="width: 100px"></el-input></el-form-item>
            <el-form-item><el-input v-model="manualInput.speciesName" placeholder="物种(豹猫)" style="width: 100px"></el-input></el-form-item>
            <el-form-item><el-button type="primary" icon="el-icon-check" @click="handleManualAdd" :disabled="!lastClickedProject">提交</el-button></el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span><i class="el-icon-search"></i> 叠加筛选</span>
            <el-button style="float: right; padding: 3px 0; color:#F56C6C" type="text" @click="clearLeftSelection">清空</el-button>
          </div>
          <el-tabs v-model="filterTab" type="card" @tab-click="handleLeftFilterChange">
            <el-tab-pane label="搜物种" name="species">
              <div style="padding: 5px 0;"><el-input v-model="searchKeywordSpecies" prefix-icon="el-icon-search" placeholder="物种名称..." size="mini" clearable></el-input></div>
              <el-table
                ref="speciesTable"
                :data="filteredSpeciesList"
                height="250"
                border
                stripe
                size="mini"
                @selection-change="handleLeftFilterChange"
                @row-click="handleSpeciesRowClick"
                row-key="name">
                <el-table-column type="selection" width="40" align="center"></el-table-column>
                <el-table-column label="物种" prop="name" align="center"></el-table-column>
                <el-table-column label="频次" prop="count" width="60" align="center"></el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="搜点位" name="site">
              <div style="padding: 5px 0;"><el-input v-model="searchKeywordSite" prefix-icon="el-icon-search" placeholder="点位编号..." size="mini" clearable></el-input></div>
              <el-table ref="siteTable" :data="filteredSiteList" height="250" border stripe size="mini" @selection-change="handleLeftFilterChange" row-key="name">
                <el-table-column type="selection" width="45" align="center"></el-table-column>
                <el-table-column label="编号" prop="name" align="center"></el-table-column>
                <el-table-column label="物种" prop="speciesStr" show-overflow-tooltip align="center"></el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card shadow="never">
          <div slot="header"><span><i class="el-icon-menu"></i> 项目列表</span></div>
          <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch">
            <el-form-item label="项目名称" prop="projectName"><el-input v-model="queryParams.projectName" placeholder="请输入" clearable @keyup.enter.native="handleQuery"/></el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <el-row :gutter="10" class="mb8" v-if="checkRole(['student', 'cameraInstalaion_manager', 'admin'])">
            <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd('point')">新增(点位)</el-button></el-col>
            <el-col :span="1.5"><el-button type="success" plain icon="el-icon-plus" size="mini" @click="handleAdd('boundary')">新增(边界)</el-button></el-col>
            <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button></el-col>
          </el-row>

          <el-table ref="projectTable" v-loading="loading" :data="CameraInstallList" @selection-change="handleProjectSelectionChange" @row-click="handleProjectRowClick" highlight-current-row border height="380" style="margin-top: 10px;">
            <el-table-column type="selection" width="40" align="center" />
            <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
            <el-table-column label="负责人" align="center" prop="manager" width="80" :show-overflow-tooltip="true" />
            <el-table-column label="审核状态" align="center" width="100">
              <template slot-scope="scope">
                <div style="display: flex; align-items: center; justify-content: center;">
                  <span v-if="scope.row.auditStatus == '0'" style="width:8px;height:8px;border-radius:50%;background:#e6a23c;margin-right:5px;"></span>
                  <span v-if="scope.row.auditStatus == '1'" style="width:8px;height:8px;border-radius:50%;background:#67c23a;margin-right:5px;"></span>
                  <span v-if="scope.row.auditStatus == '2'" style="width:8px;height:8px;border-radius:50%;background:#f56c6c;margin-right:5px;"></span>
                  <span v-if="scope.row.auditStatus == '0'">待审核</span>
                  <span v-if="scope.row.auditStatus == '1'">已通过</span>
                  <el-tooltip v-if="scope.row.auditStatus == '2'" :content="scope.row.auditReason || '无理由'" placement="top">
                    <span style="color:#f56c6c; cursor:pointer">已驳回 <i class="el-icon-question"></i></span>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-view" @click.stop="handleView(scope.row)">详情</el-button>

                <span v-if="checkRole(['cameraInstalaion_manager', 'admin', 'teacher']) || (scope.row.createBy == currentUser.userName && scope.row.auditStatus != '1')">
                    <el-button size="mini" type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)">修改</el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" style="color:#F56C6C" @click.stop="handleDelete(scope.row)">删除</el-button>
                </span>

                <el-dropdown size="mini" @command="(cmd) => handleCommand(cmd, scope.row)"
                             v-if="checkRole(['cameraInstalaion_manager', 'admin', 'teacher']) || scope.row.createBy == currentUser.userName">
                  <span class="el-dropdown-link" style="font-size:12px; color:#409EFF; cursor:pointer; margin-left:10px">
                    导入<i class="el-icon-arrow-down"></i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="point">KML点位</el-dropdown-item>
                    <el-dropdown-item command="boundary">KML边界</el-dropdown-item>
                    <el-dropdown-item command="excel" divided>Excel数据</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>

                <el-button v-if="checkRole(['teacher', 'cameraInstalaion_manager', 'admin']) && scope.row.auditStatus == '0'"
                           size="mini" type="text" icon="el-icon-check" style="color:#E6A23C; font-weight:bold"
                           @click.stop="handleAuditOpen(scope.row)">审核
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row v-if="form.auditStatus == '2'">
          <el-col :span="24"><el-alert :title="'被驳回原因：' + form.auditReason" type="error" show-icon :closable="false" style="margin-bottom:15px;"></el-alert></el-col>
        </el-row>
        <el-row>
          <el-col :span="24"><el-form-item label="项目名称" prop="projectName"><el-input v-model="form.projectName" /></el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="12"><el-form-item label="负责人" prop="manager"><el-input v-model="form.manager" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系电话" prop="phone"><el-input v-model="form.phone" /></el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="24"><el-form-item label="安装地点" prop="regionName"><el-input v-model="form.regionName" /></el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="安装时间" prop="installTime">
              <el-date-picker clearable size="small" v-model="form.installTime" type="date" value-format="yyyy-MM-dd" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目状态" prop="status">
              <el-radio-group v-model="form.status"><el-radio label="0">进行中</el-radio><el-radio label="1">已完成</el-radio></el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row style="background: #f8f8f9; padding: 10px; border-radius: 4px; margin-bottom: 20px;">
          <el-col :span="24">
            <el-form-item label="附件上传" style="margin-bottom:0">
              <el-radio-group v-model="uploadMode" size="mini" style="margin-bottom: 10px;">
                <el-radio-button label="none">暂不上传</el-radio-button>
                <el-radio-button label="point">KML点位</el-radio-button>
                <el-radio-button label="boundary">KML边界</el-radio-button>
              </el-radio-group>
              <div v-if="uploadMode !== 'none'">
                <el-upload ref="formUpload" :action="upload.url" :limit="1" :auto-upload="false" :on-change="handleFormFileChange" :on-remove="handleFormFileRemove" :file-list="fileList" accept=".kml,.kmz">
                  <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                  <div slot="tip" class="el-upload__tip">请上传对应的 KML/KMZ 文件</div>
                </el-upload>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注信息" prop="remark"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">{{ uploadMode !== 'none' && fileList.length > 0 ? '提交并上传' : '提 交' }}</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="项目审核" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="项目名称"><el-input v-model="auditForm.projectName" disabled /></el-form-item>
        <el-form-item label="审核结果" prop="auditStatus">
          <el-radio-group v-model="auditForm.auditStatus"><el-radio label="1">通过</el-radio><el-radio label="2">驳回</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditReason" v-if="auditForm.auditStatus == '2'">
          <el-input type="textarea" v-model="auditForm.auditReason" placeholder="请输入驳回原因，必填" :rows="3"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">确认审核</el-button>
        <el-button @click="auditOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="项目详细信息" :visible.sync="detailOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="项目名称">{{ detailForm.projectName }}</el-descriptions-item>
        <el-descriptions-item label="项目状态"><el-tag v-if="detailForm.status == '0'" size="mini">进行中</el-tag><el-tag v-else type="success" size="mini">已完成</el-tag></el-descriptions-item>
        <el-descriptions-item label="负责人">{{ detailForm.manager }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailForm.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="安装地点" :span="2">{{ detailForm.regionName }}</el-descriptions-item>
        <el-descriptions-item label="安装时间">{{ parseTime(detailForm.installTime, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="点位文件" :span="2">
          <span v-if="detailForm.pointFileName"><i class="el-icon-document-checked" style="color:#67C23A"></i> {{ detailForm.pointFileName }}</span><span v-else style="color:#909399">未导入</span>
        </el-descriptions-item>
        <el-descriptions-item label="边界文件" :span="2">
          <span v-if="detailForm.boundaryFileName"><i class="el-icon-map-location" style="color:#409EFF"></i> {{ detailForm.boundaryFileName }}</span><span v-else style="color:#909399">未导入</span>
        </el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag v-if="detailForm.auditStatus=='0'">待审核</el-tag><el-tag v-else-if="detailForm.auditStatus=='1'" type="success">已通过</el-tag><el-tag v-else type="danger">已驳回</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="驳回意见" v-if="detailForm.auditStatus=='2'" :span="2">{{ detailForm.auditReason }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailForm.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer"><el-button @click="detailOpen = false">关 闭</el-button></div>
    </el-dialog>

    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".kml,.xml,.kmz,.xlsx,.xls" :action="upload.url" :headers="upload.headers" :disabled="upload.isUploading"
                 :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="true" :http-request="submitUpload" drag>
        <i class="el-icon-upload"></i><div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
import { listCameraInstall, getCameraInstall, delCameraInstall, addCameraInstall, updateCameraInstall, listCameraPoints, importSpeciesExcel, addManualData, importCameraKml, auditCameraInstall } from "@/api/system/CameraInstall"
import { checkRole } from "@/utils/permission";
import { getUserProfile } from "@/api/system/user";
// 引入更新接口
import { getLibrary, addLibrary, listLibrary, updateLibrary } from "@/api/system/library";


export default {
  name: "CameraInstall",
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API, // 用于图片路径拼接
      selectedSpeciesName: '', // 当前选中的物种名
      speciesInfo: {},         // 后端查回来的详情数据
      libraryForm: {},         // 录入表单数据
      libraryLoading: false,   // 保存按钮loading状态
      isEditing: false,        // 编辑模式状态

      currentUser: {},
      activeTab: 'public', // public=公开, private=我的, audit=待审
      map: null, infoWindow: null, activeLayers: {},
      speciesTableData: [], siteTableData: [],
      searchKeywordSpecies: '', searchKeywordSite: '', filterTab: 'site',
      lastClickedProject: null, manualInput: { siteCode: '', speciesName: '' },
      loading: true, CameraInstallList: [], total: 0, showSearch: true, ids: [], single: true, multiple: true,

      queryParams: { pageNum: 1, pageSize: 10, projectName: null, auditStatus: '1', createBy: null },
      open: false, title: "", detailOpen: false, auditOpen: false,
      upload: { open: false, title: "", isUploading: false, url: "", headers: { Authorization: "Bearer " + getToken() }, installId: null, mode: 'point' },
      form: {}, detailForm: {}, auditForm: {},

      // 新增上传控制
      uploadMode: 'none', fileList: [], submitLoading: false,

      // 【1. 新增颜色库】为了支持分批次上传很多点位，这里准备了20种不同的颜色
      colorPalette: [
        '#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#9C27B0',
        '#00BCD4', '#FF9800', '#795548', '#009688', '#3F51B5',
        '#E91E63', '#9E9E9E', '#607D8B', '#FF5722', '#CDDC39',
        '#8BC34A', '#FFEB3B', '#FFC107', '#2196F3', '#673AB7'
      ],

      rules: {
        projectName: [{ required: true, message: "必填", trigger: "blur" }],
        regionName: [{ required: true, message: "必填", trigger: "blur" }],
        manager: [{ required: true, message: "必填", trigger: "blur" }],
        installTime: [{ required: true, message: "必填", trigger: "change" }]
      },
      nextAction: 'none'
    };
  },
  computed: {
    activeTabName() {
      if(this.activeTab === 'public') return '公开数据(已通过)';
      if(this.activeTab === 'private') return '我的提交';
      if(this.activeTab === 'audit') return '审核管理(待办)';
      return '';
    },
    filteredSpeciesList() {
      if (!this.searchKeywordSpecies) return this.speciesTableData;
      return this.speciesTableData.filter(item => item.name.toLowerCase().includes(this.searchKeywordSpecies.toLowerCase()));
    },
    filteredSiteList() {
      if (!this.searchKeywordSite) return this.siteTableData;
      return this.siteTableData.filter(item => item.name.toLowerCase().includes(this.searchKeywordSite.toLowerCase()));
    }
  },
  created() { this.getCurrentUser(); },
  mounted() {
    this.$nextTick(() => { this.initMap(); });
    setTimeout(() => { this.getList(); }, 300);
  },
  methods: {
    checkRole,
    getCurrentUser() { getUserProfile().then(res => { this.currentUser = res.data.user || res.data; }); },

    // Tab逻辑
    handleTabClick(tab) {
      this.resetQuery();
      if(this.map) this.map.clearMap(); this.activeLayers = {};
      if (this.activeTab === 'public') { this.queryParams.auditStatus = '1'; this.queryParams.createBy = null; }
      else if (this.activeTab === 'private') { this.queryParams.auditStatus = null; this.queryParams.createBy = this.currentUser.userName; }
      else if (this.activeTab === 'audit') { this.queryParams.auditStatus = '0'; this.queryParams.createBy = null; }
      this.getList();
    },
    getList() {
      this.loading = true;
      listCameraInstall(this.queryParams).then(res => {
        this.CameraInstallList = res.rows; this.total = res.total; this.loading = false;
        this.speciesTableData = []; this.siteTableData = [];
      });
    },

    // 弹窗文件变动处理
    handleFormFileChange(file, fileList) { this.fileList = fileList.slice(-1); },
    handleFormFileRemove(file, fileList) { this.fileList = fileList; },

    // 核心提交：先保存信息，拿到ID后再自动传文件
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          // 上传辅助函数
          const doUpload = (installId) => {
            if (this.uploadMode !== 'none' && this.fileList.length > 0) {
              const fd = new FormData();
              fd.append('file', this.fileList[0].raw); fd.append('installId', installId); fd.append('mode', this.uploadMode);
              return importCameraKml(fd).then(() => { this.$modal.msgSuccess("保存并上传成功！"); });
            }
            return Promise.resolve();
          };

          let savePromise = this.form.id != null ? updateCameraInstall(this.form) : addCameraInstall(this.form);

          savePromise.then(response => {
            // 获取目标ID
            let targetId = this.form.id ? this.form.id : (response.data ? response.data : null);
            if (targetId) {
              doUpload(targetId).then(() => {
                this.open = false; this.getList(); this.submitLoading = false;
                if (this.uploadMode === 'none') this.$modal.msgSuccess("保存成功");
              }).catch(() => {
                this.submitLoading = false; this.$modal.msgError("信息保存成功，但上传失败，请在列表重试"); this.open = false; this.getList();
              });
            } else {
              this.$modal.msgSuccess("保存成功 (未自动上传，请在列表页补充文件)");
              this.open = false; this.getList(); this.submitLoading = false;
            }
          }).catch(() => { this.submitLoading = false; });
        }
      });
    },

    // 审核逻辑
    handleAuditOpen(row) { this.auditForm = { id: row.id, projectName: row.projectName, auditStatus: '1', auditReason: '' }; this.auditOpen = true; },
    submitAudit() {
      if (this.auditForm.auditStatus === '2' && !this.auditForm.auditReason) { this.$modal.msgError("驳回时必须填写审核意见"); return; }
      auditCameraInstall(this.auditForm).then(res => { this.$modal.msgSuccess("审核完成"); this.auditOpen = false; this.getList(); });
    },

    // 地图与图层
    initMap() {
      if (typeof AMap === 'undefined') { setTimeout(() => { this.initMap(); }, 500); return; }

      this.map = new AMap.Map('camera-map-container', {
        zoom: 10,
        center: [108.254533, 28.856142],
        resizeEnable: true,
        // 【核心修改】添加 layers 配置项
        layers: [
          new AMap.TileLayer.Satellite(), // 1. 加载卫星底图
          new AMap.TileLayer.RoadNet()    // 2. 加载路网图层 (如果不加这个，就只有纯照片，看不到地名和路，建议加上)
        ]
      });

      // 这里的插件保持不变，或者你可以加一个 MapType 插件让用户自己切换
      AMap.plugin(['AMap.Scale', 'AMap.ToolBar', 'AMap.MapType'], () => {
        this.map.addControl(new AMap.Scale());
        this.map.addControl(new AMap.ToolBar());

        // 【可选】如果你想让用户能自己在右上角点击切换“标准”和“卫星”，可以加上这个控件
        this.map.addControl(new AMap.MapType({
          defaultType: 1 // 0代表标准，1代表卫星。这里设为1确保默认选中卫星
        }));
      });

      this.infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(0, -30)});
    },
    handleProjectSelectionChange(selection) {
      this.ids = selection.map(item => item.id); this.single = selection.length !== 1; this.multiple = !selection.length;
      const currentIds = selection.map(r => r.id);
      Object.keys(this.activeLayers).forEach(idStr => { if (!currentIds.includes(Number(idStr))) this.removeProjectLayer(Number(idStr)); });
      selection.forEach(row => { if (!this.activeLayers[row.id]) this.addProjectLayer(row); });
      if (selection.length === 0) { this.updateAggregatedData(); if(this.map) this.map.clearMap(); }
    },

    addProjectLayer(row) {
      const layerData = { polygon: null, markers: [], pointsData: [] };
      this.$set(this.activeLayers, row.id, layerData);

      // 【2. 分配颜色】根据项目ID计算一个固定颜色，ID不同颜色大概率不同，且循环使用
      const colorIndex = row.id % this.colorPalette.length;
      const projectColor = this.colorPalette[colorIndex];

      if (row.boundaryJson) {
        try {
          const path = JSON.parse(row.boundaryJson);
          // 边界也使用项目对应的颜色，便于区分
          const polygon = new AMap.Polygon({ path: path, strokeColor: projectColor, strokeWeight: 3, fillOpacity: 0.1, zIndex: 10 });
          this.map.add(polygon); layerData.polygon = polygon; this.map.setFitView([polygon]);
        } catch (e) {}
      }
      listCameraPoints({ installId: row.id, pageSize: 10000 }).then(res => {
        if (!this.activeLayers[row.id]) return;
        layerData.pointsData = res.rows;
        const newMarkers = [];
        res.rows.forEach(p => {
          // 传入计算好的颜色
          newMarkers.push(this.createMarker(p, projectColor));
        });
        this.map.add(newMarkers); layerData.markers = newMarkers; this.updateAggregatedData();
      });
    },

    removeProjectLayer(id) {
      const layer = this.activeLayers[id]; if (!layer) return;
      if (layer.polygon) try { this.map.remove(layer.polygon); } catch(e){}
      if (layer.markers && layer.markers.length > 0) this.map.remove(layer.markers);
      this.$delete(this.activeLayers, id); this.updateAggregatedData();
    },

    // 【修改】点击左下角物种列表某一行
    handleSpeciesRowClick(row) {
      const name = row.name;
      this.selectedSpeciesName = name;
      this.speciesInfo = {};
      this.isEditing = false; // 每次点击重置编辑状态
      this.libraryForm = { speciesName: name };

      listLibrary({ speciesName: name }).then(res => {
        if (res.rows && res.rows.length > 0) {
          this.speciesInfo = res.rows[0];
        } else {
          // 无数据，自动显示表单
        }
      });
    },

    // 【新增】进入编辑模式
    handleEditLibrary() {
      this.libraryForm = JSON.parse(JSON.stringify(this.speciesInfo));
      this.isEditing = true;
    },

    // 【修改】提交资料 (含新增和修改)
    submitLibrary() {
      this.libraryLoading = true;

      let promise;
      if (this.libraryForm.id) {
        promise = updateLibrary(this.libraryForm);
      } else {
        promise = addLibrary(this.libraryForm);
      }

      promise.then(res => {
        this.$modal.msgSuccess("资料保存成功！");
        this.libraryLoading = false;
        this.isEditing = false; // 退出编辑模式

        // 更新界面显示
        this.speciesInfo = JSON.parse(JSON.stringify(this.libraryForm));

        // 确保拿到最新数据（含ID）
        if(!this.speciesInfo.id) {
          listLibrary({ speciesName: this.libraryForm.speciesName }).then(r => {
            if(r.rows && r.rows.length > 0) this.speciesInfo = r.rows[0];
          });
        }
      }).catch(() => {
        this.libraryLoading = false;
      });
    },

    // 关闭右侧面板
    closeRightPanel() {
      this.selectedSpeciesName = '';
      this.speciesInfo = {};
      this.isEditing = false;
    },

    updateAggregatedData() {
      const uniqueSitesMap = {}; const uniqueSpeciesMap = {};
      Object.values(this.activeLayers).forEach(layer => {
        if (!layer.pointsData) return;
        layer.pointsData.forEach(p => {
          const code = p.siteCode;
          if (!uniqueSitesMap[code]) uniqueSitesMap[code] = { name: code, speciesSet: new Set() };
          if (p.speciesNames) { p.speciesNames.split(',').forEach(name => { const clean = name.trim(); if (clean) { uniqueSitesMap[code].speciesSet.add(clean); uniqueSpeciesMap[clean] = (uniqueSpeciesMap[clean] || 0) + 1; } }); }
        });
      });
      this.siteTableData = Object.values(uniqueSitesMap).map(item => ({ name: item.name, count: item.speciesSet.size, speciesStr: Array.from(item.speciesSet).join(', ') })).sort((a,b)=>a.name.localeCompare(b.name));
      this.speciesTableData = Object.keys(uniqueSpeciesMap).map(k => ({ name: k, count: uniqueSpeciesMap[k] })).sort((a,b)=>b.count - a.count);
      this.applyLeftFilter();
    },

    // 【3. 修改Marker逻辑】支持传入颜色，支持悬停显示
    createMarker(p, color) {
      const hasData = p.speciesNames && p.speciesNames.length > 0;
      // 样式优化：使用传入的 color 作为背景色。如果有数据，加黑色边框，无数据加白色边框
      const borderColor = hasData ? '#000000' : 'white';

      const content = `<div style="
        width:12px; height:12px;
        background:${color};
        border-radius:50%;
        border:2px solid ${borderColor};
        box-shadow: 0 0 3px rgba(0,0,0,0.3);
      "></div>`;

      const marker = new AMap.Marker({
        position: [p.longitude, p.latitude],
        content: content,
        offset: new AMap.Pixel(-6, -6),
        extData: { species: p.speciesNames?p.speciesNames.split(','):[], siteCode: p.siteCode }
      });

      // 改为 mouseover (悬停)
      marker.on('mouseover', () => {
        this.infoWindow.setContent(`
          <div style="padding:5px; min-width:100px;">
            <b>点位: ${p.siteCode}</b><br/>
            <span style="font-size:12px; color:#666">物种: ${p.speciesNames||'暂无数据'}</span>
          </div>
        `);
        this.infoWindow.open(this.map, marker.getPosition());
      });

      // 改为 mouseout (移出关闭)
      marker.on('mouseout', () => {
        this.infoWindow.close();
      });

      return marker;
    },

    applyLeftFilter() {
      const sSpec = this.$refs.speciesTable?this.$refs.speciesTable.selection.map(i=>i.name):[];
      const sSite = this.$refs.siteTable?this.$refs.siteTable.selection.map(i=>i.name):[];
      const isFil = sSpec.length>0 || sSite.length>0;
      const visible = [];
      Object.values(this.activeLayers).forEach(l => {
        if(!l.markers) return;
        l.markers.forEach(m => {
          const ext = m.getExtData();
          let show = true;
          if(isFil) {
            let mSpec = sSpec.length>0 ? ext.species.some(s=>sSpec.includes(s.trim())) : true;
            let mSite = sSite.length>0 ? sSite.includes(ext.siteCode) : true;
            show = (sSpec.length>0 && sSite.length===0) ? mSpec : (sSpec.length===0 && sSite.length>0) ? mSite : (mSpec && mSite);
          }
          if(show){ m.show(); visible.push(m); } else m.hide();
        });
      });
      if(isFil && visible.length>0) this.map.setFitView(visible);
    },
    handleLeftFilterChange() { this.applyLeftFilter(); },
    clearLeftSelection() { if(this.$refs.speciesTable)this.$refs.speciesTable.clearSelection(); if(this.$refs.siteTable)this.$refs.siteTable.clearSelection(); this.searchKeywordSpecies=''; this.searchKeywordSite=''; this.applyLeftFilter(); },

    // 常规操作
    handleAdd(step) { this.reset(); this.open = true; this.nextAction = step; this.title = "新建项目 (提交后需审核)"; if(step === 'point') this.uploadMode='point'; else if(step==='boundary') this.uploadMode='boundary'; },

    // 【保留之前的文件回显修复】
    handleUpdate(row) {
      this.reset();
      getCameraInstall(row.id).then(res => {
        this.form = res.data;

        // 回显逻辑
        this.fileList = [];
        this.uploadMode = 'none';

        if (this.form.boundaryFileName) {
          this.uploadMode = 'boundary';
          this.fileList = [{
            name: this.form.boundaryFileName,
            url: this.form.boundaryFile
          }];
        }
        else if (this.form.pointFileName) {
          this.uploadMode = 'point';
          this.fileList = [{
            name: this.form.pointFileName,
            url: this.form.pointFile
          }];
        }

        this.open = true;
        this.title = "修改项目";
        this.nextAction='none';
      });
    },

    handleDelete(row) { const ids = row.id || this.ids; this.$modal.confirm('确认删除？').then(() => { return delCameraInstall(ids); }).then(() => { this.getList(); this.removeProjectLayer(ids); this.$modal.msgSuccess("删除成功"); }); },
    handleView(row) { this.detailForm = row; this.detailOpen = true; },
    handleCommand(cmd, row) { if (cmd === 'excel') this.handleImportExcel(row); else this.handleImport(row, cmd); },
    handleImport(row, mode) { this.upload.installId = row.id; this.upload.mode = mode; this.upload.open = true; this.upload.title = "上传文件"; },
    handleImportExcel(row) { this.upload.installId = row.id; this.upload.mode = 'excel'; this.upload.open = true; this.upload.title = "上传数据"; },
    submitUpload(param) {
      const fd = new FormData(); fd.append('file', param.file); fd.append('installId', this.upload.installId);
      let api = this.upload.mode === 'excel' ? importSpeciesExcel : importCameraKml;
      if(this.upload.mode !== 'excel') fd.append('mode', this.upload.mode);
      api(fd).then(res => { this.$modal.msgSuccess(res.msg); this.upload.open = false;
        this.getList().then(()=>{ const row = this.CameraInstallList.find(r => r.id === this.upload.installId); if(row && this.activeLayers[row.id]) { this.removeProjectLayer(row.id); this.addProjectLayer(row); } });
      }).catch(()=>{ this.$modal.msgError("导入失败"); this.upload.open = false; });
    },
    handleFileUploadProgress() { this.upload.isUploading = true; },
    handleFileSuccess() { this.upload.open = false; this.upload.isUploading = false; this.$refs.upload.clearFiles(); },
    handleProjectRowClick(row) { this.lastClickedProject = row; this.$refs.projectTable.toggleRowSelection(row); },
    handleManualAdd() { if (!this.lastClickedProject) return; addManualData({ installId: this.lastClickedProject.id, siteCode: this.manualInput.siteCode, speciesName: this.manualInput.speciesName }).then(() => { this.$modal.msgSuccess("添加成功"); this.manualInput.siteCode = ''; this.manualInput.speciesName = ''; if (this.activeLayers[this.lastClickedProject.id]) { this.removeProjectLayer(this.lastClickedProject.id); this.addProjectLayer(this.lastClickedProject); } }); },
    reset() { this.form = { id:null, projectName:null, regionName:null, manager:null, phone:null, installTime:null, status:"0", remark:null }; this.uploadMode = 'none'; this.fileList = []; this.resetForm("form"); },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery(); },
    handleQuery() { this.queryParams.pageNum = 1; this.getList(); },
    cancel() { this.open = false; this.reset(); }
  }
};
</script>
