package com.xz.config;

import com.xz.interfaces.ICityApi;
import com.xz.interfaces.ProxyCreator;
import com.xz.interfaces.RestHandler;
import com.xz.interfaces.impl.JdkProxyCreator;
import com.xz.interfaces.impl.WebClientRestHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    @ConditionalOnMissingBean
    ProxyCreator jdkProxyCreator() {
        return new JdkProxyCreator();
    }

    /**
     * 这里为了做成类型配置的形式,将RestHandler设置为ProxyCreator中共的createProxy方法的参数,
     * <p>
     * 但是这里很别扭,如果外部使用cglib进行动态代理的话,需要实现的方法里面带上了RestHandler
     * <p>
     * 这个RestHandler的实例是在这里被初始化的
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    RestHandler restHandler() {
        return new WebClientRestHandler();
    }

    /**
     * 通过FactoryBean将ICityApi 接口注入到spring容器之中
     * <p>
     * ICityApi是通过jdk动态代理生成
     * <p>
     * rest请求是通过WebClient构建
     * <p>
     * 如何把WebClient也做成可选的配置
     *
     * @param proxyCreator
     * @return
     */
    @Bean
    FactoryBean<ICityApi> xx(ProxyCreator proxyCreator, RestHandler restHandler) {
        return new FactoryBean<ICityApi>() {

            /**
             * 返回代理对象
             */
            @Override
            public ICityApi getObject() {

                return (ICityApi) proxyCreator.createProxy(getObjectType(), restHandler);
            }

            @Override
            public Class<?> getObjectType() {
                return ICityApi.class;
            }
        };
    }

}
