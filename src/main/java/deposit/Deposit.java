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

    public synchronized boolean withdraw(int amount){
        if(balance>=amount){
            balance = balance - amount;
            return true;
        }
        return false;
    }

    public synchronized boolean deposit(int amount){
        if(amount+balance<=bound){
            balance = balance + amount;
            return true;
        }else{
            return false;
        }
    }

    public Deposit(int id, int balance, int bound, String customer){
        this.id = id;
        this.balance = balance;
        this.bound = bound;
        this.customer = customer;
    }
}
