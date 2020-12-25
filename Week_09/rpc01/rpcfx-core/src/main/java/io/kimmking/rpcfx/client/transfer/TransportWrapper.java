package io.kimmking.rpcfx.client.transfer;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class TransportWrapper {
    private final Class<?> serviceClass;
    private final String url;
    private RemoteTransport transport;

    public TransportWrapper(Class<?> serviceClass, String url, RemoteTransport transport) {
        this.serviceClass = serviceClass;
        this.url = url;
        this.transport = transport;
    }

    public RpcfxResponse post(Method method, Object[] args) throws Exception {
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(this.serviceClass);
        request.setMethod(method.getName());
        request.setParams(args);
        return transport.post(request, url);
    }

}

