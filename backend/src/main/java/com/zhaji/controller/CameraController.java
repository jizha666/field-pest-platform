package com.zhaji.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhaji.pojo.Area;
import com.zhaji.pojo.Result;
import com.zhaji.service.CameraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping
public class CameraController {
    @Autowired
    private CameraService cameraService;

    @GetMapping("/camera")
    public Result camera(String name) throws InterruptedException {
        // 获取当前日期并格式化为"yyyy-MM-dd"
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ISO_LOCAL_DATE);  // "2025-05-07"格式
        String captureDir = System.getenv().getOrDefault("CAMERA_CAPTURE_DIR", "captures");
        String path = java.nio.file.Paths.get(captureDir, formattedDate, name + ".jpg").toString();
        //log.info("path:{}", path);
        String result = cameraService.uploadImage(path);
        //log.info(result);
        // 创建Gson实例
        Gson gson = new Gson();
        // 解析JSON字符串
        JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
        // 提取number值
        int number = jsonObject.get("number").getAsInt();
        //log.info("number:{}", number);
        //return Result.error("2");
        return Result.success(number);

//        Thread.sleep(5000);
//        return Result.success(100);
    }
}


