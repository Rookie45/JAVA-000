package io.github.kimmking.gateway.outbound.netty4;

import io.github.kimmking.gateway.inbound.HttpInboundInitializer;
import io.github.kimmking.gateway.outbound.netty4.NettyHttpClientOutboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.http.protocol.HTTP;

public class NettyHttpClient {

    private String url;

    public NettyHttpClient(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void connect(FullHttpRequest fullRequest, ChannelHandlerContext ctx) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
//                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
//                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new HttpClientCodec());
//                    ch.pipeline().addLast(new NettyHttpClientOutboundHandler());
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect("localhost", 8088).sync();
            String fullUrl = this.url + fullRequest.uri();
            FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, fullUrl);
            //请求gateway的header全部拷贝到endpoint的请求里
            httpRequest.headers().add(fullRequest.headers());
//            httpRequest.headers().set(HTTP.TARGET_HOST, "localhost");
//            httpRequest.headers().set(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
//            httpRequest.headers().set(HTTP.CONTENT_LEN, httpRequest.content().readableBytes());
//            f.channel().write(request);
//            f.channel().flush();
            //发送请求到endpoint
            f.channel().writeAndFlush(httpRequest);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}