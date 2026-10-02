# Software Engineering Fundamentals - GameNight Hub: Week 1 (oplossing)

## User stories

1. Als gebruiker wil ik mij kunnen registreren, zodat ik toegang krijg tot GameNight Hub.
2. Als gebruiker wil ik mij kunnen aanmelden, zodat ik mijn gegevens en spellen kan beheren.
3. Als gebruiker wil ik een spel aan mijn collectie kunnen toevoegen, zodat ik kan bijhouden welke spellen ik in huis heb.
4. Als gebruiker wil ik een spel uit mijn collectie kunnen verwijderen, zodat mijn collectie up-to-date blijft.
5. Als gebruiker wil ik een game-avond kunnen plannen met datum, locatie en een spel uit mijn collectie, zodat mijn vrienden weten wanneer en waar we spelen.
6. Als gebruiker wil ik deelnemers aan een game-avond kunnen toevoegen, zodat duidelijk is wie er meespeelt.
7. Als gebruiker wil ik na afloop de uitslag van een game-avond kunnen registreren, zodat de scores bewaard blijven.
8. Als gebruiker wil ik zien wie een game-avond gewonnen heeft, zodat de competitie leeft binnen de groep.
9. Als gebruiker wil ik een leaderboard kunnen bekijken, zodat ik zie wie over alle game-avonden heen het best presteert.
10. Als gebruiker wil ik mijn eigen speelgeschiedenis kunnen bekijken, zodat ik zie aan hoeveel sessies ik al deelnam.

## Use case diagram

```plantuml
@startuml
left to right direction
actor Gebruiker

usecase "Registreren" as UC1
usecase "Aanmelden" as UC2
usecase "Spel toevoegen" as UC3
usecase "Game-avond plannen" as UC4
usecase "Score registreren" as UC5
usecase "Leaderboard bekijken" as UC6

Gebruiker --> UC1
Gebruiker --> UC2
Gebruiker --> UC3
Gebruiker --> UC4
Gebruiker --> UC5
Gebruiker --> UC6

UC3 ..> UC2 : <<include>>
UC4 ..> UC2 : <<include>>
UC5 ..> UC2 : <<include>>
UC6 ..> UC2 : <<include>>
@enduml
```

## Class diagram

```plantuml
@startuml
class User {
  -id: UUID
  -naam: String
  -email: String
  -wachtwoordHash: String
  +registreer()
  +meldAan(wachtwoord: String): boolean
}

class Player {
  -bijnaam: String
  +berekenTotaalScore(): int
  +bekijkGeschiedenis(): List<Score>
}

class Game {
  -titel: String
  -minSpelers: int
  -maxSpelers: int
  -gemiddeldeSpeelduur: int
}

class Session {
  -datum: Date
  -locatie: String
  +voegSpelerToe(speler: Player)
  +registreerUitslag(speler: Player, punten: int, gewonnen: boolean)
}

class Score {
  -punten: int
  -gewonnen: boolean
}

User "1" -- "1" Player : heeft profiel >
Game "1" o-- "0..*" Session : wordt gespeeld in >
Session "1" *-- "2..*" Score : registreert >
Player "1" -- "0..*" Score : behaalt >
@enduml
```

## Sequence diagram

```plantuml
@startuml
actor Gebruiker
participant "Session" as S
participant "Game" as G
participant "Player" as P
participant "Score" as Sc

Gebruiker -> S : nieuweSessie(spel, datum, locatie)
S -> G : haalDetails()
G --> S : spelinfo

loop voor elke deelnemer
    Gebruiker -> S : voegSpelerToe(speler)
    S -> P : registreerDeelname()
end

... game-avond vindt plaats ...

loop voor elke deelnemer
    Gebruiker -> S : registreerUitslag(speler, punten, gewonnen)
    S -> Sc : nieuw(speler, sessie, punten, gewonnen)
end

S --> Gebruiker : bevestiging
@enduml
```