<script>
import { upload } from "@/api/camera";

export default{
    data(){
        return{
            szIP: import.meta.env.VITE_CAMERA_IP || '',
            iPrototocol:1,
            iPort: '80',
            szUserName: import.meta.env.VITE_CAMERA_USERNAME || '',
            szPassword:'',
        }
    },
    created(){
        this.init()
    },
    methods:{
        init(){
            WebVideoCtrl.I_InitPlugin({
                iWndowType:1,
                bWndFull:true,
                cbInitPluginComplete:function(){
                    WebVideoCtrl.I_InsertOBJECTPlugin("divPlugin").then(() => {}, () => {alert("失败");});
                }
            })
        },
        Login(){
            WebVideoCtrl.I_Login(this.szIP, this.iPrototocol, this.iPort, this.szUserName, this.szPassword, {
                success:function(){
                    console.log('登陆成功')
                },
                error:function(){
                    console.log('登录失败')
                }
            })
        },
        see(){
            WebVideoCtrl.I_StartRealPlay(this.szIP + '_' + this.iPort, {
                success:() => {
                    console.log("预览成功")
                }
            })
        },
        stopAllPlay(){
            WebVideoCtrl.I_StopAllPlay()
        },
        logout(){
            WebVideoCtrl.I_Logout(this.szIP + '_' + this.iPort)
        },
        breakdom(){
            WebVideoCtrl.I_DestroyPlugin()
        },
        picture(){
            console.log(WebVideoCtrl.I_GetLocalCfg())
            WebVideoCtrl.I_CapturePic('pic')
        },
        upload(){
            upload('pic');
        }
    }
}
</script>

<template>
<div class="about">
    <div id="divPlugin" class="plugin" style="width: 500px; height: 300px;"></div>
    <!-- <button @click="Login">登录</button>
    <button @click="see">预览</button>
    <button @click="stopAllPlay">停止预览</button>
    <button @click="logout">登出设备</button>
    <button @click="breakdom">销毁设备</button>
    <button @click="init">初始化设备</button>
    <button @click="picture">获取图片</button> -->

    <br>
    <button>开始预览</button>
    <button>获取图片</button>
    <button @click="upload">传输图片</button>
</div>
</template>

<style scoped>
</style>

