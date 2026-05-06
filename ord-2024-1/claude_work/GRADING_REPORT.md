# Vurderingsrapport — ord-2024-1 (BMW, vår 2024)
Dato: 2026-05-06

## Samlet vurdering
- **Estimert karakter**: B (~83/100)
- **Estimert poengandel**: ~83 %

Sterke sider: Del 1 ser ut til å være fullstendig riktig, Del 5 er praktisk talt feilfri (alle 36 tester passerer). Del 4 MetadataReader er solid.

Svake sider: To kritiske feil i Del 3 (WeldChassisStep), to medium feil i Del 2 (getIsoCountryCode og filterFactories).

---

## Del-vurdering

### Del 1 — Flervalgsspørsmål (20 %)
**Estimert poeng**: 20/20

| Oppgave | Svar | Vurdering |
|---------|------|-----------|
| Task1 | `if(kilometer_driven > 0) return 100000 - kilometer_driven;` | ✓ Riktig |
| Task2 | `for(int i=0; i < 100; i++)` | ✓ Riktig (testes av Part1Tests) |
| Task3 | Avkommenterte to riktige linjer: `>= carB.value()` og `> carA.value()` | ✓ Riktig — dekker alle tilfeller |
| Task4 | `sorted((o1, o2) -> o2.weight() - o1.weight()).findFirst().get()` | ✓ Riktig — sorterer synkende etter vekt |
| Task5 | Null/tom-sjekk + `mapToInt(Car::value).sum()` | ✓ Riktig |
| Task6 | `new CarManufacturer("BMW", new Vat(vat), "Germany")` | ✓ Riktig — eneste konstruktør som validerer MVA korrekt via Vat |
| Task7 | FizzBuzz rekkefølge: FizzBuzz → Fizz → Buzz → tall | ✓ Riktig rekkefølge |
| Task8 | NegativeArraySizeException + returnValue(3)=3 | ✓ Riktig — korrekt analyse av array-atferd |
| Task9 | Avkommenterte "exactly one constructor" og "always public" | ✓ Riktig — begge er falske |
| Task10 | `A >= 5 \|\| B == C` | ✓ Riktig — korrekt De Morgan: !(A<5 && B!=C) = (A>=5 \|\| B==C) |

Alle tester i Part1Tests passerer.

---

### Del 2 — CarFactory og MainOffice (20 %)
**Estimert poeng**: 14/20

#### CarFactory — ~8/10

**Korrekt:**
- Konstruktør validerer: kaster `IllegalArgumentException` hvis `numberOfEmployees < 10` eller `isoCountryCode.length() < 3` ✓
- `getNumberOfEmployees()` ✓
- `getNumberOfEngineers()`: `Math.floor(employees / PI)` ✓
- `getParts()` ✓

**Feil — MEDIUM:**
```java
// Nåværende:
public String getIsoCountryCode() {
    return this.isoCountryCode;
}
```
JavaDoc krever:
- Returner **nøyaktig 3 tegn** (truncate hvis lengre)
- Alle 3 tegn **MÅ** være store bokstaver

Koden returnerer råverdien som er lagret — ingen truncering, ingen toUpperCase(). Riktig:
```java
public String getIsoCountryCode() {
    return isoCountryCode.substring(0, 3).toUpperCase();
}
```

#### MainOffice — ~6/10

**Korrekt:**
- `calculateInstallationHours()`: `employees * parts.size() * PI` ✓
- `calculateTotalEstimatedRevenue()`: bruker `getNumberOfUnitsProduced(year)`, runder ned til nærmeste tusen ✓
- `findTopFactory()`: håndterer tom liste med `Optional.empty()`, sorterer synkende ✓
- Implementerer `IMainOffice`-grensesnittet ✓ (testes av MainOfficeTests)

**Feil — MEDIUM:**
```java
// Nåværende:
public List<ICarFactory> filterFactories(List<ICarFactory> factories, char character) {
    return factories.stream().filter(f -> f.getIsoCountryCode().charAt(0) == character).toList();
}
```

To problemer:
1. **Ikke case-insensitivt**: `charAt(0) == character` sammenligner nøyaktige tegn. `'D'` matcher ikke `'d'`.
2. **Mangler validering av ugyldig tegn**: JavaDoc sier: for tegn utenfor A-Z/a-z (65–90, 97–122), returner ufiltrert liste.

Riktig:
```java
public List<ICarFactory> filterFactories(List<ICarFactory> factories, char character) {
    if ((character < 65 || character > 90) && (character < 97 || character > 122)) {
        return factories;
    }
    char upper = Character.toUpperCase(character);
    return factories.stream()
        .filter(f -> Character.toUpperCase(f.getIsoCountryCode().charAt(0)) == upper)
        .toList();
}
```

---

### Del 3 — GlobalCarProductionRegistry, ProductionLine, WeldChassisStep (20 %)
**Estimert poeng**: 13/20

#### GlobalCarProductionRegistry — 5/5 ✓
Alle fire metoder er korrekte og testen passerer.

#### ProductionLine — 5/5 ✓
`addProductionStep()`, `isStepAdded()` og `executeAll()` er alle riktig implementert. Testkrasjene skyldes Mockito-inkompatibilitet med Java 25 — **ikke feil i studentkoden**.

#### WeldChassisStep — 3/10 (to kritiske feil)

**Kritisk feil 1 — Tilgangsmodifikator:**
```java
// Nåværende — package-private:
class WeldChassisStep extends ProductionStep {
```
JavaDoc krever: "It must be possible to instantiate the class outside its defined package."
Package-private betyr klassen **ikke** kan instansieres utenfor `com.bmw.manufacturing.part3`. Mangler `public`.

**Kritisk feil 2 — execute() setter ikke startTime/endTime:**
```java
// Nåværende:
@Override
public void execute() {
    super.simulateTimePassing();
}
```

ProductionStep.execute() JavaDoc krever:
- `startTime` settes til `ZonedDateTime.now()` ved start
- `endTime` settes ved slutt
- `getStepDuration()` må returnere minst 100ms

`simulateTimePassing()` sover 100ms, men setter **ikke** tidsfelter. Begge er `protected` i superklassen og tilgjengelige. Riktig:
```java
@Override
public void execute() {
    this.startTime = ZonedDateTime.now();
    super.simulateTimePassing();
    this.endTime = ZonedDateTime.now();
}
```

---

### Del 4 — SalefiguresWriter og MetadataReader (20 %)
**Estimert poeng**: 17/20

#### MetadataReader — 10/10 ✓
Alle 4 tester passerer. Implementasjonen er solid:
- Hopper over header ✓
- Bruker `CSV_SPLIT_REGEX` ✓
- Fjerner anførselstegn fra navn ✓
- Korrekt linjetnummering (starter på 2) ✓
- Håndterer duplikater med "Duplicate country" ✓
- Kaster IOException videre ✓
- try-with-resources ✓

#### SalefiguresWriter — 7/10

3/5 tester passerer. De 2 som feiler er Mockito/Java 25-inkompatibilitet.

**Korrekt:**
- UTF-8 charset ✓
- `outputStream.write(bytes)` ✓
- `outputStream.flush()` ✓
- IOException kastes videre ✓
- Wrapper ikke OutputStream ✓
- Format `isoCountryCode;unitsSold\n` ✓

**Liten feil — NPE ved tom/null liste:**
```java
public static String fileStringBuilder(List<SalesOfficeReport> reports) {
    if (reports == null || reports.size() < 1) return null; // returnerer null
    ...
}

public static void write(...) {
    byte[] bytes = fileStringBuilder(reports).getBytes(...); // NPE hvis null!
```
Hvis `reports` er null eller tom kaller `write()` `.getBytes()` på null → NullPointerException. Burde returnere tom streng `""` eller tom byte-array.

---

### Del 5 — WarehouseInventoryManager, OrderFromPilkington, CarFactoryWithInventory (20 %)
**Estimert poeng**: 19/20

**Alle 36 tester passerer.** Dette er den sterkeste delen.

#### WarehouseInventoryManager — ✓
- `increaseQuantity()`: Håndterer null/negative ✓
- `decreaseQuantity()`: Kaster `InsufficientPartsException`, notifiserer lyttere ✓
- `addListener()` / `removeListener()`: Håndterer null og duplikater ✓
- Observer-mønsteret implementert korrekt ✓

**Marginal:** `getAvailableQuantity()` returnerer `inventoryMap.get(part)` — kaster NPE ved unboxing hvis part ikke finnes i map, men ingen test dekker dette.

#### OrderFromPilkingtonStockChangeListener — ✓
- Korrekt sjekk: `availableQuantity < 10 && partName.toLowerCase().contains("window")` ✓
- Oppdaterer orders-map kumulativt ✓
- Returnerer 0 hvis part ikke er bestilt ✓

#### CarFactoryWithInventory — ✓
- Delegerer korrekt til delegate-objektet ✓
- `produceUnit()`: Tofase-sjekk (valider alt, deretter reduser) ✓
- Kaster `AlreadyProducedException` og `InsufficientPartsException` riktig ✓
- Sporer VIN per fabrikk (ikke globalt) ✓

---

## Styrker
- **Del 1 komplett riktig** — god Java-teoriforståelse, særlig De Morgan og array-semantikk
- **Del 5 meget sterk** — observer-mønsteret, delegering og tilpassede unntak er all mestret
- **Del 4 MetadataReader** — robust CSV-parsing med riktig feilhåndtering
- **Del 3 GlobalCarProductionRegistry og ProductionLine** — alle metoder korrekte
- **Del 2 beregninger** — calculateInstallationHours, calculateTotalEstimatedRevenue og findTopFactory er riktige

## Feil og mangler

### Kritiske
- `WeldChassisStep` mangler `public` — kan ikke instansieres utenfor pakken
- `WeldChassisStep.execute()` setter ikke `startTime`/`endTime` — bryter kontrakten til superklassen

### Alvorlige
- `CarFactory.getIsoCountryCode()` truncerer ikke til 3 tegn og konverterer ikke til store bokstaver
- `MainOffice.filterFactories()` er ikke case-insensitiv og mangler validering av ugyldig tegn

### Mindre
- `SalefiguresWriter.fileStringBuilder()` returnerer `null` for tom/null liste → NPE i `write()`
- `WarehouseInventoryManager.getAvailableQuantity()` kan kaste NPE for ukjent part
