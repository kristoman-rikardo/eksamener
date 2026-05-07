package com.pharmacy.trening.partB;

import no.ntnu.tdt4100.pharmacy.IStockListener;
import no.ntnu.tdt4100.pharmacy.IStockManager;
import no.ntnu.tdt4100.pharmacy.InsufficientStockException;
import no.ntnu.tdt4100.pharmacy.Medication;

// TODO: Add any necessary imports here

/**
 * A concrete implementation of {@link IStockManager} that maintains a map of
 * medication quantities and a list of registered stock-change listeners.
 *
 * @see IStockManager
 * @see DrugStockManagerTests
 */
public class DrugStockManager implements IStockManager {

    // TODO: Add necessary fields here

    /**
     * Increases the available quantity of the given medication in stock.
     * If {@code quantity} is zero or negative, this method does nothing.
     *
     * @param medication the medication to restock
     * @param quantity   the number of units to add
     */
    @Override
    public void increaseStock(Medication medication, int quantity) {
        // TODO: Implement according to JavaDoc
    }

    /**
     * Decreases the available quantity of the given medication in stock.
     *
     * <p>After updating the quantity, all registered listeners must be notified
     * with the new available quantity of that medication.</p>
     *
     * @param medication the medication to deduct from stock
     * @param quantity   the number of units to remove
     * @throws InsufficientStockException if the available quantity is less than
     *                                    {@code quantity}
     */
    @Override
    public void decreaseStock(Medication medication, int quantity) {
        // TODO: Implement according to JavaDoc
    }

    /**
     * Registers a listener for stock-level changes.
     * The same listener instance must not be added more than once.
     *
     * @param listener the {@link IStockListener} to register
     */
    @Override
    public void addListener(IStockListener listener) {
        // TODO: Implement according to JavaDoc
    }

    /**
     * Removes a previously registered listener.
     * If the listener is not currently registered, this method does nothing.
     *
     * @param listener the {@link IStockListener} to remove
     */
    @Override
    public void removeListener(IStockListener listener) {
        // TODO: Implement according to JavaDoc
    }

    /**
     * Returns the available quantity of the given medication.
     * If the medication has never been added to this stock manager, returns {@code 0}.
     *
     * @param medication the medication to query
     * @return the available quantity, or {@code 0} if unknown
     */
    @Override
    public int getAvailableStock(Medication medication) {
        return 0; // dummy return
    }

    /**
     * Returns the number of registered listeners.
     *
     * @return the listener count
     */
    @Override
    public int getListenerCount() {
        return 0; // dummy return
    }
}
