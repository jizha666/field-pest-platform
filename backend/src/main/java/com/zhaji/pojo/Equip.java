package com.zhaji.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equip {
    private Integer id;
    private Integer fieldId;
    private String name;
    private String landName;
    private String category;
    private Integer status;
}
