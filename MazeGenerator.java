import java.util.*;


public class MazeGenerator
{
    
    static class Cell 
    {
        final int row;
        final int column;

        Cell(int row, int column) 
        {
            this.row = row;
            this.column= column;
        }

        @Override
        public boolean equals(Object obj) 
        {
            if (this == obj) 
            {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) 
            {
                return false;
            }
            Cell other = (Cell) obj;
            return this.row == other.row && this.column == other.column;
        }

        @Override
        public int hashCode() 
        {
            return Objects.hash(row, column);
        }

        @Override
        public String toString() 
        {
            return "(" + row + "," + column + ")";
        }
    }

    static class Maze 
    {
        private final int width;
        private final int height;
        private final Map<Cell, Set<Cell>> adjacency;
        private final Random random = new Random();

        Maze(int width, int height) 
        {
            if (width <= 0 || height <= 0) 
            {
                throw new IllegalArgumentException("Width and height must be positive.");
            }
            this.width = width;
            this.height = height;
            this.adjacency = new LinkedHashMap<>();

            for (int r = 0; r < height; r++) 
            {
                for (int c = 0; c < width; c++) 
                {
                    Cell cell = new Cell(r, c);
                    adjacency.put(cell, new LinkedHashSet<>());
                }
            }
        }

 //Prim algo, fix it
        void generateMaze(Cell start) 
        {
            boolean[][] inMaze = new boolean[height][width];
            boolean[][] inFrontier = new boolean[height][width];
            List<Cell> frontier = new ArrayList<>();

            Cell first;
            if (start != null) 
            {
                first = start;
            } else {
                int r = random.nextInt(height);
                int c = random.nextInt(width);
                first = new Cell(r, c);
            }

            inMaze[first.row][first.column] = true;
            addFrontierCells(first, inMaze, inFrontier, frontier);

            while (!frontier.isEmpty()) 
            {
                int index = random.nextInt(frontier.size());
                Cell cell = frontier.remove(index);
                inFrontier[cell.row][cell.column] = false;

                List<Cell> neighboursInMaze = new ArrayList<>();
                for (Cell nb : getNeighbourCells(cell)) 
                {
                    if (inMaze[nb.row][nb.column]) 
                    {
                        neighboursInMaze.add(nb);
                    }
                }

                if (!neighboursInMaze.isEmpty()) 
                {

                    Cell neighbour = neighboursInMaze.get(random.nextInt(neighboursInMaze.size()));
                    connectCells(cell, neighbour);
                }

                inMaze[cell.row][cell.column] = true;
                addFrontierCells(cell, inMaze, inFrontier, frontier);
            }
        }
//cells add them
        private void addFrontierCells(Cell cell, boolean[][] inMaze, boolean[][] inFrontier,
                                      List<Cell> frontier) 
                                      {
            for (Cell nb : getNeighbourCells(cell)) {
                if (!inMaze[nb.row][nb.column] && !inFrontier[nb.row][nb.column]) 
                {
                    frontier.add(nb);
                    inFrontier[nb.row][nb.column] = true;
                }
            }
        }


        private void connectCells(Cell a, Cell b) 
        {
            Set<Cell> aNeighbours = adjacency.get(a);
            if (aNeighbours == null) 
            {
                aNeighbours = new LinkedHashSet<>();
                adjacency.put(a, aNeighbours);
            }
            aNeighbours.add(b);

            Set<Cell> bNeighbours = adjacency.get(b);
            if (bNeighbours == null) 
            {
                bNeighbours = new LinkedHashSet<>();
                adjacency.put(b, bNeighbours);
            }
            bNeighbours.add(a);
        }

        private List<Cell> getNeighbourCells(Cell cell) 
        {
            List<Cell> neighbours = new ArrayList<>(4);
            int r = cell.row;
            int c = cell.column;

            if (r > 0) 
            {
                neighbours.add(new Cell(r - 1, c));
            }
            if (r < height - 1) 
            {
                neighbours.add(new Cell(r + 1, c));
            }
            if (c > 0) 
            {
                neighbours.add(new Cell(r, c - 1));
            }
            if (c < width - 1) 
            {
                neighbours.add(new Cell(r, c + 1));
            }

            return neighbours;
        }

        char[][] buildAscii(Cell start, Cell goal) 
        {
            int rows = height * 2 + 1;
            int columns = width * 2 + 1;
            char[][] grid = new char[rows][columns];

            for (int r = 0; r < rows; r++) 
            {
                Arrays.fill(grid[r], '#');
            }


            for (Cell cell : adjacency.keySet()) 
            {
                int r = cell.row;
                int c = cell.column;
                grid[2 * r + 1][2 * c + 1] = ' ';

                for (Cell nb : adjacency.get(cell)) 
                {
                    int nr = nb.row;
                    int nc = nb.column;

                    int mr = (r + nr) + 1;
                    int mc = (c + nc) + 1;
                    grid[mr][mc] = ' ';
                }
            }


            if (start != null) 
            {
                grid[2 * start.row + 1][0] = ' ';
            }
            if (goal != null) 
            {
                grid[2 * goal.row + 1][columns - 1] = ' ';
            }
            return grid;
        }

        void printAscii(char[][] grid) 
        {
            for (char[] row : grid) 
            {
                System.out.println(new String(row));
            }
        }


        void printAsciiPretty(char[][] grid, Cell start, Cell goal, List<Cell> path, boolean showPath) 
        {
            int rows = grid.length;
            int columns = grid[0].length;

            char[][] tmp = new char[rows][columns];
            for (int r = 0; r < rows; r++) 
            {
                tmp[r] = Arrays.copyOf(grid[r], columns);
            }

            if (showPath && path != null && !path.isEmpty()) 
            {
                for (int i = 1; i < path.size() - 1; i++) 
                {
                    Cell cell = path.get(i);
                    int ar = 2 * cell.row + 1;
                    int ac = 2 * cell.column + 1;
                    if (tmp[ar][ac] == ' ') 
                    {
                        tmp[ar][ac] = '*';
                    }
                }
            }

            if (start != null) 
            {
                int sr = 2 * start.row + 1;
                int sc = 2 * start.column + 1;
                tmp[sr][sc] = 'S';
            }
            if (goal != null) 
            {
                int gr = 2 * goal.row + 1;
                int gc = 2 * goal.column + 1;
                tmp[gr][gc] = 'E';
            }

            for (int r = 0; r < rows; r++) 
            {
                StringBuilder line = new StringBuilder();
                for (int c = 0; c < columns; c++) 
                {
                    char ch = tmp[r][c];
                    if (ch == '#') 
                    {
                        if (r % 2 == 0 && c % 2 == 0) 
                        {
                            line.append('+');
                        } else if (r % 2 == 0 && c % 2 == 1) 
                        {
                            line.append('-');
                        } else if (r % 2 == 1 && c % 2 == 0) 
                        {
                            line.append('|');
                        } else 
                        {
                            line.append(' ');
                        }
                    } else 
                    {
                        line.append(ch);
                    }
                }
                System.out.println(line.toString());
            }
        }

        char[][] overlayPath(char[][] ascii, List<Cell> path) 
        {
            char[][] withPath = new char[ascii.length][];
            for (int i = 0; i < ascii.length; i++) 
            {
                withPath[i] = Arrays.copyOf(ascii[i], ascii[i].length);
            }

            for (int i = 1; i < path.size() - 1; i++) 
            {
                Cell cell = path.get(i);
                int ar = 2 * cell.row + 1;
                int ac = 2 * cell.column + 1;
                if (withPath[ar][ac] == ' ') 
                {
                    withPath[ar][ac] = '*'; 
                }
            }
            return withPath;
        }
    }

    static class MazeSolver 
    {
        private final Maze maze;

        MazeSolver(Maze maze) 
        {
            this.maze = maze;
        }

        List<Cell> solve(Cell start, Cell goal) 
        {
            Set<Cell> visited = new HashSet<>();
            Map<Cell, Cell> parent = new HashMap<>();
            Stack<Cell> stack = new Stack<>();
            stack.push(start);
            
            visited.add(start);

            boolean found = false;

            while (!stack.isEmpty()) 
            {
                Cell current = stack.pop();
                if (current.equals(goal)) 
                {
                    found = true;
                    break;
                }

                for (Cell neighbour : maze.adjacency.getOrDefault(current, Collections.emptySet())) {
                    if (!visited.contains(neighbour)) 
                    {
                        visited.add(neighbour);
                        parent.put(neighbour, current);
                        stack.push(neighbour);
                    }
                }
            }

            if (!found) 
            {
                return Collections.emptyList();
            }

            List<Cell> path = new ArrayList<>();
            Cell step = goal;
            while (step != null && !step.equals(start)) 
            {
                path.add(step);
                step = parent.get(step);
            }
            path.add(start);
            Collections.reverse(path);
            return path;
        }
    }


    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Maze Generator and Solver ===");
        System.out.print("Enter maze width: ");
        int width = scanner.nextInt();
        System.out.print("Enter maze height: ");
        int height = scanner.nextInt();

        Maze maze = new Maze(width, height);
        Cell start = new Cell(0, 0);
        Cell goal = new Cell(height - 1, width - 1);
        maze.generateMaze(start);

        MazeSolver solver = new MazeSolver(maze);
        List<Cell> solutionPath = solver.solve(start, goal);

        char[][] ascii = maze.buildAscii(start, goal);
        System.out.println("\nGenerated Maze:");
        maze.printAsciiPretty(ascii, start, goal, null, false);

        if (!solutionPath.isEmpty()) 
        {
            System.out.println("\nMaze Solver:");
            maze.printAsciiPretty(ascii, start, goal, solutionPath, true);
        } else 
        {
            System.out.println("\nNo path found from start to goal.");
        }
    }
}
