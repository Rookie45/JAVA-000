import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class NettyHttpClient implements RemoteTransport {

    private static final String host = "localhost";
    private static final int port = 8080;

    private final static AtomicLong nextRequestId = new AtomicLong(1);

    private final Map<Long, CompletableFuture<RpcfxResponse>> responseMap = new ConcurrentHashMap<>();

    private static NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    private static ChannelFuture channelFuture;

    private static Channel channel;

    public void initNettyClient(Long requestId, Map<Long, CompletableFuture<RpcfxResponse>> respMap) {

//        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("httpClientCodec", new HttpClientCodec());
                            pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(65535));
                            pipeline.addLast("httpContentDecompressor", new HttpContentDecompressor());
                            pipeline.addLast("httpClientHandler", new NettyHttpClientHandler(requestId, respMap));
                        }
                    });

            channelFuture = bootstrap.connect(host, port).sync();
            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            log.error("[NettyHttpClient][initNettyClient] connect happen exception: ", e);
        }
    }

    public static long next() {
        return nextRequestId.getAndIncrement();
    }

    @Override
    public RpcfxResponse post(RpcfxRequest req, String url) throws Exception {
        String reqJson = JSON.toJSONString(req);
        ByteBuf content = Unpooled.copiedBuffer(reqJson, CharsetUtil.UTF_8);

        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0,
                HttpMethod.POST, url, content);
        request.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        request.headers().add(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
//        request.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        // 这里好像有线程安全问题？压测试试
        long requestId = next();
        CompletableFuture<RpcfxResponse> completableFuture = new CompletableFuture<>();
        responseMap.put(requestId, completableFuture);
        initNettyClient(requestId, responseMap);

        channel.writeAndFlush(request)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            System.out.println("连接成功...");
                        } else {
                            System.out.println("连接出现异常...");
                            channel.close().sync();
                            workerGroup.shutdownGracefully();
                        }
                    }
                });

        //回头看一下原理，这个get是异步？
        return completableFuture.get();
    }

}
