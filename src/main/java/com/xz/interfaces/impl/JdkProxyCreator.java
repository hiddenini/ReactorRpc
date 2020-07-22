package com.xz.interfaces.impl;

import com.xz.annotations.ApiServer;
import com.xz.domain.MethodInfo;
import com.xz.domain.ServerInfo;
import com.xz.interfaces.ProxyCreator;
import com.xz.interfaces.RestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;

@Slf4j
public class JdkProxyCreator implements ProxyCreator {


    @Override
    public Object createProxy(Class<?> clazz) {

        log.info("createProxy:{}", clazz);

        ServerInfo serverInfo = extractServerInfo(clazz);

        log.info("serverInfo:{}", serverInfo);


        RestHandler restHandler = null;

        restHandler.init(serverInfo);

        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{clazz},
                (Object proxy, Method method, Object[] args) -> {

                    /**
                     * 根据方法和参数得到调用信息
                     */
                    MethodInfo methodInfo = extractMethodInfo(method, args);

                    log.info("methodInfo:{}", methodInfo);


                    /**
                     * 调用rest接口 ,这里使用webClient,后面可能会有其他实现,所以抽象成接口
                     */
                    return restHandler.invokeRest(methodInfo);
                }
        );
    }

    public ServerInfo extractServerInfo(Class<?> clazz) {
        ApiServer annotation = clazz.getAnnotation(ApiServer.class);
        //拿到注解上面的value,这里是url
        String value = annotation.value();
        log.info("url:{}", value);
        ServerInfo serverInfo = ServerInfo.builder().url(value).build();
        return serverInfo;
    }

    /**
     * 根据method和args构建
     */
    public MethodInfo extractMethodInfo(Method method, Object[] args) {
        MethodInfo methodInfo = new MethodInfo();
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GetMapping) {
                GetMapping a = (GetMapping) annotation;
                methodInfo.setUrl(a.value()[0]);
                methodInfo.setMethod(HttpMethod.GET);
            } else if (annotation instanceof PostMapping) {
                PostMapping a = (PostMapping) annotation;
                methodInfo.setUrl(a.value()[0]);
                methodInfo.setMethod(HttpMethod.POST);
            } else if (annotation instanceof DeleteMapping) {
                DeleteMapping a = (DeleteMapping) annotation;
                methodInfo.setUrl(a.value()[0]);
                methodInfo.setMethod(HttpMethod.DELETE);
            } else if (annotation instanceof PutMapping) {
                PutMapping a = (PutMapping) annotation;
                methodInfo.setUrl(a.value()[0]);
                methodInfo.setMethod(HttpMethod.PUT);
            }
        }
        //构建请求参数

        return null;
    }

}
