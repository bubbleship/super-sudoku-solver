package sudoku;

public final class Rules {
	public static final char EMPTY_TILE = '0';
	private static final char[] SMALL_CHARSET = new char[]{'1', '2', '3', '4'};
	private static final char[] NORMAL_CHARSET = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static final char[] BIG_CHARSET = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g'};
	private static final int SMALL_SIDE_SIZE = 2;
	private static final int NORMAL_SIDE_SIZE = 3;
	private static final int BIG_SIDE_SIZE = 4;
	public static final int SMALL_SIZE = SMALL_SIDE_SIZE * SMALL_SIDE_SIZE;
	public static final int NORMAL_SIZE = NORMAL_SIDE_SIZE * NORMAL_SIDE_SIZE;
	public static final int BIG_SIZE = BIG_SIDE_SIZE * BIG_SIDE_SIZE;
	private static final int DEFAULT_DIFFICULTY = 1;

	private static char[] validChars = NORMAL_CHARSET;
	private static int sideSize = NORMAL_SIDE_SIZE;
	private static int size = NORMAL_SIZE;
	private static int difficulty = DEFAULT_DIFFICULTY;

	public static boolean isValidChar(char c) {
		for (char valid : validChars) if (c == valid) return true;
		return false;
	}

	public static char[] getValidChars() {
		return validChars;
	}

	public static boolean isValidPlacement(char[][] grid, char placement, int row, int column) {

		// Checking column.
		for (int i = 0; i < grid.length; i++)
			if (grid[i][column] == placement && i != row) return false;

		// Checking row.
		for (int i = 0; i < grid[row].length; i++)
			if (grid[row][i] == placement && i != column) return false;

		// Checking section.
		final int sideSize = getSideSize();
		int SectionRowStart = (row / sideSize) * sideSize;
		int SectionColumnStart = (column / sideSize) * sideSize;
		for (int i = SectionRowStart; i < SectionRowStart + sideSize; i++)
			for (int j = SectionColumnStart; j < SectionColumnStart + sideSize; j++)
				if (grid[i][j] == placement && row != i && column != j) return false;

		return true;
	}

	public static int getDifficulty() {
		return difficulty;
	}

	public static void setDifficulty(int difficulty) {
		Rules.difficulty = difficulty;
	}

	public static void setSize(Size size) {
		switch (size) {
			case SMALL:
				validChars = SMALL_CHARSET;
				sideSize = SMALL_SIDE_SIZE;
				Rules.size = SMALL_SIZE;
				break;
			case NORMAL:
				validChars = NORMAL_CHARSET;
				sideSize = NORMAL_SIDE_SIZE;
				Rules.size = NORMAL_SIZE;
				break;
			case BIG:
				validChars = BIG_CHARSET;
				sideSize = BIG_SIDE_SIZE;
				Rules.size = BIG_SIZE;
				break;
		}
	}

	public static int getSideSize() {
		return sideSize;
	}

	public static int getSize() {
		return size;
	}
}
