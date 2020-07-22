package com.xz.config;

import com.xz.interfaces.ICityApi;
import com.xz.interfaces.ProxyCreator;
import com.xz.interfaces.impl.JdkProxyCreator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    ProxyCreator jdkProxyCreator() {
        return new JdkProxyCreator();
    }


    @Bean
    FactoryBean<ICityApi> xx(ProxyCreator proxyCreator) {
        return new FactoryBean<ICityApi>() {

            /**
             * 返回代理对象
             */
            @Override
            public ICityApi getObject() {

                return (ICityApi) proxyCreator.createProxy(getObjectType());
            }

            @Override
            public Class<?> getObjectType() {
                return ICityApi.class;
            }
        };
    }

}
