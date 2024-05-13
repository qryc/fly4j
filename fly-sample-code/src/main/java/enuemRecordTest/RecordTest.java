package enuemRecordTest;

public class RecordTest {
    record User(String fName, String lName, int age) {
        public String getFullName() {
            return fName + " " + lName;
        }
    }

    public static void main(String[] args) {
        var user = new User("li", "bai", 50);
        System.out.println(user.getFullName() + " " + user.age());
        System.out.println(user);

        //instanceof
        Object o1 = user;
        if (o1 instanceof User user1) {
            System.out.println(user1.getFullName());
        }
    }
}
