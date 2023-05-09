package comps;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import sudoku.Rules;

import static sudoku.Rules.EMPTY_TILE;

public class Tile extends Label {

	public static final Border TILE_DEFAULT_BORDER = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN));
	public static final Border TILE_FOCUS_BORDER = new Border(new BorderStroke(Color.AQUA, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM));
	public static final Border TILE_VALID_BORDER = new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM));
	public static final Border TILE_INVALID_BORDER = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM));

	private final Display display;
	private char value;
	private boolean modifiable = true;

	public Tile(Display display) {
		this.display = display;

		setAlignment(Pos.CENTER);
		setBorder(TILE_DEFAULT_BORDER);

		setOnMouseClicked(this::processMouseClick);
		focusedProperty().addListener(this::processFocusChange);
		setOnKeyPressed(this::processKeyInput);
	}

	private void processMouseClick(MouseEvent event) {
		if (isFocused()) getParent().requestFocus();
		else requestFocus();
	}

	private void processFocusChange(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		if (!modifiable || display.isBlocked()) return;
		if (newValue) setBorder(TILE_FOCUS_BORDER);
		else setBorder(TILE_DEFAULT_BORDER);
	}

	private void processKeyInput(KeyEvent event) {
		if (!modifiable || display.isBlocked()) return;
		char c = event.getText().charAt(0);
		if (Rules.isValidChar(c)) setValue(c);
		else if (c == ' ') setValue(EMPTY_TILE);
	}

	public char getValue() {
		return value;
	}

	public Tile setValue(char value) {
		this.value = value;
		if (value == EMPTY_TILE) setText("");
		else setText(String.valueOf(value));
		return this;
	}

	public Tile setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
		return this;
	}

	public boolean isModifiable() {
		return modifiable;
	}
}
