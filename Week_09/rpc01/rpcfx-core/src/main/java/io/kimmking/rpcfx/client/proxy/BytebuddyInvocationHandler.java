package io.kimmking.rpcfx.client.proxy;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.RpcfxException;
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

public class BytebuddyInvocationHandler {
    private Class<?> serviceClass;
    private String url;

    public BytebuddyInvocationHandler(final Class<?> serviceClass, final String url) {
        this.serviceClass = serviceClass;
        this.url = url;
    }

    @RuntimeType
    public Object invoke(@This Object proxy, @Origin Method method, @AllArguments @RuntimeType Object[] args) {
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(this.serviceClass);
        request.setMethod(method.getName());
        request.setParams(args);
        RpcfxResponse response = postWithHttp(request, url);

        if (response.isStatus()) {
            return response.getResult();
        }
        else {
            throw new RpcfxException(response.getException());
        }
    }

    // 使用httpclient
    private RpcfxResponse postWithHttp(RpcfxRequest req, String url) {
        RpcfxResponse response = new RpcfxResponse();
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: " + reqJson);
        String respJson = null;

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(6000)
                .setConnectionRequestTimeout(6000)
                .setSocketTimeout(6000)
                .build();

        CloseableHttpResponse httpResponse = null;
        HttpPost httpPost = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnPerRoute(2)
                .setMaxConnTotal(10)
                .setDefaultRequestConfig(config)
                .build()){

            httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(reqJson);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.addHeader("Accept-Charset", "utf-8");

            httpResponse = httpClient.execute(httpPost);
            respJson = EntityUtils.toString(httpResponse.getEntity());
            response.setResult(respJson);
            response.setStatus(true);
        } catch (IOException e) {
            response.setResult(new RpcfxException(500, e));
            response.setStatus(false);
        } finally {
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    response.setResult(new RpcfxException(500, e));
                }
            }
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
        }
        return response;
    }
}
