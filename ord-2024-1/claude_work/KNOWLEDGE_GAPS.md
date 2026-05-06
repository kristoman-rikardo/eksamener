# Kunnskapsgap — ord-2024-1 (BMW, vår 2024)

---

## Gap 1: Abstrakt metodekontract — startTime/endTime i execute()

**Alvorlighet**: Høy

**Bevis**:
```java
// WeldChassisStep.execute() — feil:
@Override
public void execute() {
    super.simulateTimePassing(); // sover 100ms, men setter ingen tider
}

// Riktig:
@Override
public void execute() {
    this.startTime = ZonedDateTime.now();
    super.simulateTimePassing();
    this.endTime = ZonedDateTime.now();
}
```

**Forklaring**: Superklassen `ProductionStep` har `execute()` som en `abstract`-metode med en klar kontrakt i JavaDoc: sett `startTime` og `endTime`, og sørg for at `getStepDuration()` returnerer minst 100ms. Å kalle `simulateTimePassing()` alene oppfyller én betingelse (100ms venting), men bryter to andre: begge tidsfeltene forblir `null`. `isExecuted()` og `getStepDuration()` returnerer feil svar.

**Mønster**: Første observasjon — men svært vanlig feil. Superklassen forteller deg hva metodene skal gjøre, men det er lett å tro at `simulateTimePassing()` er alt som trengs.

**Call to action**: Når du overrider en abstrakt metode, les JavaDoc til metoden i superklassen linje for linje. Identifiser alle sideeffekter ("startTime must be set"). Skriv dem ned og kryss av.

---

## Gap 2: Tilgangsmodifikator på subklasser

**Alvorlighet**: Høy

**Bevis**:
```java
// Feil — package-private:
class WeldChassisStep extends ProductionStep {

// Riktig:
public class WeldChassisStep extends ProductionStep {
```

**Forklaring**: Uten `public` er `WeldChassisStep` package-private — den er kun synlig innenfor `com.bmw.manufacturing.part3`. JavaDoc sa eksplisitt "It must be possible to instantiate the class outside its defined package." Å glemme `public` på en klasse er en stille feil — koden kompilerer, men oppfyller ikke kravet.

**Mønster**: Første observasjon. Lett å overse når man fokuserer på selve implementasjonen.

**Call to action**: Hver gang du oppretter en ny klasse på eksamen — sjekk straks: er den `public`? Les om JavaDoc sier noe om synlighet. Default er package-private, som nesten aldri er hva eksamen vil ha.

---

## Gap 3: Getter med forretningslogikk — truncering og uppercase

**Alvorlighet**: Middels

**Bevis**:
```java
// Feil — returnerer rå lagret verdi:
public String getIsoCountryCode() {
    return this.isoCountryCode;
}

// Riktig:
public String getIsoCountryCode() {
    return isoCountryCode.substring(0, 3).toUpperCase();
}
```

**Forklaring**: JavaDoc sa to ting: (1) returverdien skal alltid være nøyaktig 3 tegn — truncer hvis lagret verdi er lengre, (2) alle 3 tegn MUST be capital case. En getter er ikke alltid bare `return this.field`. Les JavaDoc for hvert felt og sjekk om det finnes transformasjonskrav på retur.

**Mønster**: Første observasjon. Typen feil der "alt kompilerer og ser riktig ut" men bryter implisitte kontraktskrav.

**Call to action**: Les getter-JavaDoc like nøye som konstruktør-JavaDoc. Spør deg: "Skal verdien transformeres før den returneres?" Lag mentalt en sjekkliste: truncation? uppercase? kopi av liste?

---

## Gap 4: Case-insensitiv filtrering med input-validering

**Alvorlighet**: Middels

**Bevis**:
```java
// Feil — case-sensitiv, mangler validering:
return factories.stream()
    .filter(f -> f.getIsoCountryCode().charAt(0) == character)
    .toList();

// Riktig:
public List<ICarFactory> filterFactories(List<ICarFactory> factories, char character) {
    if ((character < 65 || character > 90) && (character < 97 || character > 122)) {
        return factories; // ugyldig tegn: returner ufiltrert
    }
    char upper = Character.toUpperCase(character);
    return factories.stream()
        .filter(f -> Character.toUpperCase(f.getIsoCountryCode().charAt(0)) == upper)
        .toList();
}
```

**Forklaring**: JavaDoc inneholdt to presiseringer som er lette å overse: (1) filtreringen skal være case-insensitiv — `'D'` og `'d'` skal gi samme resultat, (2) et ugyldig tegn (utenfor A-Z/a-z) skal returnere ufiltrert liste i stedet for tom liste. Begge er "special case"-krav som krever aktiv lesing av JavaDoc.

**Mønster**: Første observasjon. Case-insensitivitet glemmes ofte.

**Call to action**: Søk alltid etter ordene "case-insensitive" og "invalid" i metodens JavaDoc. Disse signalerer spesialtilfeller. Skriv en test for ugyldig input mentalt: "hva skal skje hvis character er '@'?"

---

## Gap 5: Null-retur fra hjelpemetode → NPE

**Alvorlighet**: Lav

**Bevis**:
```java
public static String fileStringBuilder(List<SalesOfficeReport> reports) {
    if (reports == null || reports.size() < 1) return null; // null!
    ...
}

public static void write(List<SalesOfficeReport> reports, OutputStream outputStream) throws IOException {
    byte[] bytes = fileStringBuilder(reports).getBytes(...); // NPE hvis null
    ...
}
```

**Forklaring**: `fileStringBuilder()` returnerer `null` for tom/null input, og `write()` kaller `.getBytes()` på resultatet uten null-sjekk. Tomme lister er gyldige input — retur av `null` fra hjelpemetoder skaper skjulte NPE-er. Returner alltid `""` (tom streng) istedenfor `null` fra String-hjelpemetoder.

**Mønster**: Ligner på tidligere identifisert gap (null-retur vs. tom samling).

**Call to action**: Hjelpemetoder som returnerer String skal returnere `""`, ikke `null`. Hjelpemetoder som returnerer List skal returnere `Collections.emptyList()`, ikke `null`.
