# Vurderingsrapport — ord-2024-2 (Mercedes)
Dato: 2026-05-09

## Samlet vurdering
- **Estimert karakter**: B (~88/100)
- **Estimert poengandel**: ~88 %

---

## Del-vurdering

### Del 1 — Programmeringsoppgaver (20 %)
**Poeng**: 20/20

Alle 10 oppgaver korrekt besvart.

| Oppgave | Svar | Vurdering |
|---------|------|-----------|
| Task01 | `lengthOfSide * 4` | ✓ |
| Task02 | `for(int i=15; i > 0; i--)` | ✓ |
| Task03 | C — returns 20 (handberegning korrekt dokumentert) | ✓ |
| Task04 | A — class needs public modifier for cross-package use | ✓ |
| Task05 | Egendefinert korrekt implementasjon (ingen av de gitte alternativene var riktige) | ✓ |
| Task06 | `((Rectangle)shapes.get(1)).calculateArea()` | ✓ |
| Task07 | `Math.pow((double) number, 2)` | ✓ |
| Task08 | E og F (begge fyller hele array med "FILL") | ✓ |
| Task09 | `if (c >= b && d == c && b > a)` + korrekt retur | ✓ |
| Task10 | E — metoden kan kaste StringIndexOutOfBoundsException | ✓ |

**Merk Task05**: Ingen av de gitte alternativene gir korrekt svar ("unique" betyr «forekommer nøyaktig én gang», ikke bare «distinct»). Studenten skrev korrekt egendefinert implementasjon. Full credit gitt.

---

### Del 2 — ResearchFacility og GroupHeadquarters (20 %)
**Poeng**: ~14/20

#### ResearchFacility
- Konstruktør med alle valideringer: ✓
- `getName()`, `getYearlyBudgetInMillions()`, `getProjects()`: ✓
- **KRITISK**: Klassen deklarerer IKKE `implements IResearchFacility`. Den kan derfor ikke brukes polymorfisk som `IResearchFacility` der GroupHeadquarters forventer det. -1.5p
- `getNumberOfDaysUntilEndTime()`:
  - **Feil**: Beregner dager fra `startDate` til `estimatedEndDate` (prosjektets varighet), men JavaDoc sier «fra TODAY til estimatedEndDate». -1p
  - **Feil**: Returnerer `OptionalInt` istedenfor `Optional<Integer>`. -0.5p

#### GroupHeadquarters
- `implements IGroupHeadquarters`: ✓
- `getTotalBudgetForAllFacilities()`: ✓ Bruker `Math.ceil()` korrekt.
- `sortFacilities()`: ✓ Alfabetisk sortering på navn.
- `getMostExpensiveProject()`: ✓ Korrekt bruk av `flatMap` + `max`.
- `filterFacilities()`: **FEIL** på to punkter:
  - Sjekker `f.getYearlyBudgetInMillions() >= budgetFilter` (fasiliteten totale budsjett) istedenfor å telle **prosjekter** med budget ≥ filter. -2p
  - Mangler null-sjekk: Hvis `budgetFilter == null` skal ALLE fasiliteter returneres. Nå kaster koden NullPointerException. -1p

```java
// Studentens feil:
.filter(f -> f.getYearlyBudgetInMillions() >= budgetFilter)

// Korrekt:
.filter(f -> f.getProjects().stream()
    .filter(p -> p.budgetInMillions() >= budgetFilter)
    .count() >= 3)
```

---

### Del 3 — GlobalResearchArchive + AssemblyLine (20 %)
**Poeng**: ~17/20

#### GlobalResearchArchive
- `submitResearchReport()`: ✓ Korrekt logikk.
- `isReportInArchive()`: ✓
- `getNumberOfSubmittedResearchReports()`: ✓
- `wipe()`: **KRITISK FEIL** — metoden kan ALDRI slette arkivet:
  - `secretCode.equals(WIPE_CODE)` bruker referanselikhet for char[]-arrays → alltid `false`.
  - Korrekt: `Arrays.equals(secretCode, WIPE_CODE)`.
  - Kaster `IllegalArgumentException` for null-input, men JavaDoc krever `NullPointerException`. -3p

```java
// Studentens feil:
if (secretCode.equals(WIPE_CODE))  // alltid false for char[]

// Korrekt:
if (Arrays.equals(secretCode, WIPE_CODE))

// Og:
if (secretCode == null) throw new NullPointerException(); // ikke IllegalArgumentException
```

#### AssemblyLine
- `addAssemblyStep()`: ✓ Traverserer til siste steg og lenker til.
- `isStepAdded()`: ✓ Sjekker alle steg ved equals.
- `assembleAll()`: ✓ Korrekt traversering og kall på assemble().

#### AssemblePistonsStep
- `extends AssemblyStep`: ✓
- `public` klasse: ✓
- Konstruktør med kun stepId, kaller `super(stepId, true)`: ✓
- `assemble()` setter startTime, kaller `simulateTimePassing()`, setter endTime: ✓

---

### Del 4 — Fil-I/O (20 %)
**Poeng**: ~19/20

#### ReportsListWriter
- Formatter output korrekt (uuid;navn;år per linje, med separator): ✓
- Kaller `outputStream.flush()`: ✓
- Formatteringstest (`write_should_produce_correct_results_line_by_line`): ✓ BESTÅTT
- **Merknad**: 2 av 3 tester feiler pga. Mockito-inkompatibilitet med Java 25 (JVM 25.0.2). Dette er et testinfrastrukturproblem, IKKE en feil i studentens kode. Studentens implementasjon er korrekt.

#### ProjectsReader
- Leser CSV korrekt med regex for quoted names: ✓
- Hopper over header: ✓
- Håndterer duplikater med korrekt feilmelding: ✓
- Håndterer parsefeil med try-catch: ✓
- Alle 4 tester bestått: ✓
- **Mindre feil**: Bruker `reader.close()` eksplisitt istedenfor try-with-resources. Ressurslekkasje ved unntak. -1p

---

### Del 5 — Observer, unntak og delegering (20 %)
**Poeng**: 20/20

Alle 45 tester bestått (24 + 12 + 9).

#### SimpleHumanResourceManager
- Implementerer `IHumanResourceManager`: ✓
- Alle listener-metoder (add/remove/count): ✓
- `hire()`: kaster alle 3 korrekte unntak i riktig rekkefølge, notifiserer lyttere: ✓
- `fire()`: fjerner og notifiserer: ✓
- `getTotalYearlySalaryForAllEmployees()`: korrekt sum: ✓
- `getSalaryFor()`: korrekt Optional-håndtering: ✓

#### HireFireStatisticsListener
- Implementerer `IEmployementListener`: ✓
- `trigger()`: null-sjekk, tracker kun HIRED-events: ✓
- `getNumberOfHiredPeople(role)`: korrekt filter: ✓

#### ResearchFacilityWithHrManager
- Implementerer `IResearchFacility` og `IHasHumanResourceManager`: ✓
- Alle delegerende metoder: ✓
- `startProject()`: sjekker alle 3 betingelser korrekt, kaster `CanNotStartProjectException`: ✓
- `endProject()`: sjekker om prosjekt finnes, avslutter alle ansatte, fjerner prosjekt: ✓

---

## Styrker
- **Del 5 er eksemplarisk**: Korrekt Observer-implementasjon, delegering, og unntakshåndtering. Alle 45 tester grønne.
- **Del 1 perfekt**: God konseptuell forståelse av Java, beregninger og uttrykk.
- **AssemblyLine og AssemblePistonsStep**: Korrekt linked-list-traversering og arv.
- **ProjectsReader**: God CSV-håndtering med regex og feilhåndtering.
- **Godt debugget**: Kommentaren i ReportsListWriter viser at studenten aktivt prøvde BufferedWriter, testet det og byttet strategi.

---

## Feil og mangler

### Kritiske
- **`wipe()` i GlobalResearchArchive**: Bruker `char[].equals()` istedenfor `Arrays.equals()`. Metoden kan ALDRI fullføres uten å kaste SecurityException — den er komplett ikke-funksjonell.

### Alvorlige
- **`filterFacilities()`**: Filtrerer på fasiliteten totale budsjett istedenfor prosjektenes individuelle budsjetter. Gir feil resultat for nesten alle inputs.
- **Manglende `implements IResearchFacility`** på ResearchFacility-klassen. Kan ikke brukes polymorfisk der grensesnitt forventes.

### Mindre
- **`getNumberOfDaysUntilEndTime()`**: Beregner fra startDate (ikke fra LocalDate.now()). Gir prosjektets varighet, ikke dager til slutt fra i dag.
- **`filterFacilities()`**: Kaster NullPointerException når budgetFilter er null (skal returnere alle fasiliteter).
- **ProjectsReader**: Mangler try-with-resources (ressurslekkasje ved IOException).
- **`wipe()`**: Kaster `IllegalArgumentException` for null istedenfor `NullPointerException`.
