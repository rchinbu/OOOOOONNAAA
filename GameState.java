import java.util.ArrayList;

public class GameState {
	Color color;
	Number number;
	int turn;
	ArrayList<Player> players;
	int direction;

	public GameState(ArrayList<Player> players, Color color, Number number) {
		this.players = players;
		this.color = color;
		this.number = number;
		turn = 0;
		direction = 1;
	}

	public Player update(Color color, Number number) {
		this.color = color;
		this.number = number;
		if (number == Number.REVERSE) {
			direction = -1;
			turn = (turn + direction) % players.size();
			return players.get(turn);
		} else if (number == Number.SKIP || number == Number.DRAW2 || number == Number.DRAW4) {
			turn = (turn + direction * 2) % players.size();
			return players.get(turn);
		} else {
			turn = (turn + direction) % players.size();
			return players.get(turn);
		}
	}

	public Color getColor() {
		return color;
	}

	public Number getNumber() {
		return number;
	
	}

	public Player getNextPlayer() {
		return players.get(turn);
	}
}
