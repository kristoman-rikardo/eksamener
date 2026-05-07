package com.pharmacy.trening.partA;

import no.ntnu.tdt4100.pharmacy.Medication;
import no.ntnu.tdt4100.pharmacy.ResultSet;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class MedicationReaderTests {

    private static InputStream toStream(String csv) {
        return new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));
    }

    // ---------------------------------------------------------------
    // parseLine
    // ---------------------------------------------------------------

    @Test
    void parseLine_returns_medication_for_valid_line() {
        Medication m = MedicationReader.parseLine("1,Paracetamol,GSK,500.0", MedicationReader.CSV_SPLIT_REGEX);
        assertNotNull(m);
        assertEquals(1, m.id());
        assertEquals("Paracetamol", m.name());
        assertEquals("GSK", m.manufacturer());
        assertEquals(500.0, m.doseMg(), 0.001);
    }

    @Test
    void parseLine_strips_quotes_from_name() {
        Medication m = MedicationReader.parseLine("3,\"Amoxicillin, oral\",Pfizer,250.0", MedicationReader.CSV_SPLIT_REGEX);
        assertNotNull(m);
        assertEquals("Amoxicillin, oral", m.name());
    }

    @Test
    void parseLine_returns_null_when_too_few_fields() {
        assertNull(MedicationReader.parseLine("BADDATA", MedicationReader.CSV_SPLIT_REGEX));
    }

    @Test
    void parseLine_returns_null_when_id_not_parseable() {
        assertNull(MedicationReader.parseLine("NOTANUMBER,Paracetamol,GSK,500.0", MedicationReader.CSV_SPLIT_REGEX));
    }

    @Test
    void parseLine_returns_null_when_dose_not_parseable() {
        assertNull(MedicationReader.parseLine("9,Diazepam,Roche,ABC", MedicationReader.CSV_SPLIT_REGEX));
    }

    @Test
    void parseLine_returns_null_when_name_is_empty() {
        assertNull(MedicationReader.parseLine("7,,Pfizer,200.0", MedicationReader.CSV_SPLIT_REGEX));
    }

    // ---------------------------------------------------------------
    // readMedications
    // ---------------------------------------------------------------

    @Test
    void readMedications_skips_header_and_returns_correct_medications() throws IOException {
        String csv = "id,name,manufacturer,dose_mg\n"
                   + "1,Paracetamol,GSK,500.0\n"
                   + "2,Ibuprofen,Nycomed,400.0\n";
        ResultSet rs = MedicationReader.readMedications(toStream(csv));
        assertEquals(2, rs.medications().size());
        assertEquals("Paracetamol", rs.medications().get(0).name());
        assertEquals("Ibuprofen", rs.medications().get(1).name());
    }

    @Test
    void readMedications_returns_empty_lists_for_header_only() throws IOException {
        ResultSet rs = MedicationReader.readMedications(toStream("id,name,manufacturer,dose_mg\n"));
        assertTrue(rs.medications().isEmpty());
        assertTrue(rs.errorLines().isEmpty());
    }

    @Test
    void readMedications_records_error_line_numbers_correctly() throws IOException {
        // Header = line 1; BADDATA is line 6; empty name is line 8; bad dose is line 10
        String csv = "id,name,manufacturer,dose_mg\n"     // line 1 — header
                   + "1,Paracetamol,GSK,500.0\n"          // line 2 — ok
                   + "2,Ibuprofen,Nycomed,400.0\n"        // line 3 — ok
                   + "3,\"Amoxicillin, oral\",Pfizer,250.0\n" // line 4 — ok
                   + "4,Insulin injection,NovoNordisk,100.0\n" // line 5 — ok
                   + "5,BADDATA\n"                         // line 6 — error
                   + "6,Metformin,AstraZeneca,850.0\n"    // line 7 — ok
                   + "7,,Pfizer,200.0\n"                  // line 8 — error (empty name)
                   + "8,Warfarin injection,Bayer,5.0\n"   // line 9 — ok
                   + "9,Diazepam,Roche,ABC\n";            // line 10 — error (bad dose)
        ResultSet rs = MedicationReader.readMedications(toStream(csv));
        assertEquals(6, rs.medications().size());
        assertEquals(3, rs.errorLines().size());
        assertTrue(rs.errorLines().contains(6));
        assertTrue(rs.errorLines().contains(8));
        assertTrue(rs.errorLines().contains(10));
    }

    @Test
    void readMedications_handles_quoted_names_with_commas() throws IOException {
        String csv = "id,name,manufacturer,dose_mg\n"
                   + "3,\"Amoxicillin, oral\",Pfizer,250.0\n";
        ResultSet rs = MedicationReader.readMedications(toStream(csv));
        assertEquals(1, rs.medications().size());
        assertEquals("Amoxicillin, oral", rs.medications().get(0).name());
    }

    @Test
    void readMedications_propagates_IOException() {
        InputStream brokenStream = new InputStream() {
            @Override public int read() throws IOException {
                throw new IOException("simulated disk failure");
            }
        };
        assertThrows(IOException.class, () -> MedicationReader.readMedications(brokenStream));
    }

    @Test
    void readMedications_reads_from_actual_csv_resource() throws IOException {
        InputStream stream = MedicationReaderTests.class
                .getClassLoader().getResourceAsStream("medications.csv");
        assertNotNull(stream, "medications.csv must be on the classpath");
        ResultSet rs = MedicationReader.readMedications(stream);
        assertEquals(6, rs.medications().size());
        assertEquals(3, rs.errorLines().size());
    }
}
