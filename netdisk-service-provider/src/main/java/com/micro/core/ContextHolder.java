package com.micro.core;

public abstract class ContextHolder {
    private static ThreadLocal<ContextRequest> tlReq = new ThreadLocal<ContextRequest>();
    private static ThreadLocal<ContextResponse> tlRes = new ThreadLocal<ContextResponse>();

    public static void setTlReq(ContextRequest tlReq) {
        ContextHolder.tlReq.set(tlReq);
    }

    public static void setTlRes(ContextResponse tlRes) {
        ContextHolder.tlRes.set(tlRes);
    }

    public static ContextRequest getTlReq() {
        return tlReq.get();
    }

    public static ContextResponse getTlRes() {
        return tlRes.get();
    }

    public static void clearReq() {
        tlReq.remove();
    }

    public static void clearRes() {
        tlRes.remove();
    }
}
