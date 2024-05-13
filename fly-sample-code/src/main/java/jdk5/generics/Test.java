package jdk5.generics;

public class Test {
    public static void main(String[] args) {
        Userinfo userinfo=new Userinfo();
        userinfo.pin="testPin";
        userinfo.config=convert2Obj("fly.cn");
        saveUser(userinfo);
    }

    public static void saveUser(Userinfo userinfo){
        System.out.println(userinfo.pin+userinfo.config);
        convert2Str(userinfo.config);
    }
    public static void convert2Str(Object obj){
        System.out.println(obj);
    }
    public static Object convert2Obj(String str){
        Config config=new Config();
        config.homePage=str;
        return config;
    }
    static class Userinfo<T>{
        String pin;
        T config;
    }
    static class Config{
        @Override
        public String toString() {
            return "Config{" +
                    "homePage='" + homePage + '\'' +
                    '}';
        }

        String homePage;
    }

}
