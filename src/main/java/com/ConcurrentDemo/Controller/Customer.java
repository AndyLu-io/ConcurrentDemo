package com.ConcurrentDemo.Controller;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * author   : qianweifeng
 * date     : 16/1/22.
 * describe : 顾客
 */
public class Customer implements Runnable {
    private static int index;
    private final int id = index++;
    private int DishesNum;
    private Resturant resturant;
    private HashMap<String, Boolean> map = new HashMap<>();

    public Customer() {
        DishesNum = 3;
    }

    public Customer(int delayTime, Resturant resturant) {
        this.DishesNum = delayTime;
        this.resturant = resturant;
//        System.err.println("Customer : id" + id + " come in");
        //System.out.println(resturant.seat.availablePermits());
    }

    public int getId() {
        return id;
    }

    public int getDishesNum() {
        return DishesNum;
    }

    public void setDishesNum(int dishesNum) {
        DishesNum = dishesNum;
    }

    @Override
    public String toString() {
        return "Customer : id=" + id + " , DishesNum=" + DishesNum;
    }

    public void getAck(String dishName, int dishID) {
        boolean ack = map.get(dishName + " dishID=" + dishID);
        if (ack) {
            System.out.println("系统错误! dishName=" + dishName + " dishState=" + ack);
            System.exit(1);
        }
        map.put(dishName + " dishID=" + dishID, true);
//        System.out.println(map.toString());
    }

    @Override
    public void run() {
        try {
            resturant.seat.tryAcquire();
            long start = System.currentTimeMillis();
            for (int i = 0; i < DishesNum; i++) {
                Dishes d = new Dishes("Customer" + id + "'s dish", this);
                resturant.beforeDishes.put(d);
                map.put(d.getName() + " dishID=" + d.getId(), false);
            }
            while (true) {
                TimeUnit.MILLISECONDS.sleep(30);
                if (map.containsValue(false)) {
                    continue;
                } else {
//                    System.out.println(map.toString());
                    break;
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("Customer : id=" + id + " , DishesNum=" + DishesNum + " , status=finish , useTime=" + (end - start));
            resturant.seat.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
