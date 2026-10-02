package com.zhaji.service.impl;

import com.zhaji.mapper.CameraMapper;
import com.zhaji.service.CameraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Slf4j
@Service
public class CameraServiceImpl implements CameraService {
    @Autowired
    private CameraMapper cameraMapper;

    @Override
    public String uploadImage(String imagePath) {
        String pythonApiUrl = System.getenv().getOrDefault(
                "YOLO_INFERENCE_URL",
                "http://localhost:5000/upload"
        );

        // 创建RestTemplate实例
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 准备文件资源
        FileSystemResource fileResource = new FileSystemResource(new File(imagePath));

        // 创建multipart请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        // 创建请求实体
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 发送POST请求
        ResponseEntity<String> response = restTemplate.postForEntity(
                pythonApiUrl,
                requestEntity,
                String.class
        );

        return response.getBody();
    }
}


