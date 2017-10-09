package versionone.codistro.github.io.organicfarm;

/**
 * Created by RAJAN on 05-10-2017.
 */

public class Price {

    private double cowMilkPrice;
    private double buffaloMilkPrice;

    public Price(){
    }

    public Price(double cowMilkPrice, double buffaloMilkPrice) {
        this.cowMilkPrice = cowMilkPrice;
        this.buffaloMilkPrice = buffaloMilkPrice;
    }

    public double getCowMilkPrice() {
        return cowMilkPrice;
    }

    public double getBuffaloMilkPrice() {
        return buffaloMilkPrice;
    }
}
