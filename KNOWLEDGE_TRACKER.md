# Kunnskapstracker — TDT4100 Eksamensforberedelse

Oppdateres av Claude etter **hver** graderingsøkt.
Formål: holde oversikt over kunnskapsgap på tvers av alle eksamener, slik at hvert nye økt fokuserer på det som teller mest.

---

## Status per eksamen

| Eksamen | Gjennomgått | Estimert karakter | Dato | Notater |
|---------|-------------|-------------------|------|---------|
| ord-2025-2 | Nei | — | — | Nyeste eksamen, høyeste prioritet |
| ord-2025-1 | Ja | B (~86/100) | 2026-05-06 | ByteBadger-tema. Sterk del 1+2+6. Svak getAllParts (kritisk rekursjonsbug). IOException svelges i write(). |
| ord-2024-2 | Ja | B (~88/100) | 2026-05-09 | Mercedes-tema. Del 1+5 perfekt. wipe()-bug: Arrays.equals() aldri brukt. filterFacilities: sjekker feil nivå (fasilitet vs prosjekt). Mangler implements IResearchFacility. |
| ord-2024-1 | Ja | B (~83/100) | 2026-05-06 | BMW-tema. Del 1 perfekt. Del 5 sterk (alle 36 tester). WeldChassisStep: mangler public + startTime/endTime. getIsoCountryCode uten uppercase/truncation. filterFactories ikke case-insensitiv. |
| kont-2024 | Ja | B+ (~84/100) | 2026-05-13 | Butikk-tema. Del 4+5 nesten feilfri. Kritisk: isDuplicate NPE (første anmeldelse feiler). Alvorlig: getTotal ignorerer rabatter. Moderat: addItem legger til 1 istedenfor quantity. |
| ord-2023 | Nei | — | — | |
| kont-2023 | Ja | A (~98/100) | 2026-05-13 | Flyplass-tema. Kompilerer rent, alle tester grønne. Eneste reelle feil: getCancelledTickets muterer input-lista istedenfor å lage ny liste. |
| ord-2022 | Ja | B (~85 % av Del 1-4) | 2026-05-13 | Re-vurdert uten Del 5. Mange bugs fikset: popAll, countLetters, computeDistance-formel, CharCounterImpl2.countChar. Gjenværende: getCharCount(Predicate) teller nøkler ikke verdier; computeDistance krasjer pga remove() på umodifiserbar samling. |
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

### Tilgangsmodifikator og manglende `implements`-deklarasjon
- **Alvorlighet**: Høy
- **Bevis fra**: ord-2024-1 — `WeldChassisStep` manglet `public`. ord-2024-2 — `ResearchFacility` implementerer alle grensesnitt-metoder men mangler `implements IResearchFacility`-deklarasjonen.
- **Gjentakende mønster**: To eksamener på rad! Klasse-deklarasjonsdetaljer er et systematisk svakt punkt.
- **Status**: KRITISK GJENTAKENDE GAP — etter å ha skrevet en klasse, sjekk alltid: public? implements riktig interface?

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

### Filter-logikk: feil nivå (fasilitet vs. prosjekt vs. person)
- **Alvorlighet**: Høy
- **Bevis fra**: ord-2024-1 — `filterFactories()` case-sensitiv + mangler validering. ord-2024-2 — `filterFacilities()` sjekker fasilitet-budsjett istedenfor prosjekt-budsjetter; mangler null-sjekk for parameter.
- **Gjentakende mønster**: To eksamener på rad! Filterfeil er et systematisk svakt punkt.
- **Status**: KRITISK GJENTAKENDE GAP — les alltid JavaDoc-subjektet nøye i filter-metoder

### Mutere input-lista i filtreringsmetoder
- **Alvorlighet**: Middels
- **Bevis fra**: kont-2023 Del 4 — `getCancelledTickets` kaller `tickets.removeAll(cancelled)` på input-lista og returnerer originalen. JavaDoc sier "new list".
- **Gjentakende mønster**: Første observasjon.
- **Status**: Huskeregel: `.collect(Collectors.toList())` lager ny liste — aldri `removeAll` på input.

### Bruke returverdi fra rene funksjoner (applyDiscount)
- **Alvorlighet**: Høy
- **Bevis fra**: kont-2024 Del 2 — `getTotal()` kaller `discount.applyDiscount(product)` men ignorerer returverdien. Bruker `product.getPrice()` direkte, som aldri er rabattert. Total er alltid full pris.
- **Gjentakende mønster**: Første observasjon.
- **Status**: KRITISK GAP — etter ethvert metodekall som returnerer en beregnet verdi: bruk den! Sjekk at du ikke kaster return-verdien.

### NullPointerException ved Map.get() på manglende nøkkel
- **Alvorlighet**: Høy
- **Bevis fra**: kont-2024 Del 3 — `isDuplicate()` kaller `reviews.get(product)` som returnerer `null` for nye produkter, deretter krasjer for-each over `null`. Bryter `addReview` totalt for første anmeldelse. kont-2023: lignende mønster med lister.
- **Gjentakende mønster**: To eksamener — systematisk svakhet ved Map-oppslag.
- **Status**: KRITISK GJENTAKENDE GAP — etter `Map.get()`: sjekk alltid om resultatet kan være null. Bruk `getOrDefault()`, `containsKey()`, eller `computeIfAbsent()`.

### NaN vs 0.0 ved divisjon med null-teller
- **Alvorlighet**: Lav
- **Bevis fra**: kont-2024 Del 3 — `getAverageRatingFor()` returnerer `0.0/0.0 = NaN` istedenfor `0.0` når ingen anmeldelser finnes.
- **Gjentakende mønster**: Første observasjon.
- **Status**: Huskeregel: sjekk alltid tom-liste-tilfellet eksplisitt før divisjon. `if (list.isEmpty()) return 0.0;`

### String-sammenligning (== vs .equals()) og Array-sammenligning
- **Alvorlighet**: Høy
- **Bevis fra**: ord-2024-2 — `wipe()` bruker `secretCode.equals(WIPE_CODE)` for `char[]`. Arrays.equals() aldri brukt → metoden kan aldri rydde arkivet.
- **Gjentakende mønster**: Første observasjon for arrays. Direkte variant av klassisk ==/equals-fellen.
- **Status**: KRITISK GAP — bruk `Arrays.equals(a, b)` for arrays, aldri `.equals()` eller `==`

### Flervalgsspørsmål (Del 1-teori)
- **Alvorlighet**: Lav
- **Bevis fra**: ord-2025-1 — ~18/20. Trolig mistet 1-2 poeng på UML objektdiagram (multiplisitet *=0..*)
- **Gjentakende mønster**: Én observasjon
- **Status**: Sterk Del 1-prestasjon generelt

---

## Prioriterte læringsmål

*(Oppdateres etter hvert som gap identifiseres — her er topp 3 som krever mest fokus)*

1. **Map.get() kan returnere null** — Gjentakende gap (kont-2023, kont-2024). Etter hvert Map.get()-kall: bruk `getOrDefault()`, `containsKey()`, eller sjekk eksplisitt. Null-iterasjon krasjer alltid.
2. **Bruk returverdier fra rene funksjoner** — kont-2024: `applyDiscount()` returnerer rabattert pris men verdien ble kastet. Etter hvert metodekall med returtype: bruk returverdien!
3. **`implements`-deklarasjon og `public` tilgangsmodifikator** — Gjentakende gap (ord-2024-1, ord-2024-2). Etter å ha skrevet en klasse: sjekk `public`? `implements riktigInterface`?

---

## Fremgangslogg

*(Claude legger til en linje her etter hver økt)*

| Dato | Eksamen | Aktivitet | Funn |
|------|---------|-----------|------|
| 2026-05-06 | ord-2025-1 | Full vurdering Del 1-6 | Sterk del 1+2+6. Kritisk bug: getAllParts() uendelig rekursjon. IOException svelges i write(). Estimert B (~86/100) |
| 2026-05-06 | ord-2024-1 | Full vurdering Del 1-5 | Del 1 perfekt. Del 5 sterk (36/36 tester). Kritisk: WeldChassisStep mangler public + startTime/endTime. Medium: getIsoCountryCode uten uppercase/truncation, filterFactories ikke case-insensitiv. Estimert B (~83/100) |
| 2026-05-09 | ord-2024-2 | Full vurdering Del 1-5 | Del 1+5 perfekt (45/45 tester). Kritisk: wipe() bruker .equals() på char[] → alltid SecurityException. filterFacilities sjekker feil nivå. ResearchFacility mangler implements IResearchFacility. Estimert B (~88/100) |
| 2026-05-13 | kont-2023 | Full vurdering Del 1-9 | Kompilerer rent, alle tester grønne. Estimert B+ (~88/100). Eneste reelle feil: getCancelledTickets muterer input-lista. Liten: toString-format. |
| 2026-05-13 | ord-2022 | Re-vurdering Del 1-4 (Del 5 ekskludert) | Mange bugs fikset siden sist: popAll, countLetters, computeDistance-formel, CharCounterImpl2.countChar. Gjenværende: getCharCount(Predicate) teller nøkler (ikke sum av verdier); computeDistance krasjer pga remove() på umodifiserbar samling. Estimert B (~85 % av Del 1-4) |
| 2026-05-13 | kont-2024 | Full vurdering Del 1-5 | Del 4+5 nesten feilfri (43/43 tester i Del 5). Alle ShoppingCart/ShippingStatus-tester feiler pga Mockito kan ikke mocke records (infrastrukturfeil). Kritisk: isDuplicate NPE bryter addReview for alle nye produkter. Alvorlig: getTotal ignorerer rabatter (return-verdi kastet). Moderat: addItem legger til 1 ikke quantity. Estimert B+ (~84/100) |
