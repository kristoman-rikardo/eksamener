package no.ntnu.tdt4100.pharmacy;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * Observer interface for listening to stock level changes in an {@link IStockManager}.
 * Implementations decide what to do when a medication's available quantity changes.
 */
public interface IStockListener {

    /**
     * Called by the {@link IStockManager} after the available quantity of a medication
     * has been updated.
     *
     * @param medication        the medication whose stock level changed
     * @param availableQuantity the new available quantity after the update
     */
    void execute(Medication medication, int availableQuantity);
}
