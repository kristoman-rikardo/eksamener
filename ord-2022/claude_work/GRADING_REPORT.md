# Vurderingsrapport — ord-2022
Dato: 2026-05-13  
**Merknad: Del 5 er ekskludert (utenfor pensum). Vurderingen gjelder Del 1–4.**

---

## Samlet vurdering
- **Estimert karakter**: B
- **Estimert poengandel**: ~85 % av tilgjengelige poeng (72/85)

| Del | Vekt | Studenten | Maks |
|-----|------|-----------|------|
| Del 1 — Comparators | 15 | 14 | 15 |
| Del 2 — DecreasingStack | 25 | 22 | 25 |
| Del 3 — CharCounter | 20 | 16 | 20 |
| Del 4 — Fil-I/O | 25 | 20 | 25 |
| **Totalt** | **85** | **72** | **85** |

**Merknad om forbedringer fra forrige vurdering (C):**  
Følgende kritiske bugs fra forrige vurdering er nå fikset:
- ✅ `popAll()` — fjerner nå faktisk elementene (via `removeAll`)
- ✅ `countLetters(File)` — teller nå riktig fra tekstfil i stedet for å parse som CharCounter-format
- ✅ `computeDistance()` — heltallsdivisjon og feil formel (`Math.exp`) er fikset
- ✅ `CharCounterImpl2.countChar()` — kaster nå `IllegalArgumentException` som JavaDoc krever

---

## Del-vurdering

### Del 1 — Comparators (15 %)
**Poeng: 14/15**

#### Styrker
- `getHeightComparator()`: ✓ `(a, b) -> b.getHeight() - a.getHeight()` — korrekt synkende sortering.
- `getTrueShootingPercentageComparator()`: ✓ Beregner sesong-snitt korrekt (summerer alle kamper). Bruker `Double.compare(tsB, tsA)` korrekt (ikke subtraksjonsmetoden som kan gi NaN). Alfabetisk tiebreaker er korrekt.
- `main`-metodens forventede output `[John, Peter, Eric]` og `[Eric, Peter, John]` stemmer med implementasjonen. ✓

#### Svakheter
- **Liten**: Ingen nullsjekk/empty-sjekk for `getSeasonStats()` — divisjon med 0 gir `NaN` / `Infinity` for en spiller uten kamper. I praksis ufarlig siden eksamen garanterer at spillere har spilt.

---

### Del 2 — DecreasingStack + DecreasingStacks + Tester (25 %)
**Poeng: 22/25**

#### DecreasingStack (2A) — nesten perfekt ✓
- Konstruktør: ✓
- `push()`: ✓ — kaller `peek()` som kaster `EmptyStackException` hvis stacken er tom. Dette treffer bare i edge-casen der alle elementer er poppet, og stacken brukes videre. I kontekst av `DecreasingStacks` skjer ikke dette.
- `pop()`, `peek()`, `toString()`, `stream()`, `isEmpty()`: alle ✓
- Liten stil-anmerkning: `this.stack == null`-sjekk i `isEmpty()` er unødvendig (feltet initialiseres alltid), men ufarlig.

#### DecreasingStacks (2B) ✓
- `isEmpty()`: ✓
- `push()`: ✓ — prøver eksisterende stacks, oppretter ny ved behov. Elegant.
- `pop()`: ✓ — `min(Comparator.comparingInt(DecreasingStack::peek))` er et idiomatisk og korrekt valg.
- `popAll()`: ✓ — **fikset!** `flatMap(DecreasingStack::stream).sorted().collect(...)` samler alle elementer, deretter `this.stacks.removeAll(this.stacks)` tømmer stacks-lista.
  - **Stil-merknad**: `removeAll(this.stacks)` er funksjonelt ekvivalent med `this.stacks.clear()`, men unortodoks. Bruk `clear()`.

#### Tester (2C) — minimale, men godkjent
- Kompilerer ✓ (den merkelige `public`-linja alene er gyldig Java — modifikator foran neste deklarasjon)
- `emptyTest()`: tester `pop()` og `isEmpty()` ✓
- `pushTest()`: tester `push()` og `peek()` ✓
- Mangler: negative tester (push som avvises → returnerer false), test for `EmptyStackException` ved pop/peek på tom stack.

---

### Del 3 — CharCounterImpl og CharCounterImpl2 (20 %)
**Poeng: 16/20**

#### CharCounterImpl (3A) — 9 av 10 metoder korrekte
✓ `add(CharCounter)` — korrekt, kaster `IllegalArgumentException` for uaksepterte tegn.  
✓ `getCountedCharsAsString()` — fungerer (konverterer alltid til lowercase, noe som er inkonsekvent med kildedata, men fungerer for bruken i `load()`).  
✓ `getCharCountIgnoreCase(char)` — korrekt.  
✓ `countChars(String/Iterator/Iterable/Stream/Reader/InputStream)` — alle 6 korrekte.

**❌ `getCharCount(Predicate<Character>)` — feil logikk:**
```java
// FEIL — teller antall distinkte tegn som matcher, IKKE summen av tellerne
return (int) this.counters.keySet().stream().filter(chars).count();
```
JavaDoc krever: "sum of all the counters for chars satisfying the predicate". Korrekt:
```java
return counters.entrySet().stream()
    .filter(e -> chars.test(e.getKey()))
    .mapToInt(Map.Entry::getValue)
    .sum();
```
Eksempel: 'a'→5, 'b'→3 med predicate `c -> true` → feil gir `2`, riktig er `8`.

#### CharCounterImpl2 (3B) — alle 5 metoder korrekte ✓
- `acceptsChar()`: ✓ (hardkodet engelsk alfabet — `Character.isLetter()` ville vært mer robust, men holder for eksamen)
- `countChar()`: ✓ **fikset** — kaster nå `IllegalArgumentException` korrekt.
- `getCountedChars()`: ✓ — returnerer ny `ArrayList` (ingen innkapslingslekkasje), med uppercase-tegn.
- `getCharCount()`, `getTotalCharCount()`: ✓

---

### Del 4 — CharCounterFileFormat og CharCounterUtil (25 %)
**Poeng: 20/25**

#### CharCounterFileFormatImpl (4A) — solid ✓
✓ **Filformat dokumentert** i JavaDoc og eksempelfil (`a:2\nb:1\nc:1\nd:1`).  
✓ `save()` — korrekt.  
✓ `load()` — smart to-pass-løsning: laster til en "accept all"-instans for å finne hvilke tegn som finnes, bygger deretter riktig begrenset `CharCounterImpl`.  
✓ `loadInto()` — korrekt parsing, håndterer kant-tilfellet der tegnet er `:` via `parts[parts.length-1]`.  

**Liten kontrakt-anmerkning**: JavaDoc for `loadInto` sier "throws IOException if CharCounter doesn't accept the characters", men `catch (Exception e) { continue; }` svelger `IllegalArgumentException` tyst. Bør kastes videre.  
**Liten**: Mangler `try-with-resources` for `BufferedReader` i `loadInto`.

#### CharCounterUtil (4B)

✓ `countLetters(File)` — **fikset!** Bruker nå `new CharCounterImpl(Character::isLetter)` med `try-with-resources` og `cc.countChars(stream)`. Korrekt.  

✓ `unmodifiableCharCounter(CharCounter)` — korrekt anonym klasse, delegerer alle lesemetoder, kaster `UnsupportedOperationException` på `countChar`. ✓

**❌ `computeDistance(CharCounter, CharCounter)` — runtime-krasj for typisk input:**

Algoritmen er nå **matematisk korrekt** (float-divisjon, kvadratsummer, riktig håndtering av felles og unike tegn). Men:

```java
Collection<Character> charsCounted2 = cc2.getCountedChars(); // unmodifiable i CharCounterImpl!
// ...
charsCounted2.remove(c); // UnsupportedOperationException ved kjøring
```

`CharCounterImpl.getCountedChars()` returnerer `Collections.unmodifiableCollection(...)`. Kall på `.remove()` kaster `UnsupportedOperationException` for enhver bokstav som er felles mellom cc1 og cc2.

**Fix — én linje**:
```java
Collection<Character> charsCounted2 = new ArrayList<>(cc2.getCountedChars());
```

Metoden ville fungert fint dersom begge argumenter er `CharCounterImpl2` (som returnerer en ny `ArrayList`). Men i det generelle tilfellet (to `CharCounterImpl`-instanser) krasjer den.

---

## Styrker
- Del 1: ren lambda-kode, korrekt `Double.compare`, alfabetisk tiebreaker ✓
- Del 2B `pop()`: elegant bruk av `min(Comparator.comparingInt(::peek))` ✓
- Del 4A `load()`: gjennomtenkt to-pass design som håndterer tegnet `:` korrekt ✓
- Flere kritiske bugs fikset siden forrige vurdering — tydelig fremgang

---

## Feil og mangler

### Alvorlige
- **Del 3A — `getCharCount(Predicate)`**: teller distinkte nøkler, ikke sum av verdier. Gir feil svar i alle ikke-trivielle tilfeller.
- **Del 4B — `computeDistance`**: `charsCounted2.remove(c)` kaster `UnsupportedOperationException` på `CharCounterImpl`-input → metoden krasjer for de fleste reelle inputs.

### Mindre
- **Del 2C** — minimale tester; mangler negative tester og exception-tester.
- **Del 2B** — `removeAll(this.stacks)` fungerer men er unortodoks; bruk `clear()`.
- **Del 4A — `loadInto()`** — svelger `IllegalArgumentException` istedenfor å kaste `IOException` som JavaDoc krever.
- **Del 3A — `getCountedCharsAsString()`** — konverterer alltid til lowercase; kan miste case-informasjon i kant-tilfeller.
