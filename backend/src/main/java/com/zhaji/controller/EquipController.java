package com.zhaji.controller;

import com.zhaji.pojo.Area;
import com.zhaji.pojo.Equip;
import com.zhaji.pojo.PageBean;
import com.zhaji.pojo.Result;
import com.zhaji.service.EquipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
public class EquipController {
    @Autowired
    private EquipService equipService;

    @GetMapping("/equip")
    public Result page(String name, String landName, String category, Integer status,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        log.info("分页查询，参数:{}, {}, {}, {}, {}, {}", name, landName, category, status, page, pageSize);
        PageBean pageBean = equipService.page(name, landName, category, status, page, pageSize);
        return Result.success(pageBean);
    }

    @PostMapping("/equip")
    public Result addEquip(@RequestBody Equip equip) {
        log.info("新增设备：{}", equip);
        Integer result = equipService.checkField(equip);
        if(result == 0) {
            return Result.error("田地不存在！请重新输入田地名称");
        }
        equipService.addEquip(equip);
        return Result.success();
    }

    @GetMapping("/equip/{id}")
    public Result getEquip(@PathVariable Integer id) {
        log.info("根据ID查询，ID={}", id);
        Equip equip = equipService.selectById(id);
        return Result.success(equip);
    }

    @PutMapping("/equip")
    public Result updateEquip(@RequestBody Equip equip) {
        log.info("更新设备：{}", equip);
        Integer result = equipService.checkField(equip);
        if(result == 0) {
            return Result.error("田地不存在！请重新输入田地名称");
        }
        equipService.updateEquip(equip);
        return Result.success();
    }

    @DeleteMapping("/equip/{id}")
    public Result deleteEquip(@PathVariable Integer id) {
        log.info("根据Id删除：ID={}", id);
        equipService.deleteEquip(id);
        return Result.success();
    }
}
