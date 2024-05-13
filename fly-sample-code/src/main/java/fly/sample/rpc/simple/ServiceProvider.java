package fly.sample.rpc.simple;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {
    private int port;
    private Map<String, Object> servicesPool = new HashMap<>();

    public ServiceProvider(int port) {
        this.port = port;
    }

    public void startService() throws Exception {

        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            try {
                System.out.println("service is start;wait for accept:");
                Socket socket = serverSocket.accept();

                //读取客户端传过来的字节，并使用协议进行解析，此处使用的是Object流协议，真实场景可以改为MsgPack等协议
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                String interfaceName = objectInputStream.readUTF();
                String methodName = objectInputStream.readUTF();
                Class<?>[] parameterType = (Class<?>[]) objectInputStream.readObject();
                Object[] params = (Object[]) objectInputStream.readObject();

                //调用内部服务
                Object service = servicesPool.get(interfaceName);
                Class<?> aClass = Class.forName(interfaceName);
                Method method = aClass.getMethod(methodName, parameterType);
                Object result = method.invoke(service, params);

                //调用结果进行协议编码，输出到客户端
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void putServiceObject(String key, Object serviceObject) {
        this.servicesPool.put(key, serviceObject);
    }
}
