package com.zhaji.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhaji.mapper.EquipMapper;
import com.zhaji.pojo.Area;
import com.zhaji.pojo.Equip;
import com.zhaji.pojo.PageBean;
import com.zhaji.service.EquipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EquipServiceImpl implements EquipService {
    @Autowired
    private EquipMapper equipMapper;

    @Override
    public PageBean page(String name, String landName, String category, Integer status, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<Equip> equipList = equipMapper.list(name, landName, category, status);
        Page<Equip> p = (Page<Equip>) equipList;

        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());

        return pageBean;
    }

    @Override
    public Integer checkField(Equip equip) {
        String result = equipMapper.checkField(equip.getLandName());
        if (result == null) {
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public void addEquip(Equip equip) {
        Equip e = equipMapper.queryField(equip);
        equip.setFieldId(e.getId());
        equipMapper.addEquip(equip);
    }

    @Override
    public Equip selectById(Integer id) {
        Equip equip = equipMapper.selectById(id);
        return equip;
    }

    @Override
    public void updateEquip(Equip equip) {
        Equip e = equipMapper.queryField(equip);
        equip.setFieldId(e.getId());
        equipMapper.updateEquip(equip);
    }

    @Override
    public void deleteEquip(Integer id) {
        equipMapper.deleteEquip(id);
    }
}
