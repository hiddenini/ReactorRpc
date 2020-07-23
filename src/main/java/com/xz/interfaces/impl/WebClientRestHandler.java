package com.xz.interfaces.impl;

import com.xz.domain.MethodInfo;
import com.xz.domain.ServerInfo;
import com.xz.interfaces.RestHandler;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientRestHandler implements RestHandler {

    private WebClient webClient;

    /**
     * 进行rest请求调用
     */
    @Override
    public Object invokeRest(MethodInfo methodInfo) {
        Object result = null;
        WebClient.RequestBodySpec accept = this.webClient.method(methodInfo.getMethod())
                .uri(methodInfo.getUrl(), methodInfo.getParams())
                .accept(MediaType.APPLICATION_JSON);

        WebClient.ResponseSpec retrieve = null;

        /**
         * 如果带有@RequestBody
         */
        if (methodInfo.getBody() != null) {
            retrieve = accept.body(methodInfo.getBody(), methodInfo.getBodyElementType()).retrieve();
        } else {
            retrieve = accept.retrieve();
        }

        /**
         * 处理返回值类型
         */
        if (methodInfo.isReturnFlux()) {
            result = retrieve.bodyToFlux(methodInfo.getReturnElementType());
        } else {
            result = retrieve.bodyToMono(methodInfo.getReturnElementType());
        }
        return result;
    }

    /**
     * 初始化webClient
     */
    @Override
    public void init(ServerInfo serverInfo) {
        this.webClient = WebClient.create(serverInfo.getUrl());
    }
}
