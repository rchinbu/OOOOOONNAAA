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

	public Player getTurn() {
		return players.get(turn)
	}
	
	public Player getNextTurn() {
		return players.get(turn + 1);
	}
}
