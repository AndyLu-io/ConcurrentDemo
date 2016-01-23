package com.ConcurrentDemo.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * author   : qianweifeng
 * date     : 16/1/22.
 * describe : 饭店类
 */
public class Resturant {
    private int seats;
    private int ChiefNum;
    private int BusBoyNum;
    private DispatcherController controller;
    public Semaphore seat;
    private Thread t;
    public LinkedBlockingQueue<Dishes> beforeDishes = new LinkedBlockingQueue<>();
    public ExecutorService outerPool = Executors.newCachedThreadPool();

    public Resturant() {
        this.seats = 10;
        this.ChiefNum = 2;
        this.BusBoyNum = 2;
        seat = new Semaphore(seats, false);
    }

    public Resturant(int seats, int ChiefNum, int BusBoyNum) {
        this.seats = seats;
        this.ChiefNum = ChiefNum;
        this.BusBoyNum = BusBoyNum;
        seat = new Semaphore(seats, true);
    }

    public void startWork() {
        controller = new DispatcherController(beforeDishes);
        t = new Thread(controller);
        t.start();
    }

    public void endWork() {
        controller.getInner().shutdown();
        t.interrupt();
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getChiefNum() {
        return ChiefNum;
    }

    public void setChiefNum(int chiefNum) {
        ChiefNum = chiefNum;
    }

    public int getBusBoyNum() {
        return BusBoyNum;
    }

    public void setBusBoyNum(int busBoyNum) {
        BusBoyNum = busBoyNum;
    }
}
