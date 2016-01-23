package Controller;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * author   : qianweifeng
 * date     : 16/1/22.
 * describe : 厨师类
 */
public class Chief implements Runnable {
    LinkedBlockingQueue<Dishes> beforeDishes;
    LinkedBlockingQueue<Dishes> afterDishes;

    public Chief() {
    }

    public Chief(LinkedBlockingQueue<Dishes> beforeDishes, LinkedBlockingQueue<Dishes> afterDishes) {
        this.beforeDishes = beforeDishes;
        this.afterDishes = afterDishes;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Dishes d = beforeDishes.take();
                TimeUnit.MILLISECONDS.sleep(50);
                afterDishes.put(d);
                System.out.println("Cheif: name=" + Thread.currentThread().getName() + " put " + d + " into after");
            } catch (InterruptedException e) {
                System.err.println("Chief:" + Thread.currentThread().getName() + " already stop");
                break;
            }
        }
    }
}
