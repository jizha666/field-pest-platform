package com.zhaji.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GzipApiUtils {
    private static final String API_KEY = System.getenv().getOrDefault("QWEATHER_API_KEY", "");


    public static JsonNode GzipApi(String url, String name) throws IOException {
        String WEATHER_URL = System.getenv().getOrDefault("QWEATHER_API_HOST", "");
        if (WEATHER_URL.isBlank() || API_KEY.isBlank()) {
            throw new IllegalStateException("QWEATHER_API_HOST and QWEATHER_API_KEY must be configured");
        }
        WEATHER_URL = WEATHER_URL + "/" + url + "?location=" + name;
        // 创建请求头并添加API密钥
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-QW-Api-Key", API_KEY);
        headers.add("Accept-Encoding", "gzip"); // 告知服务器我们接受Gzip压缩响应
        // 创建请求实体
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        // 配置RestTemplate以处理Gzip响应
        ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(java.net.HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setRequestProperty("Accept-Encoding", "gzip");
            }
        };
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        // 发送GET请求
        ResponseEntity<byte[]> response = restTemplate.exchange(
                WEATHER_URL,
                HttpMethod.GET,
                requestEntity,
                byte[].class);
        // 检查响应是否被Gzip压缩
        byte[] responseBody = response.getBody();
        if (responseBody == null) {
            System.out.println("Empty response from weather API");
            return null;
            //return;
            //return ResponseEntity.badRequest().body("Empty response from weather API");
        }
        // 处理可能的Gzip压缩
        String responseString;
        if ("gzip".equalsIgnoreCase(response.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING))) {
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(new java.io.ByteArrayInputStream(responseBody));
                 java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzipInputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                responseString = out.toString("UTF-8");
            }
        } else {
            responseString = new String(responseBody);
        }
        // 解析JSON响应
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseString);
    }
}


