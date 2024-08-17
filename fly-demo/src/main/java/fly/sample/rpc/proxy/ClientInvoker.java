package fly.sample.rpc.proxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientInvoker implements Invoker {
    private String serverIp;
    private int serverPort;

    public ClientInvoker(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    @Override
    public Object invokeServiceMethod(Class serviceClass, String methodName, Class[] argumentsClass, Object arguments) {
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
