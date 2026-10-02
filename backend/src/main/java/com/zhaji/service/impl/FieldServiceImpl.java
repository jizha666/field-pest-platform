package com.zhaji.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhaji.mapper.FieldMapper;
import com.zhaji.pojo.Field;
import com.zhaji.pojo.PageBean;
import com.zhaji.service.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FieldServiceImpl implements FieldService {
    @Autowired
    private FieldMapper fieldMapper;

    @Override
    public PageBean page(String name, String category, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<Field> fieldList = fieldMapper.list_1(name, category, begin, end, page, pageSize);
        Page<Field> p = (Page<Field>) fieldList;

        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());

        return pageBean;
    }

    @Override
    public PageBean page_a(String name, String province, String city, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<Field> fieldList = fieldMapper.list_a(name, province, city, page, pageSize);
        Page<Field> p = (Page<Field>) fieldList;

        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());

        return pageBean;
    }

    @Override
    public List<Field> queryProvince() {
        return fieldMapper.queryProvince();
    }

    @Override
    public List<Field> queryCity(String province) {
        return fieldMapper.queryCity(province);
    }

    @Override
    public void addField(Field field) {
        Field f = fieldMapper.queryProvinceAndCity(field);
        field.setId(f.getId());
        fieldMapper.addField(field);
    }

    @Override
    public Field selectById(Integer id) {
        return fieldMapper.selectById(id);
    }

    @Override
    public void update(Field field) {
        Field f = fieldMapper.queryProvinceAndCity(field);
        field.setAreaId(f.getId());
        fieldMapper.update(field);
    }

    @Override
    public void deleteField(Integer id) {
        fieldMapper.deleteField(id);
    }

    @Override
    public List<String> selectProvince() {
        return fieldMapper.selectProvince();
    }

    @Override
    public List<String> selectCity() {
        return fieldMapper.selectCity();
    }

    @Override
    public List<Integer> selectAreaId(List<String> province, List<String> city) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < province.size(); i++) {
            int t;
            t = fieldMapper.selectAreaId(province.get(i), city.get(i));
            result.add(t);
        }
        return result;
    }

    @Override
    public List<Integer> selectFieldId(Integer integer) {
        return fieldMapper.selectFieldId(integer);
    }

    @Override
    public void addRecord(Integer field_id, String temp) {
        String createTime = LocalDate.now().toString();
        String updateTime = LocalDate.now().toString();
        fieldMapper.addRecord(field_id, temp, createTime, updateTime);
    }

}
