package no.ntnu.tdt4100.pharmacy;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * Thrown when a dispensing operation cannot proceed because the required
 * quantity of a medication is not available in stock.
 */
public class InsufficientStockException extends RuntimeException {

    private final Medication medication;

    /**
     * Constructs a new {@code InsufficientStockException}.
     *
     * @param medication the medication that has insufficient stock
     * @param message    a human-readable description of the error
     */
    public InsufficientStockException(Medication medication, String message) {
        super(message);
        this.medication = medication;
    }

    /**
     * Returns the medication that caused this exception.
     *
     * @return the medication with insufficient stock
     */
    public Medication getMedication() {
        return medication;
    }
}
