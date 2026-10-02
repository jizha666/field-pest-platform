<script setup>
import { ref } from "vue";
import { loginApi } from "@/api/login";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

let loginForm = ref({ username: "", password: "", identity: "" });
let router = useRouter();

//登录
const login = async () => {
  console.log(loginForm.value);
  if(!loginForm.value.username || !loginForm.value.password || !loginForm.value.identity){
    ElMessage.error("登录信息不完整！")
    return;
  }
  const result = await loginApi(loginForm.value);
  if (result.code) {
    // 登录成功
    ElMessage.success("登录成功");
    localStorage.setItem("loginUser", JSON.stringify(result.data));
    if(loginForm.value.identity == 1){
    router.push("/1"); // 跳转
    }else{
      router.push("/2");
    }
  } else {
    ElMessage.error(result.msg);
  }
};

//取消
const clear = () => {
  loginForm.value = {
    username: "",
    password: "",
    identity: "",
  };
};
</script>

<template>
  <div id="container">
    <div class="login-form">
      <el-form label-width="80px">
        <p class="title">田间虫情分析系统</p>
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            type="password"
            v-model="loginForm.password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>

        <el-form-item label="登录身份" prop="password">
          <el-radio-group v-model="loginForm.identity" class="ml-4">
            <el-radio label="1" size="large">访客</el-radio>
            <el-radio label="0" size="large">管理员</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button class="button" type="primary" @click="login"
            >登 录</el-button
          >
          <el-button class="button" type="info" @click="clear">重 置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
#container {
  box-sizing: border-box;
  padding: 10%;
  min-height: 100vh;
  background:
    radial-gradient(circle at 15% 20%, rgba(255, 255, 255, 0.22), transparent 30%),
    linear-gradient(135deg, #0f5132 0%, #2f855a 48%, #a7d7a0 100%);
}

.login-form {
  max-width: 400px;
  padding: 30px;
  margin: 0 auto;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  background-color: white;
}

.title {
  font-size: 30px;
  font-family: "楷体";
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold;
}

.button {
  margin-top: 30px;
  width: 120px;
}
</style>
