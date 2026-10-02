package com.zhaji.service;

import com.zhaji.pojo.PageBean;
import com.zhaji.pojo.Pest;

import java.util.List;

public interface PestService {
    PageBean page(String name, String date, Integer page, Integer pageSize);

    Pest pestChart(String name, String begin, String end);

    Pest TemperatureChart(String name, String begin, String end);
}
