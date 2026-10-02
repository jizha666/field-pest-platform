package com.zhaji.service;

import com.zhaji.pojo.Area;
import com.zhaji.pojo.Equip;
import com.zhaji.pojo.PageBean;

public interface EquipService {
    PageBean page(String name, String landName, String category, Integer status, Integer page, Integer pageSize);

    Integer checkField(Equip equip);

    void addEquip(Equip equip);

    Equip selectById(Integer id);

    void updateEquip(Equip equip);

    void deleteEquip(Integer id);
}
