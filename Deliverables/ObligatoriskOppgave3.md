# Oppgave 3

## Deloppgave 1: Team og prosjekt

### Hvordan fungerer rollene i teamet?
-   Rollene våre fungerer utmerket så langt. 

### Er det noen erfaringer enten team-messig eller med tanke på prosjektmetodikk som er verdt å nevne? 
-   De siste ukene har vi gått over til en nettbasert utviklingsløsning. Dette har ført til annerledes, men også produktivt arbeid.
-   Vi synes fortsatt at arbeidsflyten fungerer godt, og har dermed ingenting på forbedringsagendaen.

### Gjør et kort retroperspektiv hvor dere vurderer hva dere har klart til nå, og hva som kan forbedres.
-   Det var en kjapp og brutal overgang til hjemmekontor for teamet, men møtene og prosjektmetodikken fungerer fortsatt godt.
-   Til nå har vi ingen forbedringspunkter å komme med.

### Forbedringspunkt(er) fra retroperspektivet
-   Bruke mer tid på prosjektet individuelt (enda viktigere nå som vi jobber hjemmefra).

## Deloppgave 2: Krav
### Prioriterte krav:
-   **Spill-mekanikk som stemmer overens med reglene til Robo Rally**
    -   _Brukerhistorier_
        -   "Som bruker ønsker jeg at spillets mekanismer overholder reglene til brettspillet Robo Rally"
            -   **Arbeidsoppgaver**
                -   Implementere brett-logikk
                -   Implementere robot-mekanikk
                -   Implementere programkort
                
-   **Robot som beveger seg på rikig måte**
    -   _Brukerhistorier_
        -   "Som bruker ønsker jeg at roboten min skal følge programkortene jeg gir den"
            -   **Arbeidsoppgaver**
                -   Implementere programkort
                -   Implementere sikkerhet for hvor roboten kan flytte seg
                -   Implementere HUD for brukeren slik at h*n kan velge programkort
        -   "Som bruker ønsker jeg at roboten skal følge reglene til brikkene på spillbrettet*
            -   **Arbeidsoppgaver**
                -   Implementere logikk og mekanikk for tiles
                -   Lage et brukbart spillebrett
                -   Lage en 'Asset Manager'
                
-   **Multiplayer**
    -   _Brukerhistorier_
        -   "Som bruker ønsker jeg å kunne hoste en server, for andre spiller som vil bli med"
            -   **Arbeidsoppgaver**
                -   Implementere en server thread
                -   Implementere en screen for hosting der IP'en vises
        -   "Som bruker ønsker å kunne koble meg opp til en host med en IP"
            -   **Arbeidsoppgaver**
                -   Implementere en client thread
                -   Implementere en screen for å bli med i et spill ved å skrive inn en IP

#### Bugs
Det finnes et par bugs når roboter treffer vegger, noen ganger ignorerer de veggene, andre ganger funker det som forventet. Dette fikses fortløpende.

### Forklar kort hvilke hovedkrav dere anser som en del av MVP
-   Vi bruker labels på issues, der MVP er en av disse. Vennligst ta en titt på Project Boardet vårt for å se dette.
