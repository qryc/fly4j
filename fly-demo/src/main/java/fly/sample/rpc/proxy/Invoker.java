package fly.sample.rpc.proxy;

public interface Invoker {
     Object invokeServiceMethod(Class serviceClass, String methodName, Class[] argumentsClass, Object arguments);
}
