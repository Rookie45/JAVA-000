package io.kimmking.rpcfx.client;


import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.client.proxy.JdkProxy;


public final class Rpcfx {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static <T> T create(final Class<T> serviceClass, final String url) {


        return JdkProxy.getInstance().createProxy(serviceClass, url);

        // 0. 替换动态代理 -> AOP
//        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url));

    }

}
