package Parafarm;

import java.util.concurrent.atomic.AtomicLong;
import java.io.Serializable;

public class Product implements Serializable{
    private static final AtomicLong idGen = new AtomicLong();
    private long productid = idGen.incrementAndGet();
    private String product_name;
    private double product_price;
    private String product_category;

    public Product(String product_name, double product_price) {
    	this.product_name = product_name;
    	this.product_price = product_price;
    }
    
    public long getId() { return productid; }
    public String getName() { return product_name; }
    public double getPrice() { return product_price; }
    public String getCategory() { return product_category; }
}