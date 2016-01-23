package Controller;

/**
 * author   : qianweifeng
 * date     : 16/1/22.
 * describe : 菜肴类
 */
public class Dishes {
    private static int index;
    private final int id = index++;
    private String name;
    private int delay;
    private Customer customer;

    public Dishes(String name, Customer customer) {
        this.name = name;
        this.customer = customer;
        delay = 30;
    }

    public Dishes(String name, int delay, Customer customer) {
        this.name = name;
        this.customer = customer;
        this.delay = delay;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "DishesID=" + id;
    }
}
