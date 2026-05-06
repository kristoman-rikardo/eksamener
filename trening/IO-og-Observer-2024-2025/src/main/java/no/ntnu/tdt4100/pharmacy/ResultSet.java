package no.ntnu.tdt4100.pharmacy;

import java.util.List;

/**
 * THIS IS SUPPLIED CODE — DO NOT MODIFY.
 *
 * Holds the result of parsing a medications CSV file.
 *
 * @param medications a list of successfully parsed {@link Medication} objects
 * @param errorLines  a list of file line numbers (1-indexed, counting the header as line 1)
 *                    where parsing errors were encountered
 */
public record ResultSet(List<Medication> medications, List<Integer> errorLines) {}
