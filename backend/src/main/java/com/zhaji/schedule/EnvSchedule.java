package com.zhaji.schedule;

import com.fasterxml.jackson.databind.JsonNode;
import com.zhaji.service.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zhaji.utils.GzipApiUtils.GzipApi;

@Component
@Slf4j
public class EnvSchedule {

    @Autowired
    private FieldService fieldService;

    // 每天12:00:00执行
    @Scheduled(cron = "0 0 12 * * ?")
    public void executeAtNoon() throws IOException, InterruptedException {
        List<String> province = fieldService.selectProvince();
        List<String> city = fieldService.selectCity();
        List<String> ids = new ArrayList<>();
        List<String> temps = new ArrayList<>();
        List<Integer> areaIds = new ArrayList<>();
        List<String> fieldIds = new ArrayList<>();
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
                if (ids.size() == i) {
                    ids.add("0");
                }
            } else {
                ids.add("0");
            }
            System.out.println(province.get(i) + city.get(i) + ids.get(i));
            Thread.sleep(300);
        }
        //System.out.println(ids.size());
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).equals("0")) {
                temps.add("null");
//                System.out.println(province.get(i) + city.get(i) + temps.get(i));
                continue;
            }
            JsonNode rootNode = GzipApi("v7/weather/now", ids.get(i));
            // 提取温度值
            String temperature = rootNode.path("now").path("temp").asText();
            temps.add(temperature);
            System.out.println(province.get(i) + city.get(i) + temps.get(i));
            Thread.sleep(300);
        }

        areaIds = fieldService.selectAreaId(province, city);
        for (int i = 0; i < areaIds.size(); i++) {
            List<Integer> fieldId = fieldService.selectFieldId(areaIds.get(i));
            for (int j = 0; j < fieldId.size(); j++) {
                if (temps.get(j).equals("null"))
                    continue;
                fieldService.addRecord(fieldId.get(j), temps.get(i));
            }
        }

    }
}


