import request from "@/utils/request";

export const upload = (name) =>  request.get(`/camera?name=${name}`);