package Parafarm;

import java.util.concurrent.atomic.AtomicLong;
import java.io.Serializable;

public class Receipt implements Serializable{
    private static final AtomicLong idGen = new AtomicLong();
    private long receiptid = idGen.incrementAndGet();
    private String receipt_number;
    private String date;
    private double total_price;
    private Order order;
    private Customer customer;

    public Receipt(Order order) {
        this.order = order;
        this.customer = order.getCustomer();
        this.date = java.time.LocalDate.now().toString();
        this.receipt_number = "INV-" + java.time.LocalDate.now().getYear() + "-" + String.format("%04d", receiptid);
    }
    
    public void setTotalPrice(double price) { this.total_price = price; }

    public long getReceiptId()       { return receiptid; }
    public String getReceiptNumber() { return receipt_number; }
    public String getDate()          { return date; }
    public double getTotalPrice()    { return total_price; }
    public Order getOrder()          { return order; }
    public Customer getCustomer()    { return customer; }
}