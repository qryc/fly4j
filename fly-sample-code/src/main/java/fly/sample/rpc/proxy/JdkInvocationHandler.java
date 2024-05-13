package fly.sample.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 客户端调用代理处理器
 */
class JdkInvocationHandler implements InvocationHandler {
    private Invoker invoker;

    public JdkInvocationHandler(Invoker serviceClient) {
        this.invoker = serviceClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return invoker.invokeServiceMethod(method.getDeclaringClass(), method.getName(), method.getParameterTypes(), args);
    }
}
