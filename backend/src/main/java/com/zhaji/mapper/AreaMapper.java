package com.zhaji.mapper;

import com.zhaji.pojo.Area;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Mapper
public interface AreaMapper {


    List<Area> list_v(String province, String city);

    List<Area> list_a(String province, String city);

    @Insert("insert into area(province, city) VALUES (#{province}, #{city})")
    void insert(Area area);

    @Select("select id, province, city from area where id = #{id}")
    Area selectById(Integer id);

    @Update("update area set province = #{province}, city = #{city} where id = #{id}")
    void update(Area area);

    @Delete("delete from area where id = #{id}")
    void deleteArea(Integer id);
}
