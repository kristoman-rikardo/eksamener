package com.pharmacy.trening.partA;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import no.ntnu.tdt4100.pharmacy.DispensedRecord;

// TODO: Add any necessary imports here

/**
 * Writes end-of-day dispensing records to an {@link OutputStream}.
 *
 * <p>All methods in this class are class methods.</p>
 *
 * @see PrescriptionWriterTests
 */
public final class PrescriptionWriter {

    private PrescriptionWriter() {} // DO NOT ADD CONSTRUCTORS

    /**
     * Writes a list of {@link DispensedRecord} objects to the provided
     * {@link OutputStream}.
     *
     * <p>Each record is written on its own line in the following format:</p>
     * <pre>prescriptionId;patientName;medicationCount</pre>
     *
     * <p>Example output for two records:</p>
     * <pre>
     * RX-001;Kari Nordmann;3
     * RX-002;Ola Hansen;1
     * </pre>
     *
     * <p>Behavioural requirements:</p>
     * <ul>
     *   <li>Lines are separated by a Unix newline ({@code \n}).</li>
     *   <li>Use {@link java.nio.charset.StandardCharsets#UTF_8} when encoding the string
     *       to bytes.</li>
     *   <li>The output stream must be flushed before this method returns.</li>
     *   <li>Do <strong>NOT</strong> wrap the output stream in any other class such as
     *       {@link java.io.BufferedWriter}, {@link java.io.PrintWriter}, or
     *       {@link java.io.OutputStreamWriter}. Write directly using
     *       {@link OutputStream#write(byte[])}.</li>
     *   <li>If an {@link IOException} is thrown by the stream, it must be propagated
     *       to the caller — do <strong>not</strong> catch and swallow it.</li>
     * </ul>
     *
     * @param records      the list of dispensed records to write; may be empty but not null
     * @param outputStream the stream to write to; assumed non-null
     * @throws IOException if an I/O error occurs while writing to the stream
     *
     * @see DispensedRecord
     * @see java.nio.charset.StandardCharsets#UTF_8
     * @see OutputStream#write(byte[])
     * @see PrescriptionWriterTests
     */
    // TODO: Implement write according to the JavaDoc
    public static void write(List<DispensedRecord> records, OutputStream outputStream) throws IOException {
        // dummy — replace with your implementation
    }
}
