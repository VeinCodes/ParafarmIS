package Parafarm;

import java.util.concurrent.atomic.AtomicLong;
import java.io.Serializable;

public class Stock implements Serializable{
    private static final AtomicLong idGen = new AtomicLong();
    private long stockid = idGen.incrementAndGet();
    private String product_name;
    private int current_amount;
    private String warehouse_position;
    private int limit;
    private String last_restock;
    private int product_number;
    private int usual_order_amount;
    private Provider usual_provider;
    private double product_price;

    public Stock(String product_name, int limit, int current_amount, double product_price, int usual_order_amount) {
        this.product_name = product_name;
        this.limit = limit;
        this.current_amount = current_amount;
        this.product_price = product_price;
        this.usual_order_amount = usual_order_amount;
    }

    public void restock(Provider provider) {
        this.current_amount += this.usual_order_amount;
        this.last_restock = java.time.LocalDateTime.now().toString();
        this.usual_provider = provider;
    }

    public void setLimit(int limit) {
    	this.limit = limit;
    }

    public boolean CheckStockandLimit() {
       return current_amount <= limit?true:false;
    }
    
    public void setCurrentAmount(int amount) {
        this.current_amount = amount;
    }
    
    
    public long getId() { return stockid; }
    public String getProductName() { return product_name; }
    public int getCurrentAmount() { return current_amount; }
    public String getWarehousePosition() { return warehouse_position; }
    public int getLimit() { return limit; }
    public String getLastRestock() { return last_restock; }
    public int getProductNumber() { return product_number; }
    public int getUsualOrderAmount() { return usual_order_amount; }
    public Provider getUsualProvider() { return usual_provider; }
    public double getProductPrice() { return product_price; }
    
}