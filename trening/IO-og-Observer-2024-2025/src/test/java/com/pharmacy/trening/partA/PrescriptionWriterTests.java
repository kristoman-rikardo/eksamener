package com.pharmacy.trening.partA;

import no.ntnu.tdt4100.pharmacy.DispensedRecord;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrescriptionWriterTests {

    @Test
    void write_produces_correct_format_for_single_record() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<DispensedRecord> records = List.of(
                new DispensedRecord("RX-001", "Kari Nordmann", 3)
        );
        PrescriptionWriter.write(records, out);
        String result = out.toString(StandardCharsets.UTF_8);
        assertEquals("RX-001;Kari Nordmann;3\n", result);
    }

    @Test
    void write_produces_correct_format_for_multiple_records() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<DispensedRecord> records = List.of(
                new DispensedRecord("RX-001", "Kari Nordmann", 3),
                new DispensedRecord("RX-002", "Ola Hansen", 1),
                new DispensedRecord("RX-003", "Per Persen", 5)
        );
        PrescriptionWriter.write(records, out);
        String result = out.toString(StandardCharsets.UTF_8);
        String expected = "RX-001;Kari Nordmann;3\nRX-002;Ola Hansen;1\nRX-003;Per Persen;5\n";
        assertEquals(expected, result);
    }

    @Test
    void write_produces_empty_output_for_empty_list() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrescriptionWriter.write(List.of(), out);
        assertEquals("", out.toString(StandardCharsets.UTF_8));
    }

    @Test
    void write_flushes_the_stream() throws IOException {
        // A custom stream that tracks whether flush() was called
        final boolean[] flushed = {false};
        OutputStream trackingStream = new OutputStream() {
            private final ByteArrayOutputStream buf = new ByteArrayOutputStream();

            @Override public void write(int b) { buf.write(b); }
            @Override public void write(byte[] b) throws IOException { buf.write(b); }

            @Override
            public void flush() throws IOException {
                flushed[0] = true;
                super.flush();
            }
        };

        PrescriptionWriter.write(List.of(new DispensedRecord("RX-001", "Test", 1)), trackingStream);
        assertTrue(flushed[0], "flush() must be called before the method returns");
    }

    @Test
    void write_propagates_IOException_from_stream() {
        OutputStream failingStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("disk full");
            }

            @Override
            public void write(byte[] b) throws IOException {
                throw new IOException("disk full");
            }
        };

        List<DispensedRecord> records = List.of(new DispensedRecord("RX-001", "Test", 2));
        assertThrows(IOException.class, () -> PrescriptionWriter.write(records, failingStream));
    }

    @Test
    void write_uses_utf8_encoding() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<DispensedRecord> records = List.of(
                new DispensedRecord("RX-001", "Åse Østerås", 2)
        );
        PrescriptionWriter.write(records, out);
        // Decode with UTF-8 — Norwegian characters must survive the round-trip
        String result = out.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("Åse Østerås"));
    }
}
