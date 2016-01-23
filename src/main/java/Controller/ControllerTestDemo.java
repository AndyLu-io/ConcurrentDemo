package Controller;

import java.util.concurrent.TimeUnit;

/**
 * author   : qianweifeng
 * date     : 16/1/22.
 * describe : 测试类
 */
public class ControllerTestDemo {
    public static void main(String[] args) {
        try {
            Resturant resturant = new Resturant();
            CustomerFactory factory = new CustomerFactory(resturant, 50);
            resturant.startWork();
            Thread t = new Thread(factory);
            t.start();
            TimeUnit.SECONDS.sleep(1);
            resturant.endWork();
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
