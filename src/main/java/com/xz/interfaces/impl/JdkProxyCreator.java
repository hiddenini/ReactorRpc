package com.xz.interfaces.impl;

import com.xz.annotations.ApiServer;
import com.xz.domain.MethodInfo;
import com.xz.domain.ServerInfo;
import com.xz.interfaces.ProxyCreator;
import com.xz.interfaces.RestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.LinkedHashMap;

@Slf4j
public class JdkProxyCreator implements ProxyCreator {


    @Override
    public Object createProxy(Class<?> clazz,RestHandler restHandler) {

        log.info("createProxy:{}", clazz);

        ServerInfo serverInfo = extractServerInfo(clazz);

        log.info("serverInfo:{}", serverInfo);


        //RestHandler restHandler = new WebClientRestHandler();

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
        extractUrlAndMethod(method, methodInfo);
        //构建请求参数

        extractParamsAndBody(method, args, methodInfo);

        //提取返回对象信息
        extractReturnInfo(method, methodInfo);

        return methodInfo;
    }

    private void extractReturnInfo(Method method, MethodInfo methodInfo) {
        //返回 flux还是mono
        boolean assignableFrom = method.getReturnType().isAssignableFrom(Flux.class);
        methodInfo.setReturnFlux(assignableFrom);
        //获取返回对象的实际类型
        methodInfo.setReturnElementType(extractElementType(method.getGenericReturnType()));
    }


    private Class extractElementType(Type genericReturnType) {
        ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return (Class) actualTypeArguments[0];
    }

    /**
     * 得到请求的param和body
     */
    private void extractParamsAndBody(Method method, Object[] args, MethodInfo methodInfo) {
        Parameter[] parameters = method.getParameters();
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            PathVariable annotation = parameters[i].getAnnotation(PathVariable.class);
            //是否有PathVariable注解
            if (annotation != null) {
                params.put(annotation.value(), args[i]);
            }
            //是否有
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                methodInfo.setBody((Mono<?>) args[i]);
                methodInfo.setBodyElementType(extractElementType(parameters[i].getParameterizedType()));
            }
        }
        methodInfo.setParams(params);
    }

    /**
     * 得到请求的url和HttpMethod
     *
     * @param method
     * @param methodInfo
     */
    private void extractUrlAndMethod(Method method, MethodInfo methodInfo) {
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
    }

}
