package io.kimmking.rpcfx.client.transfer;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RpcfxHttpClient implements RemoteTransport{

    @Override
    public RpcfxResponse post(RpcfxRequest req, String url) throws Exception {
        log.info("[RpcfxHttpClient][post] request {}, url {}", req, url);
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: " + reqJson);

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
            String respJson = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
            return JSON.parseObject(respJson, RpcfxResponse.class);
        } finally {
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    log.error("[BytebuddyInvocationHandler][post] httpResponse close failed. ",e);
                }
            }
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
        }
    }
}
