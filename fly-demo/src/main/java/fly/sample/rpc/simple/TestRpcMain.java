package fly.sample.rpc.simple;


public class TestRpcMain {
    public static void main(String[] args) throws Exception {
        //启动服务器,注册服务，并监听端口
        new Thread(() -> {
            try {
                ServiceProvider provider = new ServiceProvider(9738);
                provider.putServiceObject("fly.sample.rpc.simple.CalService", new CalServiceImpl());
                provider.startService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(300);

        ServiceClient serviceClient = new ServiceClient("127.0.0.1", 9738);
        //得到客户端代理
        CalService calServiceClient = serviceClient.getClientService(CalService.class);
        //通过客户端代理向服务器发送命令
        Integer result = calServiceClient.sum(2, 3);
        System.out.println(result);
    }
}
