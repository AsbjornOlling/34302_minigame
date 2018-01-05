
# Minigame scratch noter

## Architecture

**Server:**

	* MinigameServer: 
		- main class, indeholder f.eks. et antal sessions
	* GameSession: 
		- et antal clientconnection objekter
		- spiller-navne, point-tabeller
		- en metode til at vælge et antal spil
	* ClientConnection: 
		- forbindelse til én spiller
		- kan lægges ind i en GameSession
	* Protocol: 
		- Sørger for at kommunikationen mellem server og client
		- Sender ACK og holder øje med det samme


**Client:**

	- MinigameClient:
		- main class, indeholder alle de andre objekter
		- GUI
	* GameHandler:
		- loads minigames
		- generelle metoder til at sende scores vha ServerConnection
	* MiniGame:
		- utility metoder som kan bruges af de små spil
		- *Navnet på et spil*-klasse:
			- nedarver fra MiniGame
	* ServerConnection:
		- metoder til at snakke med serveren
	
## Protocol:


*Fra klient*

SESSIONCONNECT
PNAME: dinmor
SESSIONID: ab78qz *eller None*
END

GAMECOMPLETE
PNAME: dinmor
GSCORE: *tal mellem 0 og 100*
END

*Fra server*

SESSIONJOINED
SESSIONID: ab78qz
GAMES: 1, 15, 2, 16
END

SCOREUPDATE
PNAME: dinmor
PSCORE: *akkumuleret score so far*
PNAME: dinfar
PSCORE: *akkumuleret score so far*
...
PNAME: dinbror
PSCORE: *akkumuleret score so far*
END
