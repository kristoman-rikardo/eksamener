package com.bmw.manufacturing.part5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.tdt4100.CarPart;
import no.ntnu.tdt4100.part5.IStockChangeListener;
import no.ntnu.tdt4100.part5.InsufficientPartsException;
import no.ntnu.tdt4100.part5.InventoryManager;

/**
 * WarehouseInventoryManager must implement the {@link no.ntnu.tdt4100.part5.InventoryManager} interface
 * 
 * @see no.ntnu.tdt4100.part5.InventoryManager
 * @see WarehouseInventoryManagerTests
 */
public class WarehouseInventoryManager implements InventoryManager {
    private Map<CarPart, Integer> inventoryMap = new HashMap<>();
    private List<IStockChangeListener> listeners = new ArrayList<>();
    // TODO Implement the class according to behaviour specified in Javadoc in its interface

    /**
     * Increase the available quantity of the given car part in the inventory.
     * 
     * If the number is negative, do nothing.
     * 
     * This method will be called when processing a recieved shipment to a factory.
     *
     * @param part the car part
     * @param numberOfItems number of items of the given car part to add to the inventory
     */
    @Override
    public void increaseQuantity(CarPart part, int numberOfItems) {
        if (part == null || numberOfItems < 1) return; // if the part is null, or if the number is non-positive -> we do not need to increase quantity
        if (this.inventoryMap.containsKey(part)) {
            this.inventoryMap.put(part, this.inventoryMap.get(part) + numberOfItems);
        }
        else {
            this.inventoryMap.put(part, numberOfItems);
        }
    }

    /**
     * Decrease the available quantity of the given car part in the inventory.
     * 
     * This method will be called each time a car part is removed from the inventory 
     * to be installed in a car.
     * 
     * After the inventory is updated with the new available quantity, the method 
     * must notify all listeners/observers with remaining available quantity 
     * of the given car part
     * 
     * @param part the car part
     * @param numberOfItems the number of items of the given car part to remove from the inventory
     * @throws InsufficientPartsException if there are not enough available items in inventory of the given car part
     */
    @Override
    public void decreaseQuantity(CarPart part, int numberOfItems) {
        if (part == null || numberOfItems < 1) return; // if the part is null, or if the number is non-positive -> we do not need to increase quantity
        if (this.inventoryMap.containsKey(part)) {
            int newQuantity = this.inventoryMap.get(part) - numberOfItems;
            if (newQuantity < 0) throw new InsufficientPartsException(part, "Cannot remove " + numberOfItems + " of " + part.toString());
            this.inventoryMap.put(part, newQuantity);
            notifyListeners(part, newQuantity);
        }
    }

    public void notifyListeners(CarPart part, int newQuantity) {
        for (IStockChangeListener listener : listeners) {
            listener.execute(part, newQuantity);
        }
    }


    /**
     * Adds a listener for stock changes in the inventory
     * 
     * The addListener method adds a listener.
     * The same listener can not be added multiple times.
     * 
     * @param listener the {@link IStockChangeListener} to add 
     */
    @Override
    public void addListener(IStockChangeListener listener) {
        if (listener == null || this.listeners.contains(listener)) return;
        this.listeners.add(listener);
    }

    /**
     * Removes a previously registered listener.
     * 
     * @param listener the {@link IStockChangeListener} to remove 
     */
    @Override
    public void removeListener(IStockChangeListener listener)  {
        if (listener == null || !this.listeners.contains(listener)) return;
        this.listeners.remove(listener);
    }

    /**
     * Returns the available quantity of the given car part in stock
     * 
     * @param part the {@link CarPart}
     * @return the available quantity of the given car part
     */
    @Override
    public int getAvailableQuantity(CarPart part) {
        return this.inventoryMap.get(part);
    }

    /**
     * Returns the number of added listeners.
     * 
     * @return number of added {@link IStockChangeListener} objects
     */
    @Override
    public int getNumberOfListeners() {
        return this.listeners.size();
    }

    // public static void main(String[] args) {
    //     WarehouseInventoryManager w = new WarehouseInventoryManager();
    //     w.addListener(new IStockChangeListener() {
            
    //     });
    // }
}
