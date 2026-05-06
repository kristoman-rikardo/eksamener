package no.ntnu.tdt4100.pharmacy;

import java.util.Map;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * Represents a unit to be dispensed: a prescription tied to a set of medications
 * and the quantity of each medication required.
 *
 * @param prescriptionId the unique identifier for this prescription
 * @param medications    a map from {@link Medication} to the required quantity of that medication
 */
public record DispensableUnit(String prescriptionId, Map<Medication, Integer> medications) {}
