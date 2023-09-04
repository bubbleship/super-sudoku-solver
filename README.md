# super-sudoku-solver

![Licence](https://img.shields.io/github/license/bubbleship/super-sudoku-solver)
![Forks](https://img.shields.io/github/forks/bubbleship/super-sudoku-solver)
![Stars](https://img.shields.io/github/stars/bubbleship/super-sudoku-solver)
![Issues](https://img.shields.io/github/issues/bubbleship/super-sudoku-solver)

## An extended visualizer of the sudoku solving backtracking algorithm

### Description

An interactive JavaFX application that visualizes the steps taken by the backtracking
algorithm to solve a sudoku puzzle. The implementation used in this visualizer was modified to
allow support for grids other than the standard 9x9. In addition, and for convenience, this
visualizer can generate a sudoku puzzle by itself so there is no need to manually input one.

### How it Works

**Generation:**
<br>
The puzzle generation process can be divided into two parts:

1. Generating a complete sudoku grid.
   <br>
   This is done by first filling the sections on the main diagonal of the grid with the sudoku
   dictionary in a random order (on a 9x9 grid, for example, every section is 3x3 tiles and the
   sudoku dictionary is the digits 1-9). The next step is to solve the grid with a randomized
   version of the backtracking algorithm. The diagonal placement step is an optimization step taken
   before the backtracking step and is skipped with 4x4 grids as it often results in puzzles with
   no solutions.
2. Creating the puzzle.
   <br>
   This is done by clearing randomly selected tiles from the complete sudoku grid. For each tile
   cleared a modified implementation of the backtracking algorithm is called to check the
   uniqueness of its solution. If the grid has multiple solutions, then the tile is restored and a
   different tile is selected in its place. Doing so ensures that at the end of this process we end
   up with a sudoku puzzle that has exactly one solution.

**Solution:**
<br>
The backtracking algorithm is as follows:

1. Find the next empty tile.
2. Attempt at placing a symbol from the sudoku dictionary in that tile.
3. Check if the placement is valid that is, if it satisfies the rules of sudoku.
    1. If the placement is valid, recursively attempt to solve the grid from step 1.
    2. If the placement is invalid, try the next symbol on step 2.
    3. If all placements are invalid, backtrack to an earlier recursive call.
4. Once step 1 fails to find the next empty tile, the grid is complete. The state of the grid at
   this point is the solution of the puzzle.

The visualization in this application is done by saving every attempted placement (and whether it
was valid) into a queue data structure. The data structure is then used to visualize the steps of
the algorithm to the UI.

### Menus

* The **Speed** menu (visualization rate):
  <br>
  Controls the amount of time each step is displayed during a visualization.
  This setting can be changed at any time, even while a visualization is in progress.
  <br>
  Menu options:
    * 1 millisecond
    * 5 milliseconds
    * 20 milliseconds
    * 50 milliseconds
    * 100 milliseconds
* The **Size** menu (grid dimensions):
  <br>
  Allows the user to visualize the backtracking algorithm on grids with varied sizes.
  This setting cannot be used while a visualization is in progress.
  <br>
  Menu options:
    * Small (4x4)
    * Normal (9x9)
    * Big (16x16)
* The **Difficulty** menu: Affects the number of hints the puzzle begins with.
  <br>
  Menu options:
    * Easy (more hints)
    * Normal
  * Hard (fewer hints)

### Notes

Even though this application can generate a sudoku puzzle it is still possible to manually input
a puzzle and having the visualizer solve it. This can be done after clearing the grid with the
**clear** feature.
<br>
In addition, it is also possible to enter a solution manually and check its correctness before
starting the visualizer.

### How to Run

To run this application first download the executable .jar file from the
[releases](https://github.com/bubbleship/super-sudoku-solver/releases) section of
this repository and simply run it.
<br>
Running this application does not install anything nor leave any files behind.

### Requirements

* Java 1.8 and newer.
