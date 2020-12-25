package io.kimmking.rpcfx.client.proxy;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.transport.RpcfxHttpClient;
import io.kimmking.rpcfx.client.transport.TransportWrapper;
import io.kimmking.rpcfx.exception.RpcfxException;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BytebuddyInvocationHandler {
    private Class<?> serviceClass;
    private String url;

    public BytebuddyInvocationHandler(final Class<?> serviceClass, final String url) {
        this.serviceClass = serviceClass;
        this.url = url;
    }

    @RuntimeType
    public Object invoke(@This Object proxy, @Origin Method method, @AllArguments @RuntimeType Object[] args) {

        RpcfxResponse response = null;
        try {

            TransportWrapper transportWrapper = new TransportWrapper(serviceClass, url, new RpcfxHttpClient());
            response = transportWrapper.post(method, args);
            return response.getResult();
        } catch (Exception e) {
            throw new RpcfxException(e);
        }
    }

}
