<script setup>
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { userApi, updateApi } from "@/api/login";

const userRef = ref(null);
const user = ref({
  id: "",
  username: "",
  name: "",
  password: "",
});

onMounted(async () => {
  //打开页面时自动执行
  let username = JSON.parse(localStorage.getItem("loginUser")).username;
  //console.log(username);
  const result = await userApi(username);
  if (result.code) {
    user.value = result.data;
  }
});

// 表单验证规则
const rules = ref({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, message: "密码长度至少6位", trigger: "blur" },
  ],
});

const save = async () => {
  userRef.value.validate(async (valid) => {
    if (valid) {
      let result = await updateApi(user.value);
      if (result.code) {
        ElMessage.success("保存成功");
      } else {
        ElMessage.error(result.msg);
      }
    }else {
      ElMessage.error('表单校验不通过');
    }
  });
};
</script>

<template>
  <div class="user-container">
    <el-card class="user-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">用户信息修改</span>
        </div>
      </template>

      <el-form
        ref="userRef"
        :model="user"
        :rules="rules"
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="user.username"
            placeholder="请输入用户名"
            clearable
          />
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-input
            v-model="user.name"
            placeholder="请输入真实姓名"
            clearable
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="user.password"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="save" class="submit-btn">
            保存修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.user-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 0 20px;
}

.user-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  text-align: center;
}

.header-title {
  font-size: 1.5em;
  font-weight: 600;
  color: #409eff;
}

.submit-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}
</style>