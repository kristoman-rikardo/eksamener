# Øvingsoppgaver: Filter-metoder på riktig nivå — Tognettverk

## Kontekst

Et togsystem har `TrainLine` (togruter) og `TrainStation` (stasjoner). Hver rute har stasjoner, og hver stasjon har en ventetid (i minutter). En `NetworkManager` håndterer alle ruter.

---

## Oppgave 1: Implementer `filterLines()` i `NetworkManager`

```java
public interface ITrainLine {
    String getName();
    double getPriceInKr();
    List<IStation> getStations();
}

public interface IStation {
    String getName();
    int getWaitingTimeInMinutes();
}

public interface INetworkManager {

    /**
     * Returnerer togrutene som har minst 2 stasjoner
     * med ventetid STØRRE ENN eller lik det gitte antall minutter.
     *
     * Hvis minutesFilter er null, returneres alle ruter.
     * Hvis ingen ruter oppfyller kravet, returneres en tom liste.
     *
     * @param lines togruter å filtrere
     * @param minutesFilter minimums ventetid i minutter
     * @return filtrerte ruter
     */
    List<ITrainLine> filterLines(List<ITrainLine> lines, Integer minutesFilter);

    /**
     * Returnerer totalpris for alle ruter, rundet opp til nærmeste heltall.
     * @param lines togruter
     * @return totalprisen avrundet opp
     */
    Integer getTotalPrice(List<ITrainLine> lines);

    /**
     * Returnerer stasjonen med lengst ventetid på tvers av alle ruter.
     * Returnerer Optional.empty() hvis ingen ruter eller stasjoner.
     */
    Optional<IStation> getBusiestStation(List<ITrainLine> lines);
}
```

Implementer `NetworkManager implements INetworkManager`:

```java
public class NetworkManager implements INetworkManager {

    @Override
    public List<ITrainLine> filterLines(List<ITrainLine> lines, Integer minutesFilter) {
        // TODO
        return null;
    }

    @Override
    public Integer getTotalPrice(List<ITrainLine> lines) {
        // TODO
        return null;
    }

    @Override
    public Optional<IStation> getBusiestStation(List<ITrainLine> lines) {
        // TODO
        return null;
    }
}
```

## Tester (skal bli grønne)

```java
@Test
void filterLines_should_return_all_when_filter_is_null() {
    ITrainLine l1 = mockLine("Oslo-Bergen", 299.0, List.of(station(5), station(3)));
    ITrainLine l2 = mockLine("Oslo-Trondheim", 399.0, List.of(station(1)));
    List<ITrainLine> lines = List.of(l1, l2);

    NetworkManager nm = new NetworkManager();
    assertEquals(2, nm.filterLines(lines, null).size());
}

@Test
void filterLines_should_return_only_lines_with_at_least_2_slow_stations() {
    ITrainLine l1 = mockLine("Fast", 100.0, List.of(station(10), station(10), station(1)));
    ITrainLine l2 = mockLine("Slow", 200.0, List.of(station(2), station(1)));
    ITrainLine l3 = mockLine("None", 150.0, List.of(station(1), station(1)));

    NetworkManager nm = new NetworkManager();
    List<ITrainLine> result = nm.filterLines(List.of(l1, l2, l3), 10);
    assertEquals(1, result.size());
    assertEquals("Fast", result.get(0).getName());
}

@Test
void getTotalPrice_should_round_up() {
    ITrainLine l1 = mockLine("A", 0.4, List.of());
    ITrainLine l2 = mockLine("B", 0.4, List.of());
    assertEquals(1, new NetworkManager().getTotalPrice(List.of(l1, l2)));
}

@Test
void getBusiestStation_should_return_empty_when_no_lines() {
    assertTrue(new NetworkManager().getBusiestStation(List.of()).isEmpty());
}
```

---

## Fasit

```java
@Override
public List<ITrainLine> filterLines(List<ITrainLine> lines, Integer minutesFilter) {
    if (minutesFilter == null) return new ArrayList<>(lines);  // null-check FØRST
    return lines.stream()
        .filter(line -> line.getStations().stream()
                .filter(s -> s.getWaitingTimeInMinutes() >= minutesFilter)
                .count() >= 2)   // tell STASJONER som oppfyller kravet, ikke ruten totalt
        .toList();
}

@Override
public Integer getTotalPrice(List<ITrainLine> lines) {
    return (int) Math.ceil(
        lines.stream().mapToDouble(ITrainLine::getPriceInKr).sum()
    );
}

@Override
public Optional<IStation> getBusiestStation(List<ITrainLine> lines) {
    return lines.stream()
        .flatMap(line -> line.getStations().stream())
        .max(Comparator.comparingInt(IStation::getWaitingTimeInMinutes));
}
```

## Advarsel — fellene i denne oppgaven

**Fell 1 — Feil nivå i filter:**
Det vanlige feilen er å skrive:
```java
.filter(line -> line.getPriceInKr() >= minutesFilter)  // FEIL: sammenligner pris med ventetid!
// eller:
.filter(line -> line.getStations().size() >= 2)  // FEIL: teller alle stasjoner, ikke slow stations
```

Spør deg alltid: «Hvem er subjektet i JavaDoc-setningen?» Her er subjektet «stasjoner med ventetid ≥ filter». Betingelsen gjelder subelementer (stasjoner), ikke topruten selv. Bruk `.stream()` på listen av subelementer og tell.

**Fell 2 — Glemmer null-sjekk:**
JavaDoc sier alltid om null: «If X is null, return Y». Null-sjekk MÅ komme FØR du bruker parameteren (ellers NPE).

```java
// FEIL rekkefølge:
return lines.stream()
    .filter(line -> line.getPriceInKr() >= minutesFilter) // NullPointerException!
    .toList();

// KORREKT:
if (minutesFilter == null) return new ArrayList<>(lines);
return lines.stream()...
```

---

## Oppgave 2 (Vanskeligere): Legg til `implements ITrainLine` på din klasse

Implementer `TrainLine implements ITrainLine`:

```java
/**
 * Representerer en togrute.
 * 
 * Klassen SKAL implementere grensesnittet {@link ITrainLine}.
 * Den MÅ kunne instansieres utenfor pakken.
 * 
 * @throws IllegalArgumentException hvis name er null eller under 3 tegn,
 *         eller price er negativ
 */
public class TrainLine ??? {

    public TrainLine(String name, double priceInKr, List<IStation> stations) {
        // TODO
    }

    // TODO: implementer alle metoder fra ITrainLine
}
```

**Fellen**: Glemme `implements ITrainLine` i klasse-deklarasjonen selv om alle metodene er implementert korrekt. Uten `implements` kan ikke objektet brukes der `ITrainLine` forventes (f.eks. i `NetworkManager`-metodene).

**Kontrollsjekkliste etter implementasjon:**
1. `public class TrainLine implements ITrainLine` ✓
2. Alle tre metoder fra grensesnittet: `getName()`, `getPriceInKr()`, `getStations()` implementert ✓
3. `@Override`-annotasjon på alle overstyrte metoder ✓
