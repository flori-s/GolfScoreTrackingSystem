# Golf Score Tracking System

![Beschrijving van de afbeelding](https://i.postimg.cc/hGSvJ8b3/Scherm-afbeelding-2024-02-09-om-12-31-35.png)

## Project Overzicht

Het Golf Score Tracking System is een Java-toepassing ontworpen om golfers te helpen bijhouden van hun scores en prestaties in de loop van de tijd. Het biedt een gebruiksvriendelijke interface voor het invoeren en beheren van golfrondes, het bekijken van statistieken en het analyseren van trends in prestaties. Dit systeem heeft als doel het proces van scorekeeping te vereenvoudigen en waardevolle inzichten te bieden aan golfers die hun spel willen verbeteren.

## Installatie-instructies

Om het Golf Score Tracking System te installeren en uit te voeren, volgt u deze stappen:

### Vereisten

- Java Development Kit (JDK) versie 11 of hoger geïnstalleerd op uw systeem

### Stappen

1. Kloon de repository naar uw lokale machine:

	```bash
    git clone https://github.com/flori-s/GolfScoreTrackingSystem.git
2. Navigeer naar de projectdirectory:

	```bash
    cd golf-score-tracking-system

3. Compileer de Java-code:
	```bash
    javac -d out src/com/floris/golfscoretrackingsystem/*.java src/com/floris/golfscoretrackingsystem/classes/*.java src/com/floris/golfscoretrackingsystem/screens/*.java src/com/floris/golfscoretrackingsystem/utils/*.java

4. Voer de toepassing uit:
	```bash
    java -cp out com.floris.golfscoretrackingsystem.Application
5. Volg de instructies op het scherm om het Golf Score Tracking System te gebruiken.

## Configuratie

Het Golf Score Tracking System maakt gebruik van een online MySQL-database voor het opslaan van golfroundgegevens. Er is geen configuratie nodig voor het verbinden met de database, omdat de toepassing al is geconfigureerd om verbinding te maken met de online database. Je kunt de toepassing dus direct uitvoeren zonder verdere configuratie.

## Gebruiksaanwijzing

1. **Inloggen**: Bij het starten van de toepassing wordt u gevraagd om in te loggen met uw gebruikersnaam en wachtwoord.
2. **Startscherm**: Na het inloggen ziet u het startscherm met recente golfrounds. U kunt op een ronde klikken om de details te bekijken of bij te werken.
3. **Nieuwe Ronde Invoeren**: Klik op de knop "Add" en volg de instructies om course details, scores en weersomstandigheden in te voeren voor een nieuwe golfronde.
4. **Statistieken Bekijken**: Navigeer naar het statistieken scherm om verschillende tabellen en overzichten te bekijken op basis van uw golfroundgegevens.


## Roadmap / Tijdslijn

- Implementeer data-validatie om een nauwkeurige invoer van golfroundinformatie te garanderen.
- Voeg functionaliteit toe voor het exporteren en importeren van golfroundgegevens.
- Verbeter het statistieken scherm met gedetailleerdere inzichten en aanpasbare grafieken.
- Implementeer gebruikersauthenticatie en autorisatie om meerdere gebruikers met verschillende toegangsniveaus te ondersteunen.
- Integreer met externe API's om weersvoorspellingen voor golf rondes op te halen.

Voel je vrij om bij te dragen aan het project en extra functies of verbeteringen voor te stellen!
