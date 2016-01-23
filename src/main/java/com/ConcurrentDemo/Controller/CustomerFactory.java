package com.ConcurrentDemo.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
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
    public List<Future<?>> list = new ArrayList<>();
    public List<Thread> list2 = new ArrayList<>();

    public CustomerFactory(Resturant resturant, int number) {
        this.resturant = resturant;
        this.number = number;
    }


    @Override
    public void run() {
        int sum = 0;
        while (number > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(r.nextInt(30));
                Customer c = new Customer(r.nextInt(7), resturant);
                Thread t = new Thread(c);
                list2.add(t);
                t.start();
                number--;
                sum++;
            } catch (InterruptedException e) {
                System.err.println("factory already stop");
                break;
            }
        }
        System.err.println("factory has stop total " + sum + " customer");
    }
}
