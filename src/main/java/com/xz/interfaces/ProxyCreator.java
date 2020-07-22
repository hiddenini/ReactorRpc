package com.xz.interfaces;

/**
 * 创建代理接口  可能是jdk 可能是cglib 所以需要抽象成接口 便于以后扩展
 */
public interface ProxyCreator {
    Object createProxy(Class<?> clazz);
}
