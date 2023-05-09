package comps;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import sudoku.Rules;

import java.util.function.Consumer;

public class Display extends GridPane {

	private Tile[][] tiles;

	public Display() {
		build();
	}

	private void build() {
		final int sideSize = Rules.getSideSize();
		final int size = Rules.getSize();
		tiles = new Tile[size][size];

		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setPercentHeight(100.0 / sideSize);
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setPercentWidth(100.0 / sideSize);

		Section[] sections = new Section[sideSize];
		for (int row = 0; row < sideSize; row++) {
			getRowConstraints().add(rowConstraints);
			getColumnConstraints().add(columnConstraints);
			for (int column = 0; column < sideSize; column++) {
				Section section = new Section();
				sections[column] = section;
				add(section, column, row);
				setHgrow(section, Priority.ALWAYS);
				setVgrow(section, Priority.ALWAYS);
			}
			for (int i = 0; i < sideSize; i++)
				for (int k = 0; k < sideSize; k++)
					System.arraycopy(sections[k].tiles[i], 0, tiles[row * sideSize + i], k * sideSize, sideSize);
		}
	}

	public void rebuild() {
		getChildren().clear();
		getRowConstraints().clear();
		getColumnConstraints().clear();
		build();
	}

	public Tile getTileAt(int row, int column) {
		return tiles[row][column];
	}

	public void prepGrid(boolean blocked) {
		forEachTile(tile -> tile.prep(blocked));
	}

	public void forEachTile(Consumer<Tile> consumer) {
		for (Tile[] row : tiles)
			for (Tile tile : row) consumer.accept(tile);
	}

	public char[][] getGrid() {
		final int size = Rules.getSize();
		char[][] grid = new char[size][size];
		for (int row = 0; row < tiles.length; row++)
			for (int column = 0; column < tiles[row].length; column++)
				grid[row][column] = tiles[row][column].getValue();
		return grid;
	}

	public void showPuzzle(char[][] puzzle) {
		for (int i = 0; i < puzzle.length; i++) {
			char[] row = puzzle[i];
			for (int j = 0; j < row.length; j++) {
				char value = row[j];
				Tile tile = tiles[i][j];
				tile.setValue(value);
				tile.setBorder(Tile.TILE_DEFAULT_BORDER);
				tile.setModifiable(value == Rules.EMPTY_TILE);
				tile.prep(false);
			}
		}
	}

	public void showSolution(char[][] solution) {
		for (int row = 0; row < solution.length; row++)
			for (int column = 0; column < solution[row].length; column++) {
				Tile tile = tiles[row][column];
				tile.setValue(solution[row][column]);
				tile.setBorder(Tile.TILE_DEFAULT_BORDER);
			}
	}
}
