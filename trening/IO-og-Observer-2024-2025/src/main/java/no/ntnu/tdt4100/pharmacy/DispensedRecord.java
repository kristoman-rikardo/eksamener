package no.ntnu.tdt4100.pharmacy;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * A record representing a single entry in the end-of-day dispensing report.
 * Each entry captures which prescription was dispensed, to which patient,
 * and how many distinct medication lines it contained.
 *
 * @param prescriptionId  the unique identifier of the prescription
 * @param patientName     the full name of the patient
 * @param medicationCount the number of distinct medication lines in the prescription
 */
public record DispensedRecord(String prescriptionId, String patientName, int medicationCount) {}
