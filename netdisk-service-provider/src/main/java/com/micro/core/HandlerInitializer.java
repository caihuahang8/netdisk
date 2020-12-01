package com.micro.core;

public abstract class HandlerInitializer extends PipelineImpl {
    public HandlerInitializer(ContextRequest request,ContextResponse response){
        ContextHolder.setTlReq(request);
        ContextHolder.setTlRes(response);
    }

    protected abstract void initChannel(Pipeline pipeline);
}
