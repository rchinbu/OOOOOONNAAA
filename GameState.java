import java.util.ArrayList;

public class GameState {
	private Color color;
	private Number number;
	private int turn;
	private ArrayList<Player> players;
	private int direction;

	public GameState(ArrayList<Player> players, Color color, Number number) {
		this.players = players;
		this.color = color;
		this.number = number;
		turn = 0;
		direction = 1;
		if (
			number == Number.SKIP ||
			number == Number.DRAW2 ||
			number == Number.DRAW4 ||
			number == Number.REVERSE
		) {
			specialCase(number);
		}
	}

	private Player specialCase(Number number) {
		if (number == Number.REVERSE) {
			direction = -1;
			turn = (turn + direction + players.size()) % players.size();
			return players.get(turn);
		} else {
			turn = (turn + direction * 2 + players.size()) % players.size();
			return players.get(turn);
		}
	}

	public Player update(Color color, Number number) {
		if (color == null || number == null) {
			turn = (turn + direction + players.size()) % players.size();
			return players.get(turn);
		}
		this.color = color;
		this.number = number;
		if (
			number == Number.SKIP || 
			number == Number.DRAW2 || 
			number == Number.DRAW4 || 
			number == Number.REVERSE
		) {
			return specialCase(number);
		} else {
			turn = (turn + direction + players.size()) % players.size();
			return players.get(turn);
		}
	}

	public Color getColor() {
		return color;
	}

	public Number getNumber() {
		return number;
	
	}

	public Player getTurn() {
		return players.get(turn);
	}
	
	public Player getNextTurn() {
		return players.get(turn + 1);
	}
}
