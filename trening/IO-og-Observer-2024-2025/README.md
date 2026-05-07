# Treningsoppgave — Fil-I/O og Observer/Delegering
### Domene: Apoteksystem | Basert på eksamener ord-2024-1 og ord-2025-1

Denne oppgaveserien er litt vanskeligere enn de to eksamensettene den er basert på.
Den treffer nøyaktig de konseptene og fellene som dukket opp i begge eksamenene.

---

## Kontekst

ApotekNord AS driver apoteker over hele landet. Hvert apotek har et lager av medikamenter,
og systemet trenger å lese medikamentdata fra filer, skrive ut daglige rapporter,
og automatisk varsle leverandører når lager-beholdningen er kritisk lav.

---

## Struktur

```
src/main/java/no/ntnu/tdt4100/pharmacy/   ← LEVERT KODE — ikke endre
src/main/java/com/pharmacy/trening/partA/ ← Del A: Fil-I/O — DU IMPLEMENTERER
src/main/java/com/pharmacy/trening/partB/ ← Del B: Observer/Delegering — DU IMPLEMENTERER
src/test/java/                            ← Tester — kjør disse for å verifisere
src/main/resources/medications.csv       ← CSV-fil brukt av testene
fasit/                                    ← Åpne IKKE før du har prøvd selv!
```

---

## Del A — Fil-I/O (40 %)

### Del A1: MedicationReader (20 %)

Implementer `MedicationReader.java` med to statiske metoder:

**`parseLine(String line, String splitRegex)`** — parser en CSV-linje til `Medication`.

**`readMedications(InputStream stream)`** — leser en hel CSV-fil og returnerer en `ResultSet`
med vellykkede medikamenter og linjene som feilet.

Viktig: Linjenummerering starter på 1 for headeren, 2 for første datalinje.

### Del A2: PrescriptionWriter (20 %)

Implementer `PrescriptionWriter.write(List<DispensedRecord>, OutputStream)`.

Format per linje: `prescriptionId;patientName;medicationCount\n`

Viktig:
- Pakk IKKE inn OutputStream i BufferedWriter eller lignende
- Flush strømmen før metoden returnerer
- Kast IOException videre — IKKE svelg den

---

## Del B — Observer og Delegering (60 %)

### Del B1: DrugStockManager (20 %)

Implementer `DrugStockManager.java` som implementerer `IStockManager`.

Viktig:
- `getAvailableStock()` returnerer 0 for ukjente medikamenter (ikke NPE)
- `decreaseStock()` kaster `InsufficientStockException` og notifiserer lyttere
- `addListener()` tillater ikke duplikater

### Del B2: AutoRefillListener (20 %)

Implementer `AutoRefillListener.java` som implementerer `IStockListener`.

Viktig:
- Bestiller 20 enheter kun når: behold < 5 OG navn inneholder "injection" (case-insensitivt)
- `getOrderedCount()` returnerer akkumulert antall bestilt

### Del B3: PharmacyWithStock (20 %)

Implementer `PharmacyWithStock.java` som implementerer `IPharmacy` og `IHasStockManager`.

Viktig:
- Deleger `getIsoCode()`, `getMedications()`, `getNumberOfStaff()` til delegate
- `dispenseUnit()`:
  1. Kast `AlreadyDispensedException` hvis prescriptionId allerede er utlevert på DETTE apoteket
  2. Valider at ALLE medikamenter har nok lager FØR du reduserer noe (to-passesjekk)
  3. Kast `InsufficientStockException` for det første utilstrekkelige medikamentet
  4. Kun ved suksess: reduser lager, legg til ID, inkrementer teller

---

## Kjøre testene

```bash
mvn test
```

Kjør én enkelt testklasse:
```bash
mvn test -Dtest=MedicationReaderTests
mvn test -Dtest=PrescriptionWriterTests
mvn test -Dtest=DrugStockManagerTests
mvn test -Dtest=AutoRefillListenerTests
mvn test -Dtest=PharmacyWithStockTests
```

---

## De fem fallgruvene fra de to eksamenene

| Felle | Fra | Hva skjer uten fiksen |
|-------|-----|----------------------|
| IOException svelges | ord-2025-1 WriteBuildToFile | Caller aner ikke at skrivingen feilet |
| Linjenummerering off-by-one | ord-2025-1 ReadPartsFromFile | Feil linjenumre i ResultSet |
| getAvailableStock NPE | ord-2024-1 WarehouseInventoryManager | NullPointerException ved unboxing |
| Manglende tofase-validering | ord-2024-1 CarFactoryWithInventory | Delvis redusert lager ved feil |
| case-insensitiv sjekk | ord-2024-1 filterFactories | "Insulin INJECTION" utløser ikke ordre |

---

## Fasit

Fasiten ligger i `fasit/`-mappen. Hver fasitfil har en `FELLER:`-seksjon som
forklarer nøyaktig hvilke fallgruver som er relevante for den aktuelle klassen.

**Åpne fasit kun ETTER at du har implementert og testet selv.**
