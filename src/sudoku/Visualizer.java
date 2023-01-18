package sudoku;

import comps.Display;
import comps.Tile;
import consts.Strings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Queue;

public class Visualizer {
	private static final long DEFAULT_RATE = 20;

	private char[][] grid;
	private Display display;
	private Label stepsDisplay;
	private Timeline timeline;
	private long rate = DEFAULT_RATE;
	private volatile Queue<Step> steps;

	public void reset(char[][] grid, Display display, Label stepsDisplay) {
		this.grid = grid;
		this.display = display;
		this.stepsDisplay = stepsDisplay;

		timeline = new Timeline();
	}

	public void setOnFinished(EventHandler<ActionEvent> handler) {
		timeline.setOnFinished(event -> {
			handler.handle(event);
			timeline.getKeyFrames().clear();
		});
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
		steps = Solver.solve(Tools.copyGrid(grid));
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
			display.setTileAt(step.row, step.column, step.tileValue, step.tileBorder);
		});
	}

	public void skip() {
		timeline.stop();
		stepsDisplay.setText(Strings.STEPS_DISPLAY_PREFIX + 0);
		Step step = steps.poll();
		while (step != null) {
			display.setTileAt(step.row, step.column, step.tileValue, Tile.TILE_DEFAULT_BORDER);
			step = steps.poll();
		}
		timeline.getOnFinished().handle(null);
	}
}
