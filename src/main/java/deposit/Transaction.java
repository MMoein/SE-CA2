package deposit;

/**
 * Created by Moein on 11/18/15.
 */
public class Transaction {
    private int id = -1;
    private String type = "";
    private String deposit = "";
    private int amount = 0;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getDeposit() {
        return deposit;
    }

    public Transaction(int id,String type,String deposit,String amount){
        this.amount = Utils.decodeAmount(amount);
        this.deposit = deposit;
        this.type = type;
        this.id = id;
    }


}
