# Øvingsoppgaver: Rekursiv tre-traversering — Reisenettverket

## Kontekst

TravelNet er et system for å administrere reisenettverket til et reisebyrå. Reisemål er organisert i et geografisk hierarki:
- Verden → Kontinent → Land → By

Hvert reisemål kan inneholde reiser (`Trip`-objekter) og kan ha underordnede destinasjoner (barn).

---

## Oppgave 1: Grunnleggende traversering

Implementer metoden `getAllTrips()` i klassen `Destination`.

```java
/**
 * Represents a travel destination in a geographical hierarchy.
 * A destination can have zero or more child destinations (sub-destinations).
 * Each destination can also hold a collection of Trip objects.
 */
public class Destination {

    private String name;
    private Collection<Destination> children = new ArrayList<>();
    private Collection<Trip> trips = new ArrayList<>();

    public Destination(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public Collection<Destination> getChildren() { return children; }

    public void addChild(Destination child) {
        children.add(child);
        // (for now, no parent tracking needed)
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    /**
     * Returns all trips in this destination and all its descendants.
     * 
     * For example, if "Europe" has children "France" and "Germany",
     * and "France" has trips [Paris Tour] and "Germany" has trips [Berlin Walk],
     * then europe.getAllTrips() should return [Paris Tour, Berlin Walk].
     *
     * @return A Collection of all Trip objects in this destination and its descendants.
     *         The order is not important. The collection need not be unique.
     */
    public Collection<Trip> getAllTrips() {
        // TODO: Implement
        return null; // dummy return
    }

}
```

**Tester (skal bli grønne)**:

```java
@Test
public void testGetAllTripsFromLeafNode() {
    Destination city = new Destination("Oslo");
    Trip t1 = new Trip("Fjord tour");
    city.addTrip(t1);

    assertTrue(city.getAllTrips().contains(t1));
    assertEquals(1, city.getAllTrips().size());
}

@Test
public void testGetAllTripsFromParentNode() {
    Destination europe = new Destination("Europe");
    Destination france = new Destination("France");
    Destination germany = new Destination("Germany");
    europe.addChild(france);
    europe.addChild(germany);

    Trip t1 = new Trip("Paris Tour");
    Trip t2 = new Trip("Berlin Walk");
    france.addTrip(t1);
    germany.addTrip(t2);

    Collection<Trip> all = europe.getAllTrips();
    assertTrue(all.contains(t1));
    assertTrue(all.contains(t2));
    assertEquals(2, all.size());
}

@Test
public void testGetAllTripsWithMultipleLevels() {
    Destination world = new Destination("World");
    Destination europe = new Destination("Europe");
    Destination france = new Destination("France");
    Destination paris = new Destination("Paris");

    world.addChild(europe);
    europe.addChild(france);
    france.addChild(paris);

    Trip t1 = new Trip("Eiffel Tower");
    paris.addTrip(t1);

    Collection<Trip> all = world.getAllTrips();
    assertTrue(all.contains(t1));
}

@Test
public void testGetAllTripsOnEmptyHierarchy() {
    Destination empty = new Destination("Empty");
    assertTrue(empty.getAllTrips().isEmpty());
}
```

---

## Fasit til Oppgave 1

```java
public Collection<Trip> getAllTrips() {
    Collection<Trip> result = new ArrayList<>(this.trips);  // Start med egne turer

    for (Destination child : this.children) {               // Gå gjennom hvert barn
        result.addAll(child.getAllTrips());                  // La BARNET kalle sin egen getAllTrips()
    }

    return result;
}
```

**Hvorfor dette er riktig**:
1. Start med `this.trips` — dette destination-objektets direkte turer.
2. For hvert barn: kall `child.getAllTrips()` (IKKE `this.getAllTrips()`!).
3. `child.getAllTrips()` vil rekursivt samle BARNETS turer + barnets barns turer, og så videre.
4. Basistilfelle: Et blad-destination (ingen barn) returnerer bare `this.trips` fordi `for`-løkken ikke kjører.

---

## Advarsel — fellen i Oppgave 1

Den vanligste feilen er å skrive `getAllTrips()` uten `child.`:

```java
// FEIL:
for (Destination child : this.children) {
    result.addAll(getAllTrips());  // ← this.getAllTrips()! Ikke child!
}
```

Dette kaller metoden på `this` igjen og igjen, aldri på barna. Resultatet er en uendelig rekursjon (StackOverflowError). Java gir deg INGEN kompileringsfeil på dette — det er en logisk feil du bare oppdager under kjøring.

**Mentalt bilde**: Rekursjon i et tre er som å gi en arbeidstaker en oppgave. Hvert barn er en underarbeider. Du (this) sier: "Hei child, gjør jobben din og rapporter tilbake." Du sier IKKE: "Gjør den jobben jeg allerede holder på med." `child.getAllTrips()` = "hei du, gjør DIN jobb". `getAllTrips()` = "la meg gjøre min jobb om igjen" (sirkel).

---

## Oppgave 2: Telle etterkommer-noder (vanskeligere)

Implementer `countDescendants()` som teller alle etterkommer-destinasjoner (IKKE inkludert `this` selv).

```java
/**
 * Counts the total number of descendant Destination objects
 * (children, grandchildren, and so on).
 * Does NOT count this object itself.
 *
 * @return The number of descendants.
 */
public int countDescendants() {
    // TODO: Implement
    return 0; // dummy return
}
```

**Tester**:

```java
@Test
public void testCountDescendantsLeaf() {
    Destination leaf = new Destination("Leaf");
    assertEquals(0, leaf.countDescendants());
}

@Test
public void testCountDescendantsOneLevel() {
    Destination parent = new Destination("Parent");
    parent.addChild(new Destination("Child1"));
    parent.addChild(new Destination("Child2"));
    assertEquals(2, parent.countDescendants());
}

@Test
public void testCountDescendantsMultipleLevels() {
    Destination world = new Destination("World");
    Destination europe = new Destination("Europe");
    Destination france = new Destination("France");
    Destination paris = new Destination("Paris");

    world.addChild(europe);
    europe.addChild(france);
    france.addChild(paris);

    // world has 3 descendants: europe, france, paris
    assertEquals(3, world.countDescendants());
}
```

---

## Fasit til Oppgave 2

```java
public int countDescendants() {
    int count = 0;
    for (Destination child : this.children) {
        count += 1;                           // Tell barnet selv
        count += child.countDescendants();    // Tell barnets etterkommere
    }
    return count;
}
```

Alternativ (kompakt):
```java
public int countDescendants() {
    return this.children.stream()
        .mapToInt(child -> 1 + child.countDescendants())
        .sum();
}
```

---

## Oppgave 3: isDescendantOf med null-sikker hjelpemetode (vanskeligere)

I `PartCategory`-eksempelet (eksamens Del 4) returnerer `getChildren()` `null` når listen er tom. Implementer `isDescendantOf` og `getAllParts()` robust mot dette.

```java
/**
 * @return null if this category has no children, otherwise the children collection.
 */
public Collection<PartCategory> getChildren() {
    if (this.children.isEmpty()) return null;
    return this.children;
}

/**
 * Checks if this category is a descendant of partCategory.
 * @return true if this is a descendant, false otherwise.
 */
public boolean isDescendantOf(PartCategory partCategory) {
    // TODO: Implement — must handle null from getChildren()
    return false; // dummy
}

/**
 * Returns all IComputerPart objects in this category and all descendants.
 * @return a Collection of IComputerPart (need not be unique or ordered)
 */
public Collection<IComputerPart> getAllParts() {
    // TODO: Implement — must handle null from getChildren()
    return null; // dummy
}
```

**Tester**:

```java
@Test
public void testIsDescendantOfCorrect() {
    PartCategory root = new PartCategory("Root");
    PartCategory mid = new PartCategory("Mid");
    PartCategory leaf = new PartCategory("Leaf");
    root.addChildCategory(mid);
    mid.addChildCategory(leaf);

    assertTrue(leaf.isDescendantOf(root));
    assertTrue(leaf.isDescendantOf(mid));
    assertFalse(root.isDescendantOf(leaf));
}

@Test
public void testGetAllPartsAcrossHierarchy() {
    PartCategory root = new PartCategory("Root");
    PartCategory child = new PartCategory("Child");
    root.addChildCategory(child);

    IComputerPart part1 = new ComputerPart(1, "GPU", "NVIDIA", 5000.0);
    IComputerPart part2 = new ComputerPart(2, "CPU", "AMD", 3000.0);
    root.addPart(part1);
    child.addPart(part2);

    Collection<IComputerPart> all = root.getAllParts();
    assertTrue(all.contains(part1));
    assertTrue(all.contains(part2));
}
```

---

## Fasit til Oppgave 3

```java
public boolean isDescendantOf(PartCategory partCategory) {
    Collection<PartCategory> children = partCategory.getChildren();
    if (children == null) return false;                  // Basistilfelle: ingen barn
    for (PartCategory child : children) {
        if (child.equals(this)) return true;             // Fant det!
        if (this.isDescendantOf(child)) return true;    // Søk dypere
    }
    return false;
}

public Collection<IComputerPart> getAllParts() {
    Collection<IComputerPart> result = new HashSet<>(this.parts);
    // Iterer over this.children direkte (aldri null, initialisert i feltet)
    for (PartCategory child : this.children) {
        result.addAll(child.getAllParts());  // child, ikke this!
    }
    return result;
}
```

**Nøkkelvalg**: Iterer over `this.children` (feltet) direkte istedenfor `getChildren()`. Da slipper du null-sjekken fordi feltet alltid er initialisert som `new HashSet<>()`.

---

## Advarsel — fellen i Oppgave 3

Tre vanlige feil kombinert:
1. `getAllParts()` istedenfor `child.getAllParts()` → uendelig rekursjon
2. Iterere over `getChildren()` uten null-sjekk → NPE
3. Bruke `if/else` i stedet for ren rekursjon → mister barnebarn

Alltid spør deg selv: **"Hvem kaller metoden?"** Svaret skal alltid være et objekt et nivå dypere i hierarkiet, aldri `this`.
