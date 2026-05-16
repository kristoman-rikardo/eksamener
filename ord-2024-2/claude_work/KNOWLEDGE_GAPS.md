# Kunnskapsgap — ord-2024-2

---

## Gap 1: Array-sammenligning med Arrays.equals()
**Alvorlighet**: Høy
**Bevis**:
```java
// Fra GlobalResearchArchive.wipe():
if (secretCode.equals(WIPE_CODE))  // FEIL: sammenligner referanser

// Korrekt:
if (Arrays.equals(secretCode, WIPE_CODE))  // sammenligner innhold
```
**Forklaring**: I Java er arrays objekter. `.equals()` er arvet fra `Object` og sammenligner bare referanser (samme regel som `==`). For å sammenligne *innholdet* i to arrays bruker man `Arrays.equals(a, b)`. Dette er det tilsvarende av `String.equals()` for arrays. Resultatet her er at `wipe()` ALDRI kan rydde arkivet — den kaster alltid SecurityException.
**Mønster**: Første observasjon. Direkte variant av klassisk «== vs .equals()»-fellen, men for arrays.
**Call to action**: Husk: for arrays → `Arrays.equals()`. For String → `.equals()`. Aldri `==` eller `.equals()` for sammenligningav innhold i mutable objekter.

---

## Gap 2: Feil filter-logikk (facility-budsjett vs. prosjekt-budsjetter)
**Alvorlighet**: Høy
**Bevis**:
```java
// Fra GroupHeadquarters.filterFacilities() — FEIL:
.filter(f -> f.getProjects().size() > 2)
.filter(f -> f.getYearlyBudgetInMillions() >= budgetFilter)

// Korrekt (JavaDoc: «minst 3 prosjekter med budsjett >= filter»):
.filter(budgetFilter == null ? f -> true : 
    f -> f.getProjects().stream()
            .filter(p -> p.budgetInMillions() >= budgetFilter)
            .count() >= 3)
```
**Forklaring**: JavaDoc sier «fasiliteter som har minst 3 *prosjekter* med budsjett ≥ filter». Studenten sjekker fasiliteten sitt *totale* årsbudsjett mot filteret — det er et annet tall. Fellen er å blande to nivåer: fasilitet-nivå vs. prosjekt-nivå. Les JavaDoc-setningen nøye og spør: «Hvem er subject?» (Prosjektene, ikke fasiliteten.)
**Mønster**: Tilsvarende feil som ord-2024-1 sin `filterFactories()` (case-insensitiv filtrering). Begge eksamener har en filter-metode som studenten misforstår.
**Call to action**: Når du implementerer filter-metoder: les nøye hvem (fasilitet? prosjekt? person?) filterbetingelsen gjelder for. Bruk `flatMap` eller `.stream()` på sublisten hvis betingelsen gjelder subelementene.

---

## Gap 3: Manglende null-håndtering i grensesnitt-filter
**Alvorlighet**: Middels
**Bevis**:
```java
// filterFacilities() — mangler null-sjekk for budgetFilter:
// JavaDoc: «If the budget filter parameter is null, return all facilities»
// Studentens kode: f.getYearlyBudgetInMillions() >= budgetFilter → NullPointerException!

// Korrekt:
if (budgetFilter == null) return new ArrayList<>(facilities);
```
**Forklaring**: Når JavaDoc eksplisitt sier «if X is null, return Y», MÅ du sjekke for null. Det er ikke valgfritt. Ternary eller tidlig return er vanlige mønstre.
**Mønster**: Første observasjon.
**Call to action**: Les alltid @param-linjene i JavaDoc for null-betingelser. Etter implementasjon, spør: «Hva skjer hvis noen sender inn null? JavaDoc sier det?»

---

## Gap 4: Manglende `implements`-deklarasjon
**Alvorlighet**: Høy
**Bevis**:
```java
// ResearchFacility deklarert uten grensesnitt:
public class ResearchFacility {  // FEIL

// Korrekt:
public class ResearchFacility implements IResearchFacility {
```
**Forklaring**: Alle metoder fra `IResearchFacility` er implementert korrekt, men klassen deklarerer ikke `implements IResearchFacility`. Det betyr at objektet ikke kan brukes der `IResearchFacility` forventes: `List<IResearchFacility> list = List.of(new ResearchFacility(...));` gir kompileringsfeil.
**Mønster**: Samme problem som ord-2024-1 sin `WeldChassisStep` som manglet `public`. Tilgangsmodifikatorer og grensesnitt-deklarasjoner er detaljfeil som gir store konsekvenser.
**Call to action**: Etter du har skrevet en klasse, sjekk alltid: (1) Er `public` på rett sted? (2) Har jeg `implements InterfaceNavn` i klasse-deklarasjonen?

---

## Gap 5: Feil dato-referanse i beregning
**Alvorlighet**: Middels
**Bevis**:
```java
// getNumberOfDaysUntilEndTime() — FEIL:
LocalDate start = project.get().startDate();    // prosjektets startdato!
LocalDate end = project.get().estimatedEndDate();
return OptionalInt.of((int)ChronoUnit.DAYS.between(start, end));

// Korrekt («fra TODAY til estimert sluttdato»):
LocalDate today = LocalDate.now();
LocalDate end = project.get().estimatedEndDate();
return Optional.of((int)ChronoUnit.DAYS.between(today, end));
```
**Forklaring**: JavaDoc sier «fra I DAG til estimert sluttdato». Studenten bruker prosjektets startdato, som gir prosjektets totale varighet — et helt annet tall. Les «from today» som `LocalDate.now()`.
**Mønster**: Første observasjon. Kan skyldes at man tenkte «start-til-slutt» istedenfor «nå-til-slutt».
**Call to action**: Når JavaDoc sier «from today», bruk alltid `LocalDate.now()` eller `ZonedDateTime.now()`.
