public class GameState {
	static Color color;
	static Number number;
	static int turn;
	static ArrayList<Player> players;

	public GameState(players, Color color, Number number) {
		this.players = players
		this.color = color;
		this.number = number;
		turn = 0;
	}

	public static Player update(Color color, Number number) {
		this.color = color;
		this.number = number;
		turn = (turn + 1) % players.size();
		return ArrayList.get(turn);
	}

	public Color getColor() {
		return color;
	}

	public Number getNumber() {
		return number;
	}
}
