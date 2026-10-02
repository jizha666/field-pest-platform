package com.zhaji.controller;

import com.zhaji.pojo.Field;
import com.zhaji.pojo.PageBean;
import com.zhaji.pojo.Result;
import com.zhaji.service.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class FieldController {
    @Autowired
    private FieldService fieldService;

    @GetMapping("/field")
    public Result page(String name, String category,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                            @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询，参数：{},{},{},{},{},{}", name, category, begin, end, page, pageSize);
        PageBean pageBean = fieldService.page(name, category, begin, end, page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/field_a")
    public Result page_a(String name, String province, String city,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        log.info("管理员分页查询，参数：{}，{}，{}，{}, {}", name, province, city, page, pageSize);
        PageBean pageBean = fieldService.page_a(name, province, city, page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/field/province")
    public Result queryProvince() {
        log.info("查询所有省份");
        List<Field> f = fieldService.queryProvince();
        return Result.success(f);
    }

    @GetMapping("/field/city")
    public Result queryCity(String province) {
        log.info("查询对应城市：{}", province);
        List<Field> f = fieldService.queryCity(province);
        return Result.success(f);
    }

    @PostMapping("/field")
    public Result addField(@RequestBody Field field) {
        log.info("新增田地：{}", field);
        fieldService.addField(field);
        return Result.success();
    }

    @GetMapping("/field/{id}")
    public Result getField(@PathVariable Integer id) {
        log.info("根据ID查询：ID={}", id);
        Field field = fieldService.selectById(id);
        return Result.success(field);
    }

    @PutMapping("/field")
    public Result updateField(@RequestBody Field field) {
        log.info("更新田地：{}", field);
        fieldService.update(field);
        return Result.success();
    }

    @DeleteMapping("/field/{id}")
    public Result deleteField(@PathVariable Integer id) {
        log.info("根据Id删除：ID={}", id);
        fieldService.deleteField(id);
        return Result.success();
    }
}
