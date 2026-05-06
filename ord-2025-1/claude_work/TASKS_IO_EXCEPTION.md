# Øvingsoppgaver: Fil-I/O og Exception-propagering — Loggbok

## Kontekst

CruiseLog er et system for å logge og lese skipskruiseplaner. Klassen `VoyageLogger` skriver reiseplaner til fil, og `VoyageReader` leser dem tilbake.

---

## Oppgave 1: Riktig IOException-propagering ved skriving

Implementer `write()` i `VoyageLogger` slik at IOException kastes videre korrekt.

```java
import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes voyage plans to an output stream in a structured text format.
 */
public class VoyageLogger {

    /**
     * Writes a voyage summary to the provided output stream.
     * The format is:
     *   shipName;destination;departureDate\n
     *
     * @param voyage       The voyage to write.
     * @param outputStream The stream to write to.
     * @throws IOException If an I/O error occurs when writing to the stream.
     * @throws IllegalArgumentException If voyage is null.
     */
    public static void write(Voyage voyage, OutputStream outputStream) throws IOException {
        if (voyage == null) throw new IllegalArgumentException();
        // TODO: Implement write
        // Format: voyage.getShipName() + ";" + voyage.getDestination() + ";" + voyage.getDepartureDate() + "\n"
        // Write the string as bytes to outputStream
        // Do NOT swallow IOException — let it propagate
    }
}
```

```java
public class Voyage {
    private String shipName;
    private String destination;
    private String departureDate;

    public Voyage(String shipName, String destination, String departureDate) {
        this.shipName = shipName;
        this.destination = destination;
        this.departureDate = departureDate;
    }
    public String getShipName() { return shipName; }
    public String getDestination() { return destination; }
    public String getDepartureDate() { return departureDate; }
}
```

**Tester (skal bli grønne)**:

```java
@Test
public void testWriteProducesCorrectOutput() throws IOException {
    Voyage v = new Voyage("Queen Mary", "New York", "2026-06-01");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    VoyageLogger.write(v, out);
    assertEquals("Queen Mary;New York;2026-06-01\n", out.toString());
}

@Test
public void testWriteThrowsOnNullVoyage() {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    assertThrows(IllegalArgumentException.class, () -> VoyageLogger.write(null, out));
}

@Test
public void testWritePropagatesIOException() {
    Voyage v = new Voyage("Queen Mary", "New York", "2026-06-01");
    OutputStream brokenStream = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            throw new IOException("Disk full");
        }
    };
    assertThrows(IOException.class, () -> VoyageLogger.write(v, brokenStream));
}
```

---

## Fasit til Oppgave 1

```java
public static void write(Voyage voyage, OutputStream outputStream) throws IOException {
    if (voyage == null) throw new IllegalArgumentException();
    String line = voyage.getShipName() + ";" + voyage.getDestination() + ";" + voyage.getDepartureDate() + "\n";
    outputStream.write(line.getBytes());
    // INGEN try-catch her! IOException bobler opp naturlig.
}
```

**Alternativ med try-catch som kaster videre**:
```java
public static void write(Voyage voyage, OutputStream outputStream) throws IOException {
    if (voyage == null) throw new IllegalArgumentException();
    try {
        String line = voyage.getShipName() + ";" + voyage.getDestination() + ";" + voyage.getDepartureDate() + "\n";
        outputStream.write(line.getBytes());
    } catch (IOException e) {
        throw e;  // ← OBLIGATORISK: kast videre!
    }
}
```

---

## Advarsel — fellen i Oppgave 1

Det er fristende å "håndtere" feilen ved å fange den:
```java
// FEIL:
catch (IOException e) {
    System.out.println("Error writing: " + e.getMessage());  // svelger!
    // Ingen throw e; → kaller tror alt gikk bra!
}
```

Dette er en "silent failure": kaller tror `write()` lyktes, men filen er tom eller delvis skrevet. Regel: **Hvis JavaDoc lover `@throws IOException`, MÅ metoden kaste den.**

Huskeregel: `throws IOException` i signaturen = et løfte til kaller. Catch-uten-throw bryter løftet.

---

## Oppgave 2: Lesing med BufferedReader og korrekt feilhåndtering

Implementer `read()` i `VoyageReader` som leser reiseplaner fra en `InputStream`.

```java
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Reads voyage plans from an input stream.
 */
public class VoyageReader {

    /**
     * Reads voyages from the provided input stream.
     * 
     * The stream is expected to contain one voyage per line in the format:
     *   shipName;destination;departureDate
     * 
     * Lines that cannot be parsed (fewer or more than 3 semicolon-separated fields)
     * are silently skipped. 
     * 
     * The first line is a header and should be skipped.
     * 
     * @param stream The InputStream to read from. Will not be null.
     * @return A List of Voyage objects parsed from the stream.
     * @throws IOException If an I/O error occurs when reading from the stream.
     */
    public static List<Voyage> read(InputStream stream) throws IOException {
        // TODO: Implement using BufferedReader with try-with-resources
        // Skip header line
        // Parse each subsequent line: split on ";", check for exactly 3 fields
        // Return list of Voyage objects
        return null; // dummy
    }
}
```

**Tester**:

```java
@Test
public void testReadParsesValidLines() throws IOException {
    String csv = "Ship;Destination;Date\n" +
                 "Queen Mary;New York;2026-06-01\n" +
                 "Aurora;Tromsø;2026-07-15\n";
    InputStream stream = new ByteArrayInputStream(csv.getBytes());
    List<Voyage> voyages = VoyageReader.read(stream);

    assertEquals(2, voyages.size());
    assertEquals("Queen Mary", voyages.get(0).getShipName());
    assertEquals("Aurora", voyages.get(1).getShipName());
}

@Test
public void testReadSkipsMalformedLines() throws IOException {
    String csv = "Ship;Destination;Date\n" +
                 "Only;Two\n" +                    // too few fields
                 "Queen Mary;New York;2026-06-01\n";
    InputStream stream = new ByteArrayInputStream(csv.getBytes());
    List<Voyage> voyages = VoyageReader.read(stream);
    assertEquals(1, voyages.size());
}

@Test
public void testReadSkipsHeaderOnly() throws IOException {
    String csv = "Ship;Destination;Date\n";
    InputStream stream = new ByteArrayInputStream(csv.getBytes());
    List<Voyage> voyages = VoyageReader.read(stream);
    assertTrue(voyages.isEmpty());
}
```

---

## Fasit til Oppgave 2

```java
public static List<Voyage> read(InputStream stream) throws IOException {
    List<Voyage> voyages = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
        reader.readLine(); // skip header
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length != 3) continue;  // skip malformed
            voyages.add(new Voyage(parts[0], parts[1], parts[2]));
        }
    }
    // IOException fra readLine() bobler opp — try-with-resources lukker reader uansett
    return voyages;
}
```

**Nøkkelpoeng**:
- `try-with-resources` garanterer at `reader` lukkes, selv om IOException kastes.
- `reader.readLine()` kaster `IOException` — siden vi IKKE catcher den, bobler den opp til kaller (som vi lovet med `throws IOException`).
- `while ((line = reader.readLine()) != null)` — standard mønster for linje-for-linje-lesing.

---

## Oppgave 3: Kombinert les-parse-skriv med korrekt exception-kjede (vanskeligere)

Implementer `readAndFilter()` som leser voyages, filtrerer kun de med destinasjon "New York", og skriver dem til et output-stream.

```java
/**
 * Reads voyages from the input stream, keeps only those with destination "New York",
 * and writes them to the output stream using VoyageLogger.write().
 *
 * @param input  The InputStream to read from. Not null.
 * @param output The OutputStream to write to. Not null.
 * @throws IOException If an I/O error occurs.
 */
public static void readAndFilter(InputStream input, OutputStream output) throws IOException {
    // TODO: Implement
    // 1. Read all voyages using VoyageReader.read()
    // 2. Filter: only voyages where getDestination().equals("New York")
    // 3. Write each to output using VoyageLogger.write()
    // 4. IOException must propagate — do NOT swallow
}
```

**Test**:
```java
@Test
public void testReadAndFilterOutputsOnlyNewYork() throws IOException {
    String csv = "Ship;Destination;Date\n" +
                 "Queen Mary;New York;2026-06-01\n" +
                 "Aurora;Tromsø;2026-07-15\n" +
                 "Liberty;New York;2026-08-10\n";
    InputStream in = new ByteArrayInputStream(csv.getBytes());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    readAndFilter(in, out);
    String result = out.toString();
    assertTrue(result.contains("Queen Mary"));
    assertTrue(result.contains("Liberty"));
    assertFalse(result.contains("Aurora"));
}
```

---

## Fasit til Oppgave 3

```java
public static void readAndFilter(InputStream input, OutputStream output) throws IOException {
    List<Voyage> all = VoyageReader.read(input);    // IOException bobler opp
    for (Voyage v : all) {
        if ("New York".equals(v.getDestination())) {
            VoyageLogger.write(v, output);           // IOException bobler opp
        }
    }
}
```

**Ingen try-catch nødvendig** — begge metodene er allerede deklarert med `throws IOException`. Så lenge vi ikke fanger unntaket, bobler det naturlig.

---

## Advarsel — fellen i Oppgave 3

Den fristende fellen: "Jeg vil logge feilen":
```java
// FEIL:
try {
    VoyageLogger.write(v, output);
} catch (IOException e) {
    System.err.println("Could not write: " + e);
    // Ingen throw! Kaller vet ikke om feilen.
}
```

Hvis dette er en feil du ikke kan gjenopprette fra (og i fil-I/O kan du nesten aldri det), kast den alltid videre. Logging + re-kasting er akseptabelt:
```java
// OK:
catch (IOException e) {
    System.err.println("Could not write: " + e);
    throw e;
}
```
