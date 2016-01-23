package Controller;

//import java.util.concurrent.ArrayBlockingQueue;

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

    private void adjust() {
        if (beforeDishes.size() / ChiefNum2.get() > 2) {
            inner.execute(new Chief(beforeDishes, afterDishes));
        }
        if (afterDishes.size() / BusBoyNum2.get() > 2) {
            inner.execute(new BusBoy(afterDishes, finishDishes));
        }
    }

    public ExecutorService getInner() {
        return inner;
    }

    public void setInner(ExecutorService inner) {
        this.inner = inner;
    }

    private void startWord() {
        for (int i = 0; i < ChiefNum; i++) {
            inner.execute(new Chief(beforeDishes, afterDishes));
        }

        for (int i = 0; i < BusBoyNum; i++) {
            inner.execute(new BusBoy(afterDishes, finishDishes));
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(100);
                adjust();
            }
        } catch (InterruptedException e) {
            System.err.println("DispatcherController has stop");
        }

    }
}
