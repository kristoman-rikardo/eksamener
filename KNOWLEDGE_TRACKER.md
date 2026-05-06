# Kunnskapstracker — TDT4100 Eksamensforberedelse

Oppdateres av Claude etter **hver** graderingsøkt.
Formål: holde oversikt over kunnskapsgap på tvers av alle eksamener, slik at hvert nye økt fokuserer på det som teller mest.

---

## Status per eksamen

| Eksamen | Gjennomgått | Estimert karakter | Dato | Notater |
|---------|-------------|-------------------|------|---------|
| ord-2025-2 | Nei | — | — | Nyeste eksamen, høyeste prioritet |
| ord-2025-1 | Ja | B (~86/100) | 2026-05-06 | ByteBadger-tema. Sterk del 1+2+6. Svak getAllParts (kritisk rekursjonsbug). IOException svelges i write(). |
| ord-2024-2 | Nei | — | — | Mercedes-tema |
| ord-2024-1 | Ja | B (~83/100) | 2026-05-06 | BMW-tema. Del 1 perfekt. Del 5 sterk (alle 36 tester). WeldChassisStep: mangler public + startTime/endTime. getIsoCountryCode uten uppercase/truncation. filterFactories ikke case-insensitiv. |
| kont-2024 | Nei | — | — | |
| ord-2023 | Nei | — | — | |
| kont-2023 | Nei | — | — | |
| ord-2022 | Nei | — | — | |
| kont-2022 | Nei | — | — | |
| ord-2021 | Nei | — | — | |
| kont-2021 | Nei | — | — | |
| kont-2020 | Nei | — | — | |
| ord-2020 | Nei | — | — | |
| ord-2019 | Nei | — | — | Eldste, lavest prioritet |

---

## Konseptoversikt

### Observer-mønsteret
- **Alvorlighet**: Lav (ingen feil observert ennå)
- **Bevis fra**: ord-2025-1 (Del 6 — BuildStatus implementert korrekt)
- **Gjentakende mønster**: Ikke sett feil ennå
- **Status**: Mestret i ord-2025-1 ✓

### Grensesnittimplementering
- **Alvorlighet**: Lav
- **Bevis fra**: ord-2025-1 (Del 3 — CustomComputer implementerer ICustomBuild korrekt)
- **Gjentakende mønster**: Ikke sett feil ennå
- **Status**: Mestret i ord-2025-1 ✓

### Arv og konstruktørkjeding (super)
- **Alvorlighet**: Lav
- **Bevis fra**: ord-2024-1 — `WeldChassisStep` kaller `super(stepId, true)` korrekt
- **Gjentakende mønster**: Ingen feil ennå
- **Status**: Fungerer ✓

### Tilgangsmodifikator på subklasser
- **Alvorlighet**: Høy
- **Bevis fra**: ord-2024-1 — `WeldChassisStep` deklarert som `class` (package-private) istedenfor `public class`. JavaDoc krevde instansiering utenfor pakken.
- **Gjentakende mønster**: Første observasjon
- **Status**: KRITISK GAP — se TASKS_ABSTRAKT_EXECUTE.md for øvingsoppgaver

### Abstrakt metodekontract (startTime/endTime)
- **Alvorlighet**: Høy
- **Bevis fra**: ord-2024-1 — `WeldChassisStep.execute()` kaller kun `simulateTimePassing()` uten å sette `startTime` eller `endTime`. Superklassens kontrakt brutt.
- **Gjentakende mønster**: Første observasjon
- **Status**: KRITISK GAP — se TASKS_ABSTRAKT_EXECUTE.md for øvingsoppgaver

### Innkapsling (returnere kopi av lister/map)
- **Alvorlighet**: Lav
- **Bevis fra**: ord-2025-1 — `getParts()` returnerer intern HashMap direkte uten unmodifiableMap/kopi
- **Gjentakende mønster**: Én forekomst
- **Status**: Liten feil — huskeregel: bruk `Collections.unmodifiableMap()` for Maps som returneres

### Fil-I/O (try-with-resources, IOException-propagering)
- **Alvorlighet**: Middels
- **Bevis fra**: ord-2025-1 — `WriteBuildToFile.write()` svelger IOException i catch-blokk uten throw e
- **Gjentakende mønster**: Én forekomst (men typisk nybegynnerfeil)
- **Status**: Forbedringsområde — se TASKS_IO_EXCEPTION.md for øvingsoppgaver

### Unntak og feilhåndtering
- **Alvorlighet**: Middels
- **Bevis fra**: ord-2025-1 — IOException svelges i write(). Ellers korrekt (IllegalArgumentException, NullPointerException riktig kastet)
- **Gjentakende mønster**: Én forekomst
- **Status**: God generell håndtering, men svak på re-kasting av checked exceptions

### Rekursjon og hierarkiske strukturer
- **Alvorlighet**: Høy
- **Bevis fra**: ord-2025-1 — `getAllParts()` kaller `getAllParts()` på `this` istedenfor `child.getAllParts()` → uendelig rekursjon. I tillegg itereres over mulig null-retur fra `getChildren()` → NPE
- **Gjentakende mønster**: Første observasjon — men kritisk og vanlig feil
- **Status**: KRITISK GAP — se TASKS_REKURSJON_TRAVERSERING.md for øvingsoppgaver

### Collections og iterasjon
- **Alvorlighet**: Lav
- **Bevis fra**: ord-2025-1 — generelt korrekt bruk av HashMap, HashSet, ArrayList, Stream
- **Gjentakende mønster**: Ingen feil observert
- **Status**: Mestret ✓

### Getter med forretningslogikk (truncering, toUpperCase)
- **Alvorlighet**: Middels
- **Bevis fra**: ord-2024-1 — `CarFactory.getIsoCountryCode()` returnerer rå lagret verdi. JavaDoc krevde truncering til 3 tegn og toUpperCase().
- **Gjentakende mønster**: Første observasjon
- **Status**: Forbedringsområde — se TASKS_GETTER_TRANSFORMASJON.md

### Case-insensitiv filtrering med input-validering
- **Alvorlighet**: Middels
- **Bevis fra**: ord-2024-1 — `MainOffice.filterFactories()` bruker `== character` (case-sensitiv) og mangler validering av tegn utenfor A-Z/a-z.
- **Gjentakende mønster**: Første observasjon
- **Status**: Forbedringsområde — se TASKS_CASE_INSENSITIV_FILTER.md

### String-sammenligning (== vs .equals())
- **Alvorlighet**: — (ikke testet direkte ennå)
- **Bevis fra**: —
- **Gjentakende mønster**: —
- **Status**: Ikke testet ennå

### Flervalgsspørsmål (Del 1-teori)
- **Alvorlighet**: Lav
- **Bevis fra**: ord-2025-1 — ~18/20. Trolig mistet 1-2 poeng på UML objektdiagram (multiplisitet *=0..*)
- **Gjentakende mønster**: Én observasjon
- **Status**: Sterk Del 1-prestasjon generelt

---

## Prioriterte læringsmål

*(Oppdateres etter hvert som gap identifiseres — her er topp 3 som krever mest fokus)*

1. **Rekursiv tre-traversering** — kall alltid metoden på BARNET (`child.method()`), aldri på `this`. Øv på TASKS_REKURSJON_TRAVERSERING.md
2. **Abstrakt metodekontract** — les superklassens JavaDoc for abstract metoder linje for linje og implementer ALLE sideeffekter (startTime/endTime). Sjekk alltid tilgangsmodifikator (`public`). Øv på TASKS_ABSTRAKT_EXECUTE.md
3. **Getter med transformasjon + case-insensitiv filtrering** — getters kan ha truncering/uppercase-krav. Filtrering kan kreve `Character.toUpperCase()` og validering av ugyldige tegn. Øv på TASKS_GETTER_TRANSFORMASJON.md og TASKS_CASE_INSENSITIV_FILTER.md

---

## Fremgangslogg

*(Claude legger til en linje her etter hver økt)*

| Dato | Eksamen | Aktivitet | Funn |
|------|---------|-----------|------|
| 2026-05-06 | ord-2025-1 | Full vurdering Del 1-6 | Sterk del 1+2+6. Kritisk bug: getAllParts() uendelig rekursjon. IOException svelges i write(). Estimert B (~86/100) |
| 2026-05-06 | ord-2024-1 | Full vurdering Del 1-5 | Del 1 perfekt. Del 5 sterk (36/36 tester). Kritisk: WeldChassisStep mangler public + startTime/endTime. Medium: getIsoCountryCode uten uppercase/truncation, filterFactories ikke case-insensitiv. Estimert B (~83/100) |
