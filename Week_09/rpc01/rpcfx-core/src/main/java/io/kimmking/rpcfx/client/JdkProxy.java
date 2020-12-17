package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.RpcfxException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxy implements RpcfxProxy {

    private JdkProxy(){}

    private static class InnerClass {
        private static final JdkProxy INSTANCE = new JdkProxy();
    }
    public static JdkProxy getInstance() {
        return InnerClass.INSTANCE;
    }

    @Override
    public <T> T createProxy(final Class<T> serviceClass, final String url) {

        T target = null;
        try {
            JdkInvocationHandler handler = new JdkInvocationHandler(serviceClass, url);
            target = (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, handler);
        } catch (IllegalArgumentException e) {
            throw new RpcfxException(e);
        }
        return target;
    }

}
