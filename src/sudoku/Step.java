package sudoku;

import javafx.scene.layout.Border;

class Step {
	final char tileValue;
	final Border tileBorder;
	final int row;
	final int column;

	public Step(int row, int column, char tileValue, Border tileBorder) {
		this.row = row;
		this.column = column;
		this.tileValue = tileValue;
		this.tileBorder = tileBorder;
	}
}
