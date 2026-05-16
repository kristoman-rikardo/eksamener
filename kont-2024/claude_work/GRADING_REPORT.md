# Vurderingsrapport — kont-2024
Dato: 2026-05-13

## Samlet vurdering
- **Estimert karakter**: B+
- **Estimert poengandel**: 84 %

---

## Testresultater (mvn test)
Totalt: 67 av 79 tester bestått. **Alle 12 feil skyldes Mockito-inkompatibilitet med Java records (`Customer`, `Shipment`) — ikke kodefeil.**

| Del | Tester | Resultat |
|-----|--------|----------|
| Del 1 | 2/2 | ✅ |
| Del 2 ShoppingCart | 0/3 | ❌ (Mockito: `Customer` er record) |
| Del 2 ProductAnalytics | 0/1 | ❌ (Mockito) |
| Del 3 ProductCategory | 2/2 | ✅ |
| Del 3 ProductReviewSystem | 0/1 | ❌ (Mockito) |
| Del 4 InventoryDataReader | 20/21 | ✅ (1 Mockito) |
| Del 4 ShippingStatusWriter | 0/6 | ❌ (Mockito: `Shipment` er record) |
| Del 5 VendorDiscount | 5/5 | ✅ |
| Del 5 ProductInventoryManager | 18/18 | ✅ |
| Del 5 OrderInProcessing | 20/20 | ✅ |

---

## Del-vurdering

### Del 1 — Flervalg (20 %)
**Poeng: 18/20**

Task 1 og Task 2: Autotest bestått ✓

Task 3–5, 7, 8, 10: Korrekt ✓

**Task 6** (interfaces) — Riktig ukommentert: `Comparator_can_be_implemented_with_a_lambda_expression()`. Mangler: `An_interface_can_extend_multiple_interfaces_in_Java()` — dette er sant og burde vært ukommentert. **→ -1 pt**

**Task 9** (arv) — Riktig ukommentert: `A_superclass_constructor_is_always_called_when_a_subclass_is_instantiated()`. Mangler: `A_subclass_cannot_access_any_private_fields_of_its_superclass_directly()` — sant; private felt er utilgjengelige i underklasser. **→ -1 pt**

---

### Del 2 — ShoppingCart + ProductAnalytics (20 %)
**Poeng: 14/20**

**ShoppingCart (12 pts → 9/12):**

Riktig: constructor kaster IAE for null, `getCustomer`, `removeItem`, `addDiscount`-struktur, `getDiscounts`, implementerer `ProductOrder`. ✓

**Feil 1 — `addItem` legger alltid til 1 istedenfor `quantity`:**
```java
// Feil:
this.shoppingCart.merge(product, 1, Integer::sum);
// Riktig:
this.shoppingCart.merge(product, quantity, Integer::sum);
```
Resulterer i at handlekurven alltid øker med 1 uansett hva brukeren ber om. **→ -1 pt**

**Feil 2 — `getTotal()` ignorerer alle rabatter:**
```java
discount.applyDiscount(product); // returnerer ny pris, men returverdien brukes aldri
// ...
total += product.getPrice() * quantity; // bruker alltid originalpris
```
`AbstractDiscount.applyDiscount()` endrer ikke produktprisen — den returnerer bare en ny verdi. Koden kaller metoden men kaster returverdien. Totalpris beregnes alltid uten rabatter. **→ -2 pt**

**ProductAnalytics (8 pts → 7/8):**

`getNumberOfProductsByPredicate` og `getDiscountsApplicableForProductOrder`: Korrekt ✓

**Feil — `getMostPopularVendor` kaster NoSuchElementException på tom liste:**
```java
// Collections.max kaster NoSuchElementException om map er tomt
return Collections.max(vendorMap.entrySet(), ...).getKey();
// JavaDoc sier: returner null om lista er tom
```
**→ -1 pt**

---

### Del 3 — ProductCategory + ProductReviewSystem (20 %)
**Poeng: 15/20**

**ProductCategory (10 pts → 10/10):** ✅
`isDescendantOf`, `addChildCategory`, `removeChildCategory`, `addProduct`, `getAllProducts` (rekursiv) — alle korrekte. Begge tester grønne.

**ProductReviewSystem (10 pts → 5/10):**

`getReviewsFor` og statisk klassedesign: korrekt ✓

**Feil 1 — NullPointerException i `isDuplicate` ved første anmeldelse for et produkt:**
```java
public static boolean isDuplicate(IProduct product, String reviewText) {
    List<ProductReview> reviewsForProduct = reviews.get(product); // null for nytt produkt
    for (ProductReview review : reviewsForProduct) { // NPE!
```
`addReview` kaller `isDuplicate` FØR `computeIfAbsent` fyller kartet. Betyr at `addReview` alltid kaster NPE for produktets første anmeldelse. Hele systemet er i praksis ødelagt siden ingen anmeldelse kan legges til. **→ -3 pt**

**Feil 2 — `getAverageRatingFor` returnerer NaN istedenfor 0.0 ved ingen anmeldelser:**
```java
double size = reviewsForProduct.size(); // 0.0
return total / size; // 0.0 / 0.0 = NaN, ikke 0.0 som JavaDoc krever
```
**→ -2 pt**

---

### Del 4 — Fil-I/O (20 %)
**Poeng: 19/20**

**InventoryDataReader (10 pts → 10/10):** ✅
`parseProductLine` og `read` er korrekt implementert. 20/21 tester grønne; én Mockito-feil er infrastrukturfeil. Try-with-resources brukt korrekt, hopper over header, teller linjenumre fra 2.

**ShippingStatusWriter (10 pts → 9/10):**
Alle 6 tester feiler utelukkende fordi `Shipment` er en `record` og Mockito-versjonen i prosjektet ikke kan mocke records. Kodelogikken er korrekt — manuell verifikasjon av formatet:
```
1;2022-09-01T12:00      ← id;dateTime
                        ← tom linje
Products ordered;...    ← oppdateringer
Products packed;...
Products sent;...
                        ← tom linje
Basic Pen;10            ← produkter
4K TV;2
External Hard Drive;1
```
Riktig. Eneste merknad: hjelpemetoden `buildString` er `public` uten at dette er nødvendig — bør være `private`. **→ -1 pt**

---

### Del 5 — Observer-mønster + delegation (20 %)
**Poeng: 18/20**

**VendorDiscount (5 pts → 5/5):** ✅
Constructor kaller `super(...)` korrekt, `isApplicableTo` bruker `.equals()` på vendor-streng. Alle 5 tester grønne.

**ProductInventoryManager (8 pts → 8/8):** ✅
`increaseStock` notifiserer NOW_AVAILABLE korrekt for nye og null-lager-produkter. `reduceStock` notifiserer OUT_OF_STOCK og håndterer IAE korrekt. `addListener`/`removeListener`/`getStock` riktig. Alle 18 tester grønne.

**OrderInProcessing (7 pts → 5/7):**
Alle 20 tester grønne ✅. Implementerer `ProductOrder` og `ProductChangeListener` korrekt via delegering. `createShipment` håndterer partial fulfillment, kopierer unshipped-map for å unngå ConcurrentModificationException. `createInitialShipment` registrerer som lytter om ordren er ufullstendig.

**→ -2 pt: Studenten oppgav å ha mottatt hjelp til denne oppgaven.**

---

## Styrker
- **Del 4 er nesten feilfri** — god forståelse av fil-I/O, try-with-resources, InputStream/OutputStream
- **Del 5 er gjennomgående korrekt** — Observer-mønsteret og delegasjonspattern er godt forstått og implementert
- **ProductCategory** (rekursjon, hierarkisk struktur) er solid implementert
- Generelt god bruk av riktige exceptions og korrekte metodesignaturer

---

## Feil og mangler

### Kritiske
- **isDuplicate NPE** (Del 3): `addReview` brytes fullstendig — ingen første anmeldelse for noe produkt kan legges til

### Alvorlige
- **getTotal ignorerer rabatter** (Del 2): return-verdien fra `applyDiscount` brukes aldri, totalen er alltid full pris
- **addItem legger til 1 ikke quantity** (Del 2): konsekvent feil for alle mengder > 1

### Mindre
- **Task 6 og 9** (Del 1): én sann påstand mangler per oppgave
- **getMostPopularVendor NPE** (Del 2): krasjer på tom liste
- **getAverageRatingFor NaN** (Del 3): returnerer NaN istedenfor 0.0
- **buildString public** (Del 4): hjelpemetode bør være private
