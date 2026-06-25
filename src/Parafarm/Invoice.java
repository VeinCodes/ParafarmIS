package Parafarm;

import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.time.LocalDate;
import java.io.Serializable;

public class Invoice implements Serializable{
    private static final AtomicLong idGen = new AtomicLong();
    private long invoiceid = idGen.incrementAndGet();
    private String invoice_number;
    private String date;
    private double total_price;
    private Order order;
    private Customer customer;

    public Invoice(Order order) {
        this.order = order;
        this.customer = order.getCustomer();
        this.date = java.time.LocalDate.now().toString();
        this.invoice_number = "INV-" + java.time.LocalDate.now().getYear() + "-" + String.format("%04d", invoiceid);
    }
    
    public void setTotalPrice(double price) { this.total_price = price; }

    public long getInvoiceId()       { return invoiceid; }
    public String getInvoiceNumber() { return invoice_number; }
    public String getDate()          { return date; }
    public double getTotalPrice()    { return total_price; }
    public Order getOrder()          { return order; }
    public Customer getCustomer()    { return customer; }
}