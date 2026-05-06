package com.bmw.manufacturing.part5;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.tdt4100.CarPart;
import no.ntnu.tdt4100.part5.IPilkingtonService;
import no.ntnu.tdt4100.part5.IStockChangeListener;

/**
 * This class is an implementation of an {@link IStockChangeListener}
 * that listens for stock changes and orders new windows from Pilkington.
 * 
 * Read carefully the JavaDoc of {@link IStockChangeListener}
 * 
 * Windows in all BMW cars are produced by Pilkington.
 * Pilkington has a service to recieve orders {@link IPilkingtonService}
 * 
 * The factory needs to send its orders via
 * the service when the available quantity of the given car part in
 * the inventory is less than 10.
 * 
 * This class will only send the orders to Pilkington if
 * A) the available quantity is less than 10
 * and
 * B) the part name contains the word "window" case-insensitive.
 * 
 * For all other parts, the class will do nothing.
 * 
 * This class should keep track of the parts that are ordered and the amount of parts that are ordered
 * so that {@link #getOrderedCount(CarPart)} returns the correct result.
 * 
 * Behavioural requirements:
 * <ul>
 * <li>The class must implement the interface {@link no.ntnu.tdt4100.part5.IStockChangeListener}</li>
 * <li>When the listener is triggered and the remaining available quantity  of a part is less than 10, 
 * and the part name contains the word "window" case-insensitive,
 * 10 new items of that part should be ordered by calling {@link IPilkingtonService#sendOrder(CarPart, int)}</li>
 * 
 * @see IPilkingtonService#sendOrder(CarPart)
 * @see no.ntnu.tdt4100.part5.IStockChangeListener#execute(CarPart, int)
 * @see OrderFromPilkingtonStockChangeListenerTests
 */
public class OrderFromPilkingtonStockChangeListener implements IStockChangeListener {

    private IPilkingtonService service;
    private Map<CarPart, Integer> orders = new HashMap<>();
    /**
     * The constructor
     * 
     * @param service the service to use to send orders to Pilkington
     */
    public OrderFromPilkingtonStockChangeListener(IPilkingtonService service) {
    // TODO Implement the constructor
    this.service = service;
    }

    /**
     * Returns the number of ordered units of the given part.
     * If the part has not been ordered, 0 should be returned
     * 
     * @param part the part
     * @return the number of ordered units of the given part
     */
    public int getOrderedCount(CarPart part) {
        if (part == null || !this.orders.containsKey(part)) return 0;
        return this.orders.get(part);
    }

    @Override
    public void execute(CarPart carPart, int availableQuantity) {
        if (carPart == null || availableQuantity < 0) return;
        if (availableQuantity < 10 && carPart.partName().toLowerCase().contains("window")) {
            this.service.sendOrder(carPart, 10);
            this.orders.put(carPart, getOrderedCount(carPart) + 10);
        }
    }
}
