# Øvingsoppgaver: Case-insensitiv filtrering med input-validering

Adresserer gap 4 fra ord-2024-1: `filterFactories()` i MainOffice var ikke case-insensitiv og manglet validering av ugyldig tegn.

---

## Kontekst

Et system for å administrere bibliotekfilialer i ulike byer.
Hvert bibliotek har en kode (3-bokstav bykode), og systemet må støtte filtrering.

---

## Oppgave 1: LibraryManager

Implementer metoden `filterByCity` i klassen `LibraryManager`:

```java
import java.util.List;

public interface ILibrary {
    /**
     * Returns the 3-letter city code for this library, always uppercase.
     * Example: "OSL", "BER", "NYC"
     */
    String getCityCode();

    /** Returns the number of books in this library */
    int getNumberOfBooks();

    /** Returns the name of this library */
    String getName();
}
```

```java
import java.util.List;

/**
 * Manages a collection of libraries.
 */
public class LibraryManager {

    /**
     * Returns a filtered list of libraries whose city code starts with the given character.
     *
     * The filter is case-insensitive: 'o' and 'O' should give the same result.
     *
     * If the given character is not a letter (A-Z or a-z, decimal 65-90 or 97-122),
     * the method must return the unfiltered list unchanged.
     *
     * @param libraries the list of libraries to filter
     * @param character the starting character to filter by (case-insensitive)
     * @return a filtered list of libraries, or the original list if character is invalid
     */
    public List<ILibrary> filterByCity(List<ILibrary> libraries, char character) {
        // TODO
        return null;
    }

    /**
     * Returns the library with the most books.
     * If multiple libraries have the same maximum, return any one of them.
     * If the list is empty or null, return null.
     *
     * @param libraries the list of libraries
     * @return the library with the most books, or null if list is empty/null
     */
    public ILibrary findLargestLibrary(List<ILibrary> libraries) {
        // TODO
        return null;
    }
}
```

### Tester (skal bli grønne)

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LibraryManagerTest {
    LibraryManager manager;
    ILibrary oslo, bergen, berlin, nyc;

    @BeforeEach
    void setup() {
        manager = new LibraryManager();
        oslo   = lib("OSL", 5000, "Oslo Sentralbibliotek");
        bergen = lib("BER", 3200, "Bergen Bibliotek");
        berlin = lib("BER", 8100, "Berliner Bücherei");
        nyc    = lib("NYC", 12000, "New York Public Library");
    }

    private ILibrary lib(String code, int books, String name) {
        return new ILibrary() {
            public String getCityCode() { return code; }
            public int getNumberOfBooks() { return books; }
            public String getName() { return name; }
        };
    }

    @Test
    void filterByCity_uppercase_matches() {
        List<ILibrary> result = manager.filterByCity(List.of(oslo, bergen, berlin, nyc), 'O');
        assertEquals(1, result.size());
        assertEquals("OSL", result.get(0).getCityCode());
    }

    @Test
    void filterByCity_lowercase_matches_same_as_uppercase() {
        List<ILibrary> upper = manager.filterByCity(List.of(oslo, bergen, berlin, nyc), 'B');
        List<ILibrary> lower = manager.filterByCity(List.of(oslo, bergen, berlin, nyc), 'b');
        assertEquals(upper.size(), lower.size());
        assertEquals(2, upper.size()); // bergen + berlin
    }

    @Test
    void filterByCity_returns_unfiltered_for_invalid_char() {
        List<ILibrary> libs = List.of(oslo, bergen, berlin, nyc);
        List<ILibrary> result = manager.filterByCity(libs, '@'); // @ = 64, utenfor A-Z
        assertEquals(4, result.size()); // ufiltrert
    }

    @Test
    void filterByCity_returns_unfiltered_for_digit() {
        List<ILibrary> libs = List.of(oslo, bergen);
        List<ILibrary> result = manager.filterByCity(libs, '5');
        assertEquals(2, result.size());
    }

    @Test
    void findLargestLibrary_returns_library_with_most_books() {
        ILibrary largest = manager.findLargestLibrary(List.of(oslo, bergen, berlin, nyc));
        assertEquals("NYC", largest.getCityCode());
    }

    @Test
    void findLargestLibrary_returns_null_for_empty_list() {
        assertNull(manager.findLargestLibrary(List.of()));
    }

    @Test
    void findLargestLibrary_returns_null_for_null_list() {
        assertNull(manager.findLargestLibrary(null));
    }
}
```

### Fasit

```java
import java.util.List;

public class LibraryManager {

    public List<ILibrary> filterByCity(List<ILibrary> libraries, char character) {
        // Valider tegnet — utenfor A-Z (65-90) og a-z (97-122): returner ufiltrert
        if ((character < 65 || character > 90) && (character < 97 || character > 122)) {
            return libraries;
        }
        char upper = Character.toUpperCase(character);
        return libraries.stream()
            .filter(lib -> Character.toUpperCase(lib.getCityCode().charAt(0)) == upper)
            .toList();
    }

    public ILibrary findLargestLibrary(List<ILibrary> libraries) {
        if (libraries == null || libraries.isEmpty()) return null;
        return libraries.stream()
            .sorted((a, b) -> b.getNumberOfBooks() - a.getNumberOfBooks())
            .findFirst()
            .orElse(null);
    }
}
```

### Advarsel — fellene i denne oppgaven

**Felle 1 — Case-sensitivt filter**: `charAt(0) == character` sammenligner eksakte tegn. `'B'` og `'b'` er ikke like (`'B'` = 66, `'b'` = 98). Case-insensitivitet krever alltid `Character.toUpperCase()` på begge sider.

**Felle 2 — Ugyldig tegn gir tom liste**: Hvis du glemmer valideringen og sender inn `'@'`, vil filteret returnere en tom liste i stedet for ufiltrert liste. Les JavaDoc for "invalid" — det betyr alltid en spesialbehandling.

**Huskeregel** ved filtrering:
1. Er filteret case-insensitivt? → `Character.toUpperCase()` på begge sider
2. Hva skjer ved ugyldig input? → Returner original, kast unntak, eller returner tom? Les JavaDoc.

---

## Oppgave 2 (Vanskeligere): Toveisfiltrering

```java
/**
 * Returns libraries whose city code starts with startChar AND ends with endChar.
 * Both comparisons are case-insensitive.
 * If either character is invalid (not A-Z or a-z), return the unfiltered list.
 *
 * @param libraries the list to filter
 * @param startChar starting character (case-insensitive)
 * @param endChar   ending character (case-insensitive)
 * @return filtered list, or original if either char is invalid
 */
public List<ILibrary> filterByCityStartAndEnd(List<ILibrary> libraries, char startChar, char endChar) {
    // TODO
    return null;
}
```

### Tester

```java
@Test
void filterByCityStartAndEnd_matches_both_ends() {
    // "BER" starter på B, slutter på R
    ILibrary bergen = lib("BER", 100, "Bergen");
    ILibrary berlin = lib("BER", 200, "Berlin");
    ILibrary bkk    = lib("BKK", 150, "Bangkok"); // BKK starter på B, slutter på K
    List<ILibrary> result = manager.filterByCityStartAndEnd(List.of(bergen, berlin, bkk), 'B', 'R');
    assertEquals(2, result.size()); // bergen + berlin
}

@Test
void filterByCityStartAndEnd_is_case_insensitive() {
    ILibrary bergen = lib("BER", 100, "Bergen");
    List<ILibrary> result = manager.filterByCityStartAndEnd(List.of(bergen), 'b', 'r');
    assertEquals(1, result.size());
}

@Test
void filterByCityStartAndEnd_returns_unfiltered_for_invalid_startChar() {
    List<ILibrary> libs = List.of(lib("BER", 100, "Bergen"));
    assertEquals(1, manager.filterByCityStartAndEnd(libs, '1', 'R').size());
}
```

### Fasit

```java
private boolean isValidLetter(char c) {
    return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
}

public List<ILibrary> filterByCityStartAndEnd(List<ILibrary> libraries, char startChar, char endChar) {
    if (!isValidLetter(startChar) || !isValidLetter(endChar)) return libraries;
    char start = Character.toUpperCase(startChar);
    char end   = Character.toUpperCase(endChar);
    return libraries.stream()
        .filter(lib -> {
            String code = lib.getCityCode();
            return Character.toUpperCase(code.charAt(0)) == start
                && Character.toUpperCase(code.charAt(code.length() - 1)) == end;
        })
        .toList();
}
```
