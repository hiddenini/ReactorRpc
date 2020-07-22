package com.xz.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.Map;

@Data
@Builder
public class MethodInfo {

    /**
     * 请求url
     */
    private String url;

    /**
     * 请求方法
     */
    private HttpMethod method;

    /**
     * 请求参数
     */
    private Map<String, Object> params;

    /**
     * 请求body
     */
    private Mono<?> body;
}
