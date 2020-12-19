package test;

/**
 * 测试volatile
 */
public class TestVolatile {
    //程序一秒之后停止
    static volatile boolean run = true;

    //程序一秒之后不会停止
    //static boolean run = true;
    //原因：
    //因为jvm即时编译技术，run变量被放到局部内存中；
    //不用volatile修饰，程序会从局部内存中读取值；
    //用volatile修饰，程序会从主内存中读取值；
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (run) {
            }
        });
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("停止");
        run = false;
    }
}
