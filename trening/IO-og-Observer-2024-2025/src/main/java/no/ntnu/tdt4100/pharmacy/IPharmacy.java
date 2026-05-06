package no.ntnu.tdt4100.pharmacy;

import java.util.List;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * Represents a pharmacy in the dispensing system.
 */
public interface IPharmacy {

    /**
     * Returns the ISO 3166-1 alpha-3 country code for the country in which this
     * pharmacy operates.
     *
     * @return the 3-letter ISO country code
     */
    String getIsoCode();

    /**
     * Returns the list of medications this pharmacy is licensed to stock and dispense.
     *
     * @return the list of {@link Medication} objects
     */
    List<Medication> getMedications();

    /**
     * Returns the total number of staff members employed at this pharmacy.
     *
     * @return the number of staff
     */
    int getNumberOfStaff();

    /**
     * Returns the total number of prescription units successfully dispensed
     * at this pharmacy since it was created.
     *
     * @return the number of dispensed units
     */
    int getNumberOfDispensed();
}
