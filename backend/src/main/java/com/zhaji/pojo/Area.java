package com.zhaji.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Area {
    private Integer id;
    private String name;
    private String province;
    private String city;
    private Integer number;
}
