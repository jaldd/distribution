package test;

public class Test1 {
    public static void main(String[] args) {
        String tel = "1860013981";
        System.out.println(tel.matches(("^1[3456789]\\d{9}$")));
    }
}
