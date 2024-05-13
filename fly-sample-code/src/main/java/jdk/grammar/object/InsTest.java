package jdk.grammar.object;

import java.io.IOException;
import java.io.InputStream;

public class InsTest {
    public static void main(String[] args) throws Exception {

        //引导类加载器无对应对象
        System.out.println(String.class.getClassLoader());

        ClassLoader classLoader = Animal.class.getClassLoader();
        System.out.println(classLoader);
        MyClassLoader myClassLoader = new MyClassLoader();
        Object test1 = myClassLoader.loadClass("jdk.grammar.object.InsTest").getDeclaredConstructor().newInstance();
        InsTest test2 = new InsTest();
        System.out.println(test1 instanceof InsTest);
        System.out.println(test2 instanceof InsTest);
        System.out.println(test1.getClass().equals(InsTest.class));


    }


}

class MyClassLoader extends ClassLoader {
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        try {
            String filename = name.substring(name.lastIndexOf(".") + 1) + ".class";
            InputStream inputStream = getClass().getResourceAsStream(filename);
            if (inputStream == null) {
                return super.loadClass(name);
            }
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            return defineClass(name, buffer, 0, buffer.length);
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }
    }
}
