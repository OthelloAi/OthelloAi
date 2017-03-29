# Client Server REQUEST-RESPONSE
THIS DOCUMENT IS NOT COMPLETED YET

## Game types
* Tic-tac-toe
* Reversi

## Commands
* [wrong command](#wrong-command)
* [login](#login)
* [logout](#logout)
* [subscribe](#subscribe)
* [challenge](#challenge)
* [move](#move)
* [get](#get)
* [forfeit](#forfeit)
* [help](#help)

### Wrong Command
	request:
	<command> <arguments>
	
	response:
	ERR Unknown <command> argument: 'argument'

### Login
	request:
	login <player name>

successful login

	response:
	OK

failed login

	response:
	ERR Duplicate name exists

### Logout
	request:
	logout
	exit
	quit
	disconnect
	bye

	response:
	none

### Get
	request:
	get <gamelist | playerlist>

	response:
	<gamelist>:
	SVR GAMELIST ["Reversi", "Tic-tac-toe"]
	
	<playerlist>:
	SVR PLAYERLIST ["Player name one", "" ...]

### Subscribe
	subscribe <game> // wait for other player to join match

### Challenge
Challenge other player

    request:
	challenge "player" "game"

	response:
	OK

Other player accepted your challenge

	response:


Accept challenge from other player

    request:
	challenge accept CHALLANGENUMBER

	response:
	OK
	SVR GAME MATCH {PLAYERTOMOVE: "twee", GAMETYPE: "Tic-tac-toe", OPPONENT: "een"}
	SVR GAME YOURTURN {TURNMESSAGE: ""} // only if it is your turn

### Forfeit
	request:
	forfeit
	
Your client response

	response:
	OK
	SVR GAME LOSS {PLAYERONESCORE: "0", PLAYERTWOSCORE: "0", COMMENT: "Player forfeited match"}

Other client response

	response:
	SVR GAME WIN {PLAYERONESCORE: "0", PLAYERTWOSCORE: "0", COMMENT: "Player forfeited match"}

### Move
	request:
	move integer [1...game type field size]

#### responses:
##### made the move yourself
it is valid and you do not win or lose

    request:
    move integer

    response:
	 OK
	 SVR GAME MOVE {PLAYER: "name", MOVE: "number", DETAILS: ""}

it is NOT valid

    request:
    move integer

    response:


it is valid and you win

    request:
    move integer

    response:


it is valid and you lose while there was a previous move from other player

    request:
    move integer

    response:
    OK
    SVR GAME MOVE {PLAYER: "your player name", MOVE: "integer", DETAILS: ""}
    SVR GAME MOVE {PLAYER: "other player name", MOVE: "integer", DETAILS: ""} // previous made move by other player
    SVR GAME LOSS {PLAYERONESCORE: "1", PLAYERTWOSCORE: "-1", COMMENT: ""}

#### Other player made the move
    it is valid and it is your turn
