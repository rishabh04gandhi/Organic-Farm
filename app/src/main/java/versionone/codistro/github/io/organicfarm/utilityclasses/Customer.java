package versionone.codistro.github.io.organicfarm.utilityclasses;

/**
 * Created by RAJAN on 04-10-2017.
 */

public class Customer {
    private String id;
    private String name;
    private String mobile;

    public Customer(){

    }

    public Customer(String id, String name, String mobile) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }
}
