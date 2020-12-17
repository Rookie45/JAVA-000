package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.RpcfxException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JdkInvocationHandler implements InvocationHandler {
    private final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    private final Class<?> serviceClass;
    private final String url;

    public <T> JdkInvocationHandler(Class<T> serviceClass, String url) {
        this.serviceClass = serviceClass;
        this.url = url;
    }

    // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
    // int byte char float double long bool
    // [], data class

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) {
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(this.serviceClass.getName());
        request.setMethod(method.getName());
        request.setParams(params);

        RpcfxResponse response = post(request, url);
        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException

        if (response.isStatus()) {
            return response.getResult();
        } else {
            throw response.getException();
        }
    }

    private RpcfxResponse post(RpcfxRequest req, String url) {
        RpcfxResponse response = new RpcfxResponse();
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: " + reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = null;
        try {
            ResponseBody respBody = client.newCall(request).execute().body();
            if (null != respBody) {
                respJson = respBody.string();
            }
            response.setResult(reqJson);
            response.setStatus(true);

            throw new IOException("happen io exception");
        } catch (IOException e) {
            e.printStackTrace();
            response.setException(new RpcfxException(500, e));
            response.setStatus(false);
        }
        System.out.println("resp json: " + respJson);
//            response = JSON.parseObject(respJson, RpcfxResponse.class);
        return response;
    }


}
