package io.kimmking.rpcfx.client.proxy;

import io.kimmking.rpcfx.client.RpcfxProxy;
import io.kimmking.rpcfx.exception.RpcfxException;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteBuddyProxy implements RpcfxProxy {

    private ByteBuddyProxy() {
    }

    private static class InnerClass {
        private static final ByteBuddyProxy INSTANCE = new ByteBuddyProxy();
    }

    public static ByteBuddyProxy getInstance() {
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
            throw new RpcfxException(e);
        }
    }

}
