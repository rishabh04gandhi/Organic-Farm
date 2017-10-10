package versionone.codistro.github.io.organicfarm.utilityclasses;

import java.util.Date;

/**
 * Created by RAJAN on 04-10-2017.
 */

public class Sales {

    private String id;
    private double quantity;
    private String type;
    private double paid;
    private long date;

    public Sales(){

    }

    public Sales(String id,double quantity,double paid,String type){
        this.id = id;
        this.quantity = quantity;
        this.paid = paid;
        this.type = type;
        this.date = new Date().getTime();
    }

    public double getPaid() {
        return paid;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getQuantity() {
        return quantity;
    }

    public long getDate() {
        return date;
    }
}
