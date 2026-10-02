package com.zhaji.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhaji.mapper.PestMapper;
import com.zhaji.pojo.PageBean;
import com.zhaji.pojo.Pest;
import com.zhaji.service.PestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PestServiceImpl implements PestService {
    @Autowired
    private PestMapper pestMapper;

    @Override
    public PageBean page(String name, String date, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<Pest> pestList = pestMapper.list(name, date);

        Page<Pest> p = (Page<Pest>) pestList;

        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());

        return pageBean;
    }

    @Override
    public Pest pestChart(String name, String begin, String end) {
        List<String> dates = pestMapper.pestDates(name, begin, end);
        List<Integer> datas = new ArrayList<>();
        //log.info("dates: {}", dates);
        for(int i = 0; i < dates.size(); i++){
            int t = pestMapper.pestData(name, dates.get(i));
            datas.add(t);
        }
        Pest p = new Pest();
        p.setName("稻纵卷叶螟");
        p.setDates(dates);
        p.setValues(datas);
        return p;
    }

    @Override
    public Pest TemperatureChart(String name, String begin, String end) {
        List<String> dates = pestMapper.tempDates(name, begin, end);
        List<Integer> datas = new ArrayList<>();
        for(int i = 0; i < dates.size(); i++){
            int t = pestMapper.tempData(name, dates.get(i));
            datas.add(t);
        }
        Pest p = new Pest();
        p.setName("温度");
        p.setDates(dates);
        p.setValues(datas);
        return p;
    }

}
