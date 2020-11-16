package com.sl.homework.lesson09.spring.aop;

import java.lang.reflect.Proxy;
import java.lang.reflect.Method;

public class JDKProxy2AOP {

    private Object target;
    private BeforeAdvice beforeAdvice;
    private AfterAdvice afterAdvice;

    public Object creatProxy() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?>[] interfaces = target.getClass().getInterfaces();

        return Proxy.newProxyInstance(classLoader, interfaces,
                (Object proxy, Method method, Object[] args) -> {
                    if (null != beforeAdvice) {
                        beforeAdvice.before();
                    }
                    Object result = method.invoke(target, args);
                    if (null != afterAdvice) {
                        afterAdvice.after();
                    }
                    return result;
                });
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public BeforeAdvice getBeforeAdvice() {
        return beforeAdvice;
    }

    public void setBeforeAdvice(BeforeAdvice beforeAdvice) {
        this.beforeAdvice = beforeAdvice;
    }

    public AfterAdvice getAfterAdvice() {
        return afterAdvice;
    }

    public void setAfterAdvice(AfterAdvice afterAdvice) {
        this.afterAdvice = afterAdvice;
    }
}
