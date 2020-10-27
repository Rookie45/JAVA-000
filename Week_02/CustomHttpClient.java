package com.sl.java00.week02;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class CustomHttpClient {
    private static final String URL = "http://localhost:8801";
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
//            .setSocketTimeout(1000)
            .build();
    private static final CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.create()
            .build();//默认重试3次

    public static void main(String[] args) {
        System.out.println(httpGet(URL));
    }

    public static String httpGet(String url) {
        String response = null;
        if (null == url) {
            return response;
        }
        HttpGet httpGet = null;
        try {
            URI uri = new URIBuilder(url).build();
            httpGet = new HttpGet(uri);
            response = execute(httpGet);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String execute(HttpRequestBase requestBase) {
        String responseResult = null;
        CloseableHttpResponse response = null;
        try {
            requestBase.setConfig(REQUEST_CONFIG);
            response = HTTP_CLIENT.execute(requestBase);
            if (null == response) {
                return responseResult;
            }
            HttpEntity responseEntity = response.getEntity();
            if (null == responseEntity) {
                return responseResult;
            }
            responseResult = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return responseResult;
    }
}
