# Kunnskapsgap — ord-2022
**Oppdatert: 2026-05-13 (re-vurdering etter brukers rettelser)**

---

## Gap 1: getCharCount(Predicate) — telle nøkler vs. summere verdier
**Alvorlighet**: Middels  
**Status**: Fortsatt til stede  

**Bevis**:
```java
public int getCharCount(final Predicate<Character> chars) {
    return (int) this.counters.keySet().stream().filter(chars).count();
    // Returnerer antall DISTINKTE tegn som matcher — IKKE summen av tellerne!
}
```
**Forklaring**: `count()` teller antall elementer i streamen (f.eks. 3 tegn), ikke summen av verdiene (f.eks. 15 totale tegn). JavaDoc sier eksplisitt "sum of all the counters for chars satisfying the predicate". Riktig:
```java
return counters.entrySet().stream()
    .filter(e -> chars.test(e.getKey()))
    .mapToInt(Map.Entry::getValue)
    .sum();
```
**Mønster**: Første observasjon.  
**Call to action**: Skill mellom å *telle elementer* (`count()`) og *summere verdier* (`mapToInt(...).sum()`). Når JavaDoc sier "sum of counts" — bruk entrySet + mapToInt + sum.

---

## Gap 2: Kaste UnsupportedOperationException på umodifiserbar samling
**Alvorlighet**: Høy  
**Status**: Fortsatt til stede

**Bevis**:
```java
Collection<Character> charsCounted2 = cc2.getCountedChars(); // umodifiserbar!
// ...
charsCounted2.remove(c); // UnsupportedOperationException ved runtime!
```
**Forklaring**: `CharCounterImpl.getCountedChars()` returnerer `Collections.unmodifiableCollection(...)`. Å kalle `.remove()` kaster `UnsupportedOperationException`. Løsning:
```java
Collection<Character> charsCounted2 = new ArrayList<>(cc2.getCountedChars());
```
**Mønster**: Samme type feil som `wipe()` i ord-2024-2 (Arrays.equals vs .equals). Systematisk svakhet: bruke en samling uten å sjekke om den er modifiserbar.  
**Call to action**: Før du kaller `.remove()` eller `.add()` på en samling fra en getter — **lag alltid en kopi**: `new ArrayList<>(other.getItems())`.

---

## Gap 3: loadInto svelger exception istedenfor å kaste
**Alvorlighet**: Lav

**Bevis**:
```java
} catch (Exception e) {
    continue; // Svelger IllegalArgumentException for uaksepterte tegn
}
```
**Forklaring**: JavaDoc for `loadInto` krever at metoden kaster `IOException` dersom CharCounter ikke aksepterer tegnet. Catch-blokken ignorerer dette tyst.  
**Mønster**: Se gap om IOException fra ord-2025-1 (IOException svelges i write()). Gjentakende mønster.  
**Call to action**: Etter å ha skrevet catch-blokker: les JavaDoc for metoden og sjekk @throws. Dersom exception skal kastes videre, bruk `throw new IOException(e)` eller la spesifikke exceptions propagere.

---

## Fikset fra forrige vurdering

### Var: popAll() fjernet ikke elementer
```java
// FØR (bug): samlet, men fjernet ikke
return this.stacks.stream().flatMap(...).sorted().collect(...);
// NÅ (riktig): fjerner via removeAll (ekvivalent med clear())
this.stacks.removeAll(this.stacks);
```
✅ Fikset — metoden fjerner nå alle elementer.

### Var: countLetters(File) lastet filen som CharCounter-format
```java
// FØR (bug): loadFile.load(new FileInputStream(file))
// NÅ (riktig):
CharCounterImpl cc = new CharCounterImpl(Character::isLetter);
try (FileInputStream stream = new FileInputStream(file)) { cc.countChars(stream); }
```
✅ Fikset — teller nå faktisk bokstaver fra tekstfil.

### Var: computeDistance brukte heltallsdivisjon og Math.exp
```java
// FØR: (double)(cc2.getCharCount(c) / charTotCount2) + Math.exp(d)
// NÅ: (double) cc2.getCharCount(c) / charTotCount2 og distance += d * d
```
✅ Fikset — formelen er matematisk korrekt.

### Var: CharCounterImpl2.countChar() kastet ikke exception
```java
// FØR: ignorerte ugyldige inputs tyst
// NÅ:
if (increment < 1 || !acceptsChar(c)) throw new IllegalArgumentException();
```
✅ Fikset — kaster korrekt exception.
