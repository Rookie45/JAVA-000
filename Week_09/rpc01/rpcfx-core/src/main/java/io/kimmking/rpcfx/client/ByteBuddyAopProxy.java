package io.kimmking.rpcfx.client;

import io.kimmking.rpcfx.exception.RpcfxException;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteBuddyAopProxy implements RpcfxProxy {

    private ByteBuddyAopProxy() {
    }

    private static class InnerClass {
        private static final ByteBuddyAopProxy INSTANCE = new ByteBuddyAopProxy();
    }

    public static ByteBuddyAopProxy getInstance() {
        return InnerClass.INSTANCE;
    }

    @Override
    public <T> T createProxy(final Class<T> serviceClass, final String url) {
        BytebuddyInvocationHandler handler = new BytebuddyInvocationHandler(serviceClass, url);

        Class<? extends T> cls = new ByteBuddy()
                .subclass(serviceClass)
                .method(ElementMatchers.any())
//                    .intercept(Advice.to(BytebuddyInvocationHandler.class))
                .intercept(MethodDelegation.to(handler, "handler"))
                .make()
                .load(serviceClass.getClassLoader())
//                .load(serviceClass.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
        try {
            return cls.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new RpcfxException(e);
        }
    }

}
