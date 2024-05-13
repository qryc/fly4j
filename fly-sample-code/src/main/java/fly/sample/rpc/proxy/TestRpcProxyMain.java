package fly.sample.rpc.proxy;

import fly.sample.rpc.simple.CalService;
import fly.sample.rpc.simple.CalServiceImpl;
import fly.sample.rpc.simple.ServiceProvider;

public class TestRpcProxyMain {
    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            try {
                ServiceProvider provider = new ServiceProvider(9738);
                provider.putServiceObject("rpc.CalService", new CalServiceImpl());
                provider.startService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(300);

        ClientInvoker invoker = new ClientInvoker("127.0.0.1", 9738);
        CalService calService = ProxyFactory.buildProxzy(CalService.class,invoker);
        Integer result = calService.sum(2, 3);
        System.out.println(result);
    }
}
