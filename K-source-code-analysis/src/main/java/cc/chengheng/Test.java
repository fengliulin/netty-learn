package cc.chengheng;

public class Test {

    private String string = new String();
    private static String string1 = new String();

    static {
         string1 = new String();
    }

    public static void main(String[] args) {
        boolean isSafe = false;
        assert isSafe;
        System.out.println("断言通过!");
    }

    interface abc {

    }
    public void test() {
        A a = new A();
        System.out.println(string1);
        System.out.println();
        System.out.println();
    }
}
