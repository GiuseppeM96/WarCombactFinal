package game.net;

public class ScorePlayer {

	public String name;
	public int score;

	public ScorePlayer(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public String getName() {

		return this.name;
	}

	public int getScore() {
		return this.score;
	}
}
