package com.xz.interfaces;

import com.xz.domain.MethodInfo;
import com.xz.domain.ServerInfo;

public interface RestHandler {

    /**
     * 调用rest请求,返回结果
     */
    Object invokeRest(MethodInfo methodInfo);

    /**
     * 初始化服务器信息
     */
    void init(ServerInfo serverInfo);
}
