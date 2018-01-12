# My amasing TODO list

## Client

I Client.java har vi en class der hedder ClientOut
i klassen skal der addes så der konstant sendes dummypackets.
Disse dummypackets bruges kun til at se om connection er lost og derefter lukker socket og ændre shouldRun til false.
shouldRun bør gøres til en statisk boolean så den ændres i alle klasserne, når den ændres.
Der bør også implementeres så clienten kan sende en close connection pakke, og denne så kalder samme closeconnection metode.

## GameSession

Vil vi gerne have nogle korrekte gameslist sendt afsted, lige nu laver den noget blab med at convertere arraylist og det giver nogle sjove tegn
