package io.github.kimmking.gateway.outbound.httpclient;

import io.github.kimmking.gateway.outbound.httpclient4.NamedThreadFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
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
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpclientOutboundHandler {
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
//            .setSocketTimeout(1000)
            .build();
    private String url;
    private ExecutorService proxyService;
    private CloseableHttpClient httpClient;

    public HttpclientOutboundHandler(String url) {
        this.url = url;
        this.httpClient = HttpClientBuilder.create()
                .build();//默认重试3次

        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        //初始化线程池
        proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        final String url = this.url + fullRequest.uri();
        proxyService.submit(() -> httpGet(fullRequest, ctx, url));
    }

    private void httpGet(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final String url) {
        CloseableHttpResponse response = null;
        try {
            URI uri = new URIBuilder(url).build();
            HttpGet httpGet  = new HttpGet(uri);
            //请求gateway的header全部拷贝到endpoint的请求里
            for (Map.Entry<String, String> header : fullRequest.headers()) {
                httpGet.addHeader(header.getKey(), header.getValue());
            }
            httpGet.setConfig(REQUEST_CONFIG);
            response = httpClient.execute(httpGet);
            handleResponse(fullRequest, ctx, response);
        } catch (URISyntaxException | IOException e) {
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
    }

    private void handleResponse(FullHttpRequest fullRequest, ChannelHandlerContext ctx, CloseableHttpResponse response) {
        FullHttpResponse proxyResponse = null;
        try {
            byte[] body = EntityUtils.toByteArray(response.getEntity());
            proxyResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(body));
            proxyResponse.headers().setInt("Content-Length", Integer.parseInt(response.getFirstHeader("Content-Length").getValue()));
            //header里塞自定义的key-value
//            fullRequest.headers().entries();
            proxyResponse.headers().add("nio", fullRequest.headers().get("nio"));
            //将endpoint返回的header信息塞到代理的response对象里
            for (Header header : response.getAllHeaders()) {
                proxyResponse.headers().set(header.getName(), header.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
            proxyResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(proxyResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(proxyResponse);
                }
            }
            ctx.flush();
        }
    }


    private void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

//    private void execute(HttpRequestBase requestBase) {
//        String responseResult = null;
//        CloseableHttpResponse response = null;
//        try {
//            requestBase.setConfig(REQUEST_CONFIG);
//            response = httpClient.execute(requestBase);
//            if (null == response) {
//                return responseResult;
//            }
//            HttpEntity responseEntity = response.getEntity();
//            if (null == responseEntity) {
//                return responseResult;
//            }
//            responseResult = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != response) {
//                    response.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//        return responseResult;
//    }
}
