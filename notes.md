
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

**On session create:**
	* Generate session code
	* 

