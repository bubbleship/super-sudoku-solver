package sudoku;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static sudoku.Commons.getFirstEmpty;
import static sudoku.Rules.EMPTY_TILE;
import static sudoku.Rules.getDifficulty;
import static sudoku.Rules.getSize;
import static sudoku.Rules.getValidChars;
import static sudoku.Rules.isValidPlacement;
import static sudoku.Tools.copyGrid;
import static sudoku.Tools.toList;

public class Generator {
	private static List<Character> chars;
	private static char[] validChars;
	private static int counter;
	private static volatile boolean resume = true;

	static {
		update();
	}

	public static void update() {
		chars = toList(getValidChars());
		validChars = getValidChars();
	}

	public static char[][] generateGridSafe() {
		final char[][][] grid = {null};

		Thread thread = new Thread(() -> grid[0] = generateGridImpl());

		thread.start();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				resume = false;
			}
		}, 2000);

		try {
			thread.join();
		} catch (InterruptedException ignored) {
		} finally {
			timer.cancel();
		}

		return grid[0];
	}

	private static char[][] generateGridImpl() {
		resume = true;
		Random random = new Random();
		char[][] grid = generateEmptyGrid();
		int attempts = getDifficulty();
		int row;
		int column;
		char backup;

		generateFullSudoku(grid);
		while (attempts > 0 && resume) {
			do {
				row = random.nextInt(validChars.length);
				column = random.nextInt(validChars.length);
			} while (grid[row][column] == EMPTY_TILE);
			backup = grid[row][column];
			grid[row][column] = EMPTY_TILE;
			counter = 0;
			countSolutions(copyGrid(grid));
			if (counter != 1) {
				grid[row][column] = backup;
				attempts--;
			}
		}

		if (!resume) grid = null;
		return grid;
	}

	private static boolean countSolutions(char[][] grid) {
		if (!resume) return true;
		Point emptyPos = getFirstEmpty(grid);
		if (emptyPos == null) return true;

		int row = emptyPos.x;
		int column = emptyPos.y;

		for (char c : validChars) {
			if (isValidPlacement(grid, c, row, column)) {
				grid[row][column] = c;
				if (isGridFull(grid)) {
					counter++;
					break;
				} else if (countSolutions(grid)) return true;
				grid[row][column] = EMPTY_TILE;
			}
		}
		return false;
	}

	private static boolean generateFullSudoku(char[][] grid) {
		if (!resume) return true;
		Point emptyPos = getFirstEmpty(grid);
		if (emptyPos == null) return true;

		int row = emptyPos.x;
		int column = emptyPos.y;

		Collections.shuffle(chars);
		for (char c : chars)
			if (isValidPlacement(grid, c, row, column)) {
				grid[row][column] = c;
				if (isGridFull(grid) || generateFullSudoku(grid)) return true;
				grid[row][column] = EMPTY_TILE;
			}
		return false;
	}

	private static boolean isGridFull(char[][] grid) {
		for (char[] row : grid)
			for (char c : row)
				if (c == EMPTY_TILE) return false;
		return true;
	}

	private static char[][] generateEmptyGrid() {
		final int size = getSize();
		char[][] grid = new char[size][size];
		for (char[] row : grid)
			Arrays.fill(row, EMPTY_TILE);
		return grid;
	}
}
