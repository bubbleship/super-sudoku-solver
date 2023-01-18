package sudoku;

import java.awt.Point;

import static sudoku.Rules.EMPTY_TILE;

class Commons {

	public static Point getFirstEmpty(char[][] grid) {
		for (int row = 0; row < grid.length; row++)
			for (int column = 0; column < grid[row].length; column++)
				if (grid[row][column] == EMPTY_TILE) return new Point(row, column);

		return null;
	}
}
