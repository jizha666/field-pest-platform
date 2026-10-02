package com.zhaji;

import com.zhaji.service.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static com.zhaji.utils.GzipApiUtils.GzipApi;

@SpringBootTest
class IHatePestApplicationTests {

    //@Test
    void contextLoads() throws IOException {
        String API_KEY = System.getenv().getOrDefault("QWEATHER_API_KEY", "");
        String WEATHER_URL = "https://p64ewr4vcy.re.qweatherapi.com/v7/weather/now?location=101010100";

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
            return;
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
        JsonNode rootNode = objectMapper.readTree(responseString);
        // 提取温度值
        String temperature = rootNode.path("now").path("temp").asText();

        if (temperature.isEmpty()) {
            //return ResponseEntity.badRequest().body("Temperature data not found in response");
            System.out.println("Temperature data not found in response");
            return;
        }
        System.out.println("当前温度: " + temperature + "°C");
        //return ResponseEntity.ok("当前温度: " + temperature + "°C");
    }

    @Autowired
    private FieldService fieldService;

    @Test
    void test() throws IOException, InterruptedException {
        List<String> province = fieldService.selectProvince();
        List<String> city = fieldService.selectCity();
        List<String> ids = new ArrayList<>();
        List<String> temps = new ArrayList<>();
        for (int i = 0; i < province.size(); i++) {
            String cityName = city.get(i);
            //System.out.println(name);
            JsonNode rootNode = GzipApi("geo/v2/city/lookup", cityName);
            int len = city.get(i).length();
            cityName = city.get(i).substring(0, len - 1);
            if (rootNode != null) {
                for (JsonNode node : rootNode.get("location")) {
                    JsonNode nameNode = node.get("name");
                    JsonNode adm1Node = node.get("adm1");
                    if (nameNode != null &&
                            (nameNode.asText().equals(cityName) || nameNode.asText().equals(city.get(i))) &&
                            adm1Node != null && adm1Node.asText().equals(province.get(i))) {
                        JsonNode idNode = node.get("id");
                        String id = idNode.asText();
                        ids.add(id);
                        break;
                    }
                }
                if (ids.size() == i){
                    ids.add("0");
                }
            }else{
                ids.add("0");
            }
            System.out.println(province.get(i) + city.get(i) + ids.get(i));
            Thread.sleep(300);
        }
        //System.out.println(ids.size());
        for(int i = 0; i < ids.size(); i++){
            if(ids.get(i).equals("0")){
//                temps.add("null");
//                System.out.println(province.get(i) + city.get(i) + temps.get(i));
                continue;
            }
            JsonNode rootNode = GzipApi("v7/weather/now", ids.get(i));
            // 提取温度值
            String temperature = rootNode.path("now").path("temp").asText();
            temps.add(temperature);
            //System.out.println(province.get(i) + city.get(i) + temps.get(i));
            Thread.sleep(300);
        }
    }
}

