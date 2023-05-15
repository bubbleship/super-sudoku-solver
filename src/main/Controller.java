package main;

import comps.Display;
import comps.Tile;
import consts.Strings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import sudoku.Generator;
import sudoku.Rules;
import sudoku.Size;
import sudoku.Visualizer;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

	private char[][] grid;
	private char[][] solution;
	private Visualizer visualizer;
	private boolean running = false;
	private boolean editorMode = false;

	@FXML
	public Display display;
	@FXML
	public Button solve;
	@FXML
	public Button generate;
	@FXML
	public Button check;
	@FXML
	public Button clear;
	@FXML
	public Label stepsDisplay;

	@FXML
	public void solve() {
		check.setDisable(true);
		if (running) {
			visualizer.skip();
			return;
		}
		if (editorMode) {
			editorMode = false;
			grid = display.getGrid();
			display.forEachTile(tile -> {
				if (tile.getValue() != Rules.EMPTY_TILE) tile.setModifiable(false);
				else tile.setTextFill(Color.GRAY);
			});
		}
		solve.setText(Strings.SKIP);
		generate.setDisable(true);
		clear.setDisable(true);

		visualizer.reset(grid);
		display.setBlocked(true);
		display.forEachTile(tile -> {
			tile.setBorder(Tile.TILE_DEFAULT_BORDER);
			if (tile.isModifiable()) tile.setValue(Rules.EMPTY_TILE);
		});
		visualizer.start();
		running = true;
	}

	@FXML
	public void generate() {
		editorMode = false;
		display.requestFocus();
		setGrid();
	}

	@FXML
	public void check() {
		boolean allCorrect = true;
		char[][] grid = display.getGrid();

		for (int row = 0; row < grid.length; row++)
			for (int column = 0; column < grid[row].length; column++) {
				char value = grid[row][column];
				if (solution == null) {
					if (Rules.isValidPlacement(grid, value, row, column)) continue;
				} else if (value == solution[row][column]) continue;

				allCorrect = false;
				display.getTileAt(row, column).setBorder(Tile.TILE_INVALID_BORDER);
			}

		if (!allCorrect) return;

		display.forEachTile(tile -> tile.setBorder(Tile.TILE_VALID_BORDER));

		check.setDisable(true);
	}

	@FXML
	public void clear() {
		editorMode = true;
		grid = null;
		solution = null;

		display.setBlocked(false);
		display.forEachTile(tile -> {
			tile.setValue(Rules.EMPTY_TILE).setModifiable(true).setBorder(Tile.TILE_DEFAULT_BORDER);
			tile.setTextFill(Color.BLACK);
		});
	}

	public void setSize(Size size) {
		if (running) return;

		Rules.setSize(size);
		display.rebuild();
		setGrid();
	}

	public void setVisualizerRate(long milli) {
		visualizer.setUpdateRate(milli);
	}

	public void setDifficulty(int difficulty) {
		Rules.setDifficulty(difficulty);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setGrid();
		visualizer = new Visualizer(display, stepsDisplay, event -> {
			solve.setText(Strings.SOLVE);
			generate.setDisable(false);
			check.setDisable(false);
			clear.setDisable(false);
			running = false;
		});
	}

	private void setGrid() {
		Pair<char[][], char[][]> pair = Generator.generateGrid();
		grid = pair.getKey();
		solution = pair.getValue();
		display.showPuzzle(grid);
		check.setDisable(false);
	}

	@FXML
	public void sizeSmall() {
		setSize(Size.SMALL);
	}

	@FXML
	public void sizeNormal() {
		setSize(Size.NORMAL);
	}

	@FXML
	public void sizeBig() {
		setSize(Size.BIG);
	}

	@FXML
	public void rate1milli() {
		setVisualizerRate(1);
	}

	@FXML
	public void rate5milli() {
		setVisualizerRate(5);
	}

	@FXML
	public void rate20milli() {
		setVisualizerRate(20);
	}

	@FXML
	public void rate50milli() {
		setVisualizerRate(50);
	}

	@FXML
	public void rate100milli() {
		setVisualizerRate(100);
	}

	@FXML
	public void difficultyEasy() {
		setDifficulty(1);
	}

	@FXML
	public void difficultyNormal() {
		setDifficulty(2);
	}

	@FXML
	public void difficultyHard() {
		setDifficulty(3);
	}
}
