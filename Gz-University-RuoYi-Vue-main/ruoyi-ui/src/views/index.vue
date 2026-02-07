<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover" class="welcome-card">
          <div class="welcome-header">
            <div class="welcome-text">
              <h2 class="title">欢迎使用西米教授信息管理系统，祝您开心每一天！</h2>
              <p class="subtitle">当前版本：v{{ version }} | 致力于高效的科研团队协作与学术成果管理</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :md="12" :lg="16">

        <el-card shadow="hover" class="box-card" style="margin-bottom: 20px;">
          <div slot="header" class="clearfix">
            <span><i class="el-icon-office-building"></i> 关于团队</span>
          </div>
          <div class="about-box">
            <div class="about-content">
              <h4><i class="el-icon-collection-tag"></i> 研究方向</h4>
              <p>
                本团队主要致力于生物多样性保护、自然生态监测以及相关信息系统的开发与应用。
                通过跨学科的合作，我们将传统的生态学研究与现代信息技术相结合，探索高效的科研管理模式。
              </p>
            </div>
            <el-divider></el-divider>
            <div class="about-footer">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="info-item">
                    <i class="el-icon-location-information"></i>
                    <div class="text">
                      <div class="label">实验室地点</div>
                      <div class="value">崇理楼 C-405</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="info-item">
                    <i class="el-icon-message"></i>
                    <div class="text">
                      <div class="label">联系邮箱</div>
                      <div class="value">contact@ximilab.com</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="info-item">
                    <i class="el-icon-user"></i>
                    <div class="text">
                      <div class="label">负责人</div>
                      <div class="value">西米教授</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>

        <el-card shadow="hover" class="box-card">
          <div slot="header" class="clearfix">
            <span><i class="el-icon-cpu"></i> 技术架构</span>
          </div>
          <el-row>
            <el-col :span="12">
              <h4>后端技术栈</h4>
              <ul class="tech-list">
                <li>SpringBoot 2.x</li>
                <li>Spring Security (安全框架)</li>
                <li>JWT (无状态认证)</li>
                <li>MyBatis (ORM框架)</li>
                <li>Druid (数据库连接池)</li>
              </ul>
            </el-col>
            <el-col :span="12">
              <h4>前端技术栈</h4>
              <ul class="tech-list">
                <li>Vue 2.x</li>
                <li>Vuex (状态管理)</li>
                <li>Element-UI (UI组件库)</li>
                <li>Axios (网络请求)</li>
                <li>Sass (CSS预处理器)</li>
              </ul>
            </el-col>
          </el-row>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card shadow="hover" class="box-card">
          <div slot="header" class="clearfix">
            <span><i class="el-icon-date"></i> 更新日志</span>
            <el-tag size="mini" type="danger" style="float: right; cursor: pointer" effect="dark">New</el-tag>
          </div>
          <div class="block">
            <el-timeline>
              <el-timeline-item timestamp="2026/01/15" placement="top" color="#0bbd87">
                <el-card class="update-card" shadow="never">
                  <h4>v3.9.0 版本发布</h4>
                  <p>1. 优化登录页面背景与Logo显示</p>
                  <p>2. 修复参数配置背景图失效问题</p>
                  <p>3. 首页布局全新改版，移除冗余入口</p>
                </el-card>
              </el-timeline-item>
              <el-timeline-item timestamp="2025/12/28" placement="top">
                <el-card class="update-card" shadow="never">
                  <h4>v3.8.5 版本更新</h4>
                  <p>1. 新增学术论文投稿模块</p>
                  <p>2. 优化数据权限控制逻辑</p>
                </el-card>
              </el-timeline-item>
              <el-timeline-item timestamp="2025/11/20" placement="top">
                <el-card class="update-card" shadow="never">
                  <h4>v3.8.2 版本更新</h4>
                  <p>1. 修复大组会安排上传文件Bug</p>
                  <p>2. 优化移动端适配效果</p>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="footer">
      <p>Copyright © 2026 西米教授信息管理系统 All Rights Reserved.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      version: "3.9.0"
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);

  /* 顶部欢迎卡片样式 */
  .welcome-card {
    border: none;
    .welcome-header {
      display: flex;
      align-items: center;
      .avatar {
        width: 60px;
        height: 60px;
        margin-right: 20px;
        /* 防止图片被挤压 */
        flex-shrink: 0;
        img {
          width: 100%;
          height: 100%;
          border-radius: 50%;
          border: 2px solid #f2f2f2;
        }
      }
      .welcome-text {
        .title {
          font-size: 20px;
          color: #303133;
          margin-bottom: 8px;
          font-weight: 500;
        }
        .subtitle {
          font-size: 14px;
          color: #909399;
          margin: 0;
        }
      }
    }
  }

  /* 通用卡片样式 */
  .box-card {
    border: none;
    margin-bottom: 20px;

    ::v-deep .el-card__header {
      padding: 15px 20px;
      border-bottom: 1px solid #f0f0f0;
      span {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        display: flex;
        align-items: center;
        i {
          margin-right: 8px;
          font-size: 18px;
          color: #409EFF; /* 图标统一用主题色 */
        }
      }
    }
  }

  /* 🚀 新增：关于团队部分的样式 */
  .about-box {
    padding: 10px 0;
    .about-content {
      h4 {
        margin: 0 0 10px 0;
        font-size: 16px;
        color: #303133;
        i { color: #409EFF; margin-right: 5px; }
      }
      p {
        font-size: 14px;
        color: #606266;
        line-height: 26px;
        text-indent: 2em; /* 首行缩进 */
        margin: 0;
      }
    }

    .info-item {
      display: flex;
      align-items: center;
      margin-top: 5px;
      i {
        font-size: 32px;
        color: #e6e6e6; /* 浅色图标背景感 */
        margin-right: 15px;
      }
      .text {
        .label {
          font-size: 12px;
          color: #909399;
          margin-bottom: 4px;
        }
        .value {
          font-size: 14px;
          color: #303133;
          font-weight: 500;
        }
      }
    }
  }

  /* 技术列表样式 */
  .tech-list {
    padding-left: 15px;
    li {
      line-height: 30px;
      color: #606266;
      font-size: 14px;
      list-style-type: circle; /* 圆点列表 */
    }
  }

  /* 更新日志卡片 */
  .update-card {
    background-color: #fcfcfc;
    border: 1px solid #ebeef5;
    h4 {
      margin: 0 0 10px;
      font-size: 15px;
      font-weight: 600;
      color: #303133;
    }
    p {
      margin: 5px 0;
      font-size: 13px;
      color: #606266;
      line-height: 1.6;
    }
  }

  .footer {
    text-align: center;
    color: #c0c4cc;
    font-size: 12px;
    margin-top: 20px;
    padding-bottom: 20px;
  }
}

// 适配移动端
@media (max-width: 768px) {
  .welcome-header {
    flex-direction: column;
    text-align: center;
    .avatar {
      margin-right: 0 !important;
      margin-bottom: 10px;
    }
  }
  .info-item {
    margin-bottom: 20px;
    justify-content: center;
  }
}
</style>
