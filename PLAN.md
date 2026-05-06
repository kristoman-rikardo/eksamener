# Eksamensforberedelse TDT4100 — Mål: A 18. mai 2026

## Slik bruker du dette systemet

Hver gang du setter i gang med en ny eksamenssett, gjør følgende **i rekkefølge**:

1. Les `PLAN.md` (dette dokumentet) for overordnet strategi og studieplan.
2. Les `CLAUDE.md` for detaljerte instrukser om vurdering og oppgavelaging.
3. Les `KNOWLEDGE_TRACKER.md` for å se hvilke konsepter som har kjente svake punkter.
4. Åpne eksamensmappa som skal gjennomgås.
5. Les `QUESTION_LOG.md` i den eksamensmappa — spørsmål der er signaler på svake punkter.
6. Gjennomfør vurdering og lag oppgaver iht. `CLAUDE.md`.
7. Skriv alle resultater i `<eksamenmappe>/claude_work/`.
8. Oppdater `KNOWLEDGE_TRACKER.md` i rotnivå med nye funn.

---

## Overordnet mål

**Karaktermål: A på TDT4100-eksamen 18. mai 2026.**

TDT4100 er NTNU sitt kurs i objektorientert programmering med Java. Eksamen er typisk bygget opp slik:

| Del | Vekt | Innhold |
|-----|------|---------|
| Del 1 | 20% | Flervalgsspørsmål — Java/OOP-teori |
| Del 2 | 20% | Kodelesing, kortsvarsoppgaver |
| Del 3–6 | 60% | Implementering i gitt kodebase (basert på JavaDoc og grensesnitt) |

For **A** kreves typisk >89 % riktig. Spesielt viktig:
- Ingen kompileringsfeil (kan gi 0 poeng på en hel metode)
- Klassenavn og metodenavn må matche JavaDoc **nøyaktig**
- Grensesnitt og arv korrekt implementert
- Edge cases og unntak håndtert slik JavaDoc spesifiserer

---

## Eksamensprioriteringsliste

Nyeste eksamener er mest relevante og ligner mest på det kommende formatet.

### Høy prioritet
1. `ord-2025-2` — Sykkel-tema (TwoWheels), del 1–6
2. `ord-2025-1` — ByteBadger-tema, del 1–6
3. `ord-2024-2` — Mercedes-tema, del 1–5
4. `ord-2024-1` — BMW-tema, del 1–5
5. `kont-2024` — Kontinuasjonseksamen 2024

### Middels prioritet
6. `ord-2023` — del 1–9
7. `kont-2023` — Kontinuasjonseksamen 2023
8. `ord-2022` — del 1–5

### Lav prioritet (bruk for spesifikke konseptøvelser)
9. `kont-2022`
10. `ord-2021`
11. `kont-2021`
12. `kont-2020`
13. `ord-2020`
14. `ord-2019`

---

## Studieplan (12 dager til eksamen)

Gjøre så mange eksamenssett + øvingsoppgaver som mulig innen 18. mai. Har flere eksamener som kommer før denne, men må lære meg det da jeg henger etter her. 

## Per-eksamen arbeidsflyt (Claude følger dette)

### Steg 1: Forberedelse
- Les `README.md` og alle oppgave-READMEer i eksamensmappa
- Les all JavaDoc nøye (klasse- og metodenivå) — **viktigst av alt**
- Forstå domenet og konteksten (sykler, biler, mat osv.)
- Identifiser hvilke deler som er flervalg vs. implementering

### Steg 2: Vurdering
- Gå gjennom hver del i rekkefølge
- Kjør `mvn test` der det er mulig
- Vurder koden linje for linje mot JavaDoc-kravene
- Identifiser: manglende implementasjon, logikkfeil, kompileringsfeil, stilproblemer
- Gi poengvurdering per del

### Steg 3: Kunnskapsgap-analyse
- Identifiser mønstre på tvers av feil i denne eksamen
- Kategoriser per konsept (arv, grensesnitt, unntak, Observer, fil-I/O, etc.)
- Vurder alvorlighet: Høy / Middels / Lav

### Steg 4: Oppgavelaging
- Lag 2–4 øvingsoppgaver per identifisert svakt punkt
- Oppgavene skal ligne eksamensstil (JavaDoc-spesifisert, sparsom prosa)
- Inkluder "feller" og edge cases
- Oppgavene skal være litt vanskeligere enn det studenten hadde feil på

### Steg 5: Dokumentasjon
- Skriv alt i `<eksamenmappe>/claude_work/`
  - `GRADING_REPORT.md` — Detaljert vurdering
  - `KNOWLEDGE_GAPS.md` — Identifiserte gap
  - `TASKS_<KONSEPT>.md` — Én fil per gap med oppgaver og fasit
- Oppdater `KNOWLEDGE_TRACKER.md` i rotnivå

---

## Hva skiller A fra B?

### A-nivå kjennetegn
- Alle metoder kompilerer uten feil
- Grensesnitt implementeres korrekt og komplett
- Arv: `super()` kalles i konstruktør, `@Override` brukes konsekvent
- Observer: notify kalles ved **alle** relevante tilstandsendringer
- Fil-I/O: try-with-resources, riktig charset, IOException håndteres
- Unntak kastes nøyaktig der JavaDoc krever det
- Null og tomme lister håndteres korrekt
- Klassenavn og metodenavn er **identiske** med JavaDoc-spesifikasjonen

### Typiske B/C/D-feil
- Glemmer `super(...)` i konstruktør ved arv
- Implementerer grensesnitt men mangler noen metoder
- Observer: notifiserer ikke ved alle relevante tilstandsendringer
- Returnerer intern liste direkte istedenfor kopi (innkapslingsfeil)
- Bruker `==` istedenfor `.equals()` for strenger
- Unntak kastes ikke der JavaDoc krever det
- Kompileringsfeil pga. feil returtype eller manglende import
- Kaster RuntimeException der det skal være IllegalArgumentException

---

## Viktige Java/OOP-konsepter å mestre

### Grensesnitt og arv
- `implements` vs. `extends`
- Abstrakte klasser vs. interfaces — når brukes hva
- Polymorfisme: bruke interfacetype som variabeltype
- `instanceof` og safe casting

### Innkapsling
- `private` / `protected` / `public` — korrekt bruk
- Returnere kopier av lister (`new ArrayList<>(this.list)`)
- Defensive kopier i konstruktører

### Observer-mønsteret (svært vanlig på eksamen)
- Registrere/fjerne observers (`addObserver`, `removeObserver`)
- Notifisere ved alle relevante tilstandsendringer
- Bruke interface-type for løs kobling
- Iterere over kopi av lytterliste for å unngå `ConcurrentModificationException`

### Collections og Streams
- `List`, `Map`, `Set` — når brukes hva
- Streams og lambdas (grunnleggende: `filter`, `map`, `collect`)
- `Comparator` og `Comparable` for sortering

### Unntak
- Checked (`IOException`) vs. unchecked (`IllegalArgumentException`)
- `throws` i metodesignatur for checked exceptions
- `try-catch-finally` og try-with-resources
- Kast `IllegalArgumentException` ved ugyldig input, `IllegalStateException` ved ugyldig objekttilstand

### Fil-I/O
- `BufferedReader`, `BufferedWriter`
- `Files.readAllLines()`, `Files.write()`
- try-with-resources er obligatorisk for å lukke strømmer

### Rekursjon og hierarkiske strukturer
- Basiscase og rekursivt kall
- Traversere trær (depth-first)
- Beregne dybde, antall barn, sum av verdier rekursivt

### Flervalgsspørsmål (Del 1) — vanlige temaer
- Synlighetsmodifikatorer (`private`, `protected`, `package-private`, `public`)
- `static` metoder og felt
- Overriding vs. overloading
- Konstruktørkjeding (`this(...)`, `super(...)`)
- Hvilken metode kalles (dynamic dispatch)
- Når kastes `ClassCastException`, `NullPointerException`
