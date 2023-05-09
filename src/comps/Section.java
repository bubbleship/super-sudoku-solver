package comps;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import sudoku.Rules;

public class Section extends GridPane {

	public static final Border SECTION_BORDER = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM));

	final Tile[][] tiles;

	public Section(Display display) {
		final int sideSize = Rules.getSideSize();
		tiles = new Tile[sideSize][sideSize];

		setBorder(SECTION_BORDER);

		for (int row = 0; row < sideSize; row++)
			for (int column = 0; column < sideSize; column++) {
				Tile tile = new Tile(display);
				tile.prefWidthProperty().bind(this.widthProperty());
				tile.prefHeightProperty().bind(this.heightProperty());
				tiles[row][column] = tile;
				add(tile, column, row);
			}
	}
}
