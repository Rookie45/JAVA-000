package io.kimmking.rpcfx.client.transfer;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;

public interface RemoteTransport {
    RpcfxResponse post(RpcfxRequest req, String url) throws Exception;
}
