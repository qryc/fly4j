package fly.sample.clone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qryc on 2021/11/10
 */
public class CloneDemo {
    public static void main(String[] args) throws Exception {
        wrongListClone();
        rightListClone();
    }

    static class UserName implements Cloneable {
        public String fname;
        public String lname;

        public UserName(String fname, String lname) {
            this.fname = fname;
            this.lname = lname;
        }

        @Override
        protected UserName clone() throws CloneNotSupportedException {
            return (UserName) super.clone();
        }
    }

    static class User implements Cloneable {
        public UserName userName;

        public User(UserName name) {
            this.userName = name;
        }

        @Override
        protected User clone() throws CloneNotSupportedException {
            User user = (User) super.clone();
            user.userName = userName.clone();
            return user;
        }
    }

    private static void wrongListClone() {
        //初始化集合元素<小白>
        ArrayList<User> users = new ArrayList<>();
        User user = new User(new UserName("小", "白"));
        users.add(user);

        //先克隆，然后改变原对象的值为<小黑>,输出发现克隆对象也跟着变为<小黑>
        List<User> usersClone = (ArrayList<User>) users.clone();
        user.userName.lname = "黑";
        usersClone.forEach(u -> System.out.println(u.userName.fname + u.userName.lname));
    }

    private static ArrayList<User> cloneUsers(ArrayList<User> users) throws CloneNotSupportedException {
        ArrayList<User> cloneList = new ArrayList<>();
        for (User u : users) {
            cloneList.add(u.clone());
        }
        return cloneList;
    }

    private static void rightListClone() throws CloneNotSupportedException {
        //初始化集合元素<小白>
        ArrayList<User> users = new ArrayList<>();
        User user = new User(new UserName("小", "白"));
        users.add(user);

        //先克隆，然后改变原对象的值为<小黑>,输出克隆对象依然为<小白>
        List<User> usersClone = cloneUsers(users);
        user.userName.lname = "黑";
        usersClone.forEach(u -> System.out.println(u.userName.fname + u.userName.lname));

    }


}
