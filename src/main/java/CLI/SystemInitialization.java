package CLI;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SystemInitialization {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            SystemConfiguration config = new SystemConfiguration();

            System.out.println("\n----- Welcome to the Real-Time Event Ticketing System Configuration -----");

            //Input and validate the total number of tickets.
            while(true){
                try{
                    System.out.print("\nEnter total number of tickets: ");
                    int totalTickets = scanner.nextInt();
                    if(totalTickets <= 0){
                        System.out.println("Please enter the valid number.");
                    }else{
                        config.setTotalTickets(totalTickets); //Stores the validated total ticket value in the config object
                        break;
                    }
                }catch (InputMismatchException e){
                    System.out.println("Please enter a valid integer.");
                    scanner.next();
                }
            }

            //Input and validate the ticket release rate.
            while (true){
                try {
                    System.out.print("\nEnter ticket release rate (tickets per second): ");
                    int ticketReleaseRate = scanner.nextInt();
                    if (ticketReleaseRate <= 0){
                        System.out.println("Please enter the valid ticket release rate.");
                    }else {
                        config.setTicketReleaseRate(ticketReleaseRate);
                        break;
                    }
                }catch (InputMismatchException e){
                    System.out.println("Please enter a valid integer.");
                    scanner.next();
                }
            }

            //Input and validate the customer retrieval rate.
            while (true){
                try {
                    System.out.print("\nEnter customer retrieval rate (tickets per second): ");
                    int customerRetrievalRate = scanner.nextInt();
                    if (customerRetrievalRate <= 0 || customerRetrievalRate <= config.getTicketReleaseRate()){
                        System.out.println("Customer retrieval rate must be a positive integer and greater than ticket release rate.");
                    }else{
                        config.setCustomerRetrievalRate(customerRetrievalRate);
                        break;
                    }
                }catch (InputMismatchException e){
                    System.out.println("Please enter a valid integer.");
                    scanner.next();
                }
            }

            //Input and validate the maximum ticket capacity.
            while(true){
                try{
                    System.out.print("\nEnter maximum ticket capacity: ");
                    int maxTicketCapacity = scanner.nextInt();
                    if(maxTicketCapacity <=0 || maxTicketCapacity > config.getTotalTickets()){
                        System.out.println("Maximum ticket capacity must be a positive integer and less than total tickets.");
                    }else {
                        config.setMaxTicketCapacity(maxTicketCapacity);
                        break;
                    }
                }catch (InputMismatchException e){
                    System.out.println("Please enter a valid integer.");
                    scanner.next();
                }
            }

            //Input and validate the number of vendors.
            while (true){
                try {
                    System.out.print("\nEnter the number of vendors in the system: ");
                    int numberOfVendor = scanner.nextInt();
                    if (numberOfVendor <= 0){
                        System.out.println("Number of vendors must be positive integer.");
                    }else{
                        config.setNumberOfVendors(numberOfVendor);
                        break;
                    }
                }catch (InputMismatchException e){
                    System.out.println("Please enter a valid integer.");
                    scanner.next();
                }
            }

            //Input and validate the number of customers.
            while(true){
                try {
                    System.out.print("\nEnter the number of customers in the system: ");
                    int numberOfCustomers = scanner.nextInt();
                    if (numberOfCustomers <= 0){
                        System.out.println("Number of customers must be positive integer.");
                    }else {
                        config.setNumberOfCustomers(numberOfCustomers);
                        break;
                    }
                }catch (InputMismatchException e){
                    System.out.println("Please enter a valid integer.");
                    scanner.next();
                }
            }

            //Save configuration to a file.
            config.saveToFile("config.json");
            System.out.println("\nConfiguration successfully saved: " + config);

            //Load configuration from a file.
            SystemConfiguration loadedConfig = SystemConfiguration.loadFromFile("config.json");
            System.out.println("\nLoaded Configuration: " + loadedConfig + "\n");

            //Initialize ticket pool.
            TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());

            //Initialize vendors and customers.
            List<Thread> threads = new ArrayList<>();
            List<Vendor> vendors = new ArrayList<>();
            List<Customer> customers = new ArrayList<>();
            int ticketsPerVendor = config.getTotalTickets() / config.getNumberOfVendors();

            //Create and start the vendor threads.
            for (int i = 1; i <= config.getNumberOfVendors(); i++) {
                Vendor vendor = new Vendor("Vendor" + i, config.getTicketReleaseRate(), ticketsPerVendor, ticketPool);
                vendors.add(vendor);
                Thread vendorThread = new Thread(vendor);
                threads.add(vendorThread);
                vendorThread.start();
            }

            //Create and start the customer threads.
            for (int i = 1; i <= config.getNumberOfCustomers(); i++) {
                Customer customer = new Customer("Customer" + i, config.getCustomerRetrievalRate(), ticketPool);
                customers.add(customer);
                Thread customerThread = new Thread(customer);
                threads.add(customerThread);
                customerThread.start();
            }

            //Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            //Display summary of ticket sales
            System.out.println("\nAll tickets have been sold out.");
            System.out.println("\n----------Ticket Sales Summary----------");
            System.out.println("Total Tickets Sold: " + ticketPool.getTotalTicketSold());
            System.out.println("\n.....Vendor Ticket Addition Details.....");
            for(Vendor vendor : vendors){
                System.out.println(vendor.getVendorName() + " added " + vendor.getTicketsAdded() + " tickets.");
            }
            System.out.println("\n.....Customer Ticket Purchase Details.....");
            for (Customer customer : customers){
                System.out.println(customer.getCustomerName() + " purchased " + customer.getTicketsBought() + " tickets.");
            }
        } catch (Exception e) {
            e.printStackTrace(); //Print stack trace for debugging purposes.
        }
    }
}