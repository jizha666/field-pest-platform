package com.zhaji.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pest {
    private Integer id;
    private String province;
    private String city;
    private String name;
    private String land;
    private Integer number;
    private String temperature;
    private String humidity;
    private String date;

    private List dates;
    private List values;
}
