package sudoku;

import javafx.util.Pair;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
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
import static sudoku.Tools.copyGrid;
import static sudoku.Tools.toList;

public class Generator {
	/**
	 * An upper bound representing the maximum number of calls the {@link #countSolutions(char[][], int)}
	 * method may call itself. This is to prevent the puzzle generation part from lingering on that step for
	 * an overly extended period, which can make the entire application unresponsive.
	 */
	private static final int MAX_SOLUTIONS_CHECKER_CALLS_COUNT = 800_000;
	/**
	 * An upper bound representing the maximum number of calls the {@link #randomizedSolver(char[][])} method
	 * may call itself. This is to prevent the complete sudoku generation part from lingering on that step
	 * for an overly extended period, which can make the entire application unresponsive.
	 */
	private static final int MAX_RANDOMIZED_SOLVER_CALLS_COUNT = 6_000_000;

	private static final Random random = new Random();
	private static List<Character> chars;
	private static char[] validChars;
	/**
	 * Used by {@link #countSolutions(char[][], int)} as a counter to enforce the limit denoted by
	 * {@link #MAX_SOLUTIONS_CHECKER_CALLS_COUNT}.
	 */
	private static int solutionsCheckerCallsCount;
	/**
	 * Used by {@link #randomizedSolver(char[][])} as a counter to enforce the limit denoted by
	 * {@link #MAX_RANDOMIZED_SOLVER_CALLS_COUNT}.
	 */
	private static int randomizedSolverCallsCount;

	/**
	 * Generates a sudoku puzzle. The dimensions and difficulty level of the resulting puzzle is determined
	 * by the state of the {@link Rules} class.
	 *
	 * @return A {@link Pair} object where the key is a matrix of characters representing the sudoku puzzle
	 * and the value is a matrix of characters representing its solution.
	 */
	public static Pair<char[][], char[][]> generateGrid() {
		chars = toList(getValidChars());
		validChars = getValidChars();

		final char[][] grid = generateEmptyGrid();
		generateFullSudoku(grid);
		final char[][] solution = copyGrid(grid);
		makePuzzle(grid);
		return new Pair<>(grid, solution);
	}

	private static void makePuzzle(char[][] grid) {
		LinkedList<Point> tiles = new LinkedList<>();
		int toRemove = toRemove(); // The number of empty tiles the resulting puzzle should have.
		int row;
		int column;
		char backup;
		/*
		 * The number of times the following loop may attempt at removing a tile from the given grid. Failing
		 * that many times will end the loop. This is to prevent the loop from potentially running forever.
		 */
		int retries = 10;

		while (toRemove > 0 && retries > 0) {
			do {
				row = random.nextInt(getSize());
				column = random.nextInt(getSize());
				backup = grid[row][column];
			} while (backup == EMPTY_TILE);

			grid[row][column] = EMPTY_TILE;
			solutionsCheckerCallsCount = MAX_SOLUTIONS_CHECKER_CALLS_COUNT;
			if (countSolutions(grid, 0) > 1) {
				// Removing this tile would result in a puzzle with multiple solutions.
				grid[row][column] = backup;
				retries--;
			} else {
				toRemove--;
				tiles.add(new Point(row, column));
			}
			// The solution counter method modifies the removed tiles so the following loop reverts that.
			for (Point tile : tiles) grid[tile.x][tile.y] = EMPTY_TILE;
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

	/**
	 * A modified implementation of the backtracking algorithm designed to count the number of solutions the
	 * given sudoku puzzle has. Used to ensure the given sudoku puzzle has a unique solution so the count
	 * stops after the second solution is discovered.
	 *
	 * @param grid  A matrix of characters representing the given sudoku puzzle.
	 * @param count Used internally to track the solution count over recursive calls.
	 * @return {@code 2} if the given puzzle has more than one solution. Otherwise {@code 1}.
	 */
	private static int countSolutions(char[][] grid, int count) {
		solutionsCheckerCallsCount--;
		if (solutionsCheckerCallsCount < 0) return 2;
		Point emptyPos = getFirstEmpty(grid);
		if (emptyPos == null) return count + 1;

		int row = emptyPos.x;
		int column = emptyPos.y;

		for (char value : validChars)
			if (isValidPlacement(grid, value, row, column)) {
				grid[row][column] = value;
				count = countSolutions(grid, count);
				if (count > 1) return count;
			}

		grid[row][column] = EMPTY_TILE;
		return count;
	}

	private static void generateFullSudoku(char[][] grid) {
		if (getSize() != SMALL_SIZE)
			diagonalPlacement(grid); // With a 4x4 grid this optimization step is not critical and often generates impossible puzzles.
		randomizedSolverCallsCount = MAX_RANDOMIZED_SOLVER_CALLS_COUNT;
		randomizedSolver(grid);
	}

	/**
	 * Fills all sections on the main diagonal of the empty sudoku grid represented by the given matrix.
	 * Works by placing a shuffled copy of the sudoku charset into each section. Since all sections on the
	 * main diagonal are independent of each other the resulting grid is guaranteed to not violate any sudoku
	 * rule.
	 *
	 * @param grid A matrix of characters representing the empty sudoku grid to fill.
	 */
	private static void diagonalPlacement(char[][] grid) {
		final int size = getSize(), sideSide = getSideSize();

		for (int sectionCorner = 0; sectionCorner < size; sectionCorner += sideSide) { // For each section.
			Collections.shuffle(chars); // Shuffle a copy of the sudoku charset.
			// Placing the shuffled sudoku charset into the section.
			int i = 0;
			for (int row = 0; row < sideSide; row++)
				for (int column = 0; column < sideSide; column++)
					grid[sectionCorner + row][sectionCorner + column] = chars.get(i++);
		}
	}

	/**
	 * A modified implementation of the backtracking algorithm designed to solve a sudoku puzzle with a
	 * random modifier. Works by traversing the sudoku charset in a circular pattern starting from a random
	 * index instead of starting from the first index.
	 *
	 * @param grid A matrix of characters representing the sudoku grid to solve.
	 * @return A boolean values used internally by this method.
	 */
	private static boolean randomizedSolver(char[][] grid) {
		randomizedSolverCallsCount--;
		if (randomizedSolverCallsCount < 0) return false;
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
			if (randomizedSolverCallsCount < 0) return false;
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
