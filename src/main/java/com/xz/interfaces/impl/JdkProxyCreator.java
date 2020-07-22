package com.xz.interfaces.impl;

import com.xz.domain.MethodInfo;
import com.xz.domain.ServerInfo;
import com.xz.interfaces.ICityApi;
import com.xz.interfaces.ProxyCreator;
import com.xz.interfaces.RestHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
                    return null;
                }
        );
    }

    public ServerInfo extractServerInfo(Class<?> clazz) {
        return null;
    }

    public MethodInfo extractMethodInfo(Method method, Object[] args) {
        return null;
    }

}
