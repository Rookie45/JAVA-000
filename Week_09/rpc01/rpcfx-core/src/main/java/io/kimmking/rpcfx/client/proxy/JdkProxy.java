package io.kimmking.rpcfx.client.proxy;

import io.kimmking.rpcfx.client.Rpcfx;
import io.kimmking.rpcfx.client.RpcfxProxy;
import io.kimmking.rpcfx.exception.RpcfxException;

import java.lang.reflect.Proxy;

public class JdkProxy implements RpcfxProxy{

    private JdkProxy() {
    }

    private static class InnerClass {
        private static final JdkProxy INSTANCE = new JdkProxy();
    }

    public static JdkProxy getInstance() {
        return InnerClass.INSTANCE;
    }

    @Override
    public <T> T createProxy(final Class<T> serviceClass, final String url) throws RpcfxException {

        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new JdkInvocationHandler(serviceClass, url));
    }


}
