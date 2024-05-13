package fly.sample.rpc.simple;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ServiceClient {
    private String serverIp;
    private int serverPort;

    public ServiceClient(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    /**
     * 得到客户端调用代理
     */
    public <T> T getClientService(Class<T> interfaceClass) {
        JdkInvocationHandler jdkInvocationHandler = new JdkInvocationHandler();
        ClassLoader classLoader = ServiceClient.class.getClassLoader();
        T proxyInstance = (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, jdkInvocationHandler);
        return proxyInstance;
    }

    /**
     * 客户端代理处理器向服务器端发送命令
     */
    private class JdkInvocationHandler implements InvocationHandler {

        public JdkInvocationHandler() {
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return this.invokeServiceMethod(method.getDeclaringClass(), method.getName(), method.getParameterTypes(), args);
        }

        private Object invokeServiceMethod(Class serviceClass, String methodName, Class[] argumentsClass, Object arguments) {
            try {
                Socket socket = new Socket(serverIp, serverPort);

                //调用信息进行协议编码为字节，传输服务器端，真实场景可以改为MsgPack等协议
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.writeUTF(serviceClass.getName());
                output.writeUTF(serviceClass.getMethod(methodName, argumentsClass).getName());
                output.writeObject(serviceClass.getMethod(methodName, argumentsClass).getParameterTypes());
                output.writeObject(arguments);

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                return input.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }


}
