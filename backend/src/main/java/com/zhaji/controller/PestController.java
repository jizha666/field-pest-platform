package com.zhaji.controller;

import com.zhaji.pojo.PageBean;
import com.zhaji.pojo.Pest;
import com.zhaji.pojo.Result;
import com.zhaji.service.PestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class PestController {
    @Autowired
    private PestService pestService;

    @GetMapping("/pest")
    public Result page(String name, String date,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        log.info("访客虫情查询，参数：{}，{}，{}，{}", name, date, page, pageSize);
        PageBean pageBean = pestService.page(name, date, page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/pest/chart")
    public Result chart(String name, String begin, String end) {
        log.info("图表查询，参数:{}, {}, {}", name, begin, end);
        List<Pest> pestList = new ArrayList<>();

        Pest p1 = pestService.pestChart(name, begin, end);
//        p1.setName("稻纵卷叶螟");
//        p1.setLand("test一号田");
//        p1.setValues(Arrays.asList(5, 7, 6, 8, 9, 70, 6));
//        p1.setDates(Arrays.asList("04-05", "04-06", "04-07", "04-08", "04-09", "04-10", "04-11"));
//
        Pest p2 = pestService.TemperatureChart(name, begin, end);
//        p2.setName("温度");
//        p2.setLand("test一号田");
//        p2.setValues( Arrays.asList(25, 230, 27, 26, 24, 28, 22));
//        p2.setDates(Arrays.asList("04-05", "04-06", "04-07", "04-08", "04-09", "04-10", "04-11"));
//
        pestList.add(p1);
        pestList.add(p2);

        return Result.success(pestList);
    }
}
