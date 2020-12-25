package io.kimmking.rpcfx.client.transport;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


@Slf4j
public class RpcfxOkHttp implements RemoteTransport{

    private final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    @Override
    public RpcfxResponse post(RpcfxRequest req, String url) throws Exception {
        log.info("[RpcfxOkHttp][post] request {}, url {}", req, url);
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: " + reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request)
                .execute()
                .body()
                .string();

        System.out.println("resp json: " + respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}
