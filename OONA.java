import java.io.*; 
import java.util.*; 
import java.net.*; 
  
public class OONA  { 
    public static void main(String[] args) throws Exception  {
	int numPlayers = 0;

	//Gets keyboard input from the user
	Scanner keyboard = new Scanner(System.in);
	System.out.println("Welcome to OONA! Are you starting a new game or joining a game? (Please enter 'starting' or 'joining')");
	String specify = keyboard.nextLine();
	while (!(specify.equals("starting")) && !(specify.equals("joining"))) {
		System.out.println("Invalid input: please specify 'starting' or 'joining'!");
		specify = keyboard.nextLine();
	}
	if (specify.equals("starting")) {
		System.out.println("How many players will there be in total? You can choose between 2 and 4.");
	} else if (specify.equals("joining")) {
		System.out.println("How many players do you expect there to be total?");
	}

	String numPlay = keyboard.nextLine();
	while (!(numPlay.equals("2")) && !(numPlay.equals("3")) && !(numPlay.equals("4"))) {
		System.out.println("Invalid input: please choose a number between 2 and 4: ");
		numPlay = keyboard.nextLine();
	}

	if (numPlay.equals("2")) numPlayers = 2;
	else if (numPlay.equals("3")) numPlayers = 3;
	else if (numPlay.equals("4")) numPlayers = 4;

	if (numPlayers == 2 && specify.equals("starting")) {
		ArrayList<Player> players = new ArrayList<>();
		System.out.println("Welcome to OONA! What is your player name?");
		String player1 = keyboard.nextLine();
                System.out.println();
                players.add(new Player(player1));

                ServerSocket server = new ServerSocket(3000);
                System.out.println("Waiting for a second player...\n");

                Socket pipe = server.accept();
                System.out.println("Player connected!");

		ObjectInputStream serverInputStream = new ObjectInputStream(pipe.getInputStream());

                ObjectOutputStream serverOutputStream = new ObjectOutputStream(pipe.getOutputStream());

                String player2 = (String)serverInputStream.readObject();
                players.add(new Player(player2));
                Deck gamePiles = new Deck();

                serverOutputStream.writeObject(players);

		for (int i = 0; i < 7; i ++) {
                        players.get(0).giveCard(gamePiles.draw());
                }

                for (int i = 0; i < 7; i++) {
                        serverOutputStream.writeObject(gamePiles.draw());
                }

                System.out.println("Welcome, " + players.get(0).name + " and " + players.get(1).name + ", let's play OONA!\n");
		System.out.println("Remember, this is not UNO. The way to win OONA is by playing the UNO the way everybody ACTUALLY plays, not by the point system.\nYou go around in a clockwise fashion, laying down a card that matches either the number or the color of the one on top of the discard pile. If you don't have any valid cards, you automatically have to draw another. You will have the choice to put down the newly drawn card if it is valid. If not, it will simply get added to your hand. The first player to run out of cards is the winner!\n");

                gamePiles.discardCard(gamePiles.draw());

                Color firstColor = gamePiles.topDiscardCard().getColor();
                serverOutputStream.writeObject(firstColor);

                Number firstNumber = gamePiles.topDiscardCard().getNumber();
                serverOutputStream.writeObject(firstNumber);
		if (firstNumber == Number.DRAW2) {
                        System.out.println("First card is DRAW2! " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                        for (int i = 0; i < 2; i++) {
                                serverOutputStream.writeObject(gamePiles.draw());
                        }
                } else if (firstNumber == Number.DRAW4) {
                        System.out.println("First card is DRAW4! " + players.get(1).name + " draws 4 cards and forfeits their turn.\n");
                        for (int i = 0; i < 4; i++) {
                                serverOutputStream.writeObject(gamePiles.draw());
                        }
                } else if (firstNumber == Number.SKIP) {
                        System.out.println("First card is SKIP! " + players.get(1).name+ " forfeits their turn.\n");
                } else if (firstNumber == Number.REVERSE) {
                        System.out.println("First card is REVERSE! Reverse direction.\n");
                }
		if (firstColor == Color.WILD) {
                        System.out.println("First card is a wild card! " + players.get(1).name + " must pick a color.");
                        firstColor = (Color)serverInputStream.readObject();
                        System.out.println("They chose: " + firstColor);
                }

                GameState gameState = new GameState(players, firstColor, firstNumber);
                Player player = gameState.getTurn();

                Boolean hasWinner = false;
                String winner = "none";
                while (!hasWinner) {
                        if (player == players.get(0)) {
                                System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                Number num = Number.FIVE;
                                Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                if (playedCard == null) {
                                        playedCard = player.playAfterDraw(gamePiles.draw(), gameState.getColor(), gameState.getNumber(), keyboard);
                                }
                                serverOutputStream.writeObject(playedCard);
                                if (playedCard != null) {
                                        gamePiles.discardCard(playedCard);
                                        player.removeCard(playedCard);
                                        if (playedCard.getColor() == Color.WILD) {
                                                Color newcolor = player.chooseColor(keyboard);
                                                player = gameState.update(newcolor, playedCard.getNumber());
                                        } else {
                                                player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                        }
                                } else if (playedCard == null) {
                                        player = gameState.update(null, null);
                                        num = null;
                                }
                                Color col = gameState.getColor();
                                serverOutputStream.writeObject(col);
                                if (num == null) {
                                        num = null;
                                } else {
                                        num = gameState.getNumber();
                                }
                                serverOutputStream.writeObject(num);
                                if (num == Number.DRAW2) {
                                        for (int i = 0; i < 2; i++) {
                                                serverOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4) {
                                        for (int i = 0; i < 4; i++) {
                                                serverOutputStream.writeObject(gamePiles.draw());
                                        }
                                }
                        } else {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)serverInputStream.readObject();
                                if (playedCard == null) {
                                        serverOutputStream.writeObject(gamePiles.draw());
                                }
                                playedCard = (Card)serverInputStream.readObject();

                                if (playedCard != null) {
                                        gamePiles.discardCard((Card)serverInputStream.readObject());
                                }

                                Color col = (Color)serverInputStream.readObject();
                                Number num = (Number)serverInputStream.readObject();
                                if (playedCard != null) {
                                        if (playedCard.getColor() == Color.WILD) {
                                                System.out.println("Card played was wild. " + player.name + " chose " + col + "\n");
                                        }
                                }
                                if (num == Number.DRAW2) {
                                        System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                                }
                                } else if (num == Number.DRAW4) {
                                        System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                                }
                                } else if (num == Number.SKIP) {
                                        System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                } else if (num == Number.REVERSE) {
                                        System.out.println("Card played was REVERSE! Reverse direction.\n");
                                }

                                player = gameState.update(col, num);
                        } int player1size = (int)serverInputStream.readObject();
                        if (players.get(0).hand.size() == 0) {
                                hasWinner = true;
                                winner = players.get(0).name;
                        } else if (player1size == 0) {
                                hasWinner = true;
                                winner = players.get(1).name;
                        }

                        serverOutputStream.writeObject(hasWinner);
                        serverOutputStream.writeObject(winner);
                }
                if (winner.equals(players.get(0).name)) {
                        System.out.println("\nCongratulations! You have gained the prestigious title of OONA winner, " + winner + "!");
                } else if (winner.equals(players.get(1).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + players.get(1).name + ".");
                }
                serverOutputStream.close();
                serverInputStream.close();

	} else if (numPlayers == 2 && specify.equals("joining")) {
		System.out.println("Welcome to OONA! What is your player name?");
                String player2 = keyboard.nextLine();

                System.out.println("\nPerfect! What is the IP address of the server you want to play on?");
                String address = keyboard.nextLine();
                System.out.println();

                Socket socket = new Socket (address, 3000);
                System.out.println("Successfully connected to the server!");

                ObjectOutputStream clientOutputStream = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream clientInputStream = new ObjectInputStream(socket.getInputStream());


                clientOutputStream.writeObject(player2);

                @SuppressWarnings("unchecked")
                ArrayList<Player> players = (ArrayList)clientInputStream.readObject();

                for (int i = 0; i < 7; i++) {
                        Card card = (Card)clientInputStream.readObject();
                        players.get(1).giveCard(card);
                }
		System.out.println("Welcome, " + players.get(0).name + ", " + players.get(1).name + ", let's play OONA!\n");

                System.out.println("Remember, this is not UNO. The way to win OONA is by playing the UNO the way everybody ACTUALLY plays, not by the point system.\nYou go around in a clockwise fashion, laying down a card that matches either the number or the color of the one on top of the discard pile. If you don't have any valid cards, you automatically have to draw another. You will have the choice to put down the newly drawn card if it is valid. If not, it will simply get added to your hand. The first player to run out of cards is the winner!\n");

                Color firstColor = (Color)clientInputStream.readObject();
                Number firstNumber = (Number)clientInputStream.readObject();

                if (firstNumber == Number.DRAW2) {
                        System.out.println("First card is DRAW2! Draw 2 cards and forfeit your turn.\n");
                        for (int i = 0; i < 2; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(1).giveCard(card);
                        }
                } else if (firstNumber == Number.DRAW4) {
                        System.out.println("First card is DRAW4! Draw 4 cards and forfeit your turn.\n");
                        for (int i = 0; i < 4; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(1).giveCard(card);
                        }
                } else if (firstNumber == Number.SKIP) {
                        System.out.println("First card is SKIP! forfeit your turn.\n");
                } else if (firstNumber == Number.REVERSE) {
                        System.out.println("First card is REVERSE! Reverse direction.\n");
                }

                if (firstColor == Color.WILD) {
                        firstColor = players.get(1).chooseColor(keyboard);
                        clientOutputStream.writeObject(firstColor);
                }

                GameState gameState = new GameState(players, firstColor, firstNumber);
                Player player = gameState.getTurn();

                Boolean hasWinner = false;
                String winner = "none";
		while(!hasWinner) {
                        if (player == players.get(1)) {
                                System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                Number num = Number.FIVE;
                                Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                clientOutputStream.writeObject(playedCard);

                                if (playedCard == null) {
                                        playedCard = player.playAfterDraw((Card)clientInputStream.readObject(), gameState.getColor(), gameState.getNumber(), keyboard);
                                }
                                clientOutputStream.writeObject(playedCard);

                                if (playedCard != null) {
                                        clientOutputStream.writeObject(playedCard);
                                        player.removeCard(playedCard);
                                        if (playedCard.getColor() == Color.WILD) {
                                                Color newcolor = player.chooseColor(keyboard);
                                                player = gameState.update(newcolor, playedCard.getNumber());
                                        } else {
                                                player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                        }
                                } else if (playedCard == null) {
                                        player = gameState.update(null, null);
                                        num = null;
                                }

                                Color col = gameState.getColor();
                                clientOutputStream.writeObject(col);
                                if (num == null) {
                                        num = null;
                                } else {
                                        num = gameState.getNumber();
                                }
                                clientOutputStream.writeObject(num);
                        } else {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)clientInputStream.readObject();
                                Color col = (Color)clientInputStream.readObject();
                                Number num = (Number)clientInputStream.readObject();
                                if (playedCard != null) {
                                        if (playedCard.getColor() == Color.WILD) {
                                                System.out.println("Card played was wild. " + player.name + " chose " + col + "\n");
                                        }
                                }
                                if (num == Number.DRAW2) {
                                        System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                Card card = (Card)clientInputStream.readObject();
                                                players.get(1).giveCard(card);
                                        }
                                } else if (num == Number.DRAW4) {
                                        System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                Card card = (Card)clientInputStream.readObject();
                                                players.get(1).giveCard(card);
                                        }
                                } else if (num == Number.SKIP) {
                                        System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                } else if (num == Number.REVERSE) {
                                        System.out.println("Card played was REVERSE! Reverse direction.\n");
                                }
                                player = gameState.update(col, num);
                        }
                        clientOutputStream.writeObject(players.get(1).hand.size());
                        hasWinner = (Boolean)clientInputStream.readObject();
                        winner = (String)clientInputStream.readObject();
                }
                if (winner.equals(players.get(1).name)) {
                        System.out.println("\nCongratulations! You have gained the prestigious title of OONA winner, " + winner + "!");
                } else if (winner.equals(players.get(0).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + winner + ".");
                }

                clientOutputStream.close();
                clientInputStream.close();
	}

	if (numPlayers == 3 && specify.equals("starting")) {
		ArrayList<Player> players = new ArrayList<>();

                System.out.println("Welcome to OONA! What is your player name?");
                String player0 = keyboard.nextLine();
                System.out.println();
                players.add(new Player(player0));

                ServerSocket server = new ServerSocket(3000);
                System.out.println("Waiting for a second player...\n");

                Socket first = server.accept();
                System.out.println("Player connected!");

                ObjectInputStream firstInputStream = new ObjectInputStream(first.getInputStream());

                ObjectOutputStream firstOutputStream = new ObjectOutputStream(first.getOutputStream());

                System.out.println("Waiting for a third player...\n");
                Socket second = server.accept();
                System.out.println("Player connected!");

                ObjectInputStream secondInputStream = new ObjectInputStream(second.getInputStream());

                ObjectOutputStream secondOutputStream = new ObjectOutputStream(second.getOutputStream());

                String player1 = (String)firstInputStream.readObject();
                players.add(new Player(player1));

                String player2 = (String)secondInputStream.readObject();
                players.add(new Player(player2));

                Deck gamePiles = new Deck();
                firstOutputStream.writeObject(players);
                secondOutputStream.writeObject(players);
			
		for (int i = 0; i < 7; i ++) {
                        players.get(0).giveCard(gamePiles.draw());
                }

                for (int i = 0; i < 7; i++) {
                        firstOutputStream.writeObject(gamePiles.draw());
                }

                for (int i = 0; i < 7; i++) {
                        secondOutputStream.writeObject(gamePiles.draw());
                }

                System.out.println("Welcome, " + players.get(0).name + ", " + players.get(1).name + ", and " + players.get(2).name + ", let's play OONA!\n");

                System.out.println("Remember, this is not UNO. The way to win OONA is by playing the UNO the way everybody ACTUALLY plays, not by the point system.\nYou go around in a clockwise fashion, laying down a card that matches either the number or the color of the one on top of the discard pile. If you don't have any valid cards, you automatically have to draw another. You will have the choice to put down the newly drawn card if it is valid. If not, it will simply get added to your hand. The first player to run out of cards is the winner!\n");

                gamePiles.discardCard(gamePiles.draw());

                Color firstColor = gamePiles.topDiscardCard().getColor();
                firstOutputStream.writeObject(firstColor);
                secondOutputStream.writeObject(firstColor);

                Number firstNumber = gamePiles.topDiscardCard().getNumber();
                firstOutputStream.writeObject(firstNumber);
                secondOutputStream.writeObject(firstNumber);

                if (firstNumber == Number.DRAW2) {
                        System.out.println("First card is DRAW2! " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                        for (int i = 0; i < 2; i++) {
                                firstOutputStream.writeObject(gamePiles.draw());
                        }
                } else if (firstNumber == Number.DRAW4) {
                        System.out.println("First card is DRAW4! " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                        for (int i = 0; i < 4; i++) {
                                firstOutputStream.writeObject(gamePiles.draw());
                        }
                } else if (firstNumber == Number.REVERSE) {
                        System.out.println("First card is reverse! Reverse direction.\n");
                } else if (firstNumber == Number.SKIP) {
                        System.out.println("First card is skip! " + players.get(1).name + " forfeits their turn.\n");
                }
		if (firstColor == Color.WILD) {
                        System.out.println("First card is a wild card! " + players.get(1).name + " must pick a color.");
                        firstColor = (Color)firstInputStream.readObject();
                        System.out.println("They chose: " + firstColor);
                        firstOutputStream.writeObject(firstColor);
                        secondOutputStream.writeObject(firstColor);
                }
                GameState gameState = new GameState(players, firstColor, firstNumber);
                Player player = gameState.getTurn();

                Boolean hasWinner = false;
                String winner = "none";

		while (!hasWinner) {
                        if (player == players.get(0)) {
                                System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                Number num = Number.FIVE;
                                Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                if (playedCard == null) {
                                        playedCard = player.playAfterDraw(gamePiles.draw(), gameState.getColor(), gameState.getNumber(), keyboard);
                                }
                                firstOutputStream.writeObject(playedCard);
                                secondOutputStream.writeObject(playedCard);

                                if (playedCard != null) {
                                        gamePiles.discardCard(playedCard);
                                        player.removeCard(playedCard);
                                        if (playedCard.getColor() == Color.WILD) {
                                                Color newcolor = player.chooseColor(keyboard);
                                                player = gameState.update(newcolor, playedCard.getNumber());
                                        } else {
                                                player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                      }
                                } else if (playedCard == null) {
                                        player = gameState.update(null, null);
                                        num = null;
                                }
                                Color col = gameState.getColor();
                                firstOutputStream.writeObject(col);
                                secondOutputStream.writeObject(col);

                                if (num == null) {
                                        num = null;
                                } else {
                                        num = gameState.getNumber();
                                }
                                firstOutputStream.writeObject(num);
                                secondOutputStream.writeObject(num);

                                if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                        for (int i = 0; i < 2; i++) {
                                                firstOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                        for (int i = 0; i < 4; i++) {
                                                firstOutputStream.writeObject(gamePiles.draw());
                                        }
                                }
				if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                        for (int i = 0; i < 2; i++) {
                                                secondOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                        for (int i = 0; i < 4; i++) {
                                                secondOutputStream.writeObject(gamePiles.draw());
                                        }
                                }
                        } else if (player == players.get(1)) {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)firstInputStream.readObject();
                                if (playedCard == null) {
                                        firstOutputStream.writeObject(gamePiles.draw());
                                }
                                playedCard = (Card)firstInputStream.readObject();
                                secondOutputStream.writeObject(playedCard);

                                if (playedCard != null) {
                                        gamePiles.discardCard(playedCard);
                                }
                                Color col = (Color)firstInputStream.readObject();
                                Number num = (Number)firstInputStream.readObject();
                                secondOutputStream.writeObject(col);
                                secondOutputStream.writeObject(num);
				if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                        System.out.println(player.name + " played DRAW2. " + players.get(2).name + " draws 2 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                secondOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                        System.out.println(player.name + " played DRAW4. " + players.get(2).name + " draws 4 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                secondOutputStream.writeObject(gamePiles.draw());
                                                }
                                } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                        System.out.println("Card played was SKIP. " + players.get(2).name + " forfeits their turn.\n");
                                } else if (num == Number.REVERSE) {
                                        System.out.println("Card played was REVERSE! Reverse direction.\n");
                                }
                                if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                        System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                        System.out.println("Card played was DRAW4! Draw 4cards and forfeit your turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                        System.out.println("Card played was SKIP. Forfeit your turn.\n");
                                }
                                player = gameState.update(col, num);
                        } else if (player == players.get(2)) {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)secondInputStream.readObject();
                                if (playedCard == null) {
                                        secondOutputStream.writeObject(gamePiles.draw());
                                }
                                playedCard = (Card)secondInputStream.readObject();
                                firstOutputStream.writeObject(playedCard);

                                Color col = (Color)secondInputStream.readObject();
                                Number num = (Number)secondInputStream.readObject();

                                firstOutputStream.writeObject(col);
                                firstOutputStream.writeObject(num);

                                if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                        System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                        System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                        System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                } else if (num == Number.REVERSE) {
                                        System.out.println("Card played was REVERSE! Reverse direction.\n");
                                }
                                if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                        System.out.println("Card played was DRAW2! " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                firstOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                        System.out.println("Card played was DRAW4! " + players.get(1).name + " draws 4 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                firstOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                        System.out.println("Card played was SKIP! " + players.get(1).name + " forfeits their turn.\n");
                                }
                                player = gameState.update(col, num);
                        }
                        int player1size = (int)firstInputStream.readObject();
                        int player2size = (int)secondInputStream.readObject();
                        if (players.get(0).hand.size() == 0) {
                                hasWinner = true;
                                winner = players.get(0).name;
                        } else if (player1size == 0) {
                                hasWinner = true;
                                winner = players.get(1).name;
                        } else if (player2size == 0) {
                                hasWinner = true;
                                winner = players.get(2).name;
                        }
                        firstOutputStream.writeObject(hasWinner);
                        secondOutputStream.writeObject(hasWinner);
                        firstOutputStream.writeObject(winner);
                        secondOutputStream.writeObject(winner);
                }
                if (winner.equals(players.get(0).name)) {
                        System.out.println("\nCongratulations! You have gained the prestigious title of OONA winner, " + winner + "!");
                } else if (winner.equals(players.get(1).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + players.get(1).name + ".");
                } else if (winner.equals(players.get(2).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + players.get(1).name + ".");
                }

                secondOutputStream.close();
                secondInputStream.close();
                firstOutputStream.close();
                firstInputStream.close();

	} else if (numPlayers == 3 && specify.equals("joining")) {
		
		System.out.println("Welcome to OONA! What is your player name?");
                String myName = keyboard.nextLine();

                System.out.println("\nPerfect! What is the IP address of the server you want to play on?");
                String address = keyboard.nextLine();
                System.out.println();

                Socket socket = new Socket (address, 3000);
                System.out.println("Successfully connected to the server!");

                ObjectOutputStream clientOutputStream = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream clientInputStream = new ObjectInputStream(socket.getInputStream());

                clientOutputStream.writeObject(myName);

                @SuppressWarnings("unchecked")
                ArrayList<Player> players = (ArrayList)clientInputStream.readObject();
                if (myName.equals(players.get(1).name)) {
                        for (int i = 0; i < 7; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(1).giveCard(card);
                        }
                } else if (myName.equals(players.get(2).name)) {
                        for (int i = 0; i < 7; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(2).giveCard(card);
                        }
                }
                System.out.println("Welcome, " + players.get(0).name + ", " + players.get(1).name + ", and " + players.get(2).name + ", let's play OONA!\n");

                System.out.println("Remember, this is not UNO. The way to win OONA is by playing the UNO the way everybody ACTUALLY plays, not by the point system.\nYou go around in a clockwise fashion, laying down a card that matches either the number or the color of the one on top of the discard pile. If you don't have any valid cards, you automatically have to draw another. You will have the choice to put down the newly drawn card if it is valid. If not, it will simply get added to your hand. The first player to run out of cards is the winner!\n");

                Color firstColor = (Color)clientInputStream.readObject();
                Number firstNumber = (Number)clientInputStream.readObject();

		if (firstNumber == Number.DRAW2) {
                        if (myName.equals(players.get(1).name)) {
                                System.out.println("First card is DRAW2! Draw 2 cards and forfeit your turn.\n");
                                for (int i = 0; i < 2; i++) {
                                        Card card = (Card)clientInputStream.readObject();
                                        players.get(1).giveCard(card);
                                }
                        } else if (myName.equals(players.get(2).name)) {
                                System.out.println("First card is DRAW2! " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                        }
                } else if (firstNumber == Number. DRAW4) {
                        if (myName.equals(players.get(1).name)) {
                                System.out.println("First card is DRAW4! Draw 4 cards and forfeit your turn.\n");
                                for (int i = 0; i < 4; i++) {
                                        Card card = (Card)clientInputStream.readObject();
                                        players.get(1).giveCard(card);
                                }
                        } else if (myName.equals(players.get(2).name)) {
                                System.out.println("First card is DRAW4! " + players.get(1).name + " draws 4 cards and forfeits their turn.\n");
                                }
                } else if (firstNumber == Number.SKIP){
                        if (myName.equals(players.get(1).name)) {
                                System.out.println("First card is SKIP! Forfeit your turn.\n");
                        } else if (myName.equals(players.get(2).name)) {
                                System.out.println("First card is SKIP! " + players.get(1).name + " forfeits their turn.\n");
                        }
                } else if (firstNumber == Number.REVERSE) {
                        System.out.println("First card is REVERSE! Reverse direction.\n");
                }

                if (firstColor == Color.WILD) {
                        if (myName.equals(players.get(1).name)) {
                                firstColor = players.get(1).chooseColor(keyboard);
                                clientOutputStream.writeObject(firstColor);
                                firstColor = (Color)clientInputStream.readObject();
                        } else if (myName.equals(players.get(2).name)) {
                                System.out.println("First card is a wild card! " + players.get(1).name + " must pick a color.");
                                firstColor = (Color)clientInputStream.readObject();
                                System.out.println("They chose: " + firstColor);
                        }
                }

                GameState gameState = new GameState(players, firstColor, firstNumber);
                Player player = gameState.getTurn();

		Boolean hasWinner = false;
                String winner = "none";

                while(!hasWinner) {
                        if (player == players.get(0)) {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)clientInputStream.readObject();
                                Color col = (Color)clientInputStream.readObject();
                                Number num = (Number)clientInputStream.readObject();
                                if (playedCard != null) {
                                        if (playedCard.getColor() == Color.WILD) {
                                                System.out.println("Card played was wild. " + player.name + " chose " + col + "\n");
                                        }
                                }
                                if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                        if (myName.equals(players.get(1).name)) {
                                                System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 2; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(1).giveCard(card);
                                                }
                                        } else if (myName.equals(players.get(2).name)) {
                                                System.out.println("Card played was DRAW2. " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                        if (myName.equals(players.get(1).name)) {
                                                System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 4; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(1).giveCard(card);
                                                }
                                        } else if (myName.equals(players.get(2).name)) {
                                                System.out.println("Card played was DRAW4. " + players.get(1).name + " draws 4 cards and forfeits their turn.\n");
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                        if (myName.equals(players.get(1).name)) {
                                                System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                        } else if (myName.equals(players.get(2).name)) {
                                                System.out.println("Card played was SKIP. " + players.get(1).name + " forfeits their turn.\n");
                                        }
                                } else if (num == Number.REVERSE) {
                                        System.out.println("Card played was REVERSE! Reverse direction.\n");
                                }
                                if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                        if (myName.equals(players.get(2).name)) {
                                                System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 2; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(2).giveCard(card);
                                                }
                                        } else if (myName.equals(players.get(1).name)) {
                                                System.out.println("Card played was DRAW2. " + players.get(2).name + " draws 2 cards and forfeits their turn.\n");
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                        if (myName.equals(players.get(2).name)) {
                                                System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 4; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(2).giveCard(card);
                                                }
                                        } else if (myName.equals(players.get(1).name)) {
                                                System.out.println("Card played was DRAW4. " + players.get(2).name + " draws 2 cards and forfeits their turn.\n");
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                        if (myName.equals(players.get(2).name)) {
                                                System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                        } else if (myName.equals(players.get(1).name)) {
                                                System.out.println("Card played was SKIP! " + players.get(2).name + " forfeits their turn.\n");
                                        }
                                }
                                player = gameState.update(col, num);
                        } else if (player == players.get(1)) {
                                if (myName.equals(players.get(1).name)) {
                                        System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                        Number num = Number.FIVE;
                                        Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                        clientOutputStream.writeObject(playedCard);
                                        if (playedCard == null) {
                                                Card card = (Card)clientInputStream.readObject();
                                                playedCard = player.playAfterDraw(card, gameState.getColor(), gameState.getNumber(), keyboard);
                                        }
                                        clientOutputStream.writeObject(playedCard);
                                        if (playedCard != null) {
                                                player.removeCard(playedCard);
                                                if (playedCard.getColor() == Color.WILD) {
                                                        Color newcolor = player.chooseColor(keyboard);
                                                        player = gameState.update(newcolor, playedCard.getNumber());
                                                } else {
                                                        player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                                }
                                        } else if (playedCard == null) {
                                                player = gameState.update(null, null);
                                                num = null;
                                        }
                                        Color col = gameState.getColor();
                                        clientOutputStream.writeObject(col);
                                        if (num == null) {
                                                num = null;
                                        } else {
                                                num = gameState.getNumber();
                                        }
                                        clientOutputStream.writeObject(num);
                                } else {
                                        System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                        Card playedCard = (Card)clientInputStream.readObject();
                                        Color col = (Color)clientInputStream.readObject();
                                        Number num = (Number)clientInputStream.readObject();
                                        if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                                System.out.println(player.name + " played DRAW2. Draw 2 cards and forfeit your turn.\n");for (int i = 0; i < 2;i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(2).giveCard(card);
                                                }
                                        } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                                System.out.println(player.name + " played DRAW4. Draw 4 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 4; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(2).giveCard(card);
                                                }
                                        } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                                System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                        } else if (num == Number.REVERSE) {
                                                System.out.println("Card played was REVERSE! Reverse direction.\n");
                                        } else if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                                System.out.println("Card played was DRAW2! " + players.get(0).name + " draws 2 cards and forfeits their turn.\n");
                                        } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                                System.out.println("Card played was DRAW4! " + players.get(0).name + " draws 2 cards and forfeits their turn.\n");
                                        } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                                System.out.println("Card played was SKIP! " + players.get(0).name + " forfeits their turn.\n");
                                        }
                                        player = gameState.update(col, num);
                                }
                        }else if (player == players.get(2)) {
                                if (myName.equals(players.get(2).name)) {
                                        System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                        Number num = Number.FIVE;
                                        Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                        clientOutputStream.writeObject(playedCard);
                                        if (playedCard == null) {
                                                playedCard = player.playAfterDraw((Card)clientInputStream.readObject(), gameState.getColor(), gameState.getNumber(), keyboard);
                                }
                                        clientOutputStream.writeObject(playedCard);
                                        if (playedCard != null) {
                                                player.removeCard(playedCard);
                                                if (playedCard.getColor() == Color.WILD) {
                                                        Color newcolor = player.chooseColor(keyboard);
                                                        player = gameState.update(newcolor, playedCard.getNumber());
                                                } else {
                                                        player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                                }
                                        } else if (playedCard == null) {
                                                player = gameState.update(null, null);
                                                num = null;
                                        }
                                        Color col = gameState.getColor();
                                        clientOutputStream.writeObject(col);
                                        if (num == null) {
                                                num = null;
                                        } else {
                                                num = gameState.getNumber();
                                        }
                                        clientOutputStream.writeObject(num);
                                } else if (myName.equals(players.get(1).name)) {
                                        System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                        Card playedCard = (Card)clientInputStream.readObject();
                                        Color col = (Color)clientInputStream.readObject();
                                        Number num = (Number)clientInputStream.readObject();
                                        if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                                System.out.println("Card played was DRAW2! " + players.get(0).name + " draws 2 cards and forfeits their turn.\n");
                                        } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                                System.out.println("Card played was DRAW4! " + players.get(0).name + " draws 4 cards and forfeits their turn.\n"); 
					} else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                                System.out.println("Card played was SKIP! " + players.get(0).name + " forfeits their turn.\n");
                                        } else if (num == Number.REVERSE) {
                                                System.out.println("Card played was REVERSE! Reverse direction.\n");
                                        }
                                        if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                                System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 2; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(1).giveCard(card);
                                                }
                                        } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                                System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 4; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(1).giveCard(card);
                                                }
                                        } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                                System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                        }
                                        player = gameState.update(col, num);
                                }
                        } if (myName.equals(players.get(1).name)) {
                                clientOutputStream.writeObject(players.get(1).hand.size());
                        } else if (myName.equals(players.get(2).name)) {
                                clientOutputStream.writeObject(players.get(2).hand.size());
                        }
                        hasWinner = (Boolean)clientInputStream.readObject();
                        winner = (String)clientInputStream.readObject();
                }
                if (winner.equals(players.get(1).name)) {
                        if (myName.equals(players.get(1).name)) {
                                System.out.println("\nCongratulations! You have gained the prestigious title of OONA winner, " + winner + "!");
                        } else {
                                System.out.println("\nSorry, you suck. The winner is " + winner + ".");
                        }
                } else if (winner.equals(players.get(0).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + winner + ".");
                } else if (winner.equals(players.get(2).name)) {
                        if (myName.equals(players.get(1).name)) {
                                System.out.println("\nCongratulations! You have gained the prestigious title of OONA winner, " + winner + "!");
                        } else {
                                System.out.println("\nSorry, you suck. The winner is " + winner + ".");
                        }
                }
                clientOutputStream.close();
                clientInputStream.close();	
	}
	
	if (numPlayers == 4 && specify.equals("starting")) {
		ArrayList<Player> players = new ArrayList<>();

		System.out.println("Welcome to OONA! What is your player name?");
                String player0 = keyboard.nextLine();
                System.out.println();
                players.add(new Player(player0));
	
		ServerSocket server = new ServerSocket(3000);
                System.out.println("Waiting for a second player...\n");

                Socket first = server.accept();
                System.out.println("Player connected!");

                ObjectInputStream firstInputStream = new ObjectInputStream(first.getInputStream());
                ObjectOutputStream firstOutputStream = new ObjectOutputStream(first.getOutputStream());

                System.out.println("Waiting for a third player...\n");
                Socket second = server.accept();
                System.out.println("Player connected!");

                ObjectInputStream secondInputStream = new ObjectInputStream(second.getInputStream());
                ObjectOutputStream secondOutputStream = new ObjectOutputStream(second.getOutputStream());

                System.out.println("Waiting for a fourth player...\n");
                Socket third = server.accept();
                System.out.println("Player connected!");

                ObjectInputStream thirdInputStream = new ObjectInputStream(third.getInputStream());
                ObjectOutputStream thirdOutputStream = new ObjectOutputStream(third.getOutputStream());

                String player1 = (String)firstInputStream.readObject();
                players.add(new Player(player1));

                String player2 = (String)secondInputStream.readObject();
                players.add(new Player(player2));

                String player3 = (String)thirdInputStream.readObject();
                players.add(new Player(player3));

                Deck gamePiles = new Deck();
                firstOutputStream.writeObject(players);
                secondOutputStream.writeObject(players);
                thirdOutputStream.writeObject(players);

		for (int i = 0; i < 7; i ++) {
                        players.get(0).giveCard(gamePiles.draw());
                }

                for (int i = 0; i < 7; i++) {
                        firstOutputStream.writeObject(gamePiles.draw());
                }

                for (int i = 0; i < 7; i++) {
                        secondOutputStream.writeObject(gamePiles.draw());
                }

                for (int i = 0; i < 7; i++) {
                        thirdOutputStream.writeObject(gamePiles.draw());
                }

                System.out.println("Welcome, " + players.get(0).name + ", " + players.get(1).name + ", and " + players.get(2).name + ", and " + players.get(3).name + ", let's play OONA!\n");

                System.out.println("Remember, this is not UNO. The way to win OONA is by playing the UNO the way everybody ACTUALLY plays, not by the point system.\nYou go around in a clockwise fashion, laying down a card that matches either the number or the color of the one on top of the discard pile. If you don't have any valid cards, you automatically have to draw another. You will have the choice to put down the newly drawn card if it is valid. If not, it will simply get added to your hand. The first player to run out of cards is the winner!\n");

		gamePiles.discardCard(gamePiles.draw());

                Color firstColor = gamePiles.topDiscardCard().getColor();
                firstOutputStream.writeObject(firstColor);
                secondOutputStream.writeObject(firstColor);
                thirdOutputStream.writeObject(firstColor);

                Number firstNumber = gamePiles.topDiscardCard().getNumber();
                firstOutputStream.writeObject(firstNumber);
                secondOutputStream.writeObject(firstNumber);
                thirdOutputStream.writeObject(firstNumber);

                if (firstNumber == Number.DRAW2) {
                        System.out.println("First card is DRAW2! " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                        for (int i = 0; i < 2; i++) {
                                firstOutputStream.writeObject(gamePiles.draw());
                        }
                } else if (firstNumber == Number.DRAW4) {
                        System.out.println("First card is DRAW4! " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                        for (int i = 0; i < 4; i++) {
                                firstOutputStream.writeObject(gamePiles.draw());
                        }
                } else if (firstNumber == Number.REVERSE) {
                        System.out.println("First card is reverse! Reverse direction.\n");
                } else if (firstNumber == Number.SKIP) {
                        System.out.println("First card is skip! " + players.get(1).name + " forfeits their turn.\n");
                }

                if (firstColor == Color.WILD) {
                        System.out.println("First card is a wild card! " + players.get(1).name + " must pick a color.");
                        firstColor = (Color)firstInputStream.readObject();
                        System.out.println("They chose: " + firstColor);
                        firstOutputStream.writeObject(firstColor);
                        secondOutputStream.writeObject(firstColor);
                        thirdOutputStream.writeObject(firstColor);
                }
                GameState gameState = new GameState(players, firstColor, firstNumber);
                Player player = gameState.getTurn();

                Boolean hasWinner = false;
                String winner = "none";

		while (!hasWinner) {
                        if (player == players.get(0)) {
                                System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                Number num = Number.FIVE;
                                Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                if (playedCard == null) {
                                        playedCard = player.playAfterDraw(gamePiles.draw(), gameState.getColor(), gameState.getNumber(), keyboard);
                                }
                                firstOutputStream.writeObject(playedCard);
                                secondOutputStream.writeObject(playedCard);
                                thirdOutputStream.writeObject(playedCard);

                                if (playedCard != null) {
                                        gamePiles.discardCard(playedCard);
                                        player.removeCard(playedCard);
                                        if (playedCard.getColor() == Color.WILD) {
                                                Color newcolor = player.chooseColor(keyboard);
                                                player = gameState.update(newcolor, playedCard.getNumber());
                                        } else {
                                                player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                      }
                                } else if (playedCard == null) {
                                        player = gameState.update(null, null);
                                        num = null;
                                }
                                Color col = gameState.getColor();
                                firstOutputStream.writeObject(col);
                                secondOutputStream.writeObject(col);
                                thirdOutputStream.writeObject(col);

                                if (num == null) {
                                        num = null;
                                } else {
                                        num = gameState.getNumber();
                                }
                                firstOutputStream.writeObject(num);
                                secondOutputStream.writeObject(num);
                                thirdOutputStream.writeObject(num);

                                if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                        for (int i = 0; i < 2; i++) {
                                                firstOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                        for (int i = 0; i < 4; i++) {
                                                firstOutputStream.writeObject(gamePiles.draw());
                                        }
                                }
				if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                        for (int i = 0; i < 2; i++) {
                                                secondOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                        for (int i = 0; i < 4; i++) {
                                                secondOutputStream.writeObject(gamePiles.draw());
                                        }
                                }
                        } else if (player == players.get(1)) {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)firstInputStream.readObject();
                                if (playedCard == null) {
                                        firstOutputStream.writeObject(gamePiles.draw());
                                }
                                playedCard = (Card)firstInputStream.readObject();
                                secondOutputStream.writeObject(playedCard);
                                thirdOutputStream.writeObject(playedCard);

                                if (playedCard != null) {
                                        gamePiles.discardCard(playedCard);
                                }
                                Color col = (Color)firstInputStream.readObject();
                                Number num = (Number)firstInputStream.readObject();
                                secondOutputStream.writeObject(col);
                                thirdOutputStream.writeObject(col);
                                secondOutputStream.writeObject(num);
                                thirdOutputStream.writeObject(num);

                                if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                        System.out.println("Card played DRAW2. " + players.get(2).name + " draws 2 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                secondOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                        System.out.println("Card played was  DRAW4. " + players.get(2).name + " draws 4 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                secondOutputStream.writeObject(gamePiles.draw());
                                                }
                                } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                        System.out.println("Card played was SKIP. " + players.get(2).name + " forfeits their turn.\n");
                                } else if (num == Number.REVERSE) {
                                        System.out.println("Card played was REVERSE! Reverse direction.\n");
                                }
                                if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                        System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                        System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                        System.out.println("Card played was SKIP. Forfeit your turn.\n");
                                }

                                player = gameState.update(col, num);
                        } else if (player == players.get(2)) {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)secondInputStream.readObject();
                                if (playedCard == null) {
                                        secondOutputStream.writeObject(gamePiles.draw());
                                }
                                playedCard = (Card)secondInputStream.readObject();
                                firstOutputStream.writeObject(playedCard);
                                thirdOutputStream.writeObject(playedCard);

                                Color col = (Color)secondInputStream.readObject();
                                Number num = (Number)secondInputStream.readObject();

                                firstOutputStream.writeObject(col);
                                thirdOutputStream.writeObject(col);
                                firstOutputStream.writeObject(num);
                                thirdOutputStream.writeObject(num);

				if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                        System.out.println("Card played was DRAW2! " + players.get(3).name + " draws 2 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                thirdOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                        System.out.println("Card played was DRAW4! " + players.get(3).name + " draws 4 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                thirdOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                        System.out.println("Card played was SKIP! " + players.get(3).name + " forfeits their turn.\n");
                                } else if (num == Number.REVERSE) {
                                        System.out.println("Card played was REVERSE! Reverse direction.\n");
                                }
                                if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                        System.out.println("Card played was DRAW2! " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                firstOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                        System.out.println("Card played was DRAW4! " + players.get(1).name + " draws 4 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                firstOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                        System.out.println("Card played was SKIP! " + players.get(1).name + " forfeits their turn.\n");
                                }

                                player = gameState.update(col, num);
                        } else if (player == players.get(3)) {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)thirdInputStream.readObject();
                                if (playedCard == null) {
                                        thirdOutputStream.writeObject(gamePiles.draw());
                                }
                                playedCard = (Card)thirdInputStream.readObject();
                                firstOutputStream.writeObject(playedCard);
                                secondOutputStream.writeObject(playedCard);

                                Color col = (Color)thirdInputStream.readObject();
                                Number num = (Number)thirdInputStream.readObject();

                                firstOutputStream.writeObject(col);
                                secondOutputStream.writeObject(col);
                                firstOutputStream.writeObject(num);
                                secondOutputStream.writeObject(num);

                                if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                        System.out.println("Card played was DRAW2! " + players.get(2).name + " draws 2 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                secondOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                        System.out.println("Card played was DRAW4! " + players.get(2).name + " draws 4 cards and forfeits their turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                secondOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                        System.out.println("Card played was SKIP. " + players.get(2).name + " forfeits their turn.\n");
                                } else if (num == Number.REVERSE) {
                                        System.out.println("Card played was REVERSE! Reverse direction.\n");
                                }
                                if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                        System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                        System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                        System.out.println("Card played was SKIP. Forfeit your turn.\n");
                                }
                                player = gameState.update(col, num);
                        }
                        int player1size = (int)firstInputStream.readObject();
                        int player2size = (int)secondInputStream.readObject();
                        int player3size = (int)thirdInputStream.readObject();
			if (players.get(0).hand.size() == 0) {
                                hasWinner = true;
                                winner = players.get(0).name;
                        } else if (player1size == 0) {
                                hasWinner = true;
                                winner = players.get(1).name;
                        } else if (player2size == 0) {
                                hasWinner = true;
                                winner = players.get(2).name;
                        } else if (player3size == 0) {
                                hasWinner = true;
                                winner = players.get(3).name;
                        }
                        firstOutputStream.writeObject(hasWinner);
                        secondOutputStream.writeObject(hasWinner);
                        thirdOutputStream.writeObject(hasWinner);
                        firstOutputStream.writeObject(winner);
                        secondOutputStream.writeObject(winner);
                        thirdOutputStream.writeObject(winner);
                }

                if (winner.equals(players.get(0).name)) {
                        System.out.println("\nCongratulations! You have gained the prestigious title of OONA winner, " + winner + "!");
                } else if (winner.equals(players.get(1).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + players.get(1).name + ".");
                } else if (winner.equals(players.get(2).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + players.get(2).name + ".");
                } else if (winner.equals(players.get(3).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + players.get(3).name + ".");
                }

                thirdOutputStream.close();
                thirdInputStream.close();
                secondOutputStream.close();
                secondInputStream.close();
                firstOutputStream.close();
                firstInputStream.close();
	} else if (numPlayers ==4 && specify.equals("joining")) {
		
		System.out.println("Welcome to OONA! What is your player name?");
                String myName = keyboard.nextLine();

                System.out.println("\nPerfect! What is the IP address of the server you want to play on?");
                String address = keyboard.nextLine();
                System.out.println();

                Socket socket = new Socket (address, 3000);
		System.out.println("Successfully connected to the server!");

                ObjectOutputStream clientOutputStream = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream clientInputStream = new ObjectInputStream(socket.getInputStream());

                clientOutputStream.writeObject(myName);

                @SuppressWarnings("unchecked")
                ArrayList<Player> players = (ArrayList)clientInputStream.readObject();
                if (myName.equals(players.get(1).name)) {
                        for (int i = 0; i < 7; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(1).giveCard(card);
                        }
                } else if (myName.equals(players.get(2).name)) {
                        for (int i = 0; i < 7; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(2).giveCard(card);
                        }
                } else if (myName.equals(players.get(3).name)) {
                        for (int i = 0; i < 7; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(3).giveCard(card);
                        }
                }

                System.out.println("Welcome, " + players.get(0).name + ", " + players.get(1).name + ", " + players.get(2).name + ", and " + players.get(3).name + ", let's play OONA!\n");

                System.out.println("Remember, this is not UNO. The way to win OONA is by playing the UNO the way everybody ACTUALLY plays, not by the point system.\nYou go around in a clockwise fashion, laying down a card that matches either the number or the color of the one on top of the discard pile. If you don't have any valid cards, you automatically have to draw another. You will have the choice to put down the newly drawn card if it is valid. If not, it will simply get added to your hand. The first player to run out of cards is the winner!\n");

                Color firstColor = (Color)clientInputStream.readObject();
                Number firstNumber = (Number)clientInputStream.readObject();

                if (firstNumber == Number.DRAW2) {
                        if (myName.equals(players.get(1).name)) {
                                System.out.println("First card is DRAW2! Draw 2 cards.\n");
                                for (int i = 0; i < 2; i++) {
                                        Card card = (Card)clientInputStream.readObject();
                                        players.get(1).giveCard(card);
                                }
                        } else {
                                System.out.println("First card is DRAW2! " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                        }
                } else if (firstNumber == Number. DRAW4) {
                        if (myName.equals(players.get(1).name)) {
                                System.out.println("First card is DRAW4! Draw 4 cards and forfeit your turn.\n");
                                for (int i = 0; i < 4; i++) {
                                        Card card = (Card)clientInputStream.readObject();
                                        players.get(1).giveCard(card);
                                }
                        } else {
                                System.out.println("First card is DRAW4! " + players.get(1).name + " draws 4 cards and forfeits their turn.\n");
                         }
                } else if (firstNumber == Number.SKIP){
                        if (myName.equals(players.get(1).name)) {
                                System.out.println("First card is SKIP! Forfeit your turn.\n");
                        } else {
                                System.out.println("First card is SKIP! " + players.get(1).name + " forfeits their turn.\n");
                        }
                } else if (firstNumber == Number.REVERSE) {
                        System.out.println("First card is REVERSE! Reverse direction.\n");
                }
		if (firstColor == Color.WILD) {
                        if (myName.equals(players.get(1).name)) {
                                firstColor = players.get(1).chooseColor(keyboard);
                                clientOutputStream.writeObject(firstColor);
                                firstColor = (Color)clientInputStream.readObject();
                        } else {
                                   System.out.println("First card is a wild card! " + players.get(1).name + " must pick a color.");
                                firstColor = (Color)clientInputStream.readObject();
                                System.out.println("They chose: " + firstColor);
                        }
                }

                GameState gameState = new GameState(players, firstColor, firstNumber);
                Player player = gameState.getTurn();

                Boolean hasWinner = false;
                String winner = "none";

		while(!hasWinner) {
                        if (player == players.get(0)) {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)clientInputStream.readObject();
                                Color col = (Color)clientInputStream.readObject();
                                Number num = (Number)clientInputStream.readObject();
                                if (playedCard != null) {
                                        if (playedCard.getColor() == Color.WILD) {
                                                System.out.println("Card played was wild. " + player.name + " chose " + col + "\n");
                                        }
                                }
                                if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                        if (myName.equals(players.get(1).name)) {
                                                System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 2; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(1).giveCard(card);
                                                }
                                        } else {
                                                System.out.println("Card played was DRAW2. " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                        if (myName.equals(players.get(1).name)) {
                                                System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 4; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(1).giveCard(card);
                                                }
                                        } else {
                                                System.out.println("Card played was DRAW4. " + players.get(1).name + " draws 4 cards and forfeits their turn.\n");
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                        if (myName.equals(players.get(1).name)) {
                                                System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                        } else  {
                                                System.out.println("Card played was SKIP. " + players.get(1).name + " forfeits their turn.\n");
                                        }
                                } else if (num == Number.REVERSE) {
                                        System.out.println("Card played was REVERSE! Reverse direction.\n");
                                }
                                if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                        if (myName.equals(players.get(3).name)) {
                                                System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 2; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(3).giveCard(card);
                                                }
                                        } else {
                                                System.out.println("Card played was DRAW2. " + players.get(3).name + " draws 2 cards and forfeits their turn.\n");
                                        }
                                } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                        if (myName.equals(players.get(3).name)) {
                                                System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                                for (int i = 0; i < 4; i++) {
                                                        Card card = (Card)clientInputStream.readObject();
                                                        players.get(3).giveCard(card);
                                                }
                                        } else {
                                                System.out.println("Card played was DRAW4. " + players.get(3).name + " draws 2 cards and forfeits their turn.\n");
                                        }
                                } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                        if (myName.equals(players.get(3).name)) {
                                                System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                        } else {
                                                System.out.println("Card played was SKIP! " + players.get(3).name + " forfeits their turn.\n");
                                        }
                                }
                                player = gameState.update(col, num);
                        } else if (player == players.get(1)) {
                                if (myName.equals(players.get(1).name)) {
                                        System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                        Number num = Number.FIVE;
                                        Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                        clientOutputStream.writeObject(playedCard);
                                        if (playedCard == null) {
                                                Card card = (Card)clientInputStream.readObject();
                                                playedCard = player.playAfterDraw(card, gameState.getColor(), gameState.getNumber(), keyboard);
                                        }
                                        clientOutputStream.writeObject(playedCard);
                                        if (playedCard != null) {
                                                player.removeCard(playedCard);
                                                if (playedCard.getColor() == Color.WILD) {
                                                        Color newcolor = player.chooseColor(keyboard);
                                                        player = gameState.update(newcolor, playedCard.getNumber());
                                                } else {
                                                        player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                                }
                                        } else if (playedCard == null) {
                                                player = gameState.update(null, null);
                                                num = null;
                                        }
                                        Color col = gameState.getColor();
                                        clientOutputStream.writeObject(col);
                                        if (num == null) {
						num = null;
                                        } else {
                                                num = gameState.getNumber();
                                        }
                                        clientOutputStream.writeObject(num);
                                } else {
                                        System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                        Card playedCard = (Card)clientInputStream.readObject();
                                        Color col = (Color)clientInputStream.readObject();
                                        Number num = (Number)clientInputStream.readObject();
                                        if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                                if (myName.equals(players.get(2).name)) {
                                                        System.out.println("Card played was DRAW2. Draw 2 cards and forfeit your turn.\n");
                                                        for (int i = 0; i < 2;i++) {
                                                                Card card = (Card)clientInputStream.readObject();
                                                                players.get(2).giveCard(card);
                                                        }
                                                } else {
                                                        System.out.println("Card played was DRAW2. " + players.get(2).name + " draws 2 cards and forfeits their turn.\n");
                                                }
                                        } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                                if (myName.equals(players.get(2).name)) {
                                                         System.out.println("Card played was DRAW4. Draw 4 cards and forfeit your turn.\n");
                                                         for (int i = 0; i < 4; i++) {
                                                                Card card = (Card)clientInputStream.readObject();
                                                                players.get(2).giveCard(card);
                                                        }
                                                } else {
                                                        System.out.println("Card played was DRAW4. " + players.get(2).name + " draws 2 cards and forfeits their turn.\n");
                                                }
                                        } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                                if (myName.equals(players.get(2).name)) {
                                                      System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                                } else {
                                                        System.out.println("Card played wwas SKIP! " + players.get(2).name + " forfeits their turn.\n");
                                                }
                                        } else if (num == Number.REVERSE) {
                                                System.out.println("Card played was REVERSE! Reverse direction.\n");
                                        } else if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                                System.out.println("Card played was DRAW2! " + players.get(0).name + " draws 2 cards and forfeits their turn.\n");
                                        } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                                System.out.println("Card played was DRAW4! " + players.get(0).name + " draws 2 cards and forfeits their turn.\n");
                                        } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                                System.out.println("Card played was SKIP! " + players.get(0).name + " forfeits their turn.\n");
                                        }

                                        player = gameState.update(col, num);
                                }
                        } else if (player == players.get(2)) {
                                if (myName.equals(players.get(2).name)) {
                                        System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                        Number num = Number.FIVE;
                                        Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                        clientOutputStream.writeObject(playedCard);
                                        if (playedCard == null) {
                                                Card card = (Card)clientInputStream.readObject();
                                                playedCard = player.playAfterDraw(card, gameState.getColor(), gameState.getNumber(), keyboard);
                                        }
                                        clientOutputStream.writeObject(playedCard);
                                        if (playedCard != null) {
                                                player.removeCard(playedCard);
                                                if (playedCard.getColor() == Color.WILD) {
                                                        Color newcolor = player.chooseColor(keyboard);
                                                        player = gameState.update(newcolor, playedCard.getNumber());
                                                } else {
                                                        player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                                }
                                        } else if (playedCard == null) {
                                                player = gameState.update(null, null);
                                                num = null;
                                        }
                                        Color col = gameState.getColor();
                                        clientOutputStream.writeObject(col);
                                        if (num == null) {
                                                num = null;
                                        } else {
                                                num = gameState.getNumber();
                                        }
                                        clientOutputStream.writeObject(num);
                                } else {
                                        System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                        Card playedCard = (Card)clientInputStream.readObject();
                                        Color col = (Color)clientInputStream.readObject();
                                        Number num = (Number)clientInputStream.readObject();

                                        if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                                if (myName.equals(players.get(3).name)) {
                                                        System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                                        for (int i = 0; i < 2; i++) {
                                                                Card card = (Card)clientInputStream.readObject();
                                                                players.get(3).giveCard(card);
                                                        }
                                                } else {
                                                        System.out.println("Card played was DRAW2! " + players.get(3).name + " draws 2 cards and forfeits their turn.\n");
                                                }
                                        } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                                if (myName.equals(players.get(3).name)) {
                                                        System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                                        for (int i = 0; i < 4; i++) {
                                                                Card card = (Card)clientInputStream.readObject();
                                                                players.get(3).giveCard(card);
                                                        }
                                                } else {
                                                        System.out.println("Card played was DRAW4! " + players.get(3).name + " draws  cards and forfeits their turn.\n");
                                                }
                                        } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                                if (myName.equals(players.get(3).name)) {
                                                        System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                                } else {
                                                        System.out.println("Card played was SKIP! " + players.get(3).name + " forfeits their turn.\n");
                                                }
                                        } else if (num == Number.REVERSE) {
                                                System.out.println("Card played was REVERSE! Reverse direction.\n");
                                        }
                                        if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                                if (myName.equals(players.get(1).name)) {
                                                        System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                                        for (int i = 0; i < 2; i++) {
                                                                Card card = (Card)clientInputStream.readObject();
                                                                players.get(1).giveCard(card);
                                                        }
                                                } else {
                                                        System.out.println("Card played was DRAW2! " + players.get(1).name + " draws 2 cards and forfeits their turn.\n");
                                                }
                                        } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                                if (myName.equals(players.get(1).name)) {
                                                        System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                                        for (int i = 0; i < 4; i++) {
                                                                Card card = (Card)clientInputStream.readObject();
                                                                players.get(1).giveCard(card);
                                                        }
                                                } else {
                                                        System.out.println("Card played was DRAW4! " + players.get(1).name + " draws  cards and forfeits their turn.\n");
                                                }
                                        } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                                if (myName.equals(players.get(1).name)) {
                                                        System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                                } else {
                                                        System.out.println("Card played was SKIP! " + players.get(1).name + " forfeits their turn.\n");
                                                }
                                        }
                                        player = gameState.update(col, num);
                                }
                        }else if (player == players.get(3)) {
                                if (myName.equals(players.get(3).name)) {
                                        System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                        Number num = Number.FIVE;
                                        Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                        clientOutputStream.writeObject(playedCard);
                                        if (playedCard == null) {
                                                Card card = (Card)clientInputStream.readObject();
                                                playedCard = player.playAfterDraw(card, gameState.getColor(), gameState.getNumber(), keyboard);
                                        }
                                        clientOutputStream.writeObject(playedCard);
                                        if (playedCard != null) {
                                                player.removeCard(playedCard);
                                                if (playedCard.getColor() == Color.WILD) {
                                                        Color newcolor = player.chooseColor(keyboard);
                                                        player = gameState.update(newcolor, playedCard.getNumber());
                                                } else {
                                                        player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                                }
                                        } else if (playedCard == null) {
                                                player = gameState.update(null, null);
                                                num = null;
                                        }
                                        Color col = gameState.getColor();
                                        clientOutputStream.writeObject(col);
                                        if (num == null) {
                                                num = null;
                                        } else {
                                                num = gameState.getNumber();
                                        }
                                        clientOutputStream.writeObject(num);
                                } else {
                                        System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                        Card playedCard = (Card)clientInputStream.readObject();
                                        Color col = (Color)clientInputStream.readObject();
                                        Number num = (Number)clientInputStream.readObject();

                                        if (num == Number.DRAW2 && gameState.getDirection() == -1) {
                                                if (myName.equals(players.get(2).name)) {
                                                        System.out.println("Card played was DRAW2. Draw 2 cards and forfeit your turn.\n");
                                                        for (int i = 0; i < 2;i++) {
                                                                Card card = (Card)clientInputStream.readObject();
                                                                players.get(2).giveCard(card);
                                                        }
                                                } else {
                                                        System.out.println("Card played was DRAW2. " + players.get(2).name + " draws 2 cards and forfeits their turn.\n");
                                                }
                                        } else if (num == Number.DRAW4 && gameState.getDirection() == -1) {
                                                if (myName.equals(players.get(2).name)) {
                                                         System.out.println("Card played was DRAW4. Draw 4 cards and forfeit your turn.\n");
                                                         for (int i = 0; i < 4; i++) {
                                                                Card card = (Card)clientInputStream.readObject();
                                                                players.get(2).giveCard(card);
                                                        }
                                                } else {
                                                        System.out.println("Card played was DRAW4. " + players.get(2).name + " draws 2 cards and forfeits their turn.\n");
                                                }
                                        } else if (num == Number.SKIP && gameState.getDirection() == -1) {
                                                if (myName.equals(players.get(2).name)) {
                                                      System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                                } else {
                                                        System.out.println("Card played was SKIP! " + players.get(2).name + " forfeits their turn.\n");
                                                }
                                        } else if (num == Number.REVERSE) {
                                                System.out.println("Card played was REVERSE! Reverse direction.\n");
                                        }
                                         if (num == Number.DRAW2 && gameState.getDirection() == 1) {
                                                System.out.println("Card played was DRAW2! " + players.get(0).name + " draws 2 cards and forfeits their turn.\n");
                                        } else if (num == Number.DRAW4 && gameState.getDirection() == 1) {
                                                System.out.println("Card played was DRAW4! " + players.get(0).name + " draws 2 cards and forfeits their turn.\n");
                                        } else if (num == Number.SKIP && gameState.getDirection() == 1) {
                                                System.out.println("Card played was SKIP! " + players.get(0).name + " forfeits their turn.\n");
                                        }

                                        player = gameState.update(col, num);
                                }
                        }
			if (myName.equals(players.get(1).name)) {
                                clientOutputStream.writeObject(players.get(1).hand.size());
                        } else if (myName.equals(players.get(2).name)) {
                                clientOutputStream.writeObject(players.get(2).hand.size());
                        } else if (myName.equals(players.get(3).name)) {
                                clientOutputStream.writeObject(players.get(3).hand.size());
                        }
                        hasWinner = (Boolean)clientInputStream.readObject();
                        winner = (String)clientInputStream.readObject();
                }

                if (winner.equals(players.get(1).name)) {
                        if (myName.equals(players.get(1).name)) {
                                System.out.println("\nCongratulations! You have gained the prestigious title of OONA winner, " + winner + "!");
                        } else {
                                System.out.println("\nSorry, you suck. The winner is " + winner + ".");
                        }
                } else if (winner.equals(players.get(0).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + winner + ".");
                } else if (winner.equals(players.get(2).name)) {
                        if (myName.equals(players.get(2).name)) {
                                System.out.println("\nCongratulations! You have gained the prestigious title of OONA winner, " + winner + "!");
                        } else {
                                System.out.println("\nSorry, you suck. The winner is " + winner + ".");
                        }
                } else if (winner.equals(players.get(3).name)) {
                        if (myName.equals(players.get(3).name)) {
                                System.out.println("\nCongratulations! You have gained the presitigous title of OONA winner, " + winner + "!");
                        } else {
                                System.out.println("\nSorry, you suck. The winner is " + winner + ".");
                        }
                }

                clientOutputStream.close();
                clientInputStream.close();
	} 
  } 
} 
