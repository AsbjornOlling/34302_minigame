
# Minigame scratch noter

## TODO

**Server:**

	* System for GAMESTART packets (only receive from host, then broadcast to all players)


**Client:**

	* Send GAMESTART package
	* GAYMES



## Architecture

**Server:**

	* MinigameServer: 
		- main class, indeholder f.eks. et antal sessions
		- Loader wordlist til gamesessionID
		- Init sessions og allClients (Hashmaps)
		- constructs Listener
	* Listener
		- Implements Runnable and child of MinigameServer
		- Makes serversocket and clientsocket
		- constructs ClientS
	* Client
		- Implements Runnable and child of MinigameServer and extends PacketListener
		- Contains classes ClientIn and ClientOut
		- Call Mediator
		+ ClientIn
			- Implements Runnable
			- Read incoming packets
		+ ClientOut
			- Implementes Runnable
			- Sends packets to the client
	* PacketListener
		- Make sure that Mediator can see all the methodcall it can make, so mediator can compile
	* Mediator
		* Takes packets received in ClientIn
		* Sends it to PacketListener objects, based on a set of conditions
		+ InternalPacketListener
			* a class only used by mediator (hence: "Internal")
			* contains list of headers and sessionIDs to accept
	* Packet
		+ Created when packet received in Client class
		+ Parses packet, and checks for invalid packets
	* GameSession: 
		- Extends PacketListener
		- et antal clientconnection objekter
		- spiller-navne, point-tabeller
		- en metode til at vælge et antal spil

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
SESSIONSID: ab78qz 
GSCORE: *tal mellem 0 og 100*
END

GAMESTART
PNAME: dinmor
SESSIONID: pik slave snyd
END


*Fra server*

SESSIONJOINED
GAMES: 1, 15, 2, 16
SESSIONID: ab78qz
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

GAMESTART
END
