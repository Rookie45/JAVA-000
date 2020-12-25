import com.alibaba.fastjson.JSON;

import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;



@Slf4j
public class NettyHttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private Long requestId;
    private Map<Long, CompletableFuture<RpcfxResponse>> responseMap;

    public NettyHttpClientHandler(Long requestId, Map<Long, CompletableFuture<RpcfxResponse>> responseMap) {
        this.requestId = requestId;
        this.responseMap = responseMap;
    }

    // 收到任何一个其他客户端返回的消息，则会进入channelRead0
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {

        //返回的请求
        String respJson = response.content().toString(CharsetUtil.UTF_8);
        RpcfxResponse rpcfxResponse = JSON.parseObject(respJson, RpcfxResponse.class);
        log.info("[NettyHttpClientHandler][channelRead0] rpcfxResponse is {}.", rpcfxResponse);

        CompletableFuture<RpcfxResponse> rpcfxResponseFuture = responseMap.get(requestId);
        if (null != rpcfxResponseFuture) {
            rpcfxResponseFuture.complete(rpcfxResponse);
        } else {
            log.error("找不到requestId={}的请求信息！", requestId);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[NettyHttpClientHandler][exceptionCaught] client call failed, ", cause);
        ctx.close();
    }
}
