package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.filter.PreHttpRequestFilter;
import io.github.kimmking.gateway.outbound.httpclient.HttpclientOutboundHandler;
import io.github.kimmking.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.github.kimmking.gateway.outbound.netty4.NettyHttpClient;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.github.kimmking.gateway.router.HttpEndpointRouter;
import io.github.kimmking.gateway.router.RibbonHttpEndpointRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);

    private final String proxyServer;
    private final List<String> proxyServers;

    private HttpOutboundHandler handler;
    private OkhttpOutboundHandler okhttpHandler;
    private HttpclientOutboundHandler httpclientHandler;
    private NettyHttpClient client;
    private HttpRequestFilter requestFilter;
    private HttpEndpointRouter endpointRouter;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        this.proxyServers = Arrays.asList(proxyServer);

        requestFilter = new PreHttpRequestFilter();
        endpointRouter = new RibbonHttpEndpointRouter();

        handler = new HttpOutboundHandler(this.proxyServer);
        okhttpHandler = new OkhttpOutboundHandler(this.proxyServer);
        httpclientHandler = new HttpclientOutboundHandler(this.proxyServer);
        client = new NettyHttpClient(this.proxyServer);
    }

    public HttpInboundHandler(List<String> proxyServers) {
        this.proxyServer = proxyServers.get(0);
        this.proxyServers = proxyServers;

        requestFilter = new PreHttpRequestFilter();
        endpointRouter = new RibbonHttpEndpointRouter();

        handler = new HttpOutboundHandler(endpointRouter.route(proxyServers));
        okhttpHandler = new OkhttpOutboundHandler(endpointRouter.route(proxyServers));
        httpclientHandler = new HttpclientOutboundHandler(endpointRouter.route(proxyServers));
        client = new NettyHttpClient(endpointRouter.route(proxyServers));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
//            String uri = fullRequest.uri();
//            //logger.info("接收到的请求url为{}", uri);
//            if (uri.contains("/test")) {
//                handlerTest(fullRequest, ctx);
//            }

            //pre filter
            requestFilter.filter(fullRequest, ctx);

            okhttpHandler.setUrl(endpointRouter.route(this.proxyServers));
//            client.setUrl(endpointRouter.route(this.proxyServers));
//            handler.handle(fullRequest, ctx);
            okhttpHandler.handle(fullRequest,ctx);
//            httpclientHandler.handle(fullRequest,ctx);
//            client.connect(fullRequest, ctx);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

//    private void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
//        FullHttpResponse response = null;
//        try {
//            String value = "hello,kimmking";
//            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
//            response.headers().set("Content-Type", "application/json");
//            response.headers().setInt("Content-Length", response.content().readableBytes());
//
//        } catch (Exception e) {
//            logger.error("处理测试接口出错", e);
//            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
//        } finally {
//            if (fullRequest != null) {
//                if (!HttpUtil.isKeepAlive(fullRequest)) {
//                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
//                } else {
//                    response.headers().set(CONNECTION, KEEP_ALIVE);
//                    ctx.write(response);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }

}
