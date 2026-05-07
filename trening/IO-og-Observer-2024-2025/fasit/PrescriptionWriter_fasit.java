// FASIT — PrescriptionWriter
// Pakk dette i package com.pharmacy.trening.partA; for å kompilere

package com.pharmacy.trening.partA;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import no.ntnu.tdt4100.pharmacy.DispensedRecord;

public final class PrescriptionWriter {

    private PrescriptionWriter() {}

    public static void write(List<DispensedRecord> records, OutputStream outputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (DispensedRecord record : records) {
            sb.append(record.prescriptionId())
              .append(";")
              .append(record.patientName())
              .append(";")
              .append(record.medicationCount())
              .append("\n");
        }
        // Encode to bytes using UTF-8 — NEVER wrap OutputStream
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        outputStream.write(bytes);  // may throw IOException — propagates to caller
        outputStream.flush();       // must flush before returning
    }
}

/*
 * FELLER:
 *
 * 1. IOException MÅ propageres. I ord-2025-1 WriteBuildToFile.write() fanget IOException
 *    og printet den uten å kaste den videre (throw e;). Det bryter konktrakten i @throws
 *    og er en alvorlig feil som medfører poengreduksjon.
 *    Fasit: ingen catch-blokk for IOException her — la den boble opp naturlig.
 *
 * 2. flush() MUST bli kalt. OutputStream kan bufre internt; uten flush() kan bytes
 *    forbli i bufferen og aldri nå filen/mottakeren.
 *
 * 3. Ikke pakk inn i BufferedWriter/PrintWriter. JavaDoc forbyr det eksplisitt.
 *    Bruk OutputStream.write(byte[]) direkte.
 *
 * 4. StandardCharsets.UTF_8 for riktig koding av norske tegn (Å, Ø, Æ).
 *    String.getBytes() uten charset bruker platform-default (som kan variere).
 *
 * 5. Tom liste → tom byte-array → ingenting skrives, flush() kalles likevel. Ingen NPE.
 */
