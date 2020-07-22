package com.xz.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class City {
    /**
     * 城市编号
     */
    private String id;

    /**
     * 省份编号
     */
    private Long provinceId;

    /**
     * 城市名称
     */
    @NotBlank
    private String cityName;

    /**
     * 描述
     */
    private String description;

}
