package no.ntnu.tdt4100.pharmacy;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * Manages the stock of medications for a pharmacy, and supports
 * observer registration for stock-level changes.
 */
public interface IStockManager {

    /**
     * Increases the available quantity of the given medication in stock.
     * If {@code quantity} is zero or negative, this method does nothing.
     *
     * @param medication the medication to restock
     * @param quantity   the number of units to add
     */
    void increaseStock(Medication medication, int quantity);

    /**
     * Decreases the available quantity of the given medication in stock.
     *
     * <p>After the inventory is updated, all registered listeners must be
     * notified with the new available quantity.</p>
     *
     * @param medication the medication to deduct from stock
     * @param quantity   the number of units to remove
     * @throws InsufficientStockException if the available quantity of the given
     *                                    medication is less than {@code quantity}
     */
    void decreaseStock(Medication medication, int quantity);

    /**
     * Registers a listener to be notified when stock levels change.
     * The same listener instance must not be added more than once.
     *
     * @param listener the {@link IStockListener} to add
     */
    void addListener(IStockListener listener);

    /**
     * Removes a previously registered listener.
     * If the listener is not registered, this method does nothing.
     *
     * @param listener the {@link IStockListener} to remove
     */
    void removeListener(IStockListener listener);

    /**
     * Returns the currently available quantity of the given medication.
     * If the medication has never been added to this stock manager,
     * returns {@code 0}.
     *
     * @param medication the medication to query
     * @return the available quantity, or {@code 0} if unknown
     */
    int getAvailableStock(Medication medication);

    /**
     * Returns the number of registered listeners.
     *
     * @return the listener count
     */
    int getListenerCount();
}
