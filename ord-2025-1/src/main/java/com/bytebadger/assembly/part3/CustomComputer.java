package com.bytebadger.assembly.part3;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.tdt4100.bytebadger.Customer;
import no.ntnu.tdt4100.bytebadger.IComputerPart;
import no.ntnu.tdt4100.bytebadger.ICustomBuild;

// Import relevant libraries

/**
 * Implement the class CustomComputer which represents a custom computer build.
 * 
 * The class must implement the interface {@link no.ntnu.tdt4100.bytebadger.ICustomBuild}.
 * Read the documentation in the interface for detailed description of the behaviour of
 * the methods you need to implement.
 * 
 * In addition to the methods required by the interface, the class has a constructor and
 * some additional methods for managing the computer build.
 * 
 * The class should store a list of {@link no.ntnu.tdt4100.bytebadger.IComputerPart} computer part
 * objects and it should be able to add and remove a quantity of parts from
 * the custom build. 
 * 
 * @see no.ntnu.tdt4100.bytebadger.ICustomBuild
 * @see CustomComputerTest
*/

public class CustomComputer implements ICustomBuild {
    private Customer customer;
    private HashMap<IComputerPart, Integer> parts = new HashMap<>();
    // Add neccesary variables

    /**
     * This is the constructor for the class CustomComputer
     * 
     * @param customer the customer of a custom computer build
     * 
     * @throws IllegalArgumentException if the customer is null.
     */
    
    public CustomComputer(Customer customer) {
        if (customer == null) throw new IllegalArgumentException();
        this.customer = customer;
        // Complete the constructor according to JavaDoc
    }

    /**
     * This method adds a quantity of a specific computer part to the custom computer build.
     * 
     * @param part a computer part
     * @param quantity the number of parts to be added
     * 
     * @throws IllegalArgumentException if the quantity is not a positive integer.
     */
    
    public void addPart(IComputerPart part, int quantity) {
        if (part == null || quantity < 1) throw new IllegalArgumentException();
        if (this.parts.containsKey(part)) this.parts.put(part, this.parts.get(part) + quantity);
        else {this.parts.put(part, quantity);}
        // Complete the method according to JavaDoc

    }

    /**
     * This method removes a quantity of a specific computer part from the custom computer build.
     * 
     * @param part a computer part
     * @param quantity the number of parts to be removed
     * 
     * @throws IllegalArgumentException if the part is not in the build or if the quantity is not a positive number.
     */
    public void removePart(IComputerPart part, int quantity) {
        if (part == null || quantity < 1 || !this.parts.containsKey(part)) throw new IllegalArgumentException();
        int newQuantity = this.parts.get(part) - quantity;
        if (newQuantity <= 0) this.parts.remove(part);
        else {this.parts.put(part, this.parts.get(part) - quantity);}
        // Complete the method according to JavaDoc

    }

    // Implement the methods from the interface ICustomBuild
    @Override
    public double getTotalPrice() {
        return this.parts.entrySet().stream().mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue()).sum();
    }

    @Override
    public Customer getCustomer() {
        return this.customer;
    }

    @Override 
    public Map<IComputerPart, Integer> getParts() {
        return this.parts;
    }

    // TODO: If time, do some tests of this logic

}
