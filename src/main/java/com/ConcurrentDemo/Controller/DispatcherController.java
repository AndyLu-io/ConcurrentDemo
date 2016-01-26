package com.ConcurrentDemo.Controller;

//import java.util.concurrent.ArrayBlockingQueue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author   : qianweifeng
 * date     : 16/1/22.
 * describe : 任务分派调整器
 */
public class DispatcherController implements Runnable {
    private Semaphore seats;
    private AtomicInteger ChiefNum2;
    private AtomicInteger BusBoyNum2;
    private LinkedBlockingQueue<Dishes> beforeDishes;
    private LinkedBlockingQueue<Dishes> afterDishes;
    private LinkedBlockingQueue<Dishes> finishDishes;
    private ArrayList<Future> chiefList = new ArrayList<>();
    private ArrayList<Future> busboyList = new ArrayList<>();
    private ExecutorService inner = Executors.newCachedThreadPool();
    private int ChiefNum;
    private int BusBoyNum;

    public DispatcherController(LinkedBlockingQueue<Dishes> beforeDishes) {
        this.beforeDishes = beforeDishes;
        afterDishes = new LinkedBlockingQueue<>();
        finishDishes = new LinkedBlockingQueue<>();
        seats = new Semaphore(10, true);
        ChiefNum = 2;
        BusBoyNum = 2;
        ChiefNum2 = new AtomicInteger(ChiefNum);
        BusBoyNum2 = new AtomicInteger(ChiefNum);

        startWord();
    }

    public DispatcherController(int ChiefNum, int BusBoyNum, int seatsNum, LinkedBlockingQueue<Dishes> beforeDishes) {
        this.beforeDishes = beforeDishes;
        afterDishes = new LinkedBlockingQueue<>();
        finishDishes = new LinkedBlockingQueue<>();
        seats = new Semaphore(seatsNum, true);
        this.ChiefNum = ChiefNum;
        this.BusBoyNum = BusBoyNum;
        ChiefNum2 = new AtomicInteger(ChiefNum);
        BusBoyNum2 = new AtomicInteger(ChiefNum);
        startWord();
    }

    private void adjust() throws NoSuchFieldException, IllegalAccessException {
        if (beforeDishes.size() / ChiefNum2.get() > 2) {
            Chief c = new Chief(beforeDishes, afterDishes);
            Future future = inner.submit(c);
            chiefList.add(future);
            ChiefNum2.incrementAndGet();
            return;
        }
        if (afterDishes.size() / BusBoyNum2.get() > 2) {
            BusBoy b = new BusBoy(afterDishes, finishDishes);
            Future future = inner.submit(b);
            busboyList.add(future);
            BusBoyNum2.incrementAndGet();
            return;
        }
        if (beforeDishes.size() / ChiefNum2.get() < 1) {
            if(chiefList.size()>0) {
                Future f = chiefList.remove(chiefList.size() - 1);
                Class<? extends Future> c = f.getClass();
                Field field = c.getDeclaredField("runner");
                field.setAccessible(true);
                if(((Thread)field.get(f)).getState() != Thread.State.RUNNABLE) {
                    ((Thread) field.get(f)).interrupt();
                }
                ChiefNum2.decrementAndGet();
                return;
            }
        }
        if (afterDishes.size() / BusBoyNum2.get() < 1) {
            if(busboyList.size()>0) {
                Future f = busboyList.remove(busboyList.size() - 1);
                Class<? extends Future> c = f.getClass();
                Field field = c.getDeclaredField("runner");
                field.setAccessible(true);
                if(((Thread)field.get(f)).getState() != Thread.State.RUNNABLE) {
                    ((Thread) field.get(f)).interrupt();
                }
                BusBoyNum2.decrementAndGet();
                return;
            }
        }
        //System.out.println("chiefNum=" + chiefList.size() + ",busboyNum=" + busboyList.size());
    }


    public ExecutorService getInner() {
        return inner;
    }

    private void startWord() {
        for (int i = 0; i < ChiefNum; i++) {
            Chief c = new Chief(beforeDishes, afterDishes);
            inner.execute(c);
        }

        for (int i = 0; i < BusBoyNum; i++) {
            BusBoy b = new BusBoy(afterDishes, finishDishes);
            inner.execute(b);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(150);
                adjust();
            }
        } catch (InterruptedException e) {
            System.err.println("DispatcherController has stop");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}
