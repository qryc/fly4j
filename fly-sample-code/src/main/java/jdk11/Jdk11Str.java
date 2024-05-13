package jdk11;

public class Jdk11Str {
    public static void main(String[] args) {
        System.out.println(" ".isBlank());
        System.out.println(" abc ".strip());
        System.out.println(" abc ".trim());


        "a\nb\nc".lines().forEach(System.out::println);
        System.out.println("-".repeat(10)+"end"+"-".repeat(10));
    }
}
