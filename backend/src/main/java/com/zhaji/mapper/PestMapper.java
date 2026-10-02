package com.zhaji.mapper;

import com.zhaji.pojo.Pest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PestMapper {

    List<Pest> list(String name, String date);

    @Select("select pest_record.update_time from pest_record, field " +
            "where update_time between #{begin} and #{end} " +
            "and pest_record.field_id = field.id and field.name = #{name} " +
            "order by update_time")
    List<String> pestDates(String name, String begin, String end);

    @Select("select number from pest_record, field " +
            "where update_time = #{i} " +
            "and pest_record.field_id = field.id " +
            "and field.name = #{name}")
    int pestData(String name, String i);

    @Select("select environment_record.update_time from environment_record, field " +
            "where update_time between #{begin} and #{end} " +
            "and environment_record.field_id = field.id and field.name = #{name} " +
            "order by update_time")
    List<String> tempDates(String name, String begin, String end);

    @Select("select value from environment_record, field " +
            "where update_time = #{s} " +
            "and environment_record.field_id = field.id " +
            "and field.name = #{name}")
    int tempData(String name, String s);
}
