package sudoku;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static sudoku.Commons.getFirstEmpty;
import static sudoku.Rules.BIG_SIZE;
import static sudoku.Rules.EMPTY_TILE;
import static sudoku.Rules.NORMAL_SIZE;
import static sudoku.Rules.SMALL_SIZE;
import static sudoku.Rules.getDifficulty;
import static sudoku.Rules.getSideSize;
import static sudoku.Rules.getSize;
import static sudoku.Rules.getValidChars;
import static sudoku.Rules.isValidPlacement;
import static sudoku.Tools.toList;

public class Generator {
	private static final Random random = new Random();
	private static List<Character> chars;
	private static char[] validChars;

	public static char[][] generateGrid() {
		chars = toList(getValidChars());
		validChars = getValidChars();

		final char[][] grid = generateEmptyGrid();
		generateFullSudoku(grid);
		makePuzzle(grid);
		return grid;
	}

	private static void makePuzzle(char[][] grid) {
		int toRemove = toRemove();
		int row;
		int column;

		while (toRemove > 0) {
			do {
				row = random.nextInt(getSize());
				column = random.nextInt(getSize());
			} while (grid[row][column] == EMPTY_TILE);

			grid[row][column] = EMPTY_TILE;
			toRemove--;
		}
	}

	private static int toRemove() {
		switch (getSize()) {
			case SMALL_SIZE:
				return getSideSize() + getDifficulty() * (random.nextInt(getSideSize()) + 2);
			case NORMAL_SIZE:
				return getSize() + getDifficulty() * (random.nextInt(getSideSize()) + 8);
			case BIG_SIZE:
				return getSize() * getSideSize() + getDifficulty() * (random.nextInt(getSideSize()) + 14);
			default:
				throw new IllegalStateException("Unexpected value: " + getSize());
		}
	}

	private static void generateFullSudoku(char[][] grid) {
		if (getSize() != SMALL_SIZE)
			diagonalPlacement(grid); // With a 4x4 grid this optimization step is not critical and often generates impossible puzzles.
		randomizedSolver(grid);
	}

	private static void diagonalPlacement(char[][] grid) {
		final int size = getSize(), sideSide = getSideSize();

		for (int sectionCorner = 0; sectionCorner < size; sectionCorner += sideSide) {
			Collections.shuffle(chars);
			int i = 0;
			for (int row = 0; row < sideSide; row++)
				for (int column = 0; column < sideSide; column++)
					grid[sectionCorner + row][sectionCorner + column] = chars.get(i++);
		}
	}

	private static boolean randomizedSolver(char[][] grid) {
		Point emptyPos = getFirstEmpty(grid);
		if (emptyPos == null) return true;

		int row = emptyPos.x;
		int column = emptyPos.y;

		final int length = validChars.length, start = random.nextInt(length);
		boolean beganCircle = false;
		for (int i = start; i != start || !beganCircle; i = i + 1 < length ? i + 1 : 0, beganCircle = true) {
			char value = validChars[i];
			if (!isValidPlacement(grid, value, row, column)) continue;
			grid[row][column] = value;
			if (randomizedSolver(grid)) return true;
			grid[row][column] = EMPTY_TILE;
		}

		return false;
	}

	private static char[][] generateEmptyGrid() {
		final int size = getSize();
		char[][] grid = new char[size][size];
		for (char[] row : grid)
			Arrays.fill(row, EMPTY_TILE);
		return grid;
	}
}
