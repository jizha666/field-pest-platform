import request from "@/utils/request";

export const queryAllApi = () => request.get('/area');

export const queryPageApi_v = (name, date, page, pageSize) =>
    request.get(`/area_v?name=${name}&date=${date}&page=${page}&pageSize=${pageSize}`);

export const queryPageApi_a = (province, city, page, pageSize) =>
    request.get(`/area_a?province=${province}&city=${city}&page=${page}&pageSize=${pageSize}`)

export const addApi = (land) => request.post('/area', land);

//修改
export const updateApi = (land) =>  request.put('/area', land);

//根据ID查询
export const queryById = (id) =>  request.get(`/area/${id}`);

//删除
export const deleteApi = (id) =>  request.delete(`/area/${id}`);