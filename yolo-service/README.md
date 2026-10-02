# YOLO 推理服务

这是本人为田间虫情分析系统编写的 Flask 推理接口。服务使用 Ultralytics YOLO 加载上一届学生提供的模型权重，对外提供图片上传和检测数量统计接口。

## 模型文件

仓库不包含 `best v8s.pt`。模型权重、训练数据和指标属于上一届学生，公开前需要单独取得授权。

将获得授权的模型文件放到本目录，或通过环境变量指定路径：

```powershell
$env:YOLO_MODEL_PATH = "D:\models\best v8s.pt"
python app.py
```

## 安装与启动

```powershell
python -m venv .venv
.\.venv\Scripts\python -m pip install -r requirements.txt
.\.venv\Scripts\python app.py
```

默认监听 `http://0.0.0.0:5000`，可通过 `PORT` 环境变量修改端口。检测结果图片默认保存到 `runs/predict/`。

## 接口

- `POST /upload`
- 请求类型：`multipart/form-data`
- 文件字段：`file`
- 成功响应：`{"message": "File uploaded successfully", "number": N}`
- 缺少文件或图片损坏：`400`

```powershell
curl.exe -X POST -F "file=@sample.jpg" http://localhost:5000/upload
```

本目录中的 `app.py` 使用 MIT License；上一届学生的模型权重不适用该许可证。
