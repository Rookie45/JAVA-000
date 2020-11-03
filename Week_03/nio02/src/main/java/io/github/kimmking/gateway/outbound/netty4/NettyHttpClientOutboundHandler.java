package io.github.kimmking.gateway.outbound.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.http.protocol.HTTP;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {

    private String url;
    private FullHttpRequest request;

    public NettyHttpClientOutboundHandler(String url) {
        this.url = url;
        this.request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, this.url);
    }

    //发送请求
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        request.headers().add(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        request.headers().add(HTTP.CONTENT_LEN, request.content().readableBytes());
        ctx.writeAndFlush(request);
    }

    //处理返回
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        FullHttpResponse response = null;
        try {
            if (msg instanceof FullHttpResponse) {
                response = (FullHttpResponse) msg;
//            handleResponse(request, ctx, response);
                response.headers().set("nio", "shili");
            }
            if (msg instanceof HttpContent) {
                exceptionCaught(ctx, new Exception("no response"));
                response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            }
        } finally {
            if (request != null) {
                if (!HttpUtil.isKeepAlive(request)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
            ctx.flush();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

//    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx,
//                                final FullHttpResponse endpointResponse) {
//
//    }
}