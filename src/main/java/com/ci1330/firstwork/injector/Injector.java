package com.ci1330.firstwork.injector;

public interface Injector {
    Object getBeanByName(String beanName);
    Object getBeanByType(String beanType);
}
