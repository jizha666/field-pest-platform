# 田间虫情分析 Web 系统

这是本科毕业设计系统的脱敏工作副本，用于简历展示和面试复盘。

## 发布状态

- 仓库只包含本人编写的 Web 系统与推理服务代码、公开配置和脱敏文档。
- Docker Compose 配置已提供，但尚未在作者本机完成端到端实机验证。
- YOLO 模型、海康 Web SDK 和真实设备凭据均不进入仓库。

## 我的职责

- 独立完成 Vue 3 前端和 Spring Boot 后端的业务开发、联调与测试。
- 项目时间：2025.03-2025.06。
- YOLO 模型来自上一届学生毕设，只提供 `best v8s.pt`；我不负责模型结构、训练或指标优化。
- 我独立编写 Flask + Ultralytics YOLO 推理接口，并负责摄像头截图、HTTP 调用、结果解析、存储记录和前端展示。

## 技术栈

- 前端：Vue 3、Vite、Element Plus、Axios、ECharts、Vue Router
- 后端：Java 21、Spring Boot 3.4、MyBatis、Maven、JWT、Spring Scheduling
- 数据：MySQL 8、PageHelper
- 外部服务：和风天气 API、海康 WebVideoCtrl SDK、自建 YOLO HTTP 推理服务

## 功能模块

1. 登录与身份区分
2. 地区配置
3. 田地配置
4. 设备配置
5. 环境数据定时采集
6. 虫情查询、统计、视频采集与识别结果展示

## 目录

- `backend/`：Spring Boot 服务
- `frontend/`：Vue 3 管理后台和访客页面
- `yolo-service/`：本人编写的 Flask + Ultralytics YOLO 推理接口，模型权重不随仓库分发
- `docs/schema.sql`：根据实体和 MyBatis SQL 重建的开发库结构
- `docs/api.md`：主要接口清单
- `docs/interview-notes.md`：设计取舍、不足和重构方向

## 本地启动

### 1. 初始化 MySQL

```powershell
mysql -uroot -p < docs/schema.sql
```

### 2. 配置环境变量

将 `.env.example` 的内容填入 IDE 运行配置或 PowerShell 环境变量。不要提交真实值。

### 3. 启动后端

```powershell
cd backend
mvn spring-boot:run
```

后端默认监听 `http://localhost:8080`。

### 4. 启动前端

```powershell
cd frontend
npm install
npm run dev
```

前端默认由 Vite 提供开发服务，并把 `/api` 代理到 `http://localhost:8080`。

演示账号仅用于本地开发：`demo-admin / demo123`、`demo-user / demo123`。

## Docker Compose

```powershell
docker compose up --build
```

访问 `http://localhost:5173`。MySQL 首次启动时会自动执行 `docs/schema.sql`。天气 API 和海康 SDK 需要额外配置或补入本地文件。

> 当前配置尚未在本机完成端到端验证。首次运行时应检查镜像拉取、MySQL 初始化、端口占用和环境变量配置。

## 海康 SDK 与演示边界

- 海康摄像机由老师提供，目前无法复现实机演示。
- 公开演示使用脱敏截图和代码走查，不伪装成实时设备画面。
- 仓库不包含毕业论文、学校材料、摄像头实机录像或未脱敏数据。

## 海康 SDK 说明

出于授权和仓库体积考虑，仓库不分发海康 Web SDK。取得授权后将以下文件放入 `frontend/public/vendor/`：

- `jquery-1.7.1.min.js`
- `jsVideoPlugin-1.0.0.min.js`
- `webVideoCtrl.js`

不配置 SDK 时，登录、地区、田地、设备、环境数据和虫情查询页面仍可展示；摄像头页面无法预览。

## 已知问题

- 登录密码仍为明文比较，公开版仅用于学习和演示。
- JWT 使用静态环境变量密钥，缺少刷新、撤销和细粒度权限。
- 接口错误码和参数校验未统一。
- 定时任务缺少失败重试、幂等和补偿。
- 摄像头 SDK 是浏览器插件方案，不适合直接套用到现代生产项目。

## 许可证与第三方边界

本人编写的代码，包括 `yolo-service/app.py`，使用 [MIT License](LICENSE)。上一届学生的 `best v8s.pt`、海康 Web SDK 及其他第三方组件不包含在本仓库中，也不由本项目的 MIT License 覆盖。详细说明见 [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md)。




