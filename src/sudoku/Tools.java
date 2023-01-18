package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Tools {

	public static char[] copyArray(char[] array) {
		return Arrays.copyOf(array, array.length);
	}

	public static char[][] copyGrid(char[][] grid) {
		char[][] copy = new char[grid.length][];
		Arrays.setAll(copy, i -> copyArray(grid[i]));
		return copy;
	}

	public static List<Character> toList(char[] array) {
		ArrayList<Character> list = new ArrayList<>(array.length);
		for (char c : array) list.add(c);
		return list;
	}
}
