# Vurderingsrapport — kont-2023
Dato: 2026-05-13

## Samlet vurdering
- **Estimert karakter**: A (~98/100)
- **Overordnet inntrykk**: Svært sterk besvarelse. Kompilerer rent, alle tester grønne. Gjennomgående korrekt implementasjon i alle ni deler. Én reell funksjonsfeil: `getCancelledTickets` muterer input-lista.

---

## Del-vurdering

### Del 1 — Airport (10 %) — 10/10
Alle IAirport-metoder korrekte. Konstruktør med null-sjekk, addFlight sjekker duplikat, getFlightsByDestination bruker stream + `.equals()`. ✓

### Del 2 — BaggageCarousel + BaggageClaim (12.5 %) — 12.5/12.5
All logikk korrekt. `addFlightToQueue` tildeler umiddelbart ved ledig bånd, `clearFlight` avanserer køen, `assignFlightToCarousel` sorterer korrekt, `findAvailableCarousel` og `findLeastBusyCarousel` korrekte. ✓

### Del 3 — CrewSchedule (15 %) — 15/15
Alle fire metoder korrekte. `assignCrewToFlight` kaster `IllegalArgumentException` og tildeler ingen hvis én overstiger grensen. ✓

### Del 4 — TicketHandler (10 %) — 8.5/10
- `upgradeTicket`: `instanceof`-sjekk korrekt, price +100, carryOnBags følger metodens JavaDoc ("to be added") ✓
- `getBoardingOrder`: business sortert før economy, priority boarding innad ✓
- `getCancelledTickets`: returnerer riktig innhold (ikke-kansellerte, per JavaDoc), men kaller `tickets.removeAll(cancelled)` på **input-lista** og returnerer originalen istedenfor en ny liste. Caller sin liste endres uventet.

### Del 5 — AircraftMaintenance (12.5 %) — 12.5/12.5
`scheduleMaintenance`, `addObserver`, `removeObserver` og `updateMaintenanceStatus` fungerer korrekt for gitt scenario. Observer varsles ved SCHEDULED, ikke ved IN_PROGRESS. ✓

### Del 6 — BatchNotificationService (10 %) — 10/10
Batch-logikk korrekt, konstruktørvalidering, delegate-mønster. ✓

### Del 7 — CrewScheduleWriter (10 %) — 10/10
Format korrekt, `throws IOException` deklarert, `flush()` kalt. ✓

### Del 8 — SeatAssignment (10 %) — 10/10
Alle 3 tester grønne (bekreftet med `mvn test`). Oppgaven sier: grønne tester = full pott. ✓

### Del 9 — AirlineUtils (10 %) — 10/10
`getAverageFlightDuration`, `reduceFlights` og `getFlightsToCancel` alle korrekte. HashSet unngår duplikater. ✓

---

## Eneste reelle feil
`getCancelledTickets` i del 4: `tickets.removeAll(cancelled)` modifiserer input-lista som en sideeffekt, og returnerer den originale (nå modifiserte) lista istedenfor en ny. Korrekt løsning:
```java
return tickets.stream()
    .filter(t -> !(t instanceof BusinessTicket && ((BusinessTicket) t).isCancelled()))
    .collect(Collectors.toList());
```
