package com.zhaji.controller;

import com.zhaji.pojo.Area;
import com.zhaji.pojo.PageBean;
import com.zhaji.pojo.Result;
import com.zhaji.service.AreaService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping("/area_v")
    public Result page_v(String province, String city,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        log.info("访客分页查询，参数：{}，{}，{}，{}", province, city, page, pageSize);
        PageBean pageBean = areaService.page_v(province, city, page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/area_a")
    public Result page_a(String province, String city,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        log.info("管理员分页查询，参数：{}，{}，{}，{}", province, city, page, pageSize);
        PageBean pageBean = areaService.page_a(province, city, page, pageSize);
        return Result.success(pageBean);
    }

    @PostMapping("/area")
    public Result addArea(@RequestBody Area area) {
        log.info("新增地区{}", area);
        areaService.addArea(area);
        return Result.success();
    }

    @GetMapping("/area/{id}")
    public Result getArea(@PathVariable Integer id) {
        log.info("根据ID查询，ID={}", id);
        Area area = areaService.selectById(id);
        return Result.success(area);
    }

    @PutMapping("/area")
    public Result updateArea(@RequestBody Area area) {
        log.info("更新数据：{}", area);
        areaService.updateArea(area);
        return Result.success();
    }

    @DeleteMapping("/area/{id}")
    public Result deleteArea(@PathVariable Integer id) {
        log.info("根据Id删除：ID={}", id);
        areaService.deleteArea(id);
        return Result.success();
    }
}
