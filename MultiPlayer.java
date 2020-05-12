import java.io.*;
import java.net.*;
import java.util.*;

public class MultiPlayer {
	public static void server(Scanner keyboard) throws Exception {
		System.out.println("How many real humans are joining you in this game? Please enter a number between 0 and 9.");
		String response = keyboard.nextLine();

		while (! Character.isDigit(response.charAt(0))) {
			System.out.println("Invalid response! Please try again!");
			response = keyboard.nextLine();
		}

		int num_players = Character.getNumericValue(response.charAt(0));
		int num_AIs = 0;
		if (num_players < 9) {
			System.out.println("How many AIs would you like? Please enter a number between 0 and " + (9 - num_players));
			response = keyboard.nextLine();
			while (! Character.isDigit(response.charAt(0)) || Character.getNumericValue(response.charAt(0)) > (9 - num_players)) {
				System.out.println("Invalid response! Please try again!");
				response = keyboard.nextLine();
			}
			num_AIs = Character.getNumericValue(response.charAt(0));
		}

		ArrayList<NetworkPlayer> players = new ArrayList<NetworkPlayer>();
		
		System.out.println("What is your name?");
		String name = keyboard.nextLine();
		players.add(new NetworkPlayer(name, "self"));

		// Add instructions for how to create port?
		
		ServerSocket server = new ServerSocket(3000);
		System.out.println("Waiting for new players...");

		int num_players_joined = 0;
		while (num_players_joined < num_players) {
			Socket pipe = server.accept();
			ObjectInputStream input = new ObjectInputStream(pipe.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(pipe.getOutputStream());
			NetworkPlayer newPlayer = new NetworkPlayer((String) input.readObject(), "network");
			newPlayer.addStreams(input, output);
			players.add(newPlayer);
			num_players_joined++;
		}

		for (int i = 0; i < num_AIs; i++) {
			players.add(new NetworkPlayer("AI" + i, "AI"));
		}

		// Game can happen now

	}

	public static void join(Scanner keyboard) {
		// Essentially the same as SecondPlayer.java
	}


	public static void main(String[] arg) throws Exception {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Welcome to OONA!");
		System.out.println("Would you like to start a new game or join an existing game? (NEW/JOIN)");
		String typeOfGame = keyboard.nextLine();
		while (!(typeOfGame.equals("NEW") || typeOfGame.equals("JOIN"))) {
			System.out.println("Invalid input! Try again!");
			typeOfGame = keyboard.nextLine();
		}
		if (typeOfGame.equals("NEW")) {
			server(keyboard);
		} else {
			join(keyboard);
		}
		keyboard.close();
	}
}
