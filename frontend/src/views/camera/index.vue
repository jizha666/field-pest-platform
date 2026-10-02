<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import { ElLoading, ElMessageBox, ElMessage } from "element-plus";
import { upload } from "@/api/camera";

const data = ref({
  szIP: import.meta.env.VITE_CAMERA_IP || "",
  iPrototocol: 1,
  iPort: import.meta.env.VITE_CAMERA_PORT || "80",
  szUserName: import.meta.env.VITE_CAMERA_USERNAME || "",
  szPassword: "",
});

// const state = 0;
state = 0

const init = () => {
  WebVideoCtrl.I_InitPlugin({
    iWndowType: 1,
    bWndFull: true,
    cbInitPluginComplete: function () {
      WebVideoCtrl.I_InsertOBJECTPlugin("divPlugin").then(
        () => {},
        () => {
          alert("失败");
        }
      );
    },
  });
};

onMounted(() => {
  //打开页面时自动执行
  init();
});

const cleanup = () => {
  console.log("用户离开页面了");
  WebVideoCtrl.I_StopAllPlay();
  WebVideoCtrl.I_Logout(data.value.szIP + "_" + data.value.iPort);
  WebVideoCtrl.I_DestroyPlugin();
};

onUnmounted(() => {
  cleanup();
});

const login = () => {
  I_HidPlugin();
  const loading = ElLoading.service({
    lock: true,
    text: "正在打开设备。。。",
    background: "rgba(0, 0, 0, 0.7)",
  });

  WebVideoCtrl.I_Login(
    data.value.szIP,
    data.value.iPrototocol,
    data.value.iPort,
    data.value.szUserName,
    data.value.szPassword,
    {
      success: function () {
        console.log("登陆成功");
      },
      error: function () {
        console.log("登录失败");
      },
    }
  );
  loading.close();
  I_ShowPlugin();
  ElMessage.success('设备打开成功！请开始预览');
  state = 1;
};

const see = () => {
    console.log("等待结束");
  WebVideoCtrl.I_StartRealPlay(data.value.szIP + "_" + data.value.iPort, {
    success: () => {
      console.log("预览成功");
    },
  });
}

// const getCurrentDate = () => {
//   const now = new Date();
//   const year = now.getFullYear();
//   const month = String(now.getMonth() + 1).padStart(2, "0");
//   const day = String(now.getDate()).padStart(2, "0");
//   return `${year}-${month}-${day}`;
// };

const picture = async () => {
  // if(!state){
  //   ElMessage.error('请先开始预览！')
  //   return;
  // }
  WebVideoCtrl.I_CapturePic("pic");
    I_HidPlugin();
  const loading = ElLoading.service({
    lock: true,
    text: "图片获取成功，正在识别，请稍等。。。",
    background: "rgba(0, 0, 0, 0.7)",
  });
  // 截图路径由海康插件和 CAMERA_CAPTURE_DIR 配置共同决定。`n  const name = "pic";
  const result = await upload(name);
  //console.log('虫子数量：' + result.data);
  loading.close();
  //cleanup();

  ElMessageBox.alert(`本次共识别到稻纵卷叶螟 ${result.data} 只`, "识别结果", {
    confirmButtonText: "确认",
  })
  .then(async () => {
    // init();
    // console.log("初始化")
    I_ShowPlugin();
  });
};
</script>

<template>
<div class="camera-container">
    <div class="camera-view">
      <div id="divPlugin" class="camera-window"></div>
    </div>
    <div class="button-group">
      <el-button @click="login" type="primary" size="large" class="action-btn">
        <el-icon class="el-icon--left"><VideoCamera /></el-icon>
        打开设备
      </el-button>
            <el-button @click="see" type="primary" size="large" class="action-btn">
        <el-icon class="el-icon--left"><VideoCamera /></el-icon>
        开始预览
      </el-button>
      <el-button @click="picture" type="success" size="large" class="action-btn">
        <el-icon class="el-icon--left"><Camera /></el-icon>
        获取图片
      </el-button>
    </div>
  </div>
</template>

<style>
.camera-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.camera-view {
  width: 100%;
  display: flex;
  justify-content: center;
}
.camera-window {
  width: 1000px;
  height: 600px;
  background-color: #000;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}
.button-group {
  display: flex;
  gap: 20px;
}
.action-btn {
  padding: 12px 24px;
  font-size: 16px;
  transition: all 0.3s;
}
.action-btn:hover {
  transform: translateY(-2px);
}
@media (max-width: 1100px) {
  .camera-window {
    width: 100%;
    max-width: 800px;
    height: 480px;
  }
}
</style>

