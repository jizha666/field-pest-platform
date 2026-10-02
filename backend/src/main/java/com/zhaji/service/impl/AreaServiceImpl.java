package com.zhaji.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhaji.mapper.AreaMapper;
import com.zhaji.pojo.Area;
import com.zhaji.pojo.PageBean;
import com.zhaji.service.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;

//    @Override
//    public List<Area> list() {
//        return areaMapper.list();
//    }

    @Override
    public PageBean page_v(String province, String city, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<Area> areaList = areaMapper.list_v(province, city);
        Page<Area> p = (Page<Area>) areaList;

        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());

        return pageBean;
    }

    @Override
    public PageBean page_a(String province, String city, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<Area> areaList = areaMapper.list_a(province, city);
        Page<Area> p = (Page<Area>) areaList;

        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());

        return pageBean;
    }

    @Override
    public void addArea(Area area) {
        areaMapper.insert(area);
    }

    @Override
    public Area selectById(Integer id) {
        Area area = areaMapper.selectById(id);
        return area;
    }

    @Override
    public void updateArea(Area area) {
        areaMapper.update(area);
    }

    @Override
    public void deleteArea(Integer id) {
        areaMapper.deleteArea(id);
    }
}
