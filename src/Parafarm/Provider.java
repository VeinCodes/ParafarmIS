package Parafarm;

import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.io.Serializable;

public class Provider implements Serializable{
    private static final AtomicLong idGen = new AtomicLong();
    private long customerid = idGen.incrementAndGet();
    private String name;
    private String surname;
    private String phone;
    private String address;
    private String email;
    private ArrayList<ProviderOrder> orders;

    public Provider(String name, String email, String address) {
    }

    public void add_order(ProviderOrder provorder) {
    	orders.add(provorder);
    }
}