import request from '@/utils/request'

//登录
export const loginApi = (data) => request.post('/login', data)

//查询用户
export const userApi = (username) => request.get(`/user?username=${username}`);

//修改
export const updateApi = (user) =>  request.put('/user', user);