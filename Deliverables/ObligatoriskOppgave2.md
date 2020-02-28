# Oppgave 2

## Deloppgave 1: Prosjekt og prosjektstruktur

### Hvordan fungerer rollene i teamet?
-   Rollene våre fungerer utmerket så langt. 

### Trenger dere andre roller?
-   Vi har ikke skrevet så mange tester enda, så derfor skal Erik passe på litt ekstra på at tester blir laget.
    -   Teamlead: innebærer organisering av prosjektet og delegering av arbeid
    -   Kundekontakt: ha ansvar for ekstern kommunikasjon
    -   Utvikler: utvikle spillet
    -   Tester: skrive tester samt forsikre seg om at koden oppfører seg som forventet

### Er det noen erfaringer enten team-messig eller med tanke på prosjektmetodikk som er verdt å nevne? 
-   Kodesprint har funket bra for oss, men vi er dårlige på å tenke langsiktig, f.eks at issues vi har assigned hverandre på GitHub ender opp med å ikke være relevante fordi vi plutselig finner en bedre løsning.

### Hvordan er gruppedynamikken?
-   Dynamikken blant oss er som i et ordinært prosjekt. Ikke noe nevneverdig.

### Hvordan fungerer kommunikasjonen for dere?
-   Vi har litt dårlige i starten, men etterhvert har vi blitt bedre på å si ifra og dokumentere ting vi gjør på Slack og GitHub.
-   Vi har erfart at GitHub-boten vår på Slack har spammet ned kanalen veldig, og i velger derfor å opprete en egen kanal på Slack bare for GitHub integrasjon.
-   Dermed kan vi lettere få med oss hva hverandre skriver, men alikevel holde oss opdatert på prosjekt

### Gjør et kort retroperspektiv hvor dere vurderer hva dere har klart til nå, og hva som kan forbedres.
-   Vi har laget et godt fundament for videre utvikling via blant annet god kodestruktur, kommunikasjonskanaler og ikke minst et veldig strukturert og ryddig Project Board.
-   Vi har ikke kommet så langt i selve utvklingen av spillet, men vi tror ikke dette kommer til å bli noe problem med tanke på det gode grunnlaget vi har lagt.

### Bidrag til kodebasen
-   Bo har veldig mange linjer kode endret i sine commits, dette kommer av at han har gjort store strukturendringer.
-   Erik har ikke så mange commits enda, fordi han har hatt litt problemer med å sette opp Git og IDE


### Møtereferat
Vi har hatt ett møte siden sist, referatet ligger i Deliverables/Møtereferater/referat-21-02.md

### Tre forbedringspunkter fra retroperspektivet
-   Bruker mer tid på prosjektet individuellt.
-   Tenke mer langsiktig på issues og arbeidsoppgaver vi delegerer til prosjektmedlemmene.
  F.eks: etter forrige møte ble vi enige om forskjellige ting som skulle bli gjort.
  Mye av dette var dårlig gjennomtenkt, og vi fant ut at mange av arbeidsoppgavene var altfor omfattende eller ikke
  ga mening i lys av nyere commits/beslutninger i teamet.
-   Skrive mer tester, og skrive mer gjennomtenkt og ryddig kode 

## Deloppgave 2: krav

-   Vi har jobbet med brukerhistoriene som er spesifisert i ObligatoriskOppgave1.md, deloppgave 4

-   Andre krav vi har jobbet med frem til denne leveransen:
    -   **Meny**
        -   "Som bruker, ønsker jeg å bli presentert med en meny, der jeg kan starte spillet, eller gå ut av spillet."
            -   **Arbeidsoppgaver**
            -   Implementere play-knapp
            -   Implementere quit-knapp
    - **Spillogikk**
        -   "Som bruker, ønsker jeg at reglene for spillet er implementert på riktig måte":
            -   **Arbeirdsoppgaver**
            -   Implementere spillmekanikk etter spill-regler
            -   Implementere 'player-behaviour'
            -   Implementere UI for kort

### Forklar kort hvordan dere har prioritert oppgavene fremover
-   Det vi skal prioritere fremover er å jobbe med spillogikken

### Forklar kort hvilke hovedkrav dere anser som en del av MVP og hvorfor
-   Spillogikk prioriteres som et MVP fremover
-   Etter vi har fått på plass litt mer av koden til spillet, anser vi også Netcode som et MVP

### Oppdater hvilke krav dere har prioritert, hvor langt dere har kommet og hva dere har gjort siden forrige gang
-   Meny
    -   Er på plass, men trenger forbedring.
-   Funksjonelle program-kort
    -   Logikken er på plass. Vi må jobbe videre med textures.
-   Roterende rotators
    -   Fungerer i denne iterasjonen. Mekanikken bør omskrives.
-   Spillerbrikker
    -   Har kommet godt i gang, noe mekanikk er på plass. Trenger fortsatt support for flere spillere.
    
# Notater
Vi har skrevet en del tester, men mange av får ikke kjørt ettersom vi ikke har helt fått til å loade inn assets i testene enda. Dette skal vi fikse, men inntil videre har vi kommentert ut de aktuelle testene, slik at maven kan builde spillet.
