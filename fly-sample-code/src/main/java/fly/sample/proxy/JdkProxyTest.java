package fly.sample.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JdkProxyTest {
    public static void main(String[] args) {
        InvocationHandler handler = new UserHander(new UserServiceImpl());

        UserService proxy = getProxy(UserService.class, handler);
        System.out.println(proxy.getUser(1L));

    }

    private static <T> T getProxy(Class<T> interfaceClass, InvocationHandler handler) {
        ClassLoader classLoader = JdkProxyTest.class.getClassLoader();
        T result = (T)Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, handler);
        return result;
    }
}
