package com.zhaji.mapper;

import com.zhaji.pojo.Field;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface FieldMapper {
    List<Field> list_1(String name, String category, LocalDate begin, LocalDate end, Integer page, Integer pageSize);

    List<Field> list_a(String name, String province, String city, Integer page, Integer pageSize);

    @Select("select distinct province name from area")
    List<Field> queryProvince();

    @Select("select city name from area where province = #{province}")
    List<Field> queryCity(String province);

    @Select("select id from area where province = #{province} and city = #{city}")
    Field queryProvinceAndCity(Field field);

    @Insert("insert into field (name, area_id) VALUES (#{name}, #{id})")
    void addField(Field field);

    @Select("select field.id id, name,  province, city from area, field where field.id = #{id} and field.area_id = area.id")
    Field selectById(Integer id);

    @Update("update field set name = #{name}, area_id = #{areaId} where id = #{id}")
    void update(Field field);

    @Delete("delete from field where id = #{id}")
    void deleteField(Integer id);

    @Select("select province from area order by city")
    List<String> selectProvince();

    @Select("select city from area order by city")
    List<String> selectCity();

    @Select("select id from area where province = #{p} and city = #{c}")
    int selectAreaId(String p, String c);

    @Select("select id from field where area_id = #{integer}")
    List<Integer> selectFieldId(Integer integer);

    @Insert("insert into environment_record(category, field_id, value, create_time, update_time) " +
            "values ('温度', #{fieldId}, #{temp}, #{createTime}, #{updateTime})")
    void addRecord(Integer fieldId, String temp, String createTime, String updateTime);
}
