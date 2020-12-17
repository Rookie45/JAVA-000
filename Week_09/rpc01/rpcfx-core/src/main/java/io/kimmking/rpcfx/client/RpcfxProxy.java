package io.kimmking.rpcfx.client;

import io.kimmking.rpcfx.exception.RpcfxException;

public interface RpcfxProxy {
    <T> T createProxy(final Class<T> serviceClass, final String url) throws RpcfxException;
}
