<template>
  <div class="app-container custom-container">
    <el-card class="box-card tab-card" shadow="never">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="全部内容" name="ALL">
          <span slot="label"><i class="el-icon-menu"></i> 全部内容</span>
        </el-tab-pane>
        <el-tab-pane label="公众号宣传" name="1">
          <span slot="label"><i class="el-icon-chat-dot-round"></i> 公众号</span>
        </el-tab-pane>
        <el-tab-pane label="视频号宣传" name="2">
          <span slot="label"><i class="el-icon-video-camera"></i> 视频号</span>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-card class="box-card search-card" shadow="hover">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="70px">
        <el-form-item label="题目" prop="title">
          <el-input
            v-model="queryParams.title"
            placeholder="搜索发布题目..."
            clearable
            prefix-icon="el-icon-search"
            @keyup.enter.native="handleQuery"
            class="search-input"
          />
        </el-form-item>
        <el-form-item label="日期" prop="publishDate">
          <el-date-picker
            v-model="queryParams.publishDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择发布日期"
            prefix-icon="el-icon-date"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="box-card table-card" shadow="hover">
      <div slot="header" class="clearfix" v-if="checkRole(['mediaPublish_manager', 'admin'])">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:publish:add']">新增发布</el-button>
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:publish:export']">导出数据</el-button>
        <div class="right-toolbar-wrapper">
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="publishList"
        @selection-change="handleSelectionChange"
        :header-cell-style="{background:'#f8f8f9', color:'#515a6e', fontWeight:'bold'}"
        stripe
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" align="center" width="50">
          <template slot-scope="scope">
            <span class="index-badge">{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
          </template>
        </el-table-column>

        <el-table-column label="类型" align="center" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.publishType == '1'" type="success" effect="light"><i class="el-icon-chat-dot-round"></i> 公众号</el-tag>
            <el-tag v-else type="warning" effect="light"><i class="el-icon-video-camera"></i> 视频号</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="发布题目" align="left" prop="title" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <span class="title-text">{{ scope.row.title }}</span>
          </template>
        </el-table-column>

        <el-table-column label="发布日期" align="center" prop="publishDate" width="120">
          <template slot-scope="scope">
            <i class="el-icon-time" style="color: #909399; margin-right: 4px;"></i>
            <span>{{ parseTime(scope.row.publishDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>

        <template v-if="activeTab === 'ALL' || activeTab === '1'">
          <el-table-column label="链接" align="center" width="80">
            <template slot-scope="scope">
              <el-tooltip content="点击跳转原文" placement="top" v-if="scope.row.linkUrl">
                <el-link type="primary" :href="scope.row.linkUrl" target="_blank" :underline="false">
                  <i class="el-icon-link" style="font-size: 18px;"></i>
                </el-link>
              </el-tooltip>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>
          <el-table-column label="撰稿人" align="center" prop="author" width="90">
            <template slot-scope="scope">
              <el-tag size="mini" type="info" v-if="scope.row.author">{{ scope.row.author }}</el-tag>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="审核流程" align="left" width="220" show-overflow-tooltip>
            <template slot-scope="scope">
              <div v-if="scope.row.publishType == '1'" class="audit-steps">
                <span title="一审"><i class="el-icon-user"></i> {{scope.row.reviewerFirst}}</span>
                <i class="el-icon-arrow-right step-arrow"></i>
                <span title="二审" :class="{'text-muted': !scope.row.reviewerSecond}">{{scope.row.reviewerSecond || '无'}}</span>
                <i class="el-icon-arrow-right step-arrow"></i>
                <span title="终审" class="final-step">{{scope.row.reviewerFinal}}</span>
              </div>
              <span v-else>-</span>
            </template>
          </el-table-column>
        </template>

        <template v-if="activeTab === 'ALL' || activeTab === '2'">
          <el-table-column label="剪辑审核" align="center" width="180">
            <template slot-scope="scope">
              <div v-if="scope.row.publishType == '2'" class="video-team">
                <div><span class="label">剪辑:</span> {{ scope.row.videoEditor }}</div>
                <div><span class="label">审查:</span> {{ scope.row.videoReviewer }}</div>
              </div>
              <span v-else>-</span>
            </template>
          </el-table-column>
        </template>

        <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip/>

        <el-table-column label="操作" align="center" width="150" fixed="right" v-if="checkRole(['mediaPublish_manager', 'admin'])">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:publish:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" class="text-danger" @click="handleDelete(scope.row)" v-hasPermi="['system:publish:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </div>
    </el-card>

    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body custom-class="beautiful-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="dialog-form">

        <el-form-item label="发布类型" prop="publishType">
          <el-radio-group v-model="form.publishType" @change="handleTypeChange" size="medium">
            <el-radio-button label="1"><i class="el-icon-chat-dot-round"></i> 公众号</el-radio-button>
            <el-radio-button label="2"><i class="el-icon-video-camera"></i> 视频号</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="发布题目" prop="title">
              <el-input v-model="form.title" placeholder="请输入吸引人的标题..." prefix-icon="el-icon-edit-outline"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发布日期" prop="publishDate">
              <el-date-picker style="width: 100%" v-model="form.publishDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"/>
            </el-form-item>
          </el-col>
        </el-row>

        <div v-if="form.publishType == '1'" class="sub-form-area wechat-area">
          <div class="area-title"><i class="el-icon-document-copy"></i> 文稿信息</div>
          <el-form-item label="文章链接" prop="linkUrl">
            <el-input v-model="form.linkUrl" placeholder="https://mp.weixin.qq.com/..." prefix-icon="el-icon-link"/>
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="撰稿人" prop="author">
                <el-input v-model="form.author" placeholder="姓名"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="一审人" prop="reviewerFirst">
                <el-input v-model="form.reviewerFirst" placeholder="文稿审查"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="二审人" prop="reviewerSecond">
                <el-input v-model="form.reviewerSecond" placeholder="非必填"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="终审人" prop="reviewerFinal">
                <el-input v-model="form.reviewerFinal" placeholder="终审负责人"/>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div v-if="form.publishType == '2'" class="sub-form-area video-area">
          <div class="area-title"><i class="el-icon-film"></i> 剪辑审核</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="剪辑人" prop="videoEditor">
                <el-input v-model="form.videoEditor" placeholder="剪辑制作"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="审查人" prop="videoReviewer">
                <el-input v-model="form.videoReviewer" placeholder="内容审核"/>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="填写额外说明..."/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button plain @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 引用路径保持正确
import { listPublish, getPublish, delPublish, addPublish, updatePublish } from "@/api/system/publish"
import { checkRole } from "@/utils/permission"

export default {
  name: "Media",
  dicts: ['sys_media_type'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      publishList: [],
      title: "",
      open: false,
      activeTab: 'ALL',
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        publishType: null,
        title: null,
        publishDate: null,
      },
      form: {},
      rules: {
        publishType: [{ required: true, message: "请选择发布类型", trigger: "change" }],
        title: [{ required: true, message: "题目不能为空", trigger: "blur" }],
        publishDate: [{ required: true, message: "日期不能为空", trigger: "blur" }],
        author: [{ required: true, message: "撰稿人不能为空", trigger: "blur" }],
        reviewerFirst: [{ required: true, message: "一审人不能为空", trigger: "blur" }],
        reviewerFinal: [{ required: true, message: "终审人不能为空", trigger: "blur" }],
        videoEditor: [{ required: true, message: "剪辑人不能为空", trigger: "blur" }],
        videoReviewer: [{ required: true, message: "审查人不能为空", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    checkRole,
    getList() {
      this.loading = true;
      if (this.activeTab === 'ALL') {
        this.queryParams.publishType = null;
      } else {
        this.queryParams.publishType = this.activeTab;
      }
      listPublish(this.queryParams).then(response => {
        this.publishList = response.rows;
        this.total = response.total;
        this.loading = false;
      })
    },
    handleTabClick(tab) {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    handleTypeChange(val) {
      if (val === '1') {
        this.form.videoEditor = null;
        this.form.videoReviewer = null;
      } else {
        this.form.linkUrl = null;
        this.form.author = null;
        this.form.reviewerFirst = null;
        this.form.reviewerSecond = null;
        this.form.reviewerFinal = null;
      }
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        publishType: '1',
        title: null,
        publishDate: null,
        linkUrl: null,
        author: null,
        reviewerFirst: null,
        reviewerSecond: null,
        reviewerFinal: null,
        videoEditor: null,
        videoReviewer: null,
        remark: null
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增发布内容";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getPublish(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改发布内容";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePublish(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPublish(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('确认删除该条记录？').then(function() {
        return delPublish(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('system/publish/export', {
        ...this.queryParams
      }, `publish_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped lang="scss">
.custom-container {
  background-color: #f5f7f9; /* 浅灰背景，更有质感 */
  min-height: calc(100vh - 84px);
  padding: 20px;
}

.box-card {
  margin-bottom: 15px;
  border-radius: 8px; /* 圆角更大 */
  border: none; /* 去掉默认边框，靠阴影 */
}

/* Tab 卡片微调 */
.tab-card {
  ::v-deep .el-card__body {
    padding: 10px 20px 0 20px;
  }
  ::v-deep .el-tabs__nav-wrap::after {
    height: 1px;
    background-color: #e4e7ed;
  }
}

/* 搜索栏美化 */
.search-card {
  .el-form-item {
    margin-bottom: 0; /* 紧凑一点 */
  }
  .search-input {
    width: 220px;
  }
}

/* 表格卡片美化 */
.table-card {
  .right-toolbar-wrapper {
    float: right;
  }
  .title-text {
    font-weight: 500;
    color: #303133;
  }
  .text-muted {
    color: #c0c4cc;
  }
  .text-danger {
    color: #F56C6C;
  }
}

/* 序号徽章 */
.index-badge {
  background-color: #f0f2f5;
  color: #909399;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
}

/* 审核流程步骤条样式 */
.audit-steps {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #606266;

  .step-arrow {
    margin: 0 5px;
    color: #C0C4CC;
    font-size: 12px;
  }
  .final-step {
    color: #409EFF; /* 终审人高亮 */
    font-weight: bold;
  }
}

/* 视频团队样式 */
.video-team {
  text-align: left;
  font-size: 12px;
  line-height: 1.5;
  .label {
    color: #909399;
    display: inline-block;
    width: 36px;
    text-align: right;
    margin-right: 4px;
  }
}

/* 弹窗内部样式 */
.sub-form-area {
  background-color: #f8f8f8;
  padding: 15px 15px 5px 15px;
  border-radius: 6px;
  margin-bottom: 18px;
  border: 1px dashed #dcdfe6;

  .area-title {
    font-size: 14px;
    font-weight: bold;
    color: #606266;
    margin-bottom: 15px;
    padding-left: 5px;
    border-left: 3px solid #409EFF;
    line-height: 1;
  }
}

.wechat-area {
  border-color: #e1f3d8; /* 绿色系微调 */
  background-color: #f0f9eb;
  .area-title { border-left-color: #67C23A; }
}

.video-area {
  border-color: #faecd8; /* 橙色系微调 */
  background-color: #fdf6ec;
  .area-title { border-left-color: #E6A23C; }
}
</style>
