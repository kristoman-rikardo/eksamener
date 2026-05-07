package com.pharmacy.trening.partA;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import no.ntnu.tdt4100.pharmacy.Medication;
import no.ntnu.tdt4100.pharmacy.ResultSet;

// TODO: Add any necessary imports here

/**
 * Reads medication data from CSV input streams and parses individual CSV lines
 * into {@link Medication} objects.
 *
 * <p>All methods in this class are class methods.
 * A class method is a method that is bound to a class rather than its object.
 * It doesn't require creation of a class instance to be invoked.</p>
 *
 * @see MedicationReaderTests
 */
public final class MedicationReader {

    /**
     * Regex expression that correctly splits a CSV line on commas, while
     * ignoring commas that appear inside double-quoted fields.
     * You must use this constant when splitting lines read from the stream.
     */
    public static final String CSV_SPLIT_REGEX = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    private MedicationReader() {} // DO NOT ADD CONSTRUCTORS

    /**
     * Parses a single CSV line into a {@link Medication} object.
     *
     * <p>The expected column order is:</p>
     * <pre>id,name,manufacturer,dose_mg</pre>
     *
     * <p>Behavioural requirements:</p>
     * <ul>
     *   <li>Use {@link #CSV_SPLIT_REGEX} to split the line.</li>
     *   <li>Return {@code null} if the line does not contain exactly 4 fields.</li>
     *   <li>Return {@code null} if {@code id} cannot be parsed as an {@code int}.</li>
     *   <li>Return {@code null} if {@code dose_mg} cannot be parsed as a {@code double}.</li>
     *   <li>Return {@code null} if {@code name} is empty after stripping surrounding
     *       double-quote characters ({@code "}) and whitespace.</li>
     *   <li>Strip surrounding double-quote characters from the {@code name} field
     *       (e.g. {@code "Amoxicillin, oral"} → {@code Amoxicillin, oral}).</li>
     * </ul>
     *
     * @param line       a single CSV line representing one medication
     * @param splitRegex the regex expression to use for splitting
     * @return a {@link Medication} constructed from the line, or {@code null} if
     *         the line is malformed
     *
     * @see Medication
     * @see MedicationReaderTests
     */
    // TODO: Implement parseLine according to the JavaDoc
    public static Medication parseLine(String line, String splitRegex) {
        return null; // dummy return
    }

    /**
     * Reads all medications from the provided {@link InputStream} and returns a
     * {@link ResultSet} containing the successfully parsed medications and the
     * line numbers of any lines that could not be parsed.
     *
     * <p>The input stream is expected to contain CSV-formatted data as described in
     * {@code src/main/resources/medications.csv}.</p>
     *
     * <p>Behavioural requirements:</p>
     * <ul>
     *   <li>The first line of the file is a header line and must be skipped.</li>
     *   <li>Line numbers in the returned error list are 1-indexed from the start of
     *       the file, meaning the header is line {@code 1} and the first data line
     *       is line {@code 2}.</li>
     *   <li>If a line cannot be parsed (i.e. {@link #parseLine} returns
     *       {@code null}), add its line number to the error list and continue reading.</li>
     *   <li>The stream must be closed when this method returns, whether successfully
     *       or due to an exception. Use try-with-resources.</li>
     *   <li>If an {@link IOException} occurs, it must be propagated to the caller —
     *       do not catch and swallow it.</li>
     * </ul>
     *
     * @param stream the {@link InputStream} containing the CSV data; assumed non-null
     * @return a {@link ResultSet} with the parsed medications and error line numbers
     * @throws IOException if an I/O error occurs while reading the stream
     *
     * @see ResultSet
     * @see #parseLine(String, String)
     * @see MedicationReaderTests
     */
    // TODO: Implement readMedications according to the JavaDoc
    public static ResultSet readMedications(InputStream stream) throws IOException {
        return null; // dummy return
    }
}
