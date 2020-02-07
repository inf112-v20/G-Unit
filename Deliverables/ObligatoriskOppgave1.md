# Oppgave 1 - Møte 31.01.2020

## Deloppgave1: Kompetanse
-   **Elias:** Har god lederkompetanse
-   **Bo:** Flink i objekt-orientert programmering
-   **Andreas:** God på kodesprint
-   **Erik:** God i brettspill, flink til å teste

### Roller
-   **Elias:** Project Manager
-   **Andreas:** Utvikler/tester og kundekontakt
-   **Erik:** Utvikler/tester
-   **Bo:** Utvikler/tester og grafisk ansvarlig

### Begrunnelse
Vi har valgt en project manager ettersom det er lurt å ha en leder som har oversikt over progressjon og kan fordele oppgaver.
Vi har også valgt en kundekontakt fordi vi må ha en som tar ansvar for å ta kontakt med eventuelle eksterne aktører.
I tillegg til dette har vi valgt en som er grafisk ansvarlig, han tar hånd om grafikk og teksturer.

---
## Deloppgave 2: Spesifikasjon - Møte 31.01.2020

### Mål
Alt skal funke etter reglene, man må kunne gjennomføre et helt spill etter reglene. Det skal heller ikke være noen game-breaking bugs.

### Krav
-   Meny
-   Flerspiller- og enspiller support
-   Et brett
-   Vise en brikke
-   Dele ut kodesprint
-   Velge kodesprint
-   Flagg på brettet
-   Ikke gå gjennom vegger
-   Spille fra ulike maskiner
-   Flytte flere brikker samtidig
-   Flytte brukker etter prioritet på kort
-   Dele ut nye kort ved ny runde
-   Restarte fra backup ved ødeleggelse

### Krav i første iterasjon
-   Et vindu
-   Elementær grafikk
-   Brett med tiles
-   En figur
---
## Deloppgave 3: Prosjektmetodikk - Møte 31.01.2020
Det sto mellom scrum og kanban, men vi endte opp med **kanban** som prosjektmetodikk ettersom denne er litt frirere enn scrum. Selvom vi har valgt kanban, vil vi nok etterhvert inkludere elementer av scrum. Som f.eks. sprinter - det kan være hvis vi blir enige om møtes og ta en sprint hvis vi ser behov for det.

Vi har bestemt oss for å ha et fast tidspunkt (søndag kl. 19) hver uke hvor vi tar en catchup på slack, der vi blir enige om evt møte(r) i uken som kommer. Vi tar også en oppsummering av uken og kanban-boardet, hvor vi ser hvordan vi ligger an.

---
## Deloppgave 4 - Møte 07.02.2020
### Brukerhistorier
Vi fulgte tutorialen som ble lagt ut på mittUIB, men valgte å bruke egne textures. Brukerhistoriene vi har valgt å fokusere på frem mot denne leveransen er som følger. 
-   "Som bruker ønsker jeg å kunne åpne et program hvor jeg blir presentert med spillbrettet" 
    -   **Arbeidsoppgaver**
    -   Vi må ha et vindu
    -   Implementere tile map
    -   Elementær spillgrafikk
-   "Som bruker ønsker jeg å kunne differansiere mellom forskjellig typer tiles på brettet, fks hull, flagg og spiller" 
    -   **Arbeidsoppgaver**
    -   Individuelle tiles
    -   Individuelle textures
    -   Implementere visningslogikk
-   "Som bruker ønsker jeg å bevege meg rundt på brettet ved bruk av piltastene"
    -   **Arbeidsoppgaver**
    -   Implementere bevegelseslogikk
-   "Som bruker øsnker jeg at spillet ikke lar meg gå utenfor brettet"
    -   **Arbeidsoppgaver**
    -   Videre implemementere bevegelseslogikk
-   "Som bruker ønsker jeg å se forksjell på figuren min avhengig av hvilket felt jeg står på"
    -   **Arbeidsoppgaver**
    -   Lage ulike textures for spillerfiguren
    -   Videre implementere bevegelseslogikk
    -   Implementere logikk som tar hånd om oppdatering av figur-textures
    
    **Alle disse brukerhistoriene er oppfylt til denne leveransen**
---
## Annet
### Retroperspektiv - Møte 07.02.2020
-   I henhold til krav til første innlevering har vi nådd alle kravene
-   Valget av kanban er vi fornøyde med, og vi tok en liten sprint hvor vi kodet det meste av innleveringen derav spillebrett og spiller.
-   Fremmover skal vi bli flinkere på å skrive møtereferat og oppdatere hverandre på når man setter seg ned å jobber.
    -   Send melding i slack med en gang man begynner å jobbe med en issue
-   Vi skal også bli flinkere på å oprette flere og mindre omfattende issues
-   En viktig ting vi må bli flinkere på er å branche ut hver gang vi begynner på en issue
### Kommentarer
Vi har ikke valgt å kommentere og teste koden implementert i denne leveransen. Det er fordi vi har kun implementert grunnlaget for spillet. Når vi begynner med egne klasser, funksjoner og lignende vil vi kommentere og teste implementasjonen vår. 
