package comps;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import sudoku.Rules;

import java.util.function.Consumer;

public class Display extends GridPane {

	private Tile[][] tiles;
	private boolean blocked = false;

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
				Section section = new Section(this);
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

	public void showPuzzle(char[][] puzzle) {
		setBlocked(false);
		for (int row = 0; row < puzzle.length; row++) {
			for (int column = 0; column < puzzle[row].length; column++) {
				char value = puzzle[row][column];
				Tile tile = tiles[row][column];
				tile.setValue(value);
				tile.setBorder(Tile.TILE_DEFAULT_BORDER);
				boolean modifiable = value == Rules.EMPTY_TILE;
				tile.setModifiable(modifiable);
				tile.setTextFill(modifiable ? Color.GRAY : Color.BLACK);
			}
		}
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

	boolean isBlocked() {
		return blocked;
	}

	public void showSolution(char[][] solution) {
		for (int row = 0; row < solution.length; row++)
			for (int column = 0; column < solution[row].length; column++) {
				Tile tile = tiles[row][column];
				tile.setValue(solution[row][column]);
				tile.setBorder(Tile.TILE_DEFAULT_BORDER);
			}
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
