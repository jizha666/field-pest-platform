package com.zhaji.mapper;

import com.zhaji.pojo.Equip;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EquipMapper {
    List<Equip> list(String name, String landName, String category, Integer status);

    @Select("select name from field where name = #{landName}")
    String checkField(String landName);

    @Select("select id from field where name = #{landName}")
    Equip queryField(Equip equip);

    @Insert("insert into equipment (name, field_id, category, status) values (#{name}, #{fieldId}, #{category}, #{status})")
    void addEquip(Equip equip);

    @Select("select equipment.id, equipment.name, field.name landName, equipment.category, equipment.status " +
            "from field, equipment where equipment.field_id = field.id and equipment.id = #{id}")
    Equip selectById(Integer id);

    @Update("update equipment " +
            "set name = #{name}, field_id = #{fieldId}, category = #{category}, status = #{status} " +
            "where id = #{id}")
    void updateEquip(Equip equip);

    @Delete("delete from equipment where id = #{id}")
    void deleteEquip(Integer id);
}
