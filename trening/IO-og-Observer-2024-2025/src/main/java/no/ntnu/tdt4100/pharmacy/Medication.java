package no.ntnu.tdt4100.pharmacy;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * A record representing a medication in the pharmacy system.
 *
 * @param id           the unique numeric identifier of the medication
 * @param name         the medication name
 * @param manufacturer the name of the manufacturer
 * @param doseMg       the dose in milligrams per unit
 */
public record Medication(int id, String name, String manufacturer, double doseMg) {}
