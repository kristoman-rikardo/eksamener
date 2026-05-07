package no.ntnu.tdt4100.pharmacy;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * Represents a supplier service capable of receiving automated reorder requests.
 * Implementations may send the order via API, email, or other channels.
 */
public interface ISupplierService {

    /**
     * Sends an order for the given medication to the supplier.
     *
     * @param medication the medication to order
     * @param quantity   the number of units to order
     */
    void sendOrder(Medication medication, int quantity);
}
