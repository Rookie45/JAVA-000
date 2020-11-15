学习笔记

5-01-1
```java
package java00.week05;

import java.lang.reflect.Proxy;
import java.lang.reflect.Method;

/**
 * 利用java.lang.reflect 包中的Proxy类和InvocationHandler接口提供了生成动态代理类的能力
 * 从Proxy类的方法newProxyInstance()，可以看出要求代理的对象必须有接口
 */
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

package java00.week05;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB 运行时在内存中动态生成一个子类对象从而实现对目标对象功能的扩展
 * 既然是通过子类进行代理，那么要求代理类必须能被继承，所以不能是
 * final类，也不能是无法外部访问的内部类
 */
public class CGLibProxy2AOP implements MethodInterceptor {

    private Object target;
    private BeforeAdvice beforeAdvice;
    private AfterAdvice afterAdvice;

    public Object creatProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
        if (null != beforeAdvice) {
            beforeAdvice.before();
        }
        Object result = method.invoke(target, args);
        if (null != afterAdvice) {
            afterAdvice.after();
        }
        return result;
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

```


参考

[细说Spring——AOP详解（动态代理实现AOP）](https://www.jianshu.com/p/aaeb2355ec5c)
[bean实例化方法](https://github.com/geektime-geekbang/geekbang-lessons/blob/master/%E7%AC%AC%E5%9B%9B%E7%AB%A0%EF%BC%9ASpring%20Bean%E5%9F%BA%E7%A1%80.pdf)
