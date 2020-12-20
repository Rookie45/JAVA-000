package io.kimmking.rpcfx.exception;

public class RpcfxException extends RuntimeException{
    private int code;

    public RpcfxException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcfxException(String message) {
        super(message);
    }

    public RpcfxException(Throwable cause) {
        super(cause);
    }

    public RpcfxException(int code) {
        this.code = code;
    }

    public RpcfxException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public RpcfxException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RpcfxException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
