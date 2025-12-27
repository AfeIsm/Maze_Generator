# Maze_Generator

A Java based maze generator that creates a randomized maze based off the user's input, and solves it.

## What This Project Does

- It prompts the user for the maze width and maze height
- It generates the a randnomized fully connected maze based off the width and height given
- It builds the display of the maze using ASCII representation
- It solves the maze from:
  - Start : (0,0)
  - End : (height - 1, width - 1)
- Prints:
  1. The generated maze
  2. The solved maze with the solution path shown using *

## How the Algorithm Works

### Maze Generation (Frontier-Based Randomized Prim)
1. Start from a cell (defaults to (0,0) in the program
2. Add its neighbors to a frontier list
3. Repeatedly:
   - Pick a random frontier cell
   - Randomly connects to one of its neighbors in the maze
   - Then it adds its uninvited neighbors to the frontier
4. Continues until all the cells are included in the frontier list

This produces mazes that are 
- Random
- Fully Connected
- Has more branches (more complex)

### Maze Solving (DFS)

- Uses a Stack<Cell> to traverse the maze
- Tracks visited cells, so that it doesn't loop
- Stores a parent pointer for each visited cell to reconstruct the final path

## File Structure

This project is implemented into a singular file using different classes

-MazeGenerator.java
 - Cell (The row/column coordinates)
 - Maze (maze generation, adjacency map, ASCII builder/ renderer)
 - MazeSolver (DFS solver)
 - main() (runs generator + solver and prints the results)

## How to Run
1. Save and download the MazeGenerator file
2. Then open the file in the JDK, compile then run it
3. The program will first ask for the user width and height which you should enter
4. The program will then print the generated and solved mazes based off user input

## Future Ideas for program
- I could add difficulty settings by biasing corridors to be longer/shorter or adjusting the branching
- Try different maze solving algorithms
- Add an option to choose different start points in the maze, so that it doesn't always have to be (0,0)

