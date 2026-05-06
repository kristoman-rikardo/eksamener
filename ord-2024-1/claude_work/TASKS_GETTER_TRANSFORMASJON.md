# Øvingsoppgaver: Getter med forretningslogikk

Adresserer gap 3 fra ord-2024-1: `getIsoCountryCode()` returnerte rå lagret verdi uten truncering til 3 tegn og uten `toUpperCase()`.

---

## Kontekst

Et internasjonalt havnenettverk holder oversikt over skip og havner.
Hver havn har en kode, og koden har strenge formatkrav av operasjonelle grunner.

---

## Oppgave 1: Port (Havn)

Implementer klassen `Port`:

```java
/**
 * Represents a port in the global harbour network.
 */
public class Port {

    /**
     * Constructs a Port object.
     *
     * @param portCode  the port code. Must be at least 5 characters and must contain
     *                  only alphabetic characters (A-Z, a-z). Must not be null.
     * @param country   the country name. Must not be null or empty.
     * @param capacity  the maximum number of ships that can dock simultaneously. Must be >= 1.
     * @throws IllegalArgumentException if any validation fails
     */
    public Port(String portCode, String country, int capacity) {
        // TODO
    }

    /**
     * Returns the port code.
     * The returned code must always be exactly 5 characters, all uppercase.
     * If the portCode supplied in the constructor was longer than 5 characters,
     * only the first 5 characters are returned.
     *
     * @return the 5-character uppercase port code
     */
    public String getPortCode() {
        // TODO
        return null;
    }

    /**
     * Returns the country name in lowercase.
     * Regardless of how it was supplied in the constructor,
     * the returned value must always be lowercase.
     *
     * @return the country name in lowercase
     */
    public String getCountry() {
        // TODO
        return null;
    }

    /**
     * Returns the capacity of the port.
     *
     * @return the maximum number of ships that can dock simultaneously
     */
    public int getCapacity() {
        // TODO
        return 0;
    }
}
```

### Tester (skal bli grønne)

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PortTest {

    @Test
    void constructor_throws_for_null_portCode() {
        assertThrows(IllegalArgumentException.class, () -> new Port(null, "Norway", 5));
    }

    @Test
    void constructor_throws_when_portCode_less_than_5_chars() {
        assertThrows(IllegalArgumentException.class, () -> new Port("ABCD", "Norway", 5));
    }

    @Test
    void constructor_throws_for_nonalphabetic_portCode() {
        assertThrows(IllegalArgumentException.class, () -> new Port("ABC12", "Norway", 5));
    }

    @Test
    void constructor_throws_for_null_or_empty_country() {
        assertThrows(IllegalArgumentException.class, () -> new Port("ABCDE", null, 5));
        assertThrows(IllegalArgumentException.class, () -> new Port("ABCDE", "", 5));
    }

    @Test
    void constructor_throws_for_capacity_less_than_1() {
        assertThrows(IllegalArgumentException.class, () -> new Port("ABCDE", "Norway", 0));
    }

    @Test
    void getPortCode_returns_exactly_5_uppercase_chars() {
        Port port = new Port("abcde", "Norway", 3);
        assertEquals("ABCDE", port.getPortCode());
    }

    @Test
    void getPortCode_truncates_to_5_when_longer() {
        Port port = new Port("abcdefgh", "Norway", 3);
        assertEquals("ABCDE", port.getPortCode());
        assertEquals(5, port.getPortCode().length());
    }

    @Test
    void getCountry_returns_lowercase() {
        Port port = new Port("ABCDE", "NORWAY", 3);
        assertEquals("norway", port.getCountry());
    }

    @Test
    void getCapacity_returns_correct_value() {
        Port port = new Port("ABCDE", "Norway", 12);
        assertEquals(12, port.getCapacity());
    }
}
```

### Fasit

```java
public class Port {
    private String portCode;
    private String country;
    private int capacity;

    public Port(String portCode, String country, int capacity) {
        if (portCode == null || portCode.length() < 5)
            throw new IllegalArgumentException("portCode must be at least 5 characters");
        if (!portCode.chars().allMatch(Character::isLetter))
            throw new IllegalArgumentException("portCode must only contain alphabetic characters");
        if (country == null || country.isEmpty())
            throw new IllegalArgumentException("country must not be null or empty");
        if (capacity < 1)
            throw new IllegalArgumentException("capacity must be >= 1");

        this.portCode = portCode;
        this.country = country;
        this.capacity = capacity;
    }

    public String getPortCode() {
        return portCode.substring(0, 5).toUpperCase(); // truncer + uppercase
    }

    public String getCountry() {
        return country.toLowerCase(); // alltid lowercase
    }

    public int getCapacity() {
        return capacity;
    }
}
```

### Advarsel — fellen i denne oppgaven

En getter er IKKE alltid bare `return this.felt`. JavaDoc kan kreve transformasjoner: truncering, uppercase, lowercase, kopi av liste, eller matematiske operasjoner. Les alltid `@return`-dokumentasjonen nøye — ord som "exactly", "must be", "always" signalerer at en ren return ikke er nok.

**Huskeregel**: Spør deg for hver getter: "Skal returverdien se nøyaktig slik den er lagret, eller transformert?"

---

## Oppgave 2 (Vanskeligere): RouteCode med formatering

```java
/**
 * Represents a shipping route between two ports.
 */
public class ShippingRoute {

    /**
     * @param originPortCode       origin port code, at least 3 characters
     * @param destinationPortCode  destination port code, at least 3 characters
     * @param distanceNauticalMiles  distance in nautical miles, must be > 0
     * @throws IllegalArgumentException if any validation fails
     */
    public ShippingRoute(String originPortCode, String destinationPortCode, double distanceNauticalMiles) {
        // TODO
    }

    /**
     * Returns the route identifier.
     * The route identifier is constructed as follows:
     *   [first 3 chars of origin, uppercase]-[first 3 chars of destination, uppercase]
     * Example: originPortCode="amsterdam", destinationPortCode="rotterdam"
     *   → returns "AMS-ROT"
     *
     * @return the formatted route identifier
     */
    public String getRouteId() {
        // TODO
        return null;
    }

    /**
     * Returns the distance in nautical miles, rounded down to nearest integer.
     *
     * @return distance in nautical miles, rounded down
     */
    public int getDistanceNauticalMilesRounded() {
        // TODO
        return 0;
    }
}
```

### Tester

```java
class ShippingRouteTest {

    @Test
    void getRouteId_formats_correctly() {
        ShippingRoute route = new ShippingRoute("amsterdam", "rotterdam", 40.7);
        assertEquals("AMS-ROT", route.getRouteId());
    }

    @Test
    void getRouteId_truncates_to_3_chars() {
        ShippingRoute route = new ShippingRoute("ABCDEF", "GHIJKL", 100.0);
        assertEquals("ABC-GHI", route.getRouteId());
    }

    @Test
    void getDistanceRounded_floors_correctly() {
        ShippingRoute route = new ShippingRoute("ABC", "DEF", 40.9);
        assertEquals(40, route.getDistanceNauticalMilesRounded());
    }

    @Test
    void constructor_throws_for_short_codes() {
        assertThrows(IllegalArgumentException.class, () -> new ShippingRoute("AB", "DEF", 10.0));
    }

    @Test
    void constructor_throws_for_nonpositive_distance() {
        assertThrows(IllegalArgumentException.class, () -> new ShippingRoute("ABC", "DEF", 0.0));
    }
}
```

### Fasit

```java
public class ShippingRoute {
    private String originPortCode;
    private String destinationPortCode;
    private double distanceNauticalMiles;

    public ShippingRoute(String originPortCode, String destinationPortCode, double distanceNauticalMiles) {
        if (originPortCode == null || originPortCode.length() < 3)
            throw new IllegalArgumentException("origin must be at least 3 chars");
        if (destinationPortCode == null || destinationPortCode.length() < 3)
            throw new IllegalArgumentException("destination must be at least 3 chars");
        if (distanceNauticalMiles <= 0)
            throw new IllegalArgumentException("distance must be positive");
        this.originPortCode = originPortCode;
        this.destinationPortCode = destinationPortCode;
        this.distanceNauticalMiles = distanceNauticalMiles;
    }

    public String getRouteId() {
        String origin = originPortCode.substring(0, 3).toUpperCase();
        String dest = destinationPortCode.substring(0, 3).toUpperCase();
        return origin + "-" + dest;
    }

    public int getDistanceNauticalMilesRounded() {
        return (int) Math.floor(distanceNauticalMiles);
    }
}
```
