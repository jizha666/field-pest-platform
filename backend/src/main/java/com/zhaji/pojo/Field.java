package com.zhaji.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Field {
    private Integer id;
    private Integer areaId;
    private String name;
    private String province;
    private String city;
    private String category;
    private String value;
    private LocalDateTime date;
}
