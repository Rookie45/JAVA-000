package io.kimmking.rpcfx.client.proxy;


import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.transport.NettyHttpClient;
import io.kimmking.rpcfx.client.transport.TransportWrapper;
import io.kimmking.rpcfx.exception.RpcfxException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JdkInvocationHandler implements InvocationHandler {

    private final Class<?> serviceClass;
    private final String url;

    public <T> JdkInvocationHandler(Class<T> serviceClass, String url) {
        this.serviceClass = serviceClass;
        this.url = url;
    }

    // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
    // int byte char float double long bool
    // [], data class


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        RpcfxResponse response = null;
        try {

//            TransportWrapper transportWrapper = new TransportWrapper(serviceClass, url, new RpcfxOkHttp());
            TransportWrapper transportWrapper = new TransportWrapper(serviceClass, url, new NettyHttpClient());
            response = transportWrapper.post(method, args);
            return response.getResult();
        } catch (Exception e) {
            throw new RpcfxException(e);

        }
        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException
    }

}
