package com.xz;

import com.xz.interfaces.RestHandler;
import com.xz.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.xz.interfaces.ProxyCreator;

/**
 * @author xz
 * @date 2020/4/22 15:28
 **/

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        RestHandler bean = SpringUtil.getBean(RestHandler.class);
        log.info("容器中的RestHandler:{}", bean);

        ProxyCreator proxyCreator = SpringUtil.getBean(ProxyCreator.class);
        log.info("容器中的ProxyCreator:{}", proxyCreator);

    }

}
