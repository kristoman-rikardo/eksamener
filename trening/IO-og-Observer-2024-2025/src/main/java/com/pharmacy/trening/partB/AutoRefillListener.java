package com.pharmacy.trening.partB;

import no.ntnu.tdt4100.pharmacy.IStockListener;
import no.ntnu.tdt4100.pharmacy.ISupplierService;
import no.ntnu.tdt4100.pharmacy.Medication;

// TODO: Add any necessary imports here

/**
 * An {@link IStockListener} that automatically places reorder requests with a
 * supplier whenever injectable medications run low.
 *
 * <p>This listener monitors stock changes and places an order of 20 units
 * whenever <em>all</em> of the following conditions are true:</p>
 * <ol>
 *   <li>The remaining available quantity is strictly less than {@code 5}.</li>
 *   <li>The medication name contains the word {@code "injection"} (case-insensitive).</li>
 * </ol>
 *
 * <p>Orders are accumulated: if the same medication triggers multiple refill
 * events, the total ordered count must reflect the sum of all orders placed.</p>
 *
 * @see IStockListener
 * @see ISupplierService
 * @see AutoRefillListenerTests
 */
public class AutoRefillListener implements IStockListener {

    // TODO: Add necessary fields here

    /**
     * Constructs a new {@code AutoRefillListener}.
     *
     * @param supplierService the service used to place reorder requests
     */
    public AutoRefillListener(ISupplierService supplierService) {
        // TODO: Implement constructor
    }

    /**
     * Called when the available quantity of a medication changes.
     *
     * <p>Places an order of 20 units via the {@link ISupplierService} if and only if:</p>
     * <ul>
     *   <li>{@code availableQuantity} is strictly less than {@code 5}, <em>and</em></li>
     *   <li>the medication name contains the word {@code "injection"} (case-insensitive).</li>
     * </ul>
     *
     * <p>For all other medications or higher stock levels, this method does nothing.</p>
     *
     * <p>Each time an order is placed, the total ordered count for that medication
     * must be increased by 20, so that {@link #getOrderedCount(Medication)} returns
     * the correct cumulative total.</p>
     *
     * @param medication        the medication whose stock level changed
     * @param availableQuantity the new available quantity after the update
     */
    @Override
    public void execute(Medication medication, int availableQuantity) {
        // TODO: Implement according to JavaDoc
    }

    /**
     * Returns the total number of units ordered for the given medication since
     * this listener was created.
     *
     * <p>If no order has been placed for the medication, returns {@code 0}.</p>
     *
     * @param medication the medication to query
     * @return the total units ordered, or {@code 0} if the medication has never
     *         been ordered through this listener
     */
    public int getOrderedCount(Medication medication) {
        return 0; // dummy return
    }
}
