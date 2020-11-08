package Integer;

/**
 * @author 53137
 */
public class IntegerTest {
    public static void main(String[] args) {
        //第一段
        int a = 1;
        int b = 1;
        System.out.println(a == b);

        //第二段
        Integer a1 = 1;
        Integer b1 = 1;
        System.out.println(a1 == b1);

        //第三段
        int a2 = 1;
        Integer b2 = 1;
        System.out.println(a2 == b2);

        //第四段
        int c = 1000;
        int d = 1000;
        System.out.println(c == d);

        //第五段
        Integer c1 = 1000;
        Integer d1 = 1000;
        System.out.println(c1 == d1);

        //第五段equals
        Integer c11 = 1000;
        Integer d11 = 1000;
        System.out.println(c11.equals(d11));

        //第六段
        int c2 = 1000;
        Integer d2 = 1000;
        System.out.println(c2 == d2);

    }
}
