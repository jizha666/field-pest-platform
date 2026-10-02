import request from "@/utils/request";

export const queryPageApi = (name, category, begin, end, page, pageSize) =>
    request.get(`/field?name=${name}&category=${category}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`);

export const queryPageApi_a = (name, province, city, page, pageSize) =>
    request.get(`/field_a?name=${name}&province=${province}&city=${city}&page=${page}&pageSize=${pageSize}`)

export const queryProvince = () => request.get('/field/province')

export const queryCity = (province) => request.get(`/field/city?province=${province}`)

export const addApi = (field) => request.post('/field', field);

export const updateApi = (field) =>  request.put('/field', field);

export const queryById = (id) =>  request.get(`/field/${id}`);

//删除
export const deleteApi = (id) =>  request.delete(`/field/${id}`);