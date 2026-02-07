<template>
  <div class="login" :style="pageStyle">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">{{ title }}</h3>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          auto-complete="off"
          placeholder="账号"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img" />
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
    <div class="el-login-footer">
      <a
        href="http://beian.miit.gov.cn/"
        target="_blank"
        style="color: #fff; text-decoration: none;"
      >
        黔ICP备2025045270号
      </a>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt';
import { getConfigKey } from "@/api/system/config";

export default {
  name: "Login",
  data() {
    // 1. 预加载默认背景
    const defaultBg = require('@/assets/images/login-background.png');

    return {
      title: process.env.VUE_APP_TITLE || '信息管理系统',
      codeUrl: "",
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true,
      register: true,
      redirect: undefined,
      defaultBackground: defaultBg,

      // 2. 🚀 关键修改：直接在初始化时就设置好默认背景，不再等待接口
      // 这样页面打开瞬间就有背景，不会白屏等待
      pageStyle: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100%',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        backgroundImage: `url("${defaultBg}")`
      }
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    // 接口异步调用，不阻塞页面渲染
    this.getSystemConfig();
    this.getCode();
    this.getCookie();
  },
  methods: {
    // 更新背景样式
    updatePageStyle(backgroundUrl) {
      this.$set(this.pageStyle, 'backgroundImage', `url("${backgroundUrl}")`);
    },

    async getSystemConfig() {
      // 获取标题 (异步执行，不卡顿)
      getConfigKey('sys.login.title').then(res => {
        if (res && res.code === 200 && res.msg) {
          this.title = res.msg.trim();
          document.title = this.title;
        }
      }).catch(() => {});

      // 获取背景图
      getConfigKey('sys.login.background_image').then(res => {
        if (res && res.code === 200 && res.msg) {
          this.processBackgroundResponse(res.msg);
        }
      }).catch(() => {
        // 获取失败也没关系，反正 data() 里已经设置了默认值，用户无感知
      });
    },

    processBackgroundResponse(bgValue) {
      if (!bgValue) return; // 空值直接结束，保持默认

      const val = bgValue.trim();
      // 🚀 屏蔽词检测：如果是这些词，直接结束，保持默认背景，速度最快
      const ignoreKeywords = ['无', '默认', 'none', 'null', 'undefined', ''];

      if (ignoreKeywords.includes(val.toLowerCase())) {
        return; // 直接退出，页面依然显示 data() 里初始化的默认背景
      }

      // 如果有有效值，才去替换
      const imageUrl = this.formatImageUrl(val);
      this.updatePageStyle(imageUrl);
    },

    formatImageUrl(url) {
      if (!url) return '';
      let trimmedUrl = url.trim();

      if (trimmedUrl.startsWith('http://') || trimmedUrl.startsWith('https://')) {
        return encodeURI(trimmedUrl);
      }

      if (trimmedUrl.startsWith('/')) {
        const timestamp = new Date().getTime();
        const fullUrl = process.env.VUE_APP_BASE_API + trimmedUrl;
        return encodeURI(fullUrl) + '?t=' + timestamp;
      }

      return trimmedUrl;
    },

    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          if (res.img.startsWith('data:image')) {
            this.codeUrl = res.img;
          } else {
            this.codeUrl = "data:image/gif;base64," + res.img;
          }
          this.loginForm.uuid = res.uuid;
        }
      }).catch(() => {});
    },

    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe');
      if (username !== undefined) {
        this.loginForm.username = username;
      }
      if (password !== undefined) {
        this.loginForm.password = decrypt(password);
      }
      if (rememberMe !== undefined) {
        this.loginForm.rememberMe = Boolean(rememberMe);
      }
    },

    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{});
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #333;
  font-weight: 600;
}
.login-form {
  border-radius: 6px;
  background: rgba(255, 255, 255, 0.11) !important;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 400px;
  padding: 25px 25px 5px 25px;
  z-index: 1;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}
</style>
