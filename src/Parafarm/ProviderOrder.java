package Parafarm;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.io.Serializable;

public class ProviderOrder implements Serializable{
    private static final AtomicLong idGen = new AtomicLong();
    private long orderid = idGen.incrementAndGet();
    private String date;
    private String situation;
    private double total_price;
    private String nextorder;
    private String lastorder;
    private ArrayList<Product> products = new ArrayList<>();
    private Provider provider;
    

    public ProviderOrder() {
    }
    
    public ProviderOrder(String date, String situation) {
        this.date = date;
        this.situation = situation;
    }

    public ProviderOrder(Product product, int product_amount) {
        for (int i = 0; i < product_amount; i++) {
            products.add(product);
        }
    }

    public void add_product(Product product) {
        products.add(product);
    }

    public void remove_product(Product product) {
        products.remove(product);
    }
    
    public ArrayList<Product> getProducts() { return products; }

    public double calculateTotalPrice() {
        total_price = 0;
        for (Product p : products) {
            total_price += p.getPrice();
        }
        return total_price;
    }
    
    public Provider getProvider()           { return provider; }
    public void setProvider(Provider p)     { this.provider = p; }
    
    public long getOrderId()      { return orderid; }
    public String getDate()       { return date; }
    public String getSituation()  { return situation; }
    public String getNextOrder()  { return nextorder; }
    public String getLastOrder()  { return lastorder; }
    public double getTotalPrice() { return total_price; }
    public String getProductName() {
        if (products.isEmpty()) return "—";
        return products.get(0).getName();
    }
    public int getQuantity()      { return products.size(); }
    public String getOrderCode() { return "PO-" + orderid; }

    public void setDate(String date)           { this.date = date; }
    public void setSituation(String situation) { this.situation = situation; }
    public void setNextOrder(String next)      { this.nextorder = next; }
    public void setLastOrder(String last)      { this.lastorder = last; }
}