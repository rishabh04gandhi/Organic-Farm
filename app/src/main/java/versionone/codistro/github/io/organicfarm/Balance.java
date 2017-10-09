package versionone.codistro.github.io.organicfarm;

/**
 * Created by RAJAN on 05-10-2017.
 */

public class Balance {

    private String id;
    private Double balance;
    private Double total;

    public Balance(){

    }

    public Balance(String id,Double balance, Double total) {
        this.id = id;
        this.balance = balance;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getTotal() {
        return total;
    }
}
