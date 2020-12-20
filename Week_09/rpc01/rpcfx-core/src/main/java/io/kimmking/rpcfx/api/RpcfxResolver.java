package io.kimmking.rpcfx.api;

public interface RpcfxResolver {

    <T> T resolve(Class<T> serviceClass);

}
