package Parafarm;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.io.Serializable;

public class Customer implements Serializable{
private static final AtomicLong idGen = new AtomicLong();
private long customerid = idGen.incrementAndGet();
private String name;
private String surname;
private long phone;
private String address;
private String email;
public ArrayList<Order> orders = new ArrayList<>();

Customer(String name,long phone, String address){
	this.name = name;
	this.phone = phone;
	this.address = address;
}


public String getName() { return name; }

public void addOrder(Order order) {
    orders.add(order);
}

}



