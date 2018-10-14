package com.tail.rpc.client;

import com.tail.rpc.model.RpcRequest;
import com.tail.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author weidong
 * @date create in 13:05 2018/10/13
 **/
@Slf4j
public class RpcProxyInvoker<T> implements InvocationHandler {

    private Class<T> inter;

    private RpcConnectManager manager;


    public RpcProxyInvoker(Class<T> inter, String zkAddr) {
        manager = new RpcConnectManager(zkAddr);
        this.inter = inter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setServiceClass(method.getDeclaringClass());
        request.setMethodName(method.getDeclaringClass().getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        log.info("调用rpc服务,请求参数:{}",request.toString());

        RpcResponse response = manager.handle(request);

        return response;
    }
}
