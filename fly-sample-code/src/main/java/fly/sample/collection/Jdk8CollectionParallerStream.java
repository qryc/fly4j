//package fly.sample.collection;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class Jdk8CollectionParallerStream {
//    public static void main(String[] args) {
//        List<User> users = new ArrayList();
//        for (int i = 0; i < 5; i++)
//            users.add(new User(i, "a" + i));
//
//        users.parallelStream()
//                .forEach(System.out::println);
//
//    }
//
//
//    static class User {
//        public int id;
//        public String name;
//
//        @Override
//        public String toString() {
//            return id + " " + name;
//        }
//
//        public User(int id, String name) {
//            this.id = id;
//            this.name = name;
//        }
//    }
//
//
//}
//
