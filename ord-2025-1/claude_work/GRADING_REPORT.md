# Vurderingsrapport — ord-2025-1 (ByteBadger)
Dato: 2026-05-06

## Samlet vurdering
- **Estimert karakter**: B (mulig B+)
- **Estimert poengandel**: ~86/100

---

## Del-vurdering

### Del 1 — Flervalg (20 %)
**Poeng**: ~18/20

| Oppgave | Svar | Vurdering |
|---------|------|-----------|
| Task 1 | `i % 3 == 1 && i % 5 == 2` | **Korrekt** ✓ |
| Task 2 | `length > 4 && isUpperCase(...)` | **Korrekt** ✓ |
| Task 3 | `rand.nextInt(end - start + 1) + start` | **Korrekt** ✓ |
| Task 4 | `Double.parseDouble(unparsedDecimal)` | **Korrekt** ✓ |
| Task 5 | `stringOutput3(numLines)` | **Korrekt** ✓ (starter på 'a', teller fremover) |
| Task 6 | `ArrayIndexOutOfBoundsException` | **Korrekt** ✓ |
| Task 7 | FizzBuzz-rekkefølge riktig ordnet | **Korrekt** ✓ (combined-sjekk øverst) |
| Task 8 | `shape = new Rectangle(4, 6)` | **Korrekt** ✓ |
| Task 9 | Valgte diagram 2, 3, 4, 6 | **Sannsynligvis ~4/5** — Diagram 1 (a:A + b:B uten assosiasjon) er også gyldig: B kan ha 0 C-referanser (*=0..*). Diagram 5 korrekt ekskludert (A har ingen deklarert assosiasjon til B). |
| Task 10 | Stable sort: ID-sortering + siste bokstav | **Korrekt** ✓ (sorterer på ID først, deretter stabil sortering på siste bokstav) |

---

### Del 2 — Kortsvarsoppgaver (20 %)
**Poeng**: ~19-20/20

**LargestElementFinder**: Fremragende. Null/tom-sjekk gir `IllegalArgumentException`, Stream-basert maks-søk er effektiv og idiomatisk. ✓

**Vigenere** (mest krevende i Del 2):
- Korrekt separat teller `j` for nøkkelen vs `i` for alle tegn — løste "key desync"-problemet
- Korrekt wrap-around med `((x % 26) + 26) % 26` for både kryptering og dekryptering
- Håndterer store og små bokstaver adskilt med riktig baseline (`'A'` vs `'a'`)
- Returnerer `null` for null-input, tom streng for tom input
- StringBuilder brukes korrekt
- **Eksemplar-implementasjon** ✓

**Anagram**: Elegant løsning som bruker `CharacterFrequency.countCharacterFrequency()` for sammenligning av frekvensmap. Håndterer null, tomme strenger, og forskjellige caser. ✓

**CharacterFrequency**: Korrekt. Ignorerer mellomrom, konverterer til lowercase, teller korrekt. ✓

---

### Del 3 — Grensesnitt (15 %)
**Poeng**: ~13-14/15

**Styrker**:
- Implementerer `ICustomBuild` korrekt ✓
- Constructor validerer null ✓
- `addPart` håndterer eksisterende deler riktig ✓
- `removePart` håndterer feil del og mengde riktig; nullstiller korrekt ved ≤0 ✓
- `getTotalPrice` med Stream er idiomatisk og korrekt ✓

**Liten feil**:
- `getParts()` returnerer intern `HashMap` direkte. Bør returnere `Collections.unmodifiableMap(this.parts)` for å hindre at kaller kan modifisere den interne tilstanden uten å gå gjennom `addPart`/`removePart`.

---

### Del 4 — Arv / Hierarki (15 %)
**Poeng**: ~9-11/15

**Styrker**:
- `isDescendantOf`: Korrekt depth-first rekursjon. Alle 5 tester i `PartCategoryTest` passerer. ✓
- `addChildCategory`: Korrekt null-sjekk, self-check, descendant-check og toveis parent/children-kobling ✓
- `removeChildCategory`: Korrekt ✓
- `addPart`: Korrekt ✓

**Kritisk feil — `getAllParts()`**:

```java
// STUDENT-KODE (FEIL):
public Collection<IComputerPart> getAllParts() {
    Collection<IComputerPart> allParts = new HashSet<>(this.parts);
    Collection<PartCategory> children = this.getChildren();
    for (PartCategory child : children) {           // FEIL 1: children kan være null → NPE
        if (child.getChildren() != null)
            allParts.addAll(getAllParts());           // FEIL 2: kaller this.getAllParts(), ikke child.getAllParts() → uendelig rekursjon → StackOverflow
        else if (child.parts != null)
            allParts.addAll(child.parts);            // FEIL 3: henter kun direkte deler, ikke barnebarn
    }
    return allParts;
}
```

**Bug 1**: `getChildren()` returnerer `null` når listen er tom. Iterasjon over `null` kaster `NullPointerException`.

**Bug 2**: `getAllParts()` kalles på `this` istedenfor `child`. Dette medfører uendelig rekursjon og `StackOverflowError` for alle kategorier med barn.

**Bug 3**: `else if`-grenen hopper kun ett nivå ned — barnebarn fanges ikke.

**Korrekt implementasjon**:
```java
public Collection<IComputerPart> getAllParts() {
    Collection<IComputerPart> allParts = new HashSet<>(this.parts);
    if (this.children != null) {
        for (PartCategory child : this.children) {
            allParts.addAll(child.getAllParts());
        }
    }
    return allParts;
}
```

Merk at `this.children` (feltet) aldri er `null` (initialisert i deklarasjonen som `new HashSet<>()`), men `getChildren()` returnerer `null`. Man bør iterere over `this.children` direkte, ELLER nullsjekke returverdien fra `getChildren()`.

---

### Del 5 — Fil-I/O (15 %)
**Poeng**: ~12-13/15

**parseLine()**: Korrekt. Trimmer whitespace, håndterer antall felter og parse-feil. ✓

**read()**: Korrekt. `try-with-resources`, hopper over header, tracker feillinjenummer, kaster `IOException` videre. ✓

**write()** — Alvorlig feil:
```java
// STUDENT-KODE (FEIL):
catch(IOException e) {
    System.out.println("I/O error occured:" + e);  // svelger exception!
}
```
JavaDoc krever `@throws IOException If an I/O error occurs when writing to the stream.` Metoden deklarerer `throws IOException` i signaturen, men catch-blokken svelger unntaket uten å kaste det videre. Kaller vil aldri motta IOException.

**Fiksen**:
```java
catch(IOException e) {
    throw e;  // kast videre
}
```
Eller fjern hele try-catch siden metoden allerede deklarerer throws IOException.

Selve formateringen (semikolon-separert, tomlinjer mellom seksjoner, totalPrice sist) er korrekt og matcher `output_example.txt`.

---

### Del 6 — Observer (15 %)
**Poeng**: ~14-15/15

**Utmerket implementasjon**:
- Constructor validerer null (NullPointerException) og ugyldig status (IllegalArgumentException) korrekt per JavaDoc ✓
- `setStatus`: validerer, oppdaterer og kaller `notifyObservers` ✓
- `addObserver` / `removeObserver`: ✓
- `notifyObservers` itererer og kaller `updateBuildStatus(buildID, status)` ✓
- VALID_STATUSES brukes korrekt til validering ✓

---

## Styrker

1. **Vigenere-implementasjonen** er lærebokeksempel — korrekt modulo-håndtering, wrap-around, case-bevaring, separat key-indeks. Tydelig at det ble brukt tid på å forstå dette problemet fullt ut.
2. **Observer-mønsteret** (Del 6) er rent og komplett implementert.
3. **Rekursjon i isDescendantOf** er korrekt og elegant.
4. **Del 1 og Del 2** er begge nesten feilfrie — sterk grunnleggende Java-kunnskap.

---

## Feil og mangler

### Kritiske
- `getAllParts()` i `PartCategory`: Uendelig rekursjon + NullPointerException — metoden er ikke-funksjonell for noe hierarki med barn.

### Alvorlige
- `WriteBuildToFile.write()`: IOException fanges og svelges istedenfor å kastes videre. Bryter kontrakten i JavaDoc.

### Mindre
- `CustomComputer.getParts()`: Returnerer intern HashMap uten beskyttelse. `Collections.unmodifiableMap(...)` eller kopi burde brukes.
- `Task9`: Sannsynligvis mistet 1 poeng ved å ekskludere diagram 1 (gyldig: b:B med 0 C-referanser er lovlig siden multiplisitet er 0..*).
