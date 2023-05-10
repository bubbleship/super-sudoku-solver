package sudoku;

import comps.Display;
import comps.Tile;
import consts.Strings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.util.Duration;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

import static sudoku.Commons.getFirstEmpty;
import static sudoku.Rules.EMPTY_TILE;
import static sudoku.Rules.isValidPlacement;

public class Visualizer {
	/**
	 * An upper bound representing the maximum number of calls the
	 * {@link #recordAlgorithmSteps(Queue, char[][])} method may call itself. This is to prevent the
	 * visualizer from consuming too much memory and lingering on that step for an overly extended period,
	 * which can make the entire application unresponsive.
	 */
	private static final int MAX_ALGORITHM_RECORDER_CALLS_COUNT = 600_000;
	private static final long DEFAULT_RATE = 20;

	private static final Alert recorderStuckAlert = new Alert(Alert.AlertType.ERROR, "Sudoku puzzle is taking too long to solve");
	private static char[] validChars;
	private static int algorithmRecorderCallsCount;

	private final Display display;
	private final Label stepsDisplay;
	private final Timeline timeline;
	private final Queue<Step> steps;
	private char[][] grid;
	private char[][] solution;
	private long rate = DEFAULT_RATE;

	public Visualizer(Display display, Label stepsDisplay, EventHandler<ActionEvent> onVisualizerFinishedHandler) {
		this.display = display;
		this.stepsDisplay = stepsDisplay;
		timeline = new Timeline();
		timeline.setOnFinished(event -> {
			onVisualizerFinishedHandler.handle(event);
			timeline.getKeyFrames().clear();
		});
		steps = new LinkedList<>();
	}

	/**
	 * An implementation of the backtracking algorithm which was modified, so it records the steps taken by
	 * it to solve the given puzzle. The steps are saved into the given data structure which can be later
	 * used for the visualization part.
	 *
	 * @param steps A reference to a {@link Queue<Step>} object into which the steps taken by the algorithm
	 *              are saved.
	 * @param grid  A reference to a matrix of characters representing the puzzle to solve.
	 */
	private static boolean recordAlgorithmSteps(Queue<Step> steps, char[][] grid) {
		algorithmRecorderCallsCount--;
		if (algorithmRecorderCallsCount < 0) return false;

		Point emptyPos = getFirstEmpty(grid);
		if (emptyPos == null) return true;

		int row = emptyPos.x;
		int column = emptyPos.y;

		for (char value : validChars)
			if (isValidPlacement(grid, value, row, column)) {
				grid[row][column] = value;
				addStep(steps, row, column, value, Tile.TILE_VALID_BORDER); // Step: attempted placement is valid.
				addStep(steps, row, column, value, Tile.TILE_DEFAULT_BORDER); // Step: continue to next tile (if there is one).
				if (recordAlgorithmSteps(steps, grid)) return true;
				if (algorithmRecorderCallsCount < 0) return false;
			} else {
				addStep(steps, row, column, value, Tile.TILE_INVALID_BORDER); // Step: attempted placement is invalid.
			}

		grid[row][column] = EMPTY_TILE;
		addStep(steps, row, column, EMPTY_TILE, Tile.TILE_INVALID_BORDER); // Step: backtracking to previous tile.
		return false;
	}

	private static void addStep(Queue<Step> steps, int row, int column, char value, Border border) {
		steps.add(new Step(row, column, value, border));
	}

	public void reset(char[][] grid) {
		this.grid = grid;
	}

	public void setUpdateRate(long rate) {
		this.rate = rate;
		if (steps.isEmpty()) return;
		timeline.stop();
		timeline.setCycleCount(steps.size());
		timeline.getKeyFrames().clear();
		timeline.getKeyFrames().add(getFrame());
		timeline.play();
	}

	public void start() {
		validChars = Rules.getValidChars();
		solution = Tools.copyGrid(grid);

		algorithmRecorderCallsCount = MAX_ALGORITHM_RECORDER_CALLS_COUNT;
		recordAlgorithmSteps(steps, solution);
		if (algorithmRecorderCallsCount < 0) {
			recorderStuckAlert.show();
			skip();
			return;
		}

		stepsDisplay.setText(Strings.STEPS_DISPLAY_PREFIX + steps.size());

		if (steps.size() == 0) {
			skip();
			return;
		}
		timeline.setCycleCount(steps.size());
		timeline.getKeyFrames().add(getFrame());
		timeline.play();
	}

	private KeyFrame getFrame() {
		return new KeyFrame(Duration.millis(rate), event -> {
			Step step = steps.poll();
			stepsDisplay.setText(Strings.STEPS_DISPLAY_PREFIX + steps.size());
			if (step == null) return;
			display.getTileAt(step.row, step.column).setValue(step.tileValue).setBorder(step.tileBorder);
		});
	}

	public void skip() {
		timeline.stop();
		steps.clear();
		stepsDisplay.setText(Strings.STEPS_DISPLAY_PREFIX + 0);
		display.showSolution(solution);
		timeline.getOnFinished().handle(null);
	}
}
