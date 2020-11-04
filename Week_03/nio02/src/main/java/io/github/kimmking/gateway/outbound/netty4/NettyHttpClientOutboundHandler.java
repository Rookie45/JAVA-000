package io.github.kimmking.gateway.outbound.netty4;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;


import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {

    //处理response
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        HttpResponse response = null;
        try {
            if (msg instanceof HttpResponse) {
                response = (HttpResponse) msg;
                response.headers().add("nio", "shili");
                response.headers().setInt("Content-Length", Integer.parseInt(response.headers().get("Content-Length")));
            }

            if (msg instanceof HttpContent) {
                exceptionCaught(ctx, new Exception("error response"));
                response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            }
        } finally {
            ctx.writeAndFlush(response);
            //ctx.close();
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