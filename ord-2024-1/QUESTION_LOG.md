# Spørsmål og usikkerheter — ord-2024-1 (BMW, vår 2024 eksamen 1)

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

- Hvordan fungerer optional? 
- Forstår virkelig ikke, eller husker hvertfall ikke streams...
- Set og unike tall
- Hente statiske metoder fra andre filer, hvorfor så lang importsti?

### **FEEDBACK FRA GEMINI:**

Her er de tre hovedområdene du sliter med, hvorfor de oppstår, og de generaliserbare reglene for å overvinne dem:

---

### 1. "Black Box"-prinsippet og støy fra støttefunksjoner
Du nevner at du blir stresset av støttefunksjonene som allerede er etablert. Dette er en klassisk konsekvens av å prøve å forstå *hele* systemet på en gang. Når du fikk `RandomGenerator` eller `IPilkingtonService`, prøvde hjernen din å prosessere hvordan disse fungerte under panseret, i stedet for å bare bruke dem.

* **Symptomer fra koden din:** * Du importerte Javas egen komplekse `RandomGenerator` i stedet for å bruke den enkle metoden faglærerne hadde laget.
  * Du slet med å vite *hvordan* du skulle opprette vinduene, i stedet for å bare dytte ansvaret over på `IPilkingtonService`.
* **Mental modell (Black Box-tenkning):** Tenk på alle ferdiglagde klasser og interfaces som en magisk, svart boks (Black Box). Som systemarkitekt bryr du deg **null** om hvordan boksen fungerer inni. Du bryr deg utelukkende om to ting: *Hva trenger boksen for å starte (input)?* og *Hva spytter den ut (output)?*
* **Den generaliserbare regelen:** Hvis oppgaven gir deg et ferdig interface eller en klasse, lukk filen. Ikke les koden inni den. Les *kun* Javadoc-en for å se kontrakten (metodesignaturen). Stol blindt på at boksen gjør jobben sin.

### 2. Delegrering vs. Eierskap (Designmønstre)
Når filene begynner å snakke sammen i oppgave 4 og 5, mister du av og til tråden på *hvem* som eier dataene, og *hvem* som utfører handlingen. Dette handler om å forstå de underliggende designmønstrene (Design Patterns).

* **Symptomer fra koden din:**
  * **Observer-mønsteret (Pilkington):** Du slet med å skille mellom fabrikken (lageret) og lytteren (`OrderFromPilkingtonStockChangeListener`). Du la til `.addListener(this)` inni lytteren selv, noe som bryter med ansvarsfordelingen.
  * **Decorator-mønsteret (`CarFactoryWithInventory`):** Å bygge et "skall" rundt en eksisterende fabrikk for å gi den ny funksjonalitet (et lager). 
  * **Statisk tilstand (`GlobalCarProductionRegistry`):** Du prøvde å bruke instansvariabler (`private String vin`) i et globalt hovedkvarter, i stedet for å bruke statiske datastrukturer (`static Set<String>`).
* **Mental modell (Organisasjonskartet):** Se for deg koden din som en ekte bedrift. Hvem er sjefen? Hvem er underleverandøren? Hvem er revisoren (lytteren) som bare står i hjørnet og observerer? 
* **Den generaliserbare regelen:** Tegn alltid et kjapt organisasjonskart (diagram) på et kladdepapir før du begynner på oppgave 4 og 5. Tegn bokser for hver klasse, og trekk piler mellom dem. Står det `implements IStockChangeListener`, tegn en stiplet pil som betyr "Lytter på". Da ser du dataflyten visuelt før du drukner i syntaks.

### 3. Eksekveringsrekkefølge og tilstandsendring (Control Flow)
Du har en sterk kvantitativ og algoritmisk forståelse, men du snubler ofte i hvordan maskinen leser koden linje for linje, spesielt når data muteres (endres) underveis.

* **Symptomer fra koden din:**
  * **Off-by-one / Indeksering:** Å forveksle kolonne 3 og 4 i CSV-filen. Maskiner starter på 0, mennesker på 1.
  * **Den spiste linjen:** I CSV-leseren din (`while (reader.readLine() != null)` kombinert med `String line = reader.readLine()`) opprettet du et sort hull som slukte annenhver linje.
  * **Manglende returveier:** En `if`-setning som fikser en ugyldig VIN, men som glemmer å fortelle koden hva den skal gjøre hvis VIN-en *allerede* var gyldig.
  * **Skjulte krasj:** Å fange opp *alle* exceptions i CSV-leseren og kalle dem "Duplicate country", slik at du maskerte dine egne indekseringsfeil.
* **Mental modell (Samlebåndet):** Kjør koden din manuelt i hodet, linje for linje, med fiktive data. *Hva er verdien av `line` akkurat nå? Hva skjer hvis denne variabelen er `null`?*
* **Den generaliserbare regelen (Fail Fast):** Håndter alltid feil og edge-cases helt i toppen av metoden din. Kast exceptions, returner tidlig, eller fiks dataene umiddelbart. Ikke la ødelagte data reise nedover i koden din. 

### Oppsummering av strategien din fremover
Du har absolutt logikken som trengs; det du bygger inni de enkle metodene er ofte helt korrekt. For å knekke oppgave 4 og 5, må du tvinge deg selv til å zoome ut. 

Neste gang du åpner et slikt eksamenssett: Start med å lese beskrivelsen av alle klassene *før* du skriver en eneste linje kode. Identifiser mønstrene. Er dette en lytter (Observer)? Er dette en innpakning (Decorator)? Er det en felles ressurs (Singleton/Static)? Når organisasjonskartet sitter i hodet, blir kodingen bare en mekanisk oversettelse av oppgaven.