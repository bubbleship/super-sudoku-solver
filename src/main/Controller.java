package main;

import comps.Display;
import comps.Tile;
import consts.Strings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

	@FXML
	public Display display;
	@FXML
	public Button solve;
	@FXML
	public Button generate;
	@FXML
	public Button check;
	@FXML
	public Label stepsDisplay;

	@FXML
	public void solve() {
		check.setDisable(true);
		if (running) {
			visualizer.skip();
			return;
		}
		solve.setText(Strings.SKIP);
		generate.setDisable(true);

		visualizer.reset(grid, solution);
		display.prepGrid(true);
		visualizer.start();
		running = true;
	}

	@FXML
	public void generate() {
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
				if (value != solution[row][column]) {
					allCorrect = false;
					display.setTileAt(row, column, value, Tile.TILE_INVALID_BORDER);
				}
			}

		if (!allCorrect) return;

		for (int row = 0; row < grid.length; row++)
			for (int column = 0; column < grid[row].length; column++) {
				display.setTileAt(row, column, grid[row][column], Tile.TILE_VALID_BORDER);
				display.getTileAt(row, column).setModifiable(false);
			}

		check.setDisable(true);
	}

	public void setSize(Size size) {
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
			running = false;
		});
	}

	private void setGrid() {
		Pair<char[][], char[][]> pair = Generator.generateGrid();
		grid = pair.getKey();
		solution = pair.getValue();
		display.setGrid(grid);
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
