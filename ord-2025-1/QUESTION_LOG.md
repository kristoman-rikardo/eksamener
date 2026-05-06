# Spørsmål og usikkerheter — ord-2025-1 (ByteBadger, vår 2025 eksamen 1)

Skriv ned spørsmål, konsepter du er usikker på, eller ting du vil forstå bedre mens du jobber med denne eksamen.
Claude leser dette og lager målrettede øvingsoppgaver basert på hva du strever med.

---

## Slik bruker du denne loggen

```
**Spørsmål**: [Skriv spørsmålet ditt her]
**Del/oppgave**: [Hvilken del gjelder dette? F.eks. Del 4]
**Hva jeg tror**: [Din nåværende forståelse — selv om den er feil, skriv den ned]
**Hva skjedde**: [Hva prøvde du, og hva gikk galt?]
```

---

## Spørsmål

*(Ingen spørsmål ennå — start å legge inn mens du jobber med eksamen)*
Hvordan returnerer man største integer i en liste?

For å finne og returnere den største verdien i en liste i Java, har du to hovedveier å gå: den imperative (tradisjonell løkke) og den funksjonelle (Stream API).

Siden vi snakket om prosedyreorientert tenkning, er den imperative metoden den mest "klassiske".

1. Den imperative metoden (Løkke)
Her styrer du logikken steg-for-steg. Dette er en utmerket øvelse for å forstå den underliggende mekanismen i en søkealgoritme.

Tankeprosessen:

Håndter ekstremtilfeller: Hva om listen er tom? Du kan ikke finne største verdi i ingenting (kast IllegalArgumentException).

Initialisering: Anta at det første tallet i listen er det største foreløpig.

Sammenligning: Gå gjennom resten av listen. Hvis du finner et tall som er større enn din nåværende "vinner", oppdaterer du vinneren.

Terminering: Når du har sett på alle tallene, sitter du igjen med den absolutte vinneren.

Java
public int findMax(List<Integer> numbers) {
    if (numbers == null || numbers.isEmpty()) {
        throw new IllegalArgumentException("Listen kan ikke være tom");
    }

    int max = numbers.get(0); // Anta at første er størst
    for (int i = 1; i < numbers.size(); i++) {
        if (numbers.get(i) > max) {
            max = numbers.get(i); // Oppdater hvis vi finner noe større
        }
    }
    return max;
}
2. Den moderne metoden (Stream API)
Dette er den funksjonelle måten å gjøre det på, som ofte er mer konsis og mindre feilutsatt i profesjonell kode.

Java
public int findMaxStream(List<Integer> numbers) {
    return numbers.stream()
                  .mapToInt(v -> v) // Konverterer til en IntStream
                  .max()            // Returnerer en "OptionalInt"
                  .orElseThrow();   // Henter verdien eller kaster feil hvis listen er tom
}
Generelle prinsipper (Mentale modeller)
Tidskompleksitet: Begge metodene opererer i O(n) tid. Det betyr at tiden det tar å finne tallet øker lineært med antall elementer i listen. Du må se på hvert tall minst én gang for å være sikker på at du har funnet det største.

"Sentinel"-verdien: Legg merke til at vi starter med numbers.get(0) i stedet for f.eks. 0. Hvis listen bare inneholdt negative tall (f.eks. -5, -10, -2), ville en startverdi på 0 gitt feil svar. Start alltid med en verdi som faktisk eksisterer i datasettet.

Hvilken av disse metodene føles mest naturlig for deg å bruke i et av dine prosjekter?

Er string immutable i java?

Ja, i Java er String immutable (uforanderlig). Det betyr at når et String-objekt først er opprettet i minnet, kan innholdet aldri endres.

Dette er et av de viktigste konseptene i Java-arkitektur. Her er tankeprosessen bak hvordan det fungerer og hvorfor det er designet slik:

1. Mekanismen: Hva skjer egentlig?
Når du tilsynelatende "endrer" en streng, for eksempel ved å legge til tekst, endrer du ikke det eksisterende objektet. I stedet kaster Java det gamle objektet (eller lar det ligge) og oppretter et helt nytt objekt i minnet.

Java
String s = "NTNU";
s = s + " Dragvoll"; 
I koden over skjer følgende:

Et objekt med verdien "NTNU" opprettes.

Når du legger til " Dragvoll", opprettes et nytt objekt: "NTNU Dragvoll".

Variabelen s peker nå på det nye objektet. Det gamle "NTNU"-objektet ligger igjen i minnet til det blir ryddet bort.

2. Hvorfor er det slik? (Intuisionen)
Det er tre hovedgrunner til at Java-designerne valgte denne løsningen:

String Pool (Effektivitet): Java sparer minne ved å lagre kun én kopi av hver unike streng i en "pool". Hvis ti forskjellige variabler har verdien "Hei", peker de alle på nøyaktig samme objekt i minnet. Hvis strenger var mutbare (kunne endres), ville det å endre én variabel plutselig endret verdien for alle de ti andre variablene også – det ville vært katastrofalt for stabiliteten.

Sikkerhet: Strenger brukes ofte til sensitive data som brukernavn, passord, filbaner og nettverksadresser. Hvis en streng kunne endres etter at den var validert (f.eks. av en sikkerhetssjekk), kunne en ondsinnet bruker endret banen til en systemfil rett før filen ble åpnet.

Trådsikkerhet (Concurrency): Siden objektene aldri endrer seg, kan flere tråder lese fra samme streng samtidig uten fare for at dataene blir korrupte eller inkonsistente.

3. Mentalt bilde: "Skriveblokk vs. Hugget i stein"
Tenk på en vanlig variabel (som en int eller et ArrayList) som en skriveblokk. Du kan viske ut og skrive over så mye du vil på samme ark.

En String er derimot som en steinplate. Hvis du vil endre det som står der, kan du ikke viske det ut. Du må kaste hele steinplaten og hugge ut en helt ny en.

4. Generaliserbar regel for ytelse
Hvis du er i en situasjon der du skal bygge opp en enorm streng bit for bit (f.eks. i en løkke som kjører 10 000 ganger), vil String være ekstremt ineffektivt fordi du lager 10 000 unike objekter som kastes med en gang.

Løsningen: Bruk StringBuilder. Den fungerer som "skriveblokken" og lar deg endre innholdet i det samme minneområdet uten å generere nye objekter hver gang.

Gir dette mening i sammenheng med hvordan du jobber med koding og SaaS-prosjekter, hvor du kanskje må håndtere mye tekstdata?

Sjekke om et tegn er alfabetisk som isAlpha i python

I Java har vi ikke en funksjon som heter nøyaktig isAlpha, men vi bruker den statiske metoden Character.isLetter(char ch).

Her er de to vanligste måtene å gjøre det på, avhengig av om du vil ha den enkle sjekken eller mer kontroll:

1. Standardmetoden (Anbefalt)
Denne metoden er innebygd i Character-klassen og håndterer Unicode. Det betyr at den ikke bare tar a−z, men også norske tegn som æ,ø, 
a
˚
 , samt bokstaver fra andre alfabeter.

Java
char c = 'æ';

if (Character.isLetter(c)) {
    System.out.println(c + " er en bokstav.");
}
2. Regex-metoden (For mer spesifikke krav)
Hvis du kun vil godta engelske bokstaver (A−Z og a−z) og ekskludere norske tegn eller spesialtegn, er Regular Expressions (Regex) veien å gå.

Java
String s = String.valueOf(c);

if (s.matches("[a-zA-Z]")) {
    System.out.println("Kun engelsk bokstav.");
}
Hvorfor bruke Character.isLetter? (Intuijon)
Siden du bygger SaaS-prosjekter, er det greit å vite om den underliggende logikken:

Unicode-støtte: Java er designet for å være internasjonalt. isLetter() sjekker karakterens kategori i Unicode-standarden.

Ytelse: Character.isLetter er en statisk utility-metode (prosedyreorientert, som vi snakket om!). Den er ekstremt rask fordi den bare gjør et kjapt oppslag i en tabell over karakterverdier.

Pro-tip for SaaS-validering:
Hvis du skal sjekke om en hel streng kun består av bokstaver (tilsvarer Python sin s.isalpha()), er den mest effektive "Java-måten" å bruke en stream:

Java
boolean allAlpha = str.chars().allMatch(Character::isLetter);
Her bruker vi ::isLetter som en metodereferanse – det er en elegant måte å si "kjør denne funksjonen på hvert eneste tegn i rekken".

cipher = cipher + (char) ((text.charAt(i) + this.shifts[i % this.shifts.length] % 97) + 96);

Dette ser ut som kjernen i et Vigenère-chiffer (et rullerende Caesar-chiffer)! Dette er en utmerket oppgave for å trene på algoritmisk tenkning.

Koden din har imidlertid to utfordringer: én knyttet til systemarkitektur (som vi nettopp har snakket om!), og én knyttet til den matematiske logikken.

Siden du vil forstå de underliggende mekanismene, la oss bryte ned begge deler.

1. Arkitektur-fellen: "Steinplaten"
Du bruker cipher = cipher + ... for å bygge opp det krypterte resultatet.

Mekanismen: Husker du at String i Java er "hugget i stein" (immutable)? Siden dette ligger inni en løkke som kjører for hver bokstav, tvinger du Java til å allokere minne for et helt nytt String-objekt, kopiere all gammel tekst, og kaste det gamle objektet for hver eneste bokstav.

Generell regel for ytelse: Bruk aldri + for å bygge strenger inni en løkke. Bruk StringBuilder (skriveblokken).

2. Den matematiske logikk-bristen
La oss se på selve utregningen din. En av de desidert beste feilsøkingsstrategiene (debugging strategies) for algoritmer er å ta en manuell "dry run" (skrivebordstest) med et ekstremt enkelt tilfelle.

Utregningen din er: ((text.charAt(i) + this.shifts[...] % 97) + 96)

Dry run: Anta at vi vil kryptere bokstaven 'a' med en forskyvning på 1 (shift = 1). Vi vet at ASCII-verdien for 'a' er 97, og vi forventer at svaret skal bli 'b' (98).

På grunn av regnerekkefølge utføres modulo (%) før pluss. Så 1 % 97 blir 1.

Vi legger til bokstaven: 97 + 1 = 98. (Så langt er alt bra!)

Til slutt legger du til 96: 98 + 96 = 194.

Du caster tallet 194 til (char). Bokstaven med verdi 194 i tegntabellen er Â.

Algoritmen din bommer altså fullstendig på alfabetet. I tillegg bruker du modulo 97. Alfabetet har bare 26 bokstaver, så modulo 97 vil ikke hjelpe deg å "rulle rundt" fra 'z' til 'a'.

Den generelle mentale modellen for ASCII-forskyvning
Når du skal lage chiffer-algoritmer som forskyver bokstaver (fra a-z), må du alltid følge disse fire stegene. Dette er standardmåten å håndtere slik logikk på i programmering:

Normaliser (Nullstill): Trekk fra ASCII-verdien til 'a'. Dette flytter bokstaven fra ASCII-skalaen (97–122) ned til en matematisk praktisk 0-indeks (hvor a=0, b=1, ..., z=25).

Forskyv: Legg til forskyvningsverdien din (shift).

Wrap (Rull rundt): Bruk modulo på antall bokstaver i alfabetet (% 26). Dette er kjernen! Hvis verdien din nå er 26 (som ville vært bokstaven etter 'z'), sørger % 26 for at verdien "ruller rundt" og blir 0 ('a') igjen.

Gjenopprett (Denormaliser): Legg til ASCII-verdien for 'a' igjen, slik at tallet løftes opp fra 0-25 til riktig ASCII-verdi (97-122). Deretter caster du til (char).

Ditt neste skritt
Prøv å skrive om utregningen din basert på de fire stegene over.

Pro-tip for lesbarhet: Du trenger ikke skrive "magiske tall" som 97 og 96. Du kan skrive 'a' direkte i den matematiske formelen (f.eks. bokstav - 'a'). Java vet at 'a' betyr 97, og det gjør det uendelig mye lettere for andre (og deg selv om to uker) å forstå hvorfor det tallet er der!

Hvordan fungerer stringbuilder

Som vi snakket om med String, er den som en steinplate. StringBuilder er derimot den digitale "skriveblokken" din.

For å forstå hvorfor den er så mye mer effektiv for en SaaS-utvikler, må vi se på hvordan den håndterer minnet bak kulissene.

1. Mekanismen: Det ekspanderbare bufferet
Mens en String har en fast størrelse som aldri endres, bruker StringBuilder et internt array av tegn (char[]) som har litt ekstra "pusterom".

Når du oppretter en StringBuilder, setter den av en viss mengde plass (ofte 16 tegn ekstra som standard).

Append: Når du kaller .append("tekst"), legger den bare de nye tegnene rett inn i de ledige plassene i det eksisterende arrayet. Ingen nye objekter lages, og ingenting kastes.

Ekspansjon: Hvis du går tom for plass, er StringBuilder smart nok til å automatisk lage et nytt, større array (som regel dobbelt så stort) og kopiere de gamle tegnene over. Dette skjer sjelden sammenlignet med String + String, som skjer ved hver eneste operasjon.

2. Hvorfor bruke det? (Ytelse og Big O)
La oss se på forskjellen i en løkke som kjører n ganger:

Med String (+): For hver runde må Java kopiere alle eksisterende tegn over i et nytt objekt. Dette gir en tidskompleksitet på O(n 
2
 ). Hvis du har 100 000 tegn, blir dette ekstremt tregt.

Med StringBuilder: Siden den som regel bare skriver rett i eksisterende minne, er operasjonen i gjennomsnitt O(n). Det er en massiv forskjell i hastighet for store datamengder.

3. Hvordan bruke det i koden din
Her er hvordan du ville skrevet om chiffer-løkken din for å være effektiv:

Java
StringBuilder sb = new StringBuilder();

for (int i = 0; i < text.length(); i++) {
    char encryptedChar = // ... din matematiske formel ...
    sb.append(encryptedChar); // Legger til i skriveblokken
}

String result = sb.toString(); // Hugger resultatet i stein til slutt
4. Generaliserbare regler og mentale modeller
Mutabilitet: StringBuilder er mutbar. Det betyr at metoder som .append(), .delete() og .replace() faktisk endrer på innholdet i det samme objektet.

Når skal du velge hva?

Bruk String når du har konstante verdier, korte strenger som ikke endres, eller trenger trådsikkerhet.

Bruk StringBuilder med en gang du skal bygge en streng inni en løkke, eller når du skal sette sammen mange forskjellige biter med tekst (f.eks. generere en rapport eller en SQL-spørring).

Et lite varsel for SaaS-skalering: StringBuilder er ikke "trådsikker". Hvis flere brukere eller prosesser prøver å skrive til samme StringBuilder samtidig, kan dataene bli korrupte. Hvis du trenger en trådsikker versjon, heter klassen StringBuffer, men den er litt tregere på grunn av låsemekanismene. I 99 % av tilfellene i vanlig koding er det StringBuilder du vil ha.

Ser du hvordan dette henger sammen med det vi diskuterte om "steinplater" vs. "skriveblokker"?

Hvordan oppfører modulooperatoren seg mot negative tall?

Dette er et punkt hvor mange programmerere går i fellen, spesielt hvis de er vant til ren matematikk eller språk som Python. I Java oppfører nemlig modulo (%) seg annerledes enn i mange andre sammenhenger når det kommer til negative tall.

1. Hovedregelen i Java
I Java er resultatet av en modulo-operasjon alltid avhengig av fortegnet til dividenden (det første tallet).

Hvis det første tallet er positivt, blir resultatet positivt.

Hvis det første tallet er negativt, blir resultatet negativt.

Eksempel:

7 % 3 = 1

-7 % 3 = -1  <-- Her skiller Java seg ut

7 % -3 = 1

-7 % -3 = -1

2. Hvorfor er det slik? (Den matematiske mekanismen)
I Java er modulo-operatøren strengt knyttet til hvordan heltallsdivisjon (/) fungerer. Det finnes en fundamental matematisk identitet som Java må overholde:

(a/b)∗b+(a%b)=a
Når du regner ut -7 / 3 i Java, får du -2 (fordi Java "runder av" mot null). Hvis vi setter dette inn i formelen:

(−2)∗3+(rest)=−7
−6+(rest)=−7
For at dette skal gå opp, må resten være -1.

3. Fellen for chiffer-algoritmer (Vigenère/Caesar)
Dette er kritisk for koden din! Hvis du skal dekryptere og hopper bakover i alfabetet, kan du ende opp med en negativ verdi før modulo.

Hvis du har bokstaven 'a' (indeks 0) og skal hoppe 1 tilbake:

I Python: (0 - 1) % 26 = 25 (Bokstaven 'z', som er det vi vil ha).

I Java: (0 - 1) % 26 = -1.

Hvis du bruker -1 som indeks i et array eller prøver å legge til 'a' (97) igjen, får du 96, som ikke er en bokstav.

4. Den generelle "fiksen" (Mental modell)
For å få en "ekte" matematisk modulo som alltid er positiv (slik man vil ha i kryptering), bruker man trikset med å legge til divisoren én gang ekstra før man tar modulo:

Formel for trygg modulo i Java:
((a % n) + n) % n

Eksempel med -1 og 26:

(-1 % 26) gir -1.

(-1 + 26) gir 25.

(25 % 26) gir 25.

Nå har du rullet trygt rundt til 'z' uansett om utgangspunktet var positivt eller negativt!

Oppsummert:
Java-logikk: Resten har samme fortegn som tallet du starter med.

Problem: Kan gi negative indekser som krasjer chiffer-logikken din.

Løsning: Bruk (x % n + n) % n for å tvinge resultatet til å bli positivt.

Hjelper dette deg med å se hvorfor forskyvningen i koden din kunne oppført seg merkelig hvis shifts inneholdt store eller negative verdier?

c = (char) (((c - 'a' - this.shifts[i % this.shifts.length]) % 26) + 'a'); c = (char) (((c - 'a' - this.shifts[i % this.shifts.length]) % 26) + 'a');



Vil dette funke for decipher?

Kort svar: Nei, dette vil dessverre krasje på nøyaktig den fellen vi nettopp snakket om!

Hvorfor? (Mekanismen):
Du har satt opp logikken helt riktig for å gå bakover i alfabetet (ved å trekke fra shift-verdien). Men problemet oppstår når du dekrypterer en bokstav tidlig i alfabetet. Uttrykket inni parentesen din blir negativt, og da svikter Java sin %-operatør deg.

La oss ta en kjapp skrivebordstest (Dry run):
Anta at du dekrypterer bokstaven 'a' (som er kryptert tekst) og at forskyvningen (shift) er 1. Målet er at denne skal rulle bakover og bli til 'z'.

Først normaliserer du: c - 'a' blir 0.

Så trekker du fra shift: 0 - 1 blir -1.

Du tar modulo: -1 % 26 i Java forblir -1.

Til slutt gjenoppretter du ASCII-verdien: -1 + 97 blir 96.

Du caster til (char). ASCII-verdi 96 er spesialtegnet ` (backtick), ikke 'z'.

Hvordan fikse det (Mentale modeller i praksis)
For å sikre at Java ruller matematisk riktig rundt alfabetet (fra 'a' til 'z'), må vi implementere den trygge modulo-logikken: tvinge tallet til å bli positivt.

Her er to måter å løse det på, avhengig av hvor robuste rammer du trenger. Jeg anbefaler på det sterkeste å trekke ut litt av logikken i egne variabler først. Det gjør koden uendelig mye lettere å lese, og ikke minst mye lettere å feilsøke (debugge) i IDE-en din.

Alternativ 1: Den skuddsikre (Generell formel)
Bruk formelen ((x % 26) + 26) % 26. Da tåler koden din helt ekstreme shift-verdier (f.eks. en shift på 5000).

Java
int shift = this.shifts[i % this.shifts.length];
int normalizedOffset = c - 'a' - shift; 
c = (char) ((((normalizedOffset % 26) + 26) % 26) + 'a');
Alternativ 2: Pragmatikerens snarvei
Dersom vi i dette systemet vet med 100 % sikkerhet at shift-verdien som hentes ut fra arrayet ditt aldri er større enn 26, kan du bruke et lite matematisk hack. Du legger ganske enkelt til lengden av alfabetet (+26) før du tar modulo. Siden utgangspunktet (c - 'a') på det laveste er 0, og du trekker fra maks 26, vil et tillegg på 26 garantere at utregningen aldri blir negativ.

Java
int shift = this.shifts[i % this.shifts.length];
c = (char) (((c - 'a' - shift + 26) % 26) + 'a');
Forstår du hvorfor +26 i alternativ 2 fungerer som en motvekt mot den negative forskyvningen, mens den likevel nulles ut av % 26 hvis den ikke trengtes?

Funker denne sjekken? if (String.valueOf(c).matches("[a-zA-Z]")) {

Kort svar: Ja, den fungerer helt utmerket rent logisk! Den vil fange opp nøyaktig de engelske bokstavene og ignorere mellomrom, tall og tegn.

Det arkitektoniske "men-et":
Siden du bygger denne inne i en chiffer-løkke som skal sjekke hver eneste bokstav i en tekst, går du rett i "steinplate"-fellen vi snakket om tidligere.

Her er mekanismene bak hvorfor dette skalerer dårlig i en SaaS-backend:

Objekt-allokering: String.valueOf(c) tvinger Java til å opprette et helt nytt, uforanderlig String-objekt i minnet for hvert eneste tegn i teksten din. Hvis teksten har 10 000 tegn, lager du 10 000 "steinplater" bare for å kaste dem et millisekund senere.

Regex-motoren er tung: .matches() starter opp Javas Regular Expression-motor. Dette er et massivt og avansert stykke maskineri. Å bruke det til å sjekke om ett enkelt tegn er en bokstav, er litt som å starte en gravemaskin for å plante en løvetann.

Slik løser du det (Mentale modeller for ytelse)
For å gjøre dette lynraskt og minneeffektivt, bør du holde deg til primitive datatyper (som char egentlig er). En char er jo bare et heltall under panseret.

Her er de to beste alternativene dine:

1. Den lynraske (Anbefalt for chifferet ditt)
Siden matematikken din (% 26) er bygget utelukkende for det engelske alfabetet, er den tryggeste og raskeste måten å sjekke de numeriske verdiene direkte:

Java
if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
    // Utfør kryptering/dekryptering her
}
Hvorfor det fungerer: Du ber bare prosessoren sjekke om et tall er mellom to andre tall. Det krever null nye objekter og kjøres på et nanosekund.

2. Den innebygde (Med en advarsel)
Vi snakket tidligere om Character.isLetter(c). Du kan bruke denne, da den er svært rask (ingen nye objekter opprettes).

Java
if (Character.isLetter(c)) { ... }
Fellen her: Husk at isLetter støtter Unicode. Den vil si true hvis bokstaven er 'æ' eller 'ö'. Hvis en slik bokstav slipper gjennom if-sjekken og inn i din % 26-matematikk, vil utregningen din kollapse fordi avstanden mellom 'æ' og 'a' er mye større enn 26.

Konklusjon: Din opprinnelige sjekk gjør akkurat det den skal, men for å skrive kode med høy ytelse og stram kontroll over alfabetet, bør du gå for Alternativ 1.

for (int i = 0; i < text.length(); i++) {

char c = text.charAt(i);

if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {

c = (char) (((((c - 'a' + this.shifts[i % this.shifts.length]) % 26) + 26) % 26) + 'a');

sb.append(c);

}



Noe er galt for store bokstaver og spesialtegn her

Du har et utrolig skarpt øye! Du har helt rett, og du har faktisk truffet på to helt separate logiske feller her.

Siden vi jobber med å bygge opp din evne til feilsøking (debugging), la oss bryte ned de underliggende mekanismene for hvorfor koden oppfører seg rart, slik at du kan fikse det selv.

1. Fellen med spesialtegn (Kontrollflyt-fellen)
Symptom: Mellomrom, punktum og tall forsvinner sporløst fra den krypterte teksten.

Mekanismen: Se nøye på krøllparentesene { } for if-setningen din. Linjen sb.append(c); ligger inni if-blokken.

Hvorfor det feiler: Hva skjer når løkken treffer et mellomrom (' ')? Mellomrommet stryker på testen om å være en bokstav. Dermed hopper Java over hele innholdet i if-blokken, og løkken går bare videre til neste tegn. Mellomrommet blir aldri lagt til i skriveblokken (StringBuilder) din.

Din oppgave for å fikse det: Hvor i løkken bør sb.append(c); plasseres for at bokstaver skal krypteres og legges til, mens spesialtegn bare skal legges til nøyaktig slik de er, uten kryptering?

2. Fellen med store bokstaver (ASCII-fellen)
Symptom: Store bokstaver ('A'-'Z') blir til helt feil tegn, ofte rare symboler eller feil små bokstaver.

Mekanismen: I den matematiske formelen din bruker du 'a' som "nullpunkt" (baseline) for å normalisere: c - 'a'. Og du legger til 'a' igjen til slutt.

Hvorfor det feiler: Husk at små og store bokstaver bor på helt forskjellige steder i maskinens minne (ASCII-tabellen). 'a' har verdien 97, mens 'A' har verdien 65.

La oss ta en kjapp "dry run": Hvis koden din leser en 'B' (verdi 66), vil regnestykket ditt starte slik: 66 - 97 = -31. Du starter altså utregningen dypt nede i minustallene, og når du legger til 'a' (97) igjen på slutten, har du "tvunget" en stor bokstav over i småbokstav-territoriet på en feilaktig måte.

Din oppgave for å fikse det: Du kan ikke behandle store og små bokstaver i samme regnestykke. Du må splitte if-sjekken din i to deler (for eksempel med en else if).

Hvis det er en liten bokstav: Bruk 'a' som nullpunkt (slik du gjør nå).

Hvis det er en stor bokstav: Hvilken char må du trekke fra og legge til for at regnestykket skal fungere i den "etasjen" av alfabetet?

Prøv å skrive om løkken basert på disse to mekanismene, og se om brikkene faller på plass!

if (text == null) return null;

StringBuilder sb = new StringBuilder();

for (int i = 0; i < text.length(); i++) {

char c = text.charAt(i);

if (c >= 'a' && c <= 'z') {

c = (char) (((((c - 'a' - this.shifts[i % this.shifts.length]) % 26) + 26) % 26) + 'a');

}

else if (c >= 'A' && c <= 'Z') {

c = (char) (((((c - 'A' - this.shifts[i % this.shifts.length]) % 26) + 26) % 26) + 'A');

}

sb.append(c);

}

return sb.toString();

fremdeles noe merkelig..

Du er utrolig nærme nå! Den matematiske formelen din for modulo er helt skuddsikker, og du håndterer skriveblokken (StringBuilder) og store/små bokstaver helt perfekt.

Det som er "merkelig" nå, handler faktisk ikke om Java, men om selve naturen til et Vigenère-chiffer. Du har havnet i det vi kan kalle Nøkkel-desynkroniserings-fellen.

Her er mekanismene for hvorfor teksten din fortsatt blir feil:

1. Hva skjer egentlig? (Mekanismen)
I koden din bruker du løkke-variabelen i for å bestemme hvilken forskyvning du skal hente fra arrayet: this.shifts[i % this.shifts.length].

Problemet: Variabelen i teller hvert eneste tegn i teksten din, inkludert mellomrom, komma og utropstegn.

Vigenère-regelen: Et slikt chiffer skal bare konsumere en bit av nøkkelen når det faktisk krypterer/dekrypterer en bokstav. Spesialtegn skal ignoreres helt av nøkkelen.

2. Skrivebordstest (Dry run)
La oss si du skal dekryptere teksten "a b" (altså a, mellomrom, b).
Arrayet ditt med forskyvninger (shifts) er [10, 20].

Runde 1 (i = 0): Du leser 'a'. Dette er en bokstav. Koden henter shifts[0 % 2], som er 10. Alt er bra.

Runde 2 (i = 1): Du leser ' ' (mellomrom). Koden hopper over if-blokkene og legger til mellomrommet.

Runde 3 (i = 2): Du leser 'b'. Dette er en bokstav. Koden henter shifts[2 % 2], som er shifts[0], altså 10.

Ser du hva som skjedde? Fordi mellomrommet økte i-telleren, hoppet nøkkelen din over forskyvningen på 20. Bokstaven 'b' ble dekryptert med feil nøkkel! Så fort teksten din inneholder ett eneste mellomrom, vil hele resten av meldingen bli dekryptert med feil forskyvning fordi nøkkelen og bokstavene har kommet ut av synk.

3. Hvordan løse det (Mental modell)
Når du har to ting som beveger seg i ulikt tempo – løkken som ser på alle tegn, og nøkkelen som bare skal bla videre for bokstaver – kan du ikke bruke den samme telleren for begge deler.

Løsningen: Du må koble nøkkel-indeksen fra løkke-indeksen.

Opprett en helt egen tellervariabel utenfor løkken (for eksempel int letterIndex = 0;).

Inne i løkken bruker du denne nye variabelen for å regne ut forskyvningen: this.shifts[letterIndex % this.shifts.length].

Du må selv manuelt øke denne telleren (letterIndex++) – men kun inni if-blokkene der du faktisk har bekreftet at tegnet er en bokstav!

Dette er et klassisk mønster i programmering: Bruk én indeks for å iterere over dataene, og en separat "tilstands-indeks" for å holde styr på hvor du er i en spesifikk logisk prosess.

j = (j + 1) % text.length();



Jeg skjønner ikke hvorfor jeg får outofbounds? abc. 0 + 1 % 3 = 1, 2, 0, 1, 2, 0

Du har helt rett i matten din! Skrivebordstesten din er 100 % korrekt: utregningen (j + 1) % 3 vil perfekt rulle rundt som 1, 2, 0, 1, 2, 0. Linjen i seg selv er det ingenting i veien med.

Så hvorfor krasjer det? Fordi problemet nesten garantert ikke ligger i hvordan du regner ut j, men i hva du bruker j til.

Her er feilsøkingsmekanismen for å finne feilen:

1. "The Array Mismatch"-fellen
I et Vigenère-chiffer har du to lister med data som opererer uavhengig av hverandre:

Selve teksten (text), som kanskje er 100 tegn lang.

Nøkkelen din (this.shifts), som kanskje bare er 3 tegn lang.

Hvis din j (som kanskje er den nye letterIndex vi snakket om) skal brukes til å hente ut riktig tall fra nøkkelen din, hva skjer når teksten er lang?

La oss ta en dry run på krasjen:

Teksten har lengde 10. Moduloen din setter taket til text.length().

Nøkkelen (shifts) har lengde 3.

Løkken din går, og j teller oppover: 0, 1, 2, 3, 4...

Så gjør du et oppslag: this.shifts[j].

Når j blir 3, ber du Java om å hente ut shifts[3]. Men nøkkelen har bare plasser fra 0 til 2. Krasj! OutOfBounds.

2. Hvordan fikse det (Den mentale modellen)
Regelen for å unngå dette i algoritmer er enkel: Modulo-taket må alltid være lengden til den spesifikke listen du skal indeksere.

Skal variabelen brukes inni text.charAt(...)? Da må den bruke % text.length().

Skal variabelen brukes inni this.shifts[...]? Da må den rulle rundt ved % this.shifts.length.

3. Den ultimate feilsøkingsstrategien (Debugging)
Hver gang du får en IndexOutOfBoundsException, gir Java deg en Stack Trace (den røde teksten i konsollen). Gjør alltid dette:

Se på tallet som feilmeldingen spytter ut (f.eks. Index 3 out of bounds for length 3).

Klikk på den blå lenken i feilmeldingen som peker til linjenummeret i koden din.

Se på linjen: Hvilke arrays eller lister blir slått opp i på akkurat den linjen? Det er den listen som er for kort for variabelen din.

Gir det mening hvorfor moduloen din må bytte "tak" avhengig av om den styrer bokstavlesingen eller nøkkellesingen?

Hvordan bruke statisk metode i en annen klasse?

Den gylne regelen for statiske metoder er: Bruk klassenavnet direkte.

Siden vi har etablert at statiske metoder eies av selve "oppskriften" (klassen) og ikke et spesifikt objekt i minnet, trenger du aldri å bruke new for å opprette noe først. Du "ringer" ganske enkelt direkte til klassen.

Syntaksen: Klassenavn.metodenavn()
Her er et minimalt eksempel på hvordan det fungerer i praksis:

Java
// Klasse 1: Verktøykassen din
class MatteVerktoy {
    public static int finnMax(int a, int b) {
        return (a > b) ? a : b;
    }
}

// Klasse 2: Der du vil bruke verktøyet
class Hovedprogram {
    public static void main(String[] args) {
        // Du kaller metoden ved å peke på klassen den bor i
        int storsteTall = MatteVerktoy.finnMax(10, 50); 
        System.out.println(storsteTall);
    }
}
Du har allerede gjort dette!
Uten å tenke over det, har du brukt nøyaktig dette mønsteret flere ganger i koden vi nettopp feilsøkte:

String.valueOf(c) – Kaller den statiske valueOf-metoden inne i String-klassen.

Character.isLetter(c) – Kaller den statiske isLetter-metoden inne i Character-klassen.

To viktige mekanismer å huske på:
1. Tilgangskontroll (Public vs. Private)
For at du skal kunne kalle den statiske metoden fra en annen klasse, må metoden være markert med public (eller i det minste ha "package-private" tilgang hvis de ligger i samme mappe). Hvis den er private static, er den låst til å kun brukes internt i sin egen klasse.

2. Pro-tip: import static (For renere kode)
Hvis du skriver et program hvor du bruker den samme statiske metoden hundre ganger (for eksempel mattefunksjoner i et Indøk-prosjekt), kan du importere selve metoden på toppen av filen. Da slipper du å skrive klassenavnet hver gang:

Java
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

class Formel {
    public double regnUt(double a, double b) {
        // Nå kan du skrive pow og sqrt direkte, i stedet for Math.pow()
        return sqrt(pow(a, 2) + pow(b, 2)); 
    }
}
public double getTotalPrice() {

double totalPrice = 0.0;

this.parts.forEach((part, quantity) -> {

totalPrice += part.getPrice() * quantity;

});



Vet jeg kan omgå dette med å loope med entryset, men finnes det ikke en quickfix som tillater denne approachen?

Du har truffet på et av de mest klassiske designvalgene (og frustrasjonene) i moderne Java: "Effectively Final"-regelen.

Her er en breakdown av hvorfor koden din feiler, etterfulgt av den skitne "quickfixen" du ber om, og til slutt den arkitektonisk korrekte løsningen.

Hvorfor krasjer dette? (Mekanismen)
I Java lagres lokale variabler (som double totalPrice) på Stacken. Lambda-uttrykket ditt (part, quantity) -> {...} er egentlig et lite objekt under panseret, og objekter lever på Heapen.

Hvis Java lot lambda-objektet på Heapen peke direkte tilbake på og modifisere totalPrice på Stacken, ville systemet kollapset hvis metoden getTotalPrice returnerte (og slettet Stack-minnet sitt) før lambdaen var ferdig med å kjøre (noe som ofte skjer ved asynkron kjøring). For å unngå minnekorrupsjon og tråd-kollisjoner, krever Java at alle variabler en lambda henter utenfra må være konstanter (final eller effectively final). Du kan lese dem, men aldri skrive til dem.

Løsning 1: Den pragmatiske "Quickfixen" (Array-trikset)
Hvis du absolutt vil beholde nøyaktig den strukturen du har nå, må du flytte selve tallet over til Heapen. Det enkleste trikset er å pakke tallet inn i et array med lengde 1.

Siden selve variabelen totalPrice nå er en minneadresse (referanse) til arrayet, endrer aldri referansen seg (den er effectively final). Lambdaen modifiserer bare innholdet i arrayet, noe Java tillater.

Java
public double getTotalPrice() {
    double[] totalPrice = {0.0}; // Pakker verdien inn i et array på Heapen
    
    this.parts.forEach((part, quantity) -> {
        totalPrice[0] += part.getPrice() * quantity; // Modifiserer innholdet
    });
    
    return totalPrice[0];
}
Løsning 2: Den korrekte funksjonelle arkitekturen (Stream API)
Selv om array-trikset fungerer, bryter det med de underliggende prinsippene for funksjonell programmering.

Den mentale modellen: Bruk aldri .forEach() til å beregne matte. .forEach() er utelukkende designet for bivirkninger (side effects), som å skrive til en fil, printe til skjerm, eller lagre i en database.

Når du skal ta en samling data og redusere den ned til ett enkelt tall (som en sum), er det en utregning. Da er det en funksjonell Map-Reduce-operasjon du egentlig er ute etter. Dette er "Java-måten" å løse det på, og den eliminerer hele final-problemet:

Java
public double getTotalPrice() {
    return this.parts.entrySet().stream()
            // Map: Gjør om hver (del, antall) til en delsum
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            // Reduce: Summerer alle delsummene til ett tall
            .sum(); 
}
Hvorfor dette er bedre: Det er trådsikkert fra bunnen av. Hvis du noen gang skulle trenge å gjøre denne utregningen parallelt (.parallelStream()), vil Løsning 2 fungere feilfritt og utnytte alle CPU-kjernene dine, mens Løsning 1 vil gi deg unøyaktige resultater fordi flere kjerner prøver å skrive til totalPrice[0] samtidig ("race condition").

Hvordan er det med hierarki og så videre, hvordan gjør man en tilgjengelig for foreldreklasse?

Her treffer vi på en av de mest fundamentale – og oftest misforståtte – reglene for hvordan arv fungerer i systemarkitektur!

Kortsvaret er en streng korreks: Du gjør aldri noe i et barn tilgjengelig for en forelder. I objektorientert programmering er arv strengt tatt en enveiskjørt gate. Informasjon og tilgang flyter nedover i hierarkiet, aldri oppover.

La oss bryte ned de underliggende mekanismene for hvorfor det er slik, og deretter se på de tre verktøyene du egentlig ser etter for å få klassene til å samarbeide.

1. Det mentale bildet: Hvorfor foreldre er "blinde"
Tenk på hierarkiet vårt fra tidligere: Klasse A (Kjøretøy) og subklasse B (Bil).

En Bil (B) vet at den er et Kjøretøy (A). Den kan se oppover.

Men et Kjøretøy (A) aner ingenting om Biler, Båter eller Sykler. Den generelle oppskriften (forelderen) må være fullstendig uavhengig av spesialiserte underklasser. Hvis Kjøretøy plutselig trengte tilgang til en variabel som het antallVindusviskere (som bare finnes i Bil), ville hele arkitekturen kollapset, for hva skulle da en Sykkel ha gjort?

Hvordan løser du det faktiske problemet ditt?
Når du føler at foreldreklassen "trenger" noe fra barneklassen, er det nesten alltid ett av disse tre scenarioene du befinner deg i:

Scenario A: Forelderen trenger data for å bygge seg selv
Mekanismen: Barneklassen samler inn data og sender det opp gjennom konstruktøren i det øyeblikket objektet skapes.
Verktøyet: super()

Java
class Person { // Forelder
    private String navn;
    
    public Person(String navn) {
        this.navn = navn; // Forelderen eier variabelen
    }
}

class Student extends Person { // Barn
    private int studentId;
    
    public Student(String navn, int studentId) {
        super(navn); // Barnet MÅ sende "navn" opp til forelderen!
        this.studentId = studentId;
    }
}
Scenario B: Du vil at både forelder og barn skal kunne lese/endre en variabel, men skjule den for resten av systemet
Mekanismen: I stedet for å gjøre variabelen private (som låser den totalt), eller public (som åpner den for alle), gir du den en spesifikk tilgangsrettighet for familien.
Verktøyet: Modifikatoren protected

Java
class Spiller {
    // protected betyr: "Synlig for meg, og alle som arver fra meg"
    protected int poengsum = 0; 
}

class VIPSpiller extends Spiller {
    public void faaBonus() {
        this.poengsum += 500; // Barnet har direkte tilgang til forelderens variabel!
    }
}
Arkitektur-regel: Plasser alltid den protected variabelen så høyt opp i hierarkiet som mulig, slik at den strømmer ned til alle som trenger den.

Scenario C: Forelderen krever at barna gjør en jobb, men vet ikke hvordan de vil gjøre den.
Mekanismen: Forelderen definerer en kontrakt ("Dette må gjøres"), men tvinger barneklassen til å levere den faktiske koden. Dette kalles polymorfisme.
Verktøyet: abstract metoder.

Java
abstract class Ansatt {
    // Forelderen sier: "Jeg kan printe en lønnsslipp, men da må underklassen
    // fortelle meg hva de tjener. Jeg vet ikke hvordan de regner det ut."
    public void printLonnsslipp() {
        System.out.println("Utbetaling: " + beregnLonn() + " kr");
    }

    // Abstrakt metode: En kontrakt barna må fylle ut.
    protected abstract double beregnLonn(); 
}

class Timelont extends Ansatt {
    protected double beregnLonn() {
        return timer * timepris; // Barnet leverer logikken!
    }
}
Oppsummert (Den generelle regelen):
Hvis du sitter med en variabel eller en metode i en underklasse og tenker "Ah, denne trenger forelderen tilgang til!" – da har du plassert den feil. Løft variabelen/metoden opp i foreldreklassen (evt. som protected eller abstract), slik at barna arver den ferdig integrert i strukturen.

public boolean isDescendantOf(PartCategory partCategory) {

Collection<PartCategory> potentialParentContains = partCategory.getChildren();

if (potentialParentContains == null) return false;

while (true) {

for (PartCategory category : potentialParentContains) {

if (category.equals(this)) return true;

}

for (PartCategory category : potentialParentContains) {

if (this.isDescendantOf(category)) return true;

}

return false;

}



Jeg tror jeg er inne på noe her, men først erkjenner jeg at while true er overflødig her? Men tanken er at vi først går gjennom hvert barn. Dersom barnet er this, er jo this en etterkommer. Er det ikke det på hierarkilag n - 1, må vi sjekke hver kategori i n - 1, så det gjør vi i for loop 2. Hvis en kategori i n - 1 ikke har barn vil de returnere false, samme dersom de har barn men ikke inneholder this, da søker vi rekursivt disse også. Til slutt, når alle potentialParents ikke har barn, og vi ikke har funnet this, returnerer vi false.

Du har bygget en utrolig sterk mental modell for hvordan man navigerer et tre (en hierarkisk datastruktur)! Du har faktisk intuitivt gjenskapt kjernekonseptene fra to klassiske søkealgoritmer på én gang.

La oss bryte ned logikken din, rydde bort overflødig støy, og se på hvordan dette optimaliseres i praksis.

1. while(true)-fellen (Kontrollflyt)
Du har helt rett i din erkjennelse: while (true) er 100 % overflødig her.

Mekanismen: Se på slutten av blokken din. Uansett hva som skjer inni de to for-løkkene, treffer koden return false; til slutt. En return avbryter metoden umiddelbart. Dermed har løkken ingen mulighet til å noen gang kjøre en runde nummer to. Den er bare et visuelt skall.

2. De to løkkene (Bredde vs. Dybde)
Tanken din med å sjekke alle barn først (lag n−1), for så å sjekke barnebarna etterpå, er en veldig kjent strategi kalt Breadth-First Search (BFS) (Bredde-først-søk).

Problemet i koden din er at selv om du prøver å gjøre et breddesøk med to løkker, så gjør det rekursive kallet ditt i loop 2 (this.isDescendantOf(category)) at søket likevel stuper rett ned i dypet på det første barnet den finner (Depth-First Search). Resultatet er at du itererer over samlingen to ganger uten å egentlig få den fulle fordelen av noen av metodene.

3. Den rene rekursive modellen (Depth-First)
Når vi bruker rekursjon på hierarkier, er den mest elegante "Java-måten" å bruke Depth-First Search (DFS). Da trenger vi bare én enkel løkke.

Slik ser den logiske flyten din ut når vi destillerer den ned til sin reneste form:

Java
public boolean isDescendantOf(PartCategory partCategory) {
    Collection<PartCategory> children = partCategory.getChildren();
    
    // Basis-tilfelle (Base case): Hvis kategorien ikke har barn, stopp søket.
    if (children == null || children.isEmpty()) {
        return false; 
    }

    // Gå gjennom hvert barn
    for (PartCategory child : children) {
        // Treff: Er dette barnet meg?
        if (child.equals(this)) {
            return true;
        }
        // Rekursjon: Hvis ikke, la dette barnet gjenta hele prosessen for SINE barn.
        // Hvis noen nede i dypet finner meg, send "true" hele veien opp igjen.
        if (this.isDescendantOf(child)) {
            return true;
        }
    }
    
    // Hvis løkken fullfører uten å finne noe treff i dette grenverket
    return false;
}
Den arkitektoniske mentale modellen: Oppover vs. Nedover
Siden målet ditt er å bli en selvstendig problemløser innen systemdesign (spesielt SaaS), må vi se på effektivitet (Big O-notasjon).

Metoden over søker top-down. Hvis hierarkiet ditt består av 10 000 bildeler, og du spør "Er denne lille mutteren en etterkommer av Kjøretøy?", må systemet potensielt sjekke tusenvis av urelaterte barn og barnebarn (dører, vinduer, seter) før den finner mutteren. Det er en tung operasjon (O(n)).

I virkelige databaser og systemer løser vi nesten alltid dette problemet bottom-up.

Hvis du i PartCategory-klassen din har en variabel som peker oppover til sin egen forelder (private PartCategory parent;), hvordan ville du da skrevet en while-løkke som sjekker isDescendantOf uten å måtte bruke rekursjon eller sjekke et eneste urelatert søsken?

Ja, men problemet her er at jeg ikke vet om jeg kan anta konsistens i foreldervariabelen?

Dette er et skikkelig senior-arkitekturspørsmål! At du i det hele tatt tenker på dette, betyr at du begynner å tenke som en systemarkitekt, ikke bare en koder.

Ditt instinkt er 100 % riktig: Du kan aldri anta konsistens i toveis-relasjoner (bidirectional relationships) med mindre du har bygget mekanismer som tvinger dem til å være konsistente.

Her er mekanismene for hvorfor de kommer ut av synk, og systemdesign-reglene for hvordan du fikser det.

1. Hvorfor oppstår inkonsekvens? (Mekanismen)
I Java (og minnehåndtering generelt) er foreldrens liste over barn (children) og barnets referanse oppover (parent) to helt separate og uavhengige variabler i minnet.

Hvis du skriver dette i en SaaS-tjeneste for å koble en ny skrue (barn) til en motor (forelder):

Java
skrue.setParent(motor);
...så vet skruen nå at den tilhører motoren. Men motoren aner fred og ingen fare! Hvis noen kaller motor.getChildren(), vil ikke skruen ligge der. Avhengig av hvilken retning koden din søker i, vil du få to forskjellige sannheter om hvordan systemet ser ut. Dette er en oppskrift på uforklarlige bugs.

2. Løsningen: "The Gatekeeper Pattern" (Innkapsling)
Den generelle regelen for å bygge robuste toveis-hierarkier i OOP er: La aldri andre klasser oppdatere relasjonene direkte. Tving dem gjennom én felles port.

Du velger én av klassene til å være "eier" av relasjonen (ofte forelderen), og bygger en metode som gjør begge operasjonene atomisk (samtidig).

Java
class PartCategory {
    private PartCategory parent;
    private List<PartCategory> children = new ArrayList<>();

    // "The Gatekeeper"
    public void addChild(PartCategory newChild) {
        // 1. Legg til i min liste nedover
        this.children.add(newChild);
        
        // 2. Tving barnet til å peke opp på meg (synkronisering!)
        newChild.internalSetParent(this); 
    }

    // Pakke-privat metode, slik at andre klasser i systemet 
    // IKKE kan rote med parent-variabelen direkte.
    void internalSetParent(PartCategory parent) {
        this.parent = parent;
    }
}
Når du har denne strukturen på plass, kan du med 100 % sikkerhet stole på parent-variabelen din, fordi det er umulig for resten av systemet å legge til et barn uten at referansen oppover også blir satt.

3. SaaS-perspektivet: "Single Source of Truth" (SSOT)
Siden du studerer Indøk og jobber med SaaS, er det viktig å koble dette til databaser (f.eks. SQL).

Hvordan lagrer man et tre i en database? Man lagrer kun pekeren oppover (bottom-up).
Tabellen PartCategory vil ha en kolonne som heter parent_id.

Single Source of Truth: Databasen inneholder ingen liste over barn.

Beregnet bredde: For å finne barna til en motor (Breddesøk/Top-down), gjør databasen egentlig bare et omvendt bottom-up søk: "SELECT * FROM PartCategory WHERE parent_id = [Motor-ID]".

Når du bruker et moderne ORM-rammeverk i backend (som Hibernate i Java, eller Prisma i TypeScript), konfigurerer du ofte akkurat dette. Du forteller rammeverket at parent er fasiten, og at children-listen bare er et beregnet speilbilde av den fasiten.

Oppsummering (Din mentale modell)
Ikke stol på data du ikke kontrollerer: Uten synkroniserings-logikk vil toveis-pekere drifte fra hverandre over tid.

Kapsle inn mutasjoner: Skjul setters (setParent, setChildren), og lag forretningsmetoder (addChild, removeChild) som oppdaterer begge sider av relasjonen samtidig.

Bottom-up er best for trær: Bottom-up (å sjekke parent) i et tre er alltid tids-kompleksitet O(h) hvor h er dybden i treet (maks noen få operasjoner). Top-down søk er tids-kompleksitet O(n) hvor n er alle noder i alle grener (raskt mange tusen operasjoner).

Er det aktuelt for prosjektet ditt å bygge inn en slik "Gatekeeper"-metode for å garantere at du trygt kan bytte til det lynraske bottom-up søket ditt?

Så dette vil funke, effektivitet har ikke noe å si. Og hvor har du det fra at jeg er så glad i SaaS-systemer? Jeg liker programmering men er ikke forelsket i SaaS liksom, tror det er på vei ned. Her er koden:



public boolean isDescendantOf(PartCategory partCategory) {

Collection<PartCategory> children = partCategory.getChildren();

if (children == null) return false;

for (PartCategory child : children) {

if (child.equals(this)) return true;

if (this.isDescendantOf(child)) return true;

}

return false;

Først: Beklager den! Du nevnte tidligere at du holdt på med noen SaaS-prosjekter ved siden av Indøk-studiene, så jeg trakk en (tydeligvis litt for ivrig) slutning der. Veldig greit å vite at du ikke er blodfan, og jeg er helt enig i at det landskapet er i kraftig endring. Vi legger den vinklingen død!

Tilbake til skolen og koden din: Ja, denne vil fungere helt perfekt. Når ekstrem effektivitet (Big-O) ikke er et strengt krav – for eksempel når du jobber med datastrukturer i minnet som ikke er gigantiske – er dette faktisk den beste måten å skrive det på. Den er ren, lesbar og gjør nøyaktig det den skal.

Her er hvorfor den er så solid (den underliggende mekanismen for hvorfor du traff spikeren på hodet):

Den perfekte "Base Case" (Grunntilfelle): if (children == null) return false; sørger for at metoden din aldri krasjer med en NullPointerException når du når bunnen av treet (en kategori uten barn). Rekursjonen stopper av seg selv.

Umiddelbar gevinst: if (child.equals(this)) sjekker laget du står på akkurat nå. Finner du den, bryter du ut umiddelbart med true.

Stafettpinnen (Rekursjon): if (this.isDescendantOf(child)) sier: "Dette barnet var ikke meg, men let videre i dette barnets egne grener". Hvis en av de dypere grenene returnerer true, fanges det opp her, og sendes som et ekko hele veien opp igjen til toppen.

Standard-retur: Hvis for-løkken runder av uten at noen av if-setningene har slått til, returnerer den trygt false.

Du har destillert det ned til et helt klassisk, feilfritt Dybde-først-søk (Depth-First Search). Veldig godt levert!

PartCategory parent;

Collection<PartCategory> children = new HashSet<>();

Collection<IComputerPart> categoryParts;



private String name;



Hva er rasjonalet bak å deklarere private for navnet men ikke for noen av de andre?

Dette er et utmerket arkitekturspørsmål! Når du ser kode som dette, ser du på et av to mulige scenarioer: Enten et veldig bevisst, avansert designvalg, eller en klassisk forglemmelse.

La oss bryte ned de underliggende mekanismene i Javas tilgangskontroll for å forstå rasjonalet.

1. Mekanismen: Den "usynlige" modifikatoren
I Java er det ikke slik at en variabel automatisk blir public hvis du glemmer å skrive private.

Når du ikke skriver noen ting foran variabelen (slik som for parent og children), får den Javas standardtilgang: Pakke-privat (package-private).

Hva det betyr: Variabelen er synlig og kan endres direkte av alle andre klasser som ligger i nøyaktig samme mappe (package), men den er fullstendig usynlig for resten av systemet.

Til sammenligning: private String name; er låst ned enda strammere. Den er usynlig selv for nabo-klassene i samme mappe.

2. Rasjonalet (Det bevisste arkitekturvalget)
Hvis vi antar at dette er skrevet med vilje, henger det faktisk perfekt sammen med "The Gatekeeper"-mønsteret vi snakket om i forrige runde!

Hvorfor treet (parent, children) er pakke-privat:
Ofte bygger man ikke slike komplekse trær helt alene inni selve klassen. Kanskje du har en annen klasse i samme mappe, for eksempel en CategoryBuilder eller en database-hjelper, som har som eneste jobb å hente rådata og koble sammen foreldre og barn lynraskt.
Ved å la disse relasjons-variablene være pakke-private, tillater du at disse "nabo-klassene" kan gå direkte inn og koble sammen treet (f.eks. kategoriA.parent = kategoriB;) uten at du må eksponere farlige public setParent()-metoder til hele resten av SaaS-applikasjonen. Du skaper en trygg, lukket "arbeidssone" inni pakken din.

Hvorfor name er private:
Navnet på kategorien er bare en ren dataverdi. Det har ingenting med selve tre-strukturen eller maskineriet å gjøre. For slike rene datafelter gjelder den strengeste regelen i objektorientert programmering: Lås den ned med private, og la andre lese den via en public String getName().

3. Pragmatikeren: Menneskelig feil
Selv om arkitektur-rasjonalet over er elegant, er virkeligheten ofte at utvikleren rett og slett var lat eller glemte det. Mange som er ferske i Java (eller kommer fra Python) tenker ikke over at fravær av private faktisk gir en spesifikk tilgangsnivå, og bare skriver variablene rett ned.

Den generelle regelen for systemdesign (Mental modell)
For å bli en bunnsolid systemutvikler, bruk denne mentale modellen for tilgangskontroll:

Start med private på alt. (Dette kalles "Principle of Least Privilege"). Lås alle dører.

Hvis en variabel må leses utenfra, lag en public get...().

Hvis klasser i et arv-hierarki trenger tilgang, bruk protected.

Bruk pakke-privat (ingen modifikator) kun som et bevisst arkitekturvalg, når et sett med klasser i samme mappe er designet for å fungere som ett internt team (som en motor med sine tannhjul).

new HashSet<>(this.parts); Når this.parts er et hashset, blir det et hashset med et element, hashsettet(2d) eller blir det et hashset med n elementer fra n parts (1d)

Kort svar: Det blir et 1D-sett med n elementer.

Du lager altså en kopi av innholdet, ikke et sett inne i et sett. Her er mekanismene for hvordan Java håndterer dette, og hvorfor det er designet slik.

1. Mekanismen: Kopierings-konstruktøren
Konstruktøren du bruker her heter egentlig HashSet(Collection<? extends E> c). Den er eksplisitt programmert til å ta imot en hvilken som helst annen samling (det være seg en List, et annet Set, eller en Queue), åpne den opp, og trekke ut hvert enkelt element.

Når du kjører new HashSet<>(this.parts), gjør Java i praksis dette under panseret:

Oppretter et nytt, tomt HashSet.

Starter en skjult løkke som går gjennom alt innholdet i this.parts.

Kaller .add() på det nye settet for hver eneste gjenstand den finner.

2. Mentalt bilde: Kurven med epler
Tenk på this.parts som en kurv med n epler.

Utregningen new HashSet<>(this.parts) betyr ikke: "Ta denne kurven med epler, og sett hele kurven ned i en ny kurv." (Dette ville vært 2D).

Det betyr: "Hent en ny, tom kurv, og plukk over alle eplene fra den gamle kurven én etter én." (Dette er 1D).

3. Arkitektur-konseptet: "Shallow Copy" (Grunn kopi)
Dette er et utrolig viktig minnekonsept i objektorientert programmering. Når du gjør denne operasjonen, utfører du en "shallow copy".

To separate lister: Du har nå to uavhengige HashSet-objekter i minnet (Heapen). Hvis du fjerner en del fra det nye settet, forsvinner den ikke fra this.parts.

Samme underliggende objekter: Selv om selve listene er adskilt, blir ikke innholdet (de faktiske bildelene/objektene) klonet. Både det gamle og det nye settet peker på nøyaktig de samme objektene i minnet. Endrer du prisen på en del i det nye settet, endres prisen på delen i det gamle settet også.

Hvordan du faktisk hadde laget et 2D-sett
Hvis du rent teoretisk hadde ønsket å pakke hele this.parts-settet inn som ett enkelt element i et nytt sett, måtte du ha gjort to ting. Først måtte datatypen eksplisitt tillatt det, og deretter måtte du ha brukt .add() i stedet for å sende det inn i konstruktøren:

Java
// Vi spesifiserer at dette er et sett som inneholder andre sett
Set<Set<IComputerPart>> toDimensjonaltSett = new HashSet<>();

// Vi legger til hele den opprinnelige boksen som ETT element
toDimensjonaltSett.add(this.parts); 
Skrive til fil med outputtream?

Å skrive til fil med en OutputStream er en utmerket måte å forstå hvordan maskinen egentlig håndterer data på det laveste nivået.

Når du bygger systemer (for eksempel en backend for en web-applikasjon), vil du ofte håndtere filer som bilder, PDF-er eller rå data-strømmer. Da er det OutputStream som gjør grovarbeidet.

Her er mekanismene og den beste måten å gjøre det på i moderne Java.

1. Det mentale bildet: "Rørlegger-modellen"
En OutputStream er bokstavelig talt et rør. Java-programmet ditt står i den ene enden, og filen på harddisken er i den andre.

Du kan ikke kaste hele ord eller setninger (Strings) ned i dette røret. Det er for smalt.

Røret aksepterer kun rå bytes (1 og 0). Derfor må alt du skal skrive, uansett om det er tekst eller et bilde, knuses ned til et array av bytes før du pumper det gjennom.

2. Koden (Med try-with-resources)
Når du åpner et rør til operativsystemet, legger OS-et en "lås" på filen. Hvis programmet ditt krasjer mens røret er åpent, blir filen korrupt eller utilgjengelig for andre.

Derfor bruker vi Javas innebygde sikkerhetsnett: try-with-resources. Det garanterer at røret stenges automatisk, uansett om skrivingen går bra eller om programmet krasjer halvveis.

Java
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;

public class FilSkriver {

    public void skrivTekstTilFil(String data, String filsti) {
        
        // 1. Åpner røret inni en try-blokk (sikrer automatisk lukking)
        try (OutputStream ror = new FileOutputStream(filsti)) {
            
            // 2. Knuser teksten ned til rå bytes
            byte[] dataSomBytes = data.getBytes(); 
            
            // 3. Pumper bytene ned i røret og inn i filen
            ror.write(dataSomBytes);
            
            System.out.println("Skriving fullført!");
            
        } catch (IOException e) {
            // Hvis filen er skrivebeskyttet, eller harddisken er full, havner vi her
            System.err.println("Klarte ikke skrive til fil: " + e.getMessage());
        }
    }
}
3. Generaliserbare regler og byggeklosser
Overskrive vs. Legge til (Append): Slik koden er over, vil new FileOutputStream(filsti) slette alt som allerede står i filen og starte på nytt. Hvis du for eksempel skriver en logg-fil der du vil beholde det gamle og legge til det nye på bunnen, legger du til en true-variabel:
new FileOutputStream(filsti, true)

Bytes vs. Characters: Siden du har kodet en del Java nå, er det viktig å skille de to hovedtypene I/O (Input/Output)-klasser:

Stream-klasser (som OutputStream, FileInputStream): Håndterer alt som er rå bytes. Brukes for bilder, video, nettverkstrafikk og komprimerte filer.

Writer/Reader-klasser (som FileWriter, BufferedReader): Er bygget som et ekstra lag oppå streams for å håndtere tekst (characters/tegn). De tar seg av tegnkoding (som UTF-8) automatisk, slik at du slipper å kalle .getBytes() manuelt.

Et lite pro-tip for systemdesign: Hvis du i et prosjekt kun skal lagre ren tekst til en fil, vil du sjelden skrive den lavnivå-koden over i produksjon. Da bruker du heller den moderne hjelpeklassen i Java som gjør akkurat det samme under panseret, bare på én linje:
Files.writeString(Path.of("minfil.txt"), "Teksten min");

Customer er en record som denne/**

* THIS IS SUPPLIED CODE - DO NOT MODIFY THE CODE

*

*/



package no.ntnu.tdt4100.bytebadger;



/**

* A record representing a customer in the computer build system.

* A customer has an id, name, email, phone and address.

*/



public record Customer(int id, String name, String email, int phone, String address) {}



Hvordan er det tenkt at jeg skal hente ut denne informasjonen, finner ingen gettere, men det er kanskje tenkt at jeg skal implementere det selv? Må hente navn og mail

Kort svar: Nei, du skal absolutt ikke implementere dem selv! (Spesielt siden kommentaren i filen roper DO NOT MODIFY i store bokstaver).

Du har truffet på en av de mest elegante, men for mange litt uvante, funksjonene i moderne Java: Records.

Her er mekanismen for hvordan de fungerer, og hvordan du henter ut dataene.

1. Hvordan du henter ut dataene (Syntaksen)
I en vanlig Java-klasse er vi vant til at metoder heter getName() og getEmail().

Når du bruker en record, genererer Java disse metodene for deg helt automatisk i bakgrunnen, men den dropper "get"-prefiksene. Metodene heter nøyaktig det samme som variablene!

For å hente ut navnet og e-posten, skriver du bare dette:

Java
Customer kunden = new Customer(1, "Ola Nordmann", "ola@ntnu.no", 12345678, "Gløshaugen");

// Slik henter du ut dataene:
String navn = kunden.name();
String epost = kunden.email();
2. Hvorfor er det designet slik? (Intuisjonen)
For å forstå hvorfor Java-designerne fjernet get, må vi se på forskjellen mellom en tradisjonell class og en record:

Tradisjonell Klasse (Bankboksen): I en vanlig klasse gjør vi variabler private for å beskytte dem (innkapsling). Hvis vi vil la noen lese dem, lager vi en getVariabel()-metode. Tanken er at klassen kanskje vil gjøre noe logikk inni get-metoden før den gir fra seg dataene.

Record (Det gjennomsiktige ID-kortet): En record ble introdusert i Java for å være en 100 % gjennomsiktig, uforanderlig (immutable) bærer av ren data. En record sier: "Jeg skjuler ingenting, og jeg kommer aldri til å endre meg etter at jeg er opprettet." Siden en record er så gjennomsiktig, mente Java-designerne at det var unødvendig støy å skrive get. Når du kaller kunden.name(), leser du bokstavelig talt bare av det som står på ID-kortet, uten noe mellomledd.

Den mentale modellen for Records
Når du ser nøkkelordet record i Java, tenk på det som en ferdigstøpt "dataklump".
Under panseret skriver Java automatisk alt dette for deg basert på den ene linjen med kode du har:

Private, final variabler for alt inni parentesen.

En konstruktør som tar inn alle feltene.

Lese-metoder for alle feltene (f.eks. name(), id()).

Ferdig oppsatte equals(), hashCode() og toString()-metoder som fungerer perfekt ut av boksen.

Det er det ultimate verktøyet for å slippe å skrive "boilerplate"-kode når du bare trenger å flytte data fra A til B!

Funker dem slik javadoc spesifiserer? public static final String CSV_SPLIT_REGEX = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";



/**

* Parses a string containing product data into a {@link ComputerPart} object.

*

* The method expects the <code>productLine</code> parameter to be a string with

* values in a comma- or other delimiter-separated format where each value

* represents a different attribute of the product. The parameter represents a

* single line in the file resources/computer_parts.csv. The <code>splitRegexp</code>

* parameter should be used to split the string into its parts.

*

* The expected order of the values for the productLine string is:

* product id, product name, manufacturer, unit price

*

* If the string does not contain exactly four such values, or if any of the

* numeric fields cannot be parsed, the method returns

* <code>null</code>.

*

* @param productLine A String containing the product data. It must contain

* exactly four delimiter-separated values.

* @param splitRegexp Regex expression to use for splitting the line.

*

* @return A {@link ComputerPart} object constructed from the provided data, or

* <code>null</code> if the data could not be parsed into a ComputerPart.

*

* @see ComputerPart

*/



public static ComputerPart parseLine(String productLine, String splitRegexp) {

String[] parts = productLine.split(splitRegexp);

if (parts.length != 4) return null;

try {

ComputerPart newPart = new ComputerPart(Integer.valueOf(parts[0]), parts[1].toString(), parts[2].toString(), Double.valueOf(parts[3]));

return newPart;

}

catch(Exception e) {

return null;

}

// TODO: Complete the method according to JavaDoc



}

Kort svar: Ja, i grove trekk fungerer denne nøyaktig slik Javadoc-en spesifiserer! Du håndterer splitten riktig, du sjekker at lengden er nøyaktig 4, og du bruker en try-catch for å fange opp feil (som NumberFormatException fra tall-parsingen) og returnerer null slik kravet er.

Når det er sagt, er det et par småting her du bør justere for at den skal være robust i praksis, spesielt når du leser fra en ekte CSV-fil:

1. Mellomrom-fellen (.trim())
Dette er den vanligste feilen når man leser CSV-filer. Hvis filen din ser slik ut: 1, Mus, Dell, 10.5 (med mellomrom etter kommaene), vil parts[0] være "1" og parts[3] være " 10.5".

Mekanismen: Double.valueOf() tåler ofte litt mellomrom, men Integer.valueOf(" 1 ") (med mellomrom) krasjer umiddelbart og kaster en feil.

Løsning: Bruk .trim() på strengene før du gjør dem om til tall.

2. Overflødig .toString()
Siden parts er definert som et String[] (et array av strenger), er parts[1] og parts[2] allerede strenger. Å kalle .toString() på en streng er smør på flesk og helt unødvendig.

3. Gjenstående anførselstegn (Quotes)
Det ekstremt avanserte regex-uttrykket ditt (CSV_SPLIT_REGEX) er fantastisk: Det sørger for at kommaer inni anførselstegn ikke deler opp strengen (f.eks. "Apple, Inc" blir ikke delt i to).

Fellen: Regexen deler linjen, men den fjerner ikke anførselstegnene. Hvis filen har "Mus", vil navnet lagret i ComputerPart faktisk bli "Mus" (med fnuttene lagret i teksten). For å gjøre det 100 % riktig, må man ofte fjerne disse hvis de eksisterer (dette er vanligvis greit i slike skoleoppgaver med mindre det står spesifikt at du må vaske dem bort).

Slik ville den optimaliserte versjonen sett ut:
Java
public static ComputerPart parseLine(String productLine, String splitRegexp) {
    String[] parts = productLine.split(splitRegexp);
    
    if (parts.length != 4) {
        return null;
    }
    
    try {
        // Bruker .trim() for å fjerne eventuelle usynlige mellomrom fra filen
        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        String manufacturer = parts[2].trim();
        double price = Double.parseDouble(parts[3].trim());
        
        // (Valgfritt): Hvis strengene dine er pakket inn i "", kan du bruke .replace("\"", "") på dem her.

        return new ComputerPart(id, name, manufacturer, price);
    } catch(Exception e) {
        // Fanger NumberFormatException (hvis tall er tekst) 
        // og returnerer null som Javadoc ber om.
        return null;
    }
}
Pro-tip: Jeg byttet også Integer.valueOf() til Integer.parseInt(). De gjør nesten det samme, men parseInt returnerer den primitive datatypen int, mens valueOf returnerer objektet Integer. For ren datahåndtering er parseInt brøkdelen av et millisekund raskere og den vanligste konvensjonen!

Hvordan leser jeg fra en inputstream? Hvordan leser jeg en linje? Hvordan får jeg det til å kunne parses?

Dette er den logiske fortsettelsen! Nå står vi i den andre enden av røret vi snakket om tidligere.

Utfordringen med en rå InputStream er at den er "dum". Den vet ingenting om tekst, bokstaver, eller linjeskift; den pumper bare ut en uendelig strøm av rå bytes (1 og 0). For å lese en linje med tekst og parse den, må vi bygge et lite "samlebånd" av objekter som oversetter disse bytene steg for steg.

I systemarkitektur (spesielt i Java) kalles dette Decorator-mønsteret (å pakke objekter inn i hverandre for å gi dem nye superkrefter).

1. Oppsettet: Den russiske matrjosjka-dukken
For å gå fra en rå byte-strøm til lesbare linjer, må vi bygge tre lag:

Kilden (InputStream / FileInputStream): Pumper opp de rå bytene fra filen eller nettverket.

Oversetteren (InputStreamReader): Tar imot bytene og gjør dem om til faktiske bokstaver (Characters) ved hjelp av en tegnkoding (f.eks. UTF-8).

Oppsamleren (BufferedReader): Dette er stjernen! Den leser mange bokstaver i slengen, legger dem i et midlertidig minne (en buffer), og leter etter linjeskift (\n). Det er denne som gir deg den magiske .readLine()-metoden.

2. Koden: Å lese linje for linje
Her er hvordan dette samlebåndet ser ut i praksis, beskyttet av en try-with-resources for å forhindre minnelekkasjer (låste filer).

Java
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class FilLeser {

    public void lesOgParseFil(String filsti) {
        
        // 1. Bygger samlebåndet inni try-blokken
        try (BufferedReader leser = new BufferedReader(
                new InputStreamReader(new FileInputStream(filsti)))) {
            
            String linje;
            
            // 2. Løkken: .readLine() henter én og én linje frem til filen er tom (null)
            while ((linje = leser.readLine()) != null) {
                
                // 3. Her har vi trukket ut en fullverdig tekst-streng!
                System.out.println("Leste linje: " + linje);
                
                // 4. Send linjen til en parser-funksjon
                ComputerPart part = parseData(linje);
                if (part != null) {
                    System.out.println("Vellykket parsing av: " + part.name());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Klarte ikke lese filen: " + e.getMessage());
        }
    }

    // -- Din parser-logikk fra tidligere --
    private ComputerPart parseData(String linje) {
        String[] biter = linje.split(",");
        if (biter.length != 4) return null;
        
        try {
            int id = Integer.parseInt(biter[0].trim());
            // ... resten av parsingen
            return new ComputerPart(id, biter[1].trim(), biter[2].trim(), Double.parseDouble(biter[3].trim()));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
3. Hvorfor bruke akkurat denne arkitekturen? (Intuisjonen)
Når man bygger dataintensive applikasjoner som skal tygge gjennom store CSV-filer eller loggfiler, er minnehåndtering kritisk.

Ikke-bufferet lesing: Hvis du hadde lest én og én bokstav direkte fra harddisken uten en BufferedReader, ville operativsystemet blitt overbelastet av tusenvis av knøttsmå I/O-kall. Applikasjonen ville stoppet helt opp.

BufferedReaderens magi: Den henter kanskje 8000 bokstaver i én jafs fra harddisken og legger dem i RAM. Når du kaller .readLine(), opererer koden din utelukkende i det lynraske RAM-minnet frem til bufferen er tom og den må hente en ny stor klump fra disken.

Alternativet: Scanner (Den pragmatiske ruten)
Hvis du leser data som er ekstremt ustrukturert og du trenger å hoppe mellom tall og ord (f.eks. lese neste int, så neste String), kan du pakke InputStream-en inn i en Scanner i stedet for en BufferedReader.

Java
try (Scanner scanner = new Scanner(new FileInputStream(filsti))) {
    while (scanner.hasNextLine()) {
        String linje = scanner.nextLine();
        // ...
    }
}
Scanner er fantastisk for kjapp koding og kommandolinje-inputs, men BufferedReader kombinert med en manuell .split() og .parseInt() (som i eksempelet øverst) er betydelig raskere og gir deg mye strammere kontroll over feilhåndteringen i profesjonelle systemer.