import request from "@/utils/request";

export const queryPageApi = (name, landName, category, status, page, pageSize) =>
    request.get(`/equip?name=${name}&landName=${landName}&category=${category}&status=${status}&page=${page}&pageSize=${pageSize}`);

export const addApi = (equip) => request.post('/equip', equip);

//修改
export const updateApi = (equip) =>  request.put('/equip', equip);

//根据ID查询
export const queryById = (id) =>  request.get(`/equip/${id}`);

//删除
export const deleteApi = (id) =>  request.delete(`/equip/${id}`);