<template>
  <div class="app-container">
    <el-card class="box-card" v-loading="loading">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px;">我的注册申请</span>
      </div>

      <div style="text-align: center; padding: 30px 0;">
        <div v-if="user.auditStatus == '0'">
          <i class="el-icon-time" style="font-size: 60px; color: #E6A23C; margin-bottom: 10px;"></i>
          <h2>您的申请正在审核中...</h2>
          <p class="tips">请耐心等待导师审核。审核期间无法修改信息。</p>
        </div>

        <div v-else-if="user.auditStatus == '1'">
          <i class="el-icon-circle-check" style="font-size: 60px; color: #67C23A; margin-bottom: 10px;"></i>
          <h2>恭喜，审核已通过！</h2>
          <p class="tips">您已成为正式成员。请刷新页面或重新登录以加载完整功能。</p>
          <el-button type="primary" @click="$router.push('/')">去首页</el-button>
        </div>

        <div v-else-if="user.auditStatus == '2'">
          <i class="el-icon-circle-close" style="font-size: 60px; color: #F56C6C; margin-bottom: 10px;"></i>
          <h2 style="color: #F56C6C">审核未通过</h2>
          <div class="reject-reason">
            拒绝原因：{{ user.remark || '未填写原因' }}
          </div>
          <p class="tips">请检查并修改下方所有可能有误的信息，然后点击“重新提交”按钮。</p>
        </div>
      </div>

      <el-divider v-if="user.auditStatus != '1'"></el-divider>

      <el-form
        v-if="user.auditStatus != '1'"
        ref="form"
        :model="form"
        :rules="rules"
        label-width="110px"
        :disabled="user.auditStatus == '0'"
        style="max-width: 900px; margin: 0 auto;"
      >

        <h4 class="section-title">基础信息</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入真实姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择" style="width: 100%">
                <el-option v-for="dict in dict.type.sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="民族" prop="ethnicity">
              <el-input v-model="form.ethnicity" placeholder="请输入民族" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="家庭住址" prop="address">
              <el-input v-model="form.address" placeholder="请输入家庭住址" />
            </el-form-item>
          </el-col>
        </el-row>

        <h4 class="section-title">联系方式</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phonenumber">
              <el-input v-model="form.phonenumber" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>

        <h4 class="section-title">学籍信息</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号" prop="studentId">
              <el-input v-model="form.studentId" placeholder="请输入学号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-select v-model="form.major" placeholder="请选择专业" style="width: 100%">
                <el-option v-for="dict in dict.type.sys_major" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入学年份" prop="enrollmentYear">
              <el-date-picker v-model="form.enrollmentYear" type="year" value-format="yyyy" style="width: 100%" placeholder="选择年份"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学位类别" prop="degreeType">
              <el-select v-model="form.degreeType" placeholder="请选择" style="width: 100%">
                <el-option v-for="dict in dict.type.xuewei" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前状态" prop="currentStatus">
              <el-select v-model="form.currentStatus" placeholder="请选择" style="width: 100%">
                <el-option v-for="dict in dict.type.current_education" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <h4 class="section-title">履历信息</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="本科院校" prop="undergradUniv">
              <el-input v-model="form.undergradUniv" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="硕士院校" prop="masterUniv">
              <el-input v-model="form.masterUniv" placeholder="无硕士经历可不填" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="就业单位" prop="employer">
              <el-input v-model="form.employer" placeholder="无就业单位可不填" />
            </el-form-item>
          </el-col>
        </el-row>

        <h4 class="section-title">紧急联系人</h4>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="联系人姓名" prop="emergencyContact">
              <el-input v-model="form.emergencyContact" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="关系" prop="emergencyRelation">
              <el-input v-model="form.emergencyRelation" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话" prop="emergencyPhone">
              <el-input v-model="form.emergencyPhone" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item style="text-align: center; margin-top: 30px;">
          <el-button
            v-if="user.auditStatus == '2'"
            type="primary"
            icon="el-icon-refresh"
            :loading="submitLoading"
            @click="submit"
            style="width: 200px;"
          >修改并重新提交申请</el-button>
        </el-form-item>
      </el-form>

    </el-card>
  </div>
</template>

<script>
import { getUserProfile } from "@/api/system/user";
import { updateApplication } from "@/api/system/user";

export default {
  name: "MyApplication",
  // 引入所需的所有字典
  dicts: ['sys_user_sex', 'xuewei', 'sys_major', 'current_education'],
  data() {
    return {
      loading: true,
      submitLoading: false,
      user: {}, // 存储原始用户信息（包含状态和拒绝原因）
      form: {}, // 表单数据
      // 校验规则，根据实际必填项调整
      rules: {
        nickName: [{ required: true, message: "真实姓名不能为空", trigger: "blur" }],
        sex: [{ required: true, message: "请选择性别", trigger: "change" }],
        studentId: [{ required: true, message: "学号不能为空", trigger: "blur" }],
        phonenumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }
        ],
        email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
        major: [{ required: true, message: "请选择专业", trigger: "change" }],
        enrollmentYear: [{ required: true, message: "请选择入学年份", trigger: "change" }],
        degreeType: [{ required: true, message: "请选择学位类别", trigger: "change" }],
        currentStatus: [{ required: true, message: "请选择当前状态", trigger: "change" }],
        undergradUniv: [{ required: true, message: "请输入本科毕业院校", trigger: "blur" }],
      }
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      this.loading = true;
      getUserProfile().then(response => {
        this.user = response.data; // 这里是 SysUser 对象
        // 将用户信息复制给 form 用于编辑
        this.form = { ...response.data };
        this.loading = false;
      });
    },
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          updateApplication(this.form).then(response => {
            this.$modal.msgSuccess("重新提交成功！请耐心等待导师审核。");
            this.submitLoading = false;
            // 重新获取用户信息，刷新页面状态（会变成待审核状态）
            this.getUser();
          }).catch(() => {
            this.submitLoading = false;
          });
        }
      });
    }
  }
};
</script>

<style scoped>
.tips {
  color: #606266;
  font-size: 14px;
  margin-top: 10px;
}
.reject-reason {
  background-color: #fef0f0;
  color: #f56c6c;
  padding: 10px 20px;
  border-radius: 4px;
  display: inline-block;
  margin: 15px 0;
  font-weight: bold;
  border: 1px solid #fde2e2;
}
.section-title {
  font-size: 16px;
  color: #303133;
  margin: 20px 0 15px 0;
  padding-left: 10px;
  border-left: 4px solid #1890ff;
}
</style>
