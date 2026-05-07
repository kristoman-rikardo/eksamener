// FASIT — MedicationReader
// Pakk dette i package com.pharmacy.trening.partA; for å kompilere

package com.pharmacy.trening.partA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4100.pharmacy.Medication;
import no.ntnu.tdt4100.pharmacy.ResultSet;

public final class MedicationReader {

    public static final String CSV_SPLIT_REGEX = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    private MedicationReader() {}

    public static Medication parseLine(String line, String splitRegex) {
        String[] parts = line.split(splitRegex);
        if (parts.length != 4) return null;
        try {
            int id              = Integer.parseInt(parts[0].trim());
            String name         = parts[1].replace("\"", "").trim(); // strip quotes
            String manufacturer = parts[2].trim();
            double doseMg       = Double.parseDouble(parts[3].trim());

            if (name.isEmpty()) return null; // empty name is invalid

            return new Medication(id, name, manufacturer, doseMg);
        } catch (Exception e) {
            return null;
        }
    }

    public static ResultSet readMedications(InputStream stream) throws IOException {
        List<Medication> medications = new ArrayList<>();
        List<Integer> errorLines    = new ArrayList<>();

        // try-with-resources — stream is guaranteed to close
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            reader.readLine();          // skip header (line 1)
            int lineNumber = 2;         // first data line is line 2
            String line;
            while ((line = reader.readLine()) != null) {
                Medication m = parseLine(line, CSV_SPLIT_REGEX);
                if (m == null) {
                    errorLines.add(lineNumber);
                } else {
                    medications.add(m);
                }
                lineNumber++;
            }
        }
        // IOException from BufferedReader propagates to caller — NOT caught here

        return new ResultSet(medications, errorLines);
    }
}

/*
 * FELLER:
 *
 * 1. IOException svelges IKKE. Det er den vanligste feilen (se WriteBuildToFile i ord-2025-1).
 *    Catch-blokk for IOException skal enten rethrow eller ikke eksistere.
 *
 * 2. Linjenummerering starter på 2 (header = linje 1).
 *    Vanlig feil: starte på 1 uten å ta høyde for headeren, noe som gir off-by-one.
 *
 * 3. try-with-resources lukker strømmen automatisk. Uten dette: minnelekkasje og låst fil.
 *
 * 4. Quotes i navn strippes med replace("\"", ""). Uten dette: navn som '"Amoxicillin, oral"'
 *    returneres MED anførselstegnene.
 *
 * 5. Tomme navn sjekkes ETTER stripping (name.isEmpty()), ikke på rådata.
 */
