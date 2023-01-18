package sudoku;

import comps.Tile;
import javafx.scene.layout.Border;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

import static sudoku.Rules.EMPTY_TILE;
import static sudoku.Commons.getFirstEmpty;
import static sudoku.Rules.isValidPlacement;

public class Solver {
	private static char[] validChars;

	public static Queue<Step> solve(char[][] grid) {
		Queue<Step> steps = new LinkedList<>();
		algorithm(steps, grid);
		return steps;
	}

	public static void algorithm(Queue<Step> steps, char[][] grid) {
		validChars = Rules.getValidChars();
		algorithmImpl(steps, grid);
	}

	private static boolean algorithmImpl(Queue<Step> steps, char[][] grid) {
		Point emptyPos = getFirstEmpty(grid);
		if (emptyPos == null) return true;

		int row = emptyPos.x;
		int column = emptyPos.y;

		for (char value : validChars)
			if (isValidPlacement(grid, value, row, column)) {
				grid[row][column] = value;
				addStep(steps, row, column, value, Tile.TILE_VALID_BORDER);
				addStep(steps, row, column, value, Tile.TILE_DEFAULT_BORDER);
				if (algorithmImpl(steps, grid)) return true;
				grid[row][column] = EMPTY_TILE;
				addStep(steps, row, column, value, Tile.TILE_DEFAULT_BORDER);
			} else {
				addStep(steps, row, column, value, Tile.TILE_INVALID_BORDER);
			}

		addStep(steps, row, column, EMPTY_TILE, Tile.TILE_INVALID_BORDER);
		return false;
	}

	private static void addStep(Queue<Step> steps, int row, int column, char value, Border border) {
		if (steps == null) return;
		steps.add(new Step(row, column, value, border));
	}
}
