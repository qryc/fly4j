package fly.sample.rpc.proxy;

import java.lang.reflect.Proxy;

public class ProxyFactory {
    /**
     * 得到客户端调用代理
     */
    public static  <T> T buildProxzy(Class<T> interfaceClass,Invoker invoker) {
        JdkInvocationHandler jdkInvocationHandler = new JdkInvocationHandler(invoker);
        ClassLoader classLoader = ClientInvoker.class.getClassLoader();
        T proxyInstance = (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, jdkInvocationHandler);
        return proxyInstance;
    }
}
