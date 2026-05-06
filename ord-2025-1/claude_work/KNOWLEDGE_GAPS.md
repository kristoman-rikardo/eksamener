# Kunnskapsgap — ord-2025-1 (ByteBadger)
Dato: 2026-05-06

---

## Gap 1: Rekursjon på feil objekt i tre-traversering

**Alvorlighet**: Høy

**Bevis**:
```java
// Fra getAllParts() i PartCategory.java:
for (PartCategory child : children) {
    if (child.getChildren() != null)
        allParts.addAll(getAllParts());  // ← kaller this.getAllParts(), ikke child.getAllParts()
    ...
}
```

**Forklaring**:
`getAllParts()` uten et objekt foran betyr `this.getAllParts()` — du rekurerer på deg selv, ikke på barnet. Resultatet er en uendelig rekursjon som ender med `StackOverflowError`. Korrekt: `allParts.addAll(child.getAllParts())`.

**Mønster**: Typisk nybegynnerfeil i rekursive trær. Ser ut som "kaller metoden rekursivt" — men rekursjonen må alltid gå ett nivå NED i hierarkiet, ikke tilbake på seg selv.

**Call to action**: Skriv ut `child.getAllParts()` høyt: "på dette barnet, kall getAllParts". Deretter skriv koden. Aldri stol på at "metodekall uten prefix" er riktig i rekursiv traversering.

---

## Gap 2: NullPointerException ved iterasjon over mulig null-samling

**Alvorlighet**: Middels

**Bevis**:
```java
// Fra getAllParts() i PartCategory.java:
Collection<PartCategory> children = this.getChildren();
for (PartCategory child : children) {  // ← children kan være null → NPE
```

**Forklaring**:
`getChildren()` returnerer `null` når listen er tom (se implementasjonen). En for-each-løkke over `null` kaster umiddelbart `NullPointerException`. Korrekt:
```java
if (children != null) {
    for (PartCategory child : children) { ... }
}
```
Alternativt: iterer over `this.children` (feltet) direkte, siden det aldri er null (initialisert i deklarasjonen).

**Mønster**: Gjentakende problem: blandingen av "getChildren() returnerer null hvis tom" og "for-each antar ikke-null" er farlig. Enten returner alltid en tom samling fra getChildren() (anbefalt), eller sjekk alltid for null før iterasjon.

**Call to action**: Vær konsekvent: enten skal `getChildren()` alltid returnere en samling (aldri null), eller du må alltid nullsjekke returverdien. Valg nr. 1 (returner tom samling) er industristandard.

---

## Gap 3: IOException svelges istedenfor å kastes videre

**Alvorlighet**: Middels

**Bevis**:
```java
// Fra WriteBuildToFile.write():
public static void write(ICustomBuild build, OutputStream outputStream) throws IOException {
    try {
        ...
        outputStream.write(outputBytes);
    }
    catch(IOException e) {
        System.out.println("I/O error occured:" + e);  // ← svelger exception!
        // Ingen throw e; her!
    }
}
```

**Forklaring**:
Metoden deklarerer `throws IOException` (lovet til kaller), men catch-blokken skriver ut meldingen og returnerer normalt. Kaller tror alt gikk bra mens filen egentlig ikke ble skrevet. JavaDoc sier eksplisitt `@throws IOException` — dette er en kontraktsbrudd.

Rettingen er enkel:
```java
catch(IOException e) {
    throw e;  // re-kast
}
```
Eller enda enklere: fjern hele try-catch. Kompilatoren lar deg deklarere `throws IOException` og la unntaket boble opp naturlig.

**Mønster**: Vanlig feil: try-catch rundt I/O-kode der studenten "vil håndtere feil" men ikke vet at svelging av exceptions er verre enn å la dem krasje. "Fail loudly" er alltid bedre enn silent failure.

**Call to action**: Tommelfingerregel: Hvis en metode deklarerer `throws XException` og du legger inn en try-catch — MÅ du enten håndtere unntaket fullt ut, eller kaste det videre med `throw e` (eller `throw new IOException(e)`). Aldri la en catch-blokk slutte uten return/throw for checked exceptions.

---

## Gap 4: Innkapsling — returnere intern mutable samling

**Alvorlighet**: Lav

**Bevis**:
```java
// Fra CustomComputer.java:
@Override
public Map<IComputerPart, Integer> getParts() {
    return this.parts;  // ← returnerer intern HashMap direkte
}
```

**Forklaring**:
Kaller mottar referansen til den interne `HashMap`. De kan kalle `parts.clear()`, `parts.put(...)`, `parts.remove(...)` uten å gå gjennom `addPart`/`removePart` og valideringslogikken din. For eksamen er dette en liten feil, men i produksjonskode er det alvorlig.

Korrekt:
```java
return Collections.unmodifiableMap(this.parts);
```
eller:
```java
return new HashMap<>(this.parts);
```

**Call to action**: Hver gang du returnerer en `Collection` eller `Map` fra en getter: spør deg "kan kaller misbruke dette?". Svar ja → bruk `Collections.unmodifiableMap/List/Set` eller returner en kopi.

---

## Gap 5: UML-objektdiagram — multiplisitet 0..* vs 1..*

**Alvorlighet**: Lav

**Bevis**:
I Task 9 ekskluderte studenten diagram 1 (a:A og b:B uten C-referanser). Diagram 1 er gyldig: B har `*`-multiplisitet mot C, som betyr 0..* (null eller flere). En b:B uten C-referanser er lovlig.

**Forklaring**:
I UML betyr `*` = "null eller mange" (0..*). For at en eksistens-begrensning skal være obligatorisk, må multiplisiteten være `1` eller `1..*`. Studenten tolket `*` som "må ha minst én C", men det er feil.

**Call to action**: Memorér: `*` = 0..*, aldri obligatorisk. Kun `1` eller `1..*` krever eksistens. Se gjerne på eksamen-spørsmål om multiplisitet.
