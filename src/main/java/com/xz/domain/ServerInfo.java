package com.xz.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerInfo {

    /**
     * 服务
     */
    private String url;
}
