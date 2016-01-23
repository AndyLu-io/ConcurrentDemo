package Controller;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * author   : qianweifeng
 * date     : 16/1/22.
 * describe : 餐馆工
 */
public class BusBoy implements Runnable {

    LinkedBlockingQueue<Dishes> afterDishes;
    LinkedBlockingQueue<Dishes> finishDishes;

    public BusBoy() {
    }

    public BusBoy(LinkedBlockingQueue<Dishes> afterDishes, LinkedBlockingQueue<Dishes> finishDishes) {
        this.afterDishes = afterDishes;
        this.finishDishes = finishDishes;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Dishes d = afterDishes.take();
                TimeUnit.MILLISECONDS.sleep(5);
                finishDishes.put(d);
                d.getCustomer().getAck(d.getName(), d.getId());
                System.out.println("BusBoy: name=" + Thread.currentThread().getName() + " put " + d + " into finish");
            } catch (InterruptedException e) {
                System.err.println("BusBoy:" + Thread.currentThread().getName() + " already stop");
            }
        }
    }
}
