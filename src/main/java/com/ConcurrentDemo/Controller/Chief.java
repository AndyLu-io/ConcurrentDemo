package com.ConcurrentDemo.Controller;

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
    private boolean interruped = false;
    public Chief() {
    }

    public Chief(LinkedBlockingQueue<Dishes> beforeDishes, LinkedBlockingQueue<Dishes> afterDishes) {
        this.beforeDishes = beforeDishes;
        this.afterDishes = afterDishes;
    }

    public boolean isInterruped() {
        return interruped;
    }

    public void setInterruped(boolean interruped) {
        this.interruped = interruped;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                System.err.println("Chief:" + Thread.currentThread().getName() + " start work");
                Dishes d = beforeDishes.take();
                TimeUnit.MILLISECONDS.sleep(50);
                afterDishes.put(d);
//                System.out.println("Cheif: name=" + Thread.currentThread().getName() + " put " + d + " into after");
//                if (interruped){
//                    System.err.println("Chief:" + Thread.currentThread().getName() + " already stop");
//                    Thread.currentThread().interrupt();
//                }
            } catch (InterruptedException e) {
                System.err.println("Chief:" + Thread.currentThread().getName() + " already stop");
                break;
            }
        }
    }
}
