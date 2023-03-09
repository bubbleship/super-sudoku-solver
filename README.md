# sudoku-solver

![Licence](https://img.shields.io/github/license/bubbleship/sudoku-solver)
![Forks](https://img.shields.io/github/forks/bubbleship/sudoku-solver)
![Stars](https://img.shields.io/github/stars/bubbleship/sudoku-solver)
![Issues](https://img.shields.io/github/issues/bubbleship/sudoku-solver)

## A visualizer for the sudoku solving backtracking algorithm

### Description

An interactive JavaFX application that visualizes the steps taken by the backtracking
algorithm to solve a sudoku puzzle. The implementation used in this visualizer was modified
to allow support for grids other than the standard 9x9. In addition, this visualizer is
capable of generating a sudoku puzzle by itself so there is no need to manually input one.

### Features & Settings

* Modifiable visualization speed: The amount of time each step is displayed during a
  visualization. This setting may be changed at any time, even while a visualization
  is running.
  <br>
  Available visualization speeds:
	* 1 millisecond
	* 5 milliseconds
	* 20 milliseconds
	* 50 milliseconds
	* 100 milliseconds
* Modifiable grid: This application demonstrate the backtracking algorithm on more than the
  standard grid.
  <br>
  Supported grids:
	* 4x4 (small)
	* 9x9 (normal)
	* 16x16 (big)
* Difficulty: Affects the number of hints the puzzle begins with.
  <br>
  Possible difficulties:
	* Easy
	* Normal
	* Hard

### How To Run

To run this application first download the executable .jar file from the
[releases](https://github.com/bubbleship/sudoku-solver/releases) section of
this repository and simply run it as it is.

### Requirements

* Java 1.8 and newer.
