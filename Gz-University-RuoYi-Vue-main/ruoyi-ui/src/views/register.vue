<template>
  <div class="register">
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">团队成员注册申请</h3>

      <div class="form-section">账号信息</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="username">
            <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="用户账号">
              <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="password">
            <el-input v-model="registerForm.password" type="password" auto-complete="off" placeholder="密码">
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="confirmPassword">
            <el-input v-model="registerForm.confirmPassword" type="password" auto-complete="off" placeholder="确认密码">
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="form-section">个人信息</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="nickName">
            <el-input v-model="registerForm.nickName" placeholder="真实姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="sex">
            <el-select v-model="registerForm.sex" placeholder="性别" style="width: 100%">
              <el-option
                v-for="dict in dict.type.sys_user_sex"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="ethnicity">
            <el-input v-model="registerForm.ethnicity" placeholder="民族" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="phonenumber">
            <el-input v-model="registerForm.phonenumber" placeholder="手机号码" maxlength="11" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="email">
            <el-input v-model="registerForm.email" placeholder="邮箱" maxlength="50" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="address">
            <el-input v-model="registerForm.address" placeholder="家庭住址" />
          </el-form-item>
        </el-col>
      </el-row>

      <div class="form-section">学籍信息</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="studentId">
            <el-input v-model="registerForm.studentId" placeholder="学号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="enrollmentYear">
            <el-date-picker
              v-model="registerForm.enrollmentYear"
              type="year"
              value-format="yyyy"
              placeholder="入学年份"
              style="width: 100%"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="degreeType">
            <el-select v-model="registerForm.degreeType" placeholder="学位类别" style="width: 100%">
              <el-option
                v-for="dict in dict.type.xuewei"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="major">
            <el-select v-model="registerForm.major" placeholder="专业" style="width: 100%">
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
          <el-form-item prop="currentStatus">
            <el-select v-model="registerForm.currentStatus" placeholder="当前状态" style="width: 100%">
              <el-option
                v-for="dict in dict.type.current_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="undergradUniv">
            <el-input v-model="registerForm.undergradUniv" placeholder="本科毕业院校" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="masterUniv">
            <el-input v-model="registerForm.masterUniv" placeholder="硕士毕业院校(非必填)" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="employer">
            <el-input v-model="registerForm.employer" placeholder="就业单位(非必填)" />
          </el-form-item>
        </el-col>
      </el-row>

      <div class="form-section">紧急联系人(选填)</div>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item prop="emergencyContact">
            <el-input v-model="registerForm.emergencyContact" placeholder="联系人姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="emergencyRelation">
            <el-input v-model="registerForm.emergencyRelation" placeholder="关系" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="emergencyPhone">
            <el-input v-model="registerForm.emergencyPhone" placeholder="联系电话" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="registerForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="register-code">
          <img :src="codeUrl" @click="getCode" class="register-code-img"/>
        </div>
      </el-form-item>

      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleRegister"
        >
          <span v-if="!loading">提交注册申请 (等待审核)</span>
          <span v-else>提 交 中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <div class="el-register-footer">
      <span>Copyright © 2018-2025 ruoyi.vip All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg, register } from "@/api/login"

export default {
  name: "Register",
  dicts: ['sys_user_sex', 'xuewei', 'sys_major', 'current_status'], // 引入字典
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"))
      } else {
        callback()
      }
    }
    return {
      title: "团队成员注册", // 修改标题
      codeUrl: "",
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        code: "",
        uuid: "",
        // 新增字段初始化
        nickName: "",
        sex: "",
        ethnicity: "",
        phonenumber: "",
        email: "",
        address: "",
        studentId: "",
        enrollmentYear: "",
        degreeType: "",
        major: "",
        currentStatus: "",
        undergradUniv: "",
        masterUniv: "",
        employer: "",
        emergencyContact: "",
        emergencyRelation: "",
        emergencyPhone: ""
      },
      registerRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
          { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
          { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入您的密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }],
        // 新增字段校验
        nickName: [{ required: true, trigger: "blur", message: "请输入真实姓名" }],
        sex: [{ required: true, trigger: "change", message: "请选择性别" }],
        studentId: [{ required: true, trigger: "blur", message: "请输入学号" }],
        major: [{ required: true, trigger: "change", message: "请选择专业" }],
        degreeType: [{ required: true, trigger: "change", message: "请选择学位类别" }],
        enrollmentYear: [{ required: true, trigger: "blur", message: "请选择入学年份" }],
        currentStatus: [{ required: true, trigger: "change", message: "请选择当前状态" }],
        undergradUniv: [{ required: true, trigger: "blur", message: "请输入本科毕业院校" }],
        phonenumber: [
          { required: true, trigger: "blur", message: "请输入手机号码" },
          { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }
        ],
        email: [
          { required: true, trigger: "blur", message: "请输入邮箱" },
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }
        ],
        address: [{ required: true, trigger: "blur", message: "请输入家庭住址" }]
      },
      loading: false,
      captchaEnabled: true
    }
  },
  created() {
    this.getCode()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        // 1. 先打印看看后端到底返了什么
        console.log("验证码返回:", res.img);

        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          // 2. 动态判断是否需要拼接前缀
          // 如果后端返回的字符串已经包含了 "data:image"，就直接用
          if (res.img.startsWith("data:image")) {
            this.codeUrl = res.img;
          } else {
            // 否则，拼上 png 的前缀（若依默认是png，不是gif）
            this.codeUrl = "data:image/png;base64," + res.img;
          }
          this.registerForm.uuid = res.uuid
        }
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          register(this.registerForm).then(res => {
            const username = this.registerForm.username
            // 修改提示语
            this.$alert("<font color='red'>账号 " + username + " 注册申请已提交，请等待导师审核！</font>", '系统提示', {
              dangerouslyUseHTMLString: true,
              type: 'success'
            }).then(() => {
              this.$router.push("/login")
            }).catch(() => {})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.png");
  background-size: cover;
  overflow: hidden; /* 防止背景滚动 */
}
.title {
  margin: 0px auto 20px auto;
  text-align: center;
  color: #707070;
}

.register-form {
  border-radius: 6px;
  background: #ffffff;
  width: 800px; /* 增加宽度 */
  padding: 25px 25px 5px 25px;
  max-height: 90vh; /* 限制最大高度 */
  overflow-y: auto; /* 超出高度显示滚动条 */

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

.form-section {
  font-size: 14px;
  color: #1890ff;
  font-weight: bold;
  margin-bottom: 15px;
  padding-left: 5px;
  border-left: 3px solid #1890ff;
  line-height: 1;
}

.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.register-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-register-footer {
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
.register-code-img {
  height: 38px;
}

/* 滚动条样式优化 */
.register-form::-webkit-scrollbar {
  width: 6px;
}
.register-form::-webkit-scrollbar-thumb {
  background-color: #d9d9d9;
  border-radius: 3px;
}
</style>
