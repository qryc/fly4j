package fly.sample.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserHander implements InvocationHandler {
    private Object object;

    public UserHander(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        return method.invoke(object, args);
    }
}
