package CLI;

public class Customer implements Runnable {
    private final String customerName; //Name of the customer
    private final int customerRetrievalRate; //Tickets retrieved per second by the customer
    private final TicketPool ticketPool; //Shared ticket pool from which tickets are retrieved
    private int ticketsBought = 0; //Tracks the total tickets bought by this customer

    //Constructor to initialize a customer with its attributes.
    public Customer(String customerName, int customerRetrievalRate, TicketPool ticketPool) {
        this.customerName = customerName;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketPool = ticketPool;
    }

    //Gets the name of the customer.
    public String getCustomerName() {
        return customerName;
    }

    //Gets the total number of tickets bought by this customer.
    public int getTicketsBought() {
        return ticketsBought;
    }

    @Override
    public void run() {
        try {
            //Continuously attempt to buy tickets until none are Left.
            while (true) {
                //Attempt to buy a ticket from the shared ticket pool.
                String ticket = ticketPool.buyTickets(customerName);
                if(ticket == null){ //If no tickets are Left, break out of the Loop
                    break;
                }
                ticketsBought++; //Increment the count of tickets bought by this customer
                Thread.sleep(1000 / customerRetrievalRate); //Simulate ticket purchase rate
            }
        } catch (InterruptedException e) {
            //Handel thread interruption and restore the interrupt status.
            Thread.currentThread().interrupt();
            System.out.println(customerName + " was interrupted.");
        }
    }
}