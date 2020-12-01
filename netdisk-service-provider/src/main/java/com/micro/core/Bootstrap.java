package com.micro.core;

public class Bootstrap {
    private HandlerInitializer handlerInitializer;

    public void childHandler(HandlerInitializer handlerInitializer){
        this.handlerInitializer=handlerInitializer;
        handlerInitializer.initChannel(handlerInitializer);
    }

    public ContextResponse execute(){
        //1.执行责任链
        execHandler(handlerInitializer.firstHandler, ContextHolder.getTlReq(), ContextHolder.getTlRes());
        //2.获取响应结果
        ContextResponse res = ContextHolder.getTlRes();

        //3.清空
        ContextHolder.clearReq();
        ContextHolder.clearRes();
        return res;
    }

    private void execHandler(Handler handler,ContextRequest request,ContextResponse response){
        handler.doHandler(request,response);

        //递归执行（下一个节点）
        if(handler.nextHandler!=null){
            execHandler(handler.nextHandler,ContextHolder.getTlReq(),ContextHolder.getTlRes());
        }
    }

}
