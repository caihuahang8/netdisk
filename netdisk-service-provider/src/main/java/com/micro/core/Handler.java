package com.micro.core;

public abstract  class Handler {
    public Handler nextHandler;
    public void setNextHandler( Handler nextHandler) {
        this.nextHandler =  nextHandler;
    }
    public void updateResponse(ContextResponse contextResponse) {
        ContextHolder.clearRes();
        ContextHolder.setTlRes(contextResponse);
    }
    public void updateRequest(ContextRequest contextRequest) {
        ContextHolder.clearReq();
        ContextHolder.setTlReq(contextRequest);
    }
    public abstract void doHandler(ContextRequest request,ContextResponse response);


}
