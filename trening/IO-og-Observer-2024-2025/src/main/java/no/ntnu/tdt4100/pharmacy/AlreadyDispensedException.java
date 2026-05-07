package no.ntnu.tdt4100.pharmacy;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * Thrown when an attempt is made to dispense a prescription that has already
 * been dispensed at the same pharmacy.
 */
public class AlreadyDispensedException extends RuntimeException {

    private final DispensableUnit unit;

    /**
     * Constructs a new {@code AlreadyDispensedException}.
     *
     * @param unit    the unit that was already dispensed
     * @param message a human-readable description of the error
     */
    public AlreadyDispensedException(DispensableUnit unit, String message) {
        super(message);
        this.unit = unit;
    }

    /**
     * Returns the unit that triggered this exception.
     *
     * @return the already-dispensed unit
     */
    public DispensableUnit getUnit() {
        return unit;
    }
}
