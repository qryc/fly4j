package bit;

public class IntegerBit {
    public static void main(String[] args) {
        System.out.println("0: %s".formatted(IntegerBit.toBinaryString(0)));
        System.out.println(Integer.toBinaryString(1));
        System.out.println(Integer.toBinaryString(-1));
        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));
        System.out.println(Integer.toBinaryString(Integer.MIN_VALUE));
//        System.out.println(Integer.numberOfLeadingZeros(1));
//        System.out.println(Integer.numberOfLeadingZeros(Integer.MIN_VALUE));
    }

    public static String toBinaryString(Integer i) {
        String str = Integer.toBinaryString(i);
        if (str.length() == 32) {
            return str;
        }
        return "0".repeat(32 - str.length()) + str;
    }
}
