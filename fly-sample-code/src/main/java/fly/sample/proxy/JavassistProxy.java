//package fly.sample.proxy;
//
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtNewConstructor;
//import javassist.CtNewMethod;
//import javassist.util.proxy.MethodFilter;
//import javassist.util.proxy.MethodHandler;
//import javassist.util.proxy.Proxy;
//import javassist.util.proxy.ProxyFactory;
//
//import java.lang.reflect.Method;
//
//public class JavassistProxy {
//    public static void main(String[] args) throws Exception {
//        UserService userService=createJavassistBytecodeDynamicProxy();
//        System.out.println(userService.getName());
//
//    }
//    static interface UserService {
//        public String getName();
//    }
//
//
//    private static UserService createJavassistBytecodeDynamicProxy() throws Exception {
//        ClassPool mPool = new ClassPool(true);
//        CtClass mCtc = mPool.makeClass(UserService.class.getName() + "JavaassistProxy");
//        mCtc.addInterface(mPool.get(UserService.class.getName()));
//        mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
//        mCtc.addMethod(CtNewMethod.make(
//                "public String getName() { return \"javassist name \"; }", mCtc));
//        Class<?> pc = mCtc.toClass();
//        UserService bytecodeProxy = (UserService) pc.getDeclaredConstructor().newInstance();
//        return bytecodeProxy;
//    }
//}
