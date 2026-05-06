package no.ntnu.tdt4100.pharmacy;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * Marks a pharmacy as one that owns an {@link IStockManager}.
 */
public interface IHasStockManager {

    /**
     * Returns the {@link IStockManager} for this pharmacy.
     *
     * @return the stock manager
     */
    IStockManager getStockManager();
}
