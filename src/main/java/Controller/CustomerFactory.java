package Controller;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * author   : qianweifeng
 * date     : 16/1/22.
 * describe :
 */
public class CustomerFactory implements Runnable {
    private int number;
    private Resturant resturant;
    private Random r = new Random();

    public CustomerFactory(Resturant resturant, int number) {
        this.resturant = resturant;
        this.number = number;
    }


    @Override
    public void run() {
        while (number > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(r.nextInt(30));
                int delay = r.nextInt(60);
                Customer c = new Customer(2, resturant);
                resturant.outerPool.execute(c);
                number--;
            } catch (InterruptedException e) {
                System.err.println("factory already stop");
                break;
            }
        }
        System.err.println("factory has stop");
    }
}
