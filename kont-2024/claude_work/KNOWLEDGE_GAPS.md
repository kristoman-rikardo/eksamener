# Kunnskapsgap — kont-2024

## Gap 1: Return-verdier fra metoder som ikke muterer (applyDiscount)
**Alvorlighet**: Høy

**Bevis**:
```java
// getTotal() i ShoppingCart:
discount.applyDiscount(product); // kaster returverdien
// ...
total += product.getPrice() * quantity; // bruker originalpris – aldri rabattert
```

**Forklaring**:
`AbstractDiscount.applyDiscount()` er en ren funksjon — den muterer ikke produktet, den returnerer en ny beregnet pris. Studenten kalte metoden men ignorerte returverdien og brukte `product.getPrice()` direkte. For å bruke rabattert pris måtte man ha fanget returverdien:
```java
double discountedPrice = discount.applyDiscount(product);
total += discountedPrice * quantity;
```
Dette er et klassisk misforståelses-mønster: man antar at metoden gjør noe "til" objektet, men den bare beregner og returnerer.

**Mønster**: Sett første gang her. Kan også dukke opp der man forventerer at `Collections.sort()` returnerer sortert liste (den muterer på plass).

**Call to action**: Øv på å lese JavaDoc for returtypen. Hvis metoden returnerer `double`, BRUK den doulen. Skriv unit-tester som sjekker at `getTotal()` er lavere med rabatt enn uten.

---

## Gap 2: NullPointerException ved manglende mapoppslag (reviews-kart)
**Alvorlighet**: Høy

**Bevis**:
```java
public static boolean isDuplicate(IProduct product, String reviewText) {
    List<ProductReview> reviewsForProduct = reviews.get(product); // null for nytt produkt
    for (ProductReview review : reviewsForProduct) { // NPE!
        if (review.reviewText().equals(reviewText)) return true;
    }
    return false;
}
```

**Forklaring**:
`Map.get()` returnerer `null` om nøkkelen ikke finnes. Å iterere over `null` med en for-each kaster NullPointerException. Riktig løsning: bruk `getOrDefault()` eller sjekk `containsKey()` først:
```java
List<ProductReview> reviewsForProduct = reviews.getOrDefault(product, List.of());
```
Alternativt, bruk `reviews.computeIfAbsent()` kun én plass og aldri ring `isDuplicate` for produkter du vet ikke er i kartet.

**Mønster**: Sett i kont-2023 (getCancelledTickets muterte input-liste). Her er det en variant: antok at kartet alltid har en verdi for alle nøkler. Systematisk svakhet rundt Map-operasjoner.

**Call to action**: Etter hvert Map.get()-kall, still deg spørsmålet: "Hva skjer om nøkkelen ikke finnes?" Bruk `getOrDefault`, `computeIfAbsent` eller en null-sjekk konsekvent.

---

## Gap 3: Feil variabel i merge-kall (addItem)
**Alvorlighet**: Middels

**Bevis**:
```java
public void addItem(IProduct product, int quantity) {
    if (product == null || quantity < 1) throw new IllegalArgumentException();
    this.shoppingCart.merge(product, 1, Integer::sum); // 1 istedenfor quantity
}
```

**Forklaring**:
`Map.merge(key, value, remappingFunction)` slår sammen eksisterende verdi med `value`. Her sendes hardkodede `1` istedenfor parameteren `quantity`. Resultatet er at `addItem(product, 10)` bare legger til 1. Dette er en liten skrivefeil med store konsekvenser.

**Mønster**: Ikke sett i andre eksamener ennå. Kan skyldes at man begynte med å teste med mengde 1 og aldri testet med større mengder.

**Call to action**: Etter implementasjon av addItem — skriv en test som legger til 5 av ett produkt og sjekker at getItems().get(product) == 5. Én integrasjonstest ville avslørt dette umiddelbart.

---

## Gap 4: Intervall-grenser i flervalg (Task 6 og 9)
**Alvorlighet**: Lav

**Bevis**:
- Task 6: Mangler `An_interface_can_extend_multiple_interfaces_in_Java()` — sant
- Task 9: Mangler `A_subclass_cannot_access_any_private_fields_of_its_superclass_directly()` — sant

**Forklaring**:
For Task 6: Et interface KAN utvide flere interfaces (`interface A extends B, C`). Studenten husket lambdaer men glemte grensesnittarv.

For Task 9: Private felt i en superklasse er utilgjengelige i underklassen — selv om underklassen arver de andre feltene. Studenten husket konstruktørkjeding men overså dette.

**Mønster**: Lav alvorlighet, men viser at det er lettere å huske "positivt" (hva Java kan) enn "negativt" (begrensninger).

**Call to action**: Øv på Java-teori med spørsmål som fokuserer på edge cases i arv og interfaces. Lag flashcards for de vanligste "ja/nei i Java"-spørsmålene.

---

## Gap 5: NaN vs 0.0 ved divisjon
**Alvorlighet**: Lav

**Bevis**:
```java
public static double getAverageRatingFor(IProduct product) {
    double size = reviewsForProduct.size(); // 0.0 om ingen anmeldelser
    return total / size; // 0.0 / 0.0 = NaN, ikke 0.0
}
```

**Forklaring**:
I Java er `0.0 / 0.0 = NaN` (Not a Number). JavaDoc krever at metoden returnerer `0.0` om ingen anmeldelser finnes. Riktig løsning:
```java
if (reviewsForProduct.isEmpty()) return 0.0;
```

**Mønster**: Vanlig edge case. Lett å glemme å håndtere tom-liste-tilfellet eksplisitt.

**Call to action**: Tenk alltid på "hva skjer om listen er tom?" som et standardspørsmål etter enhver metode som opererer på samlinger. Skriv alltid en test for tom input.
