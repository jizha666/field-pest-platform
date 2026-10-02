# 主要接口

所有受保护接口通过请求头 `token` 携带 JWT。

## 登录与用户

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/login` | 用户名、密码和身份登录，返回用户信息与 JWT |
| GET | `/user?username=` | 查询用户 |
| PUT | `/user` | 修改用户信息 |

## 地区与田地

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/area_v`、`/area_a` | 访客/管理员分页查询地区 |
| POST/PUT | `/area` | 新增或修改地区 |
| GET/DELETE | `/area/{id}` | 查询或删除地区 |
| GET | `/field`、`/field_a` | 环境记录或管理员分页查询 |
| GET | `/field/province`、`/field/city` | 动态省市级联 |
| POST/PUT | `/field` | 新增或修改田地 |
| GET/DELETE | `/field/{id}` | 查询或删除田地 |

## 设备、环境与虫情

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/equip` | 设备分页与条件查询 |
| POST/PUT/DELETE | `/equip`、`/equip/{id}` | 设备维护 |
| GET | `/pest` | 虫情记录分页查询 |
| GET | `/pest/chart` | 虫情数量和温度趋势 |
| GET | `/camera?name=` | 获取截图路径并调用 YOLO 推理服务 |
