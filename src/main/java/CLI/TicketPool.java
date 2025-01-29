package CLI;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<String> tickets; //Synchronized List to hold tickets
    private final int maxTicketCapacity; //Maximum capacity of the ticket pool
    private final int totalTicketsToSell; //Total number of tickets to be sold
    private boolean waitingLogForVendor = false; //Tracks if any vendor is waiting to add tickets
    private boolean waitingLogForCustomer = false; //Tracks if any customer is waiting to buy tickets
    private int totalTicketSold = 0; //Tracks the total number of tickets sold

    public TicketPool(int maxTicketCapacity, int totalTicketsToSell) {
        this.tickets = Collections.synchronizedList(new LinkedList<>()); //Thread-safe List for ticket management
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTicketsToSell = totalTicketsToSell;
    }

    // Method for vendors to add tickets
    public synchronized void addTickets(String ticket, String vendorName) {
        //Wait if the pool is at maximum capacity
        while (tickets.size() >= maxTicketCapacity) {
            if (!waitingLogForVendor) { //Log only once for each waiting state
                System.out.println("\nTicket pool is full. Vendor is waiting to add tickets...");
                waitingLogForVendor = true;
            }
            try {
                wait(); //Wait until notified by customers
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); //Restore the interrupt status
                System.out.println(vendorName + " thread interrupted: " + e.getMessage());
            }
        }
        //Add the ticket to the pool
        tickets.add(ticket);
        waitingLogForVendor = false; //Reset the waiting Log for vendors
        System.out.println(vendorName + " added: " + ticket + " | Pool size: " + tickets.size());
        notifyAll(); // Notify waiting customers
    }

    // Method for customers to buy tickets
    public synchronized String buyTickets(String customerName) {
        //Wait if the pool is empty and tickets are still available to sell
        while (tickets.isEmpty() && totalTicketSold < totalTicketsToSell) {
            if (!waitingLogForCustomer) { //Log only once for each waiting state
                System.out.println("\nTicket pool is empty. Customer is waiting to buy tickets...");
                waitingLogForCustomer = true;
            }
            try {
                wait(); //Wait until notified by vendors
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); //Restore the interrupt status
                System.out.println(customerName + " thread interrupted: " + e.getMessage());
            }
        }
        //Return null if all tickets have been sold
        if (totalTicketSold >= totalTicketsToSell && tickets.isEmpty()){
            return null;
        }
        //Remove a ticket from the pool and increment the sold count
        String ticket = tickets.remove(0);
        totalTicketSold++;
        waitingLogForCustomer = false; //Reset the waiting Log for customers
        System.out.println(customerName + " purchased: " + ticket + " | Pool size: " + tickets.size());
        notifyAll(); // Notify waiting vendors
        return ticket;
    }

    public int getTotalTicketSold() {
        return totalTicketSold;
    }
}