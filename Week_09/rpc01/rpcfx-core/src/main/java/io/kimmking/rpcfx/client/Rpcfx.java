package io.kimmking.rpcfx.client;


import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.client.proxy.ByteBuddyProxy;
import io.kimmking.rpcfx.client.proxy.JdkProxy;

import java.util.HashMap;


public final class Rpcfx {

    private static HashMap<String, RpcfxProxy> proxyMap = new HashMap<>();

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
        proxyMap.put("JDK", JdkProxy.getInstance());
        proxyMap.put("ByteBuddy", ByteBuddyProxy.getInstance());
    }

    public static <T> T create(final Class<T> serviceClass, final String url) {


        //return JdkProxy.getInstance().createProxy(serviceClass, url);
        
        return proxyMap.get("ByteBuddy").createProxy(serviceClass, url);

        // 0. 替换动态代理 -> AOP
//        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url));

    }

}
