# Kunnskapsgap — kont-2023

---

## Gap 1: Mutere input-lista i filtreringsmetoder
**Alvorlighet**: Middels
**Bevis**:
```java
// FEIL — modifiserer lista som ble sendt inn
public static List<Ticket> getCancelledTickets(List<Ticket> tickets) {
    List<Ticket> cancelled = ...;
    tickets.removeAll(cancelled);  // ← muterer caller sin liste!
    return tickets;                // ← returnerer originalen, ikke "new list"
}

// RIKTIG
return tickets.stream()
    .filter(t -> !(t instanceof BusinessTicket && ((BusinessTicket) t).isCancelled()))
    .collect(Collectors.toList());  // lager ny liste, rører ikke input
```
**Forklaring**: Caller forventer at listen de sendte inn er uendret etter kallet. `removeAll` ødelegger denne antagelsen. JavaDoc sier "returns a **new** list" — `return tickets` etter `removeAll` er ikke en ny liste, det er den modifiserte originalen.

**Mønster**: Første observasjon for denne spesifikke feilen. Klassisk Java-fallgruve.
**Call to action**: Filtreringsmetoder skal alltid lage sin egen liste med `.collect(Collectors.toList())`. Aldri kall `remove`/`removeAll`/`clear` på input-parametere.
