package game.object;

import game.pools.GameConfig;

public class Letter extends StaticObject {
	char value;

	public Letter(char value) {
		super();
		setSize(GameConfig.LETTER_SIZE);
		this.value = value;
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

	@Override
	public String getType() {
		switch (value) {
		case 'a':
			return "A";
		case 'e':
			return "E";
		case 'g':
			return "G";
		case 'h':
			return "H";
		case 'i':
			return "I";
		case 'l':
			return "L";
		case 'n':
			return "N";
		case 'o':
			return "O";
		case 'p':
			return "P";
		case 's':
			return "S";
		case 'v':
			return "V";
		default:
			return "";
		}
	}
}
