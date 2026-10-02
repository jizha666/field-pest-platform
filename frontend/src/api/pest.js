import request from "@/utils/request";

export const queryPageApi = (name, date, page, pageSize) =>
    request.get(`/pest?name=${name}&date=${date}&page=${page}&pageSize=${pageSize}`);

export const queryChartApi = (name, begin, end) =>
    request.get(`/pest/chart?name=${name}&begin=${begin}&end=${end}`);