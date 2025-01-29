package CLI;

public class Vendor implements Runnable {
    private final String vendorName;
    private final int ticketReleaseRate;
    private final int totalTickets;
    private final TicketPool ticketPool;
    private int ticketsAdded = 0; // Tracks tickets added by this vendor

    public Vendor(String vendorName, int ticketReleaseRate, int totalTickets, TicketPool ticketPool) {
        this.vendorName = vendorName; //Name of the vendor
        this.ticketReleaseRate = ticketReleaseRate; //Tickets released per second by the vendor
        this.totalTickets = totalTickets; //Total number of the tickets in the system
        this.ticketPool = ticketPool; //Shared ticket pool for vendors and customers
    }

    //Gets the name of the vendor.
    public String getVendorName() {
        return vendorName;
    }

    //Gets the total number of tickets added by the vendor.
    public int getTicketsAdded() {
        return ticketsAdded;
    }

    @Override
    public void run() {
        try {
            //Loap to add tickets up to the total specified for the vendor.
            for (int i = 1; i <= totalTickets; i++) {
                String ticket = "Ticket-" + i; //Create a ticket with a unique identifier
                ticketPool.addTickets(ticket, vendorName); //Add the ticket to the shared ticket pool
                ticketsAdded++; //Increment the count of tickets added by vendor
                Thread.sleep(1000 / ticketReleaseRate); // Simulate ticket release rate
            }
        } catch (InterruptedException e) {
            //Handle thread interruption and restore the interrupt status
            Thread.currentThread().interrupt();
            System.out.println(vendorName + " was interrupted.");
        }
    }
}