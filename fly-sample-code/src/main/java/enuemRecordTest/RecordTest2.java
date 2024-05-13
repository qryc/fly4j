package enuemRecordTest;

public class RecordTest2 {
    record User(String fName, String lName, int age) {
    }

    public static void main(String[] args) {
        var user1 = new User("li", "bai", 50);
        var json = RecordJsonUtil.writeValueAsString(user1);
        System.out.println(json);
        var user2= RecordJsonUtil.readValue(json,User.class);
        System.out.println(user2);
    }
}
