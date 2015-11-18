package deposit;

/**
 * Created by Moein on 11/18/15.
 */
public class Deposit {
    private int id;
    private String customer;
    private int balance;
    private int bound;


    public int getBalance() {
        return balance;
    }

    public int getBound() {
        return bound;
    }

    public int getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setBound(int bound) {
        this.bound = bound;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Deposit(int id, int balance, int bound, String customer){
        this.id = id;
        this.balance = balance;
        this.bound = bound;
        this.customer = customer;
    }
}
