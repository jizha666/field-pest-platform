package com.zhaji.service;

import com.zhaji.pojo.Area;
import com.zhaji.pojo.PageBean;

import java.util.List;

public interface AreaService {
    //List<Area> list();

    PageBean page_v(String province, String city, Integer page, Integer pageSize);

    PageBean page_a(String province, String city, Integer page, Integer pageSize);

    void addArea(Area area);

    Area selectById(Integer id);

    void updateArea(Area area);

    void deleteArea(Integer id);
}
