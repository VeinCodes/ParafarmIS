package Parafarm;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.io.Serializable;

public class Order implements Serializable{
    private static final AtomicLong idGen = new AtomicLong();
    private long orderid = idGen.incrementAndGet();
    private String date = java.time.LocalDate.now().toString();
    private double total_price;
    private ArrayList<Product> products = new ArrayList<>();
    private String status = "active";
    private Customer customer;
    public Invoice currentInvoice;
    private String seller = "";


    public Order() {
    }

    public void make_invoice() {
        currentInvoice = new Invoice(this);
        Main.invoices.add(currentInvoice);
    }

    public Invoice getInvoice() {
        return currentInvoice;
    }
    
    public Customer getCustomer() { return customer; }
    
    public void make_receipt() {
    	Receipt rec = new Receipt(this);
    }

    public void add_product(Product product) {
    	products.add(product);
    }
    
    public void remove_product(Product p) {
        products.remove(p);
    }
    
    public ArrayList<Product> getProducts() {
    	return products;
    }
    
    public void setSeller(String s)    { this.seller = s; }
    public String getStatus()       { return status; }
    public void setStatus(String s) { this.status = s; }
    public String getDate()         { return date; }
    public String getOrderId()      { return "ORD-" + orderid; }
    public String getSeller()          { return seller; }
    public void setCustomer(Customer c) { this.customer = c; }
}