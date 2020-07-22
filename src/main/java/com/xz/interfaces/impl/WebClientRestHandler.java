package com.xz.interfaces.impl;

import com.xz.domain.MethodInfo;
import com.xz.domain.ServerInfo;
import com.xz.interfaces.RestHandler;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientRestHandler implements RestHandler {
    @Override
    public Object invokeRest(MethodInfo methodInfo) {
        return null;
    }

    @Override
    public void init(ServerInfo serverInfo) {

    }
}
