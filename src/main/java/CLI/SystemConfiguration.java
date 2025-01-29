package CLI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SystemConfiguration {
    private int totalTickets; //Total number of tickets available in the system
    private int ticketReleaseRate; //Rate at which tickets are released by vendors(tickets per second)
    private int customerRetrievalRate; //Rate at which customers retrieve tickets(tickets per second)
    private int maxTicketCapacity; //Maximum number of tickets that can be held at any given time in the ticket pool
    private int numberOfVendors; //Number of the vendors in the system
    private int numberOfCustomers; //Number of the customers in the system

    public SystemConfiguration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity, int numberOfVendors, int numberOfCustomers){
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.numberOfVendors = numberOfVendors;
        this.numberOfCustomers = numberOfCustomers;
    }

    //Default constructor for creating and empty configuration.
    public SystemConfiguration() {}

    //Getter and Setter methods for each parameter.
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getNumberOfVendors() {
        return numberOfVendors;
    }

    public void setNumberOfVendors(int numberOfVendors) {
        this.numberOfVendors = numberOfVendors;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    //Save the configuration to a JSON file.
    public void saveToFile(String filePath){
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); //Gson object for JSON serialization with prettyprinting
        try(FileWriter writer = new FileWriter(filePath)){ //FileWriter for writing to the file
            gson.toJson(this, writer); //Convert the current object to JSON and save it
        }catch (IOException e){
            e.printStackTrace(); //Print error if any exception occurs
        }
    }

    //Load a configuratiom from a JSON file.
    public static SystemConfiguration loadFromFile(String filePath) {
        Gson gson = new Gson(); //Gson object for JSON deserialization
        try (FileReader reader = new FileReader(filePath)) { //FileReader for reading the file
            return gson.fromJson(reader, SystemConfiguration.class); //Convert JSON data back to a SystemConfiguration object
        } catch (IOException e) {
            e.printStackTrace(); //Print error if any exception occurs
            return null; //Return null if loading fails
        }
    }

    //Provide a string containing the details of the configuration.
    @Override
    public String toString(){
        return "SystemConfiguration {" +
                "Total Tickets = " + totalTickets +
                ", Ticket Release Rate = " + ticketReleaseRate +
                ", Customer Retrieval Rate = " + customerRetrievalRate +
                ", Maximum Ticket Capacity = " + maxTicketCapacity +
                ", Number of Vendors = " + numberOfVendors +
                ", Number of Customers = " + numberOfCustomers +
                '}';
    }
}