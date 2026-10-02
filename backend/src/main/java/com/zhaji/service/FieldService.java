package com.zhaji.service;

import com.zhaji.pojo.Field;
import com.zhaji.pojo.PageBean;

import java.time.LocalDate;
import java.util.List;

public interface FieldService {
    PageBean page(String name, String category, LocalDate begin, LocalDate end, Integer page, Integer pageSize);

    PageBean page_a(String name, String province, String city, Integer page, Integer pageSize);

    List<Field> queryProvince();

    List<Field> queryCity(String province);

    void addField(Field field);

    Field selectById(Integer id);

    void update(Field field);

    void deleteField(Integer id);

    List<String> selectProvince();

    List<String> selectCity();

    List<Integer> selectAreaId(List<String> province, List<String> city);

    List<Integer> selectFieldId(Integer integer);

    void addRecord(Integer integer, String s);
}
