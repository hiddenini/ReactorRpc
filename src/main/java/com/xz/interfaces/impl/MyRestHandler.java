package com.xz.interfaces.impl;

import com.xz.domain.MethodInfo;
import com.xz.domain.ServerInfo;
import com.xz.interfaces.RestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * 自己实现的RestHandler 使用@Component注入到spring容器时,那么此时就不会走CommonConfig中的配置的默认的RestHandler
 *
 * {@link MyProxyCreator}
 *
 * 这2个自定义的类主要是为了演示    @ConditionalOnMissingBean 以及后续如果有其他实现了大概的做法
 */
@Slf4j
//@Component
public class MyRestHandler implements RestHandler {

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
        log.info("MyRestHandler result:{}", result);
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
