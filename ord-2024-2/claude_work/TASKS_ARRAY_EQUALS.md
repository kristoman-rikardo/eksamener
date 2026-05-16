# Øvingsoppgaver: Array-sammenligning og equals-kontrakt — Sikkerhetsarkiv

## Kontekst

Et biblioteksystem har et sikkerhetsarkiv (`SecurityVault`) for hemmelige dokumenter. For å åpne eller slette hvelvet trenger man en PIN-kode (`char[]`). Systemet bruker en statisk klasse-metode for dette, akkurat som eksamen brukte `SecretCode.WIPE_CODE`.

---

## Oppgave 1: Implementer `SecurityVault`

```java
/**
 * Et globalt sikkerhetsarkiv for hemmelige dokumenter.
 * Alle metoder er klasse-metoder (static).
 * Kan ikke instansieres.
 */
public final class SecurityVault {

    private static final char[] MASTER_PIN = "s3cr3t".toCharArray();
    private static List<String> documents = new ArrayList<>();

    private SecurityVault() {}

    /**
     * Legger til et dokument i hvelvet.
     * @return true hvis dokumentet ble lagt til, false hvis et dokument med samme navn allerede finnes
     * @throws NullPointerException hvis document er null
     */
    public static boolean addDocument(String document) {
        // TODO
        return false;
    }

    /**
     * Returnerer antall dokumenter i hvelvet.
     */
    public static int getDocumentCount() {
        // TODO
        return 0;
    }

    /**
     * Sletter alle dokumenter i hvelvet.
     *
     * @param pin char-array med PIN-kode
     * @throws NullPointerException hvis pin er null
     * @throws SecurityException hvis pin ikke matcher MASTER_PIN
     */
    public static void wipeAll(char[] pin) {
        // TODO
    }
}
```

## Tester (skal bli grønne)

```java
@Test
void addDocument_should_return_true_for_new_document() {
    assertTrue(SecurityVault.addDocument("TopSecret.pdf"));
}

@Test
void addDocument_should_return_false_for_duplicate() {
    SecurityVault.addDocument("TopSecret.pdf");
    assertFalse(SecurityVault.addDocument("TopSecret.pdf"));
}

@Test
void addDocument_should_throw_for_null() {
    assertThrows(NullPointerException.class, () -> SecurityVault.addDocument(null));
}

@Test
void wipeAll_should_clear_documents_with_correct_pin() {
    SecurityVault.addDocument("TopSecret.pdf");
    SecurityVault.wipeAll("s3cr3t".toCharArray()); // ny char[] med samme innhold
    assertEquals(0, SecurityVault.getDocumentCount());
}

@Test
void wipeAll_should_throw_SecurityException_with_wrong_pin() {
    assertThrows(SecurityException.class, () -> SecurityVault.wipeAll("feil".toCharArray()));
}

@Test
void wipeAll_should_throw_NullPointerException_for_null_pin() {
    assertThrows(NullPointerException.class, () -> SecurityVault.wipeAll(null));
}
```

---

## Fasit til Oppgave 1

```java
public static boolean addDocument(String document) {
    if (document == null) throw new NullPointerException();
    if (documents.contains(document)) return false;
    documents.add(document);
    return true;
}

public static int getDocumentCount() {
    return documents.size();
}

public static void wipeAll(char[] pin) {
    if (pin == null) throw new NullPointerException();      // NullPointerException, ikke IllegalArgumentException!
    if (!Arrays.equals(pin, MASTER_PIN)) throw new SecurityException();  // Arrays.equals(), IKKE pin.equals()!
    documents.clear();
}
```

## Advarsel — fellen i denne oppgaven

**`pin.equals(MASTER_PIN)`** kaller `Object.equals()` på et `char[]`-array. Arrays i Java arver `.equals()` fra `Object`, som gjør **referanselikhet** (`==`). Det vil si: to forskjellige `char[]`-objekter med IDENTISK innhold vil ALLTID returnere `false` fra `.equals()`.

```java
char[] a = "hei".toCharArray();
char[] b = "hei".toCharArray();
a.equals(b);     // false — ulike referanser!
Arrays.equals(a, b);  // true — innholdet er likt
```

Huskeregel:
- `String` → bruk `.equals()`
- `char[]`, `int[]`, `byte[]`, osv. → bruk `Arrays.equals(a, b)`

Og husk: for `null`-input: `NullPointerException`, **ikke** `IllegalArgumentException`.

---

## Oppgave 2 (Vanskeligere): Verifikasjon med SHA-lignende koding

Systemet oppdateres til å støtte to forskjellige PIN-koder (admin-PIN og backup-PIN). Implementer:

```java
/**
 * Sjekker om den gitte PIN-koden matcher enten admin-PIN eller backup-PIN.
 *
 * @param pin char-array med PIN-kode
 * @return true hvis PIN matcher én av kodene, false ellers
 * @throws NullPointerException hvis pin er null
 */
public static boolean isValidPin(char[] pin) {
    // TODO
    return false;
}
```

```java
private static final char[] ADMIN_PIN = "adminCode123".toCharArray();
private static final char[] BACKUP_PIN = "backupCode456".toCharArray();
```

### Fasit Oppgave 2

```java
public static boolean isValidPin(char[] pin) {
    if (pin == null) throw new NullPointerException();
    return Arrays.equals(pin, ADMIN_PIN) || Arrays.equals(pin, BACKUP_PIN);
}
```

**Fellen**: Det kan friste å skrive `pin.equals(ADMIN_PIN) || pin.equals(BACKUP_PIN)`. Begge vil alltid returnere `false`. Resultatet er at systemet alltid sier ugyldig PIN — selv når den er korrekt.
