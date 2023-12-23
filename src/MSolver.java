import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Used to attempt to solve a user inputted Minesweeper game
 */
public class MSolver {

    /* 2d array representing the board
     * -2 for mine
     * -1 for unrevealed cells
     * 0-8 for number of adjacent mines
     */
    private final GridSquare[][] board;

    // the number of unflagged mines left on the board
    private int minesLeft;

    // a list of all gridSquares that have a current val 1-8
    private ArrayList<GridSquare> activeSquares;

    //Class constants used to represent mines and unrevealed values
    private final int MINE = -2;
    private final int UNREVEALED = -1;

    /**
     * Constructs a new MSolver, populates the board with GridSquares
     * @param rows the number of rows in the board
     * @param columns the number of columns in the board
     * @param numMines the number of mines initially on the board
     */
    public MSolver(int rows, int columns, int numMines) {
        // set mineCount
        minesLeft = numMines;

        //initialize the board
        board = new GridSquare[rows][columns];

        // initialize list of active squares
        activeSquares = new ArrayList<>();


        // fill the board with GridSqaures
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j< board[i].length; j++) {

                // declare the new board
                board[i][j] = new GridSquare();

                // populate the boards column and row values
                board[i][j].column = j;
                board[i][j].row = i;
            }
        }
    }

    /**
     * getter method for minesleft field
     */
    public int getMines() {
        return this.minesLeft;
    }

    /**
     * setter method for minesleft field
     * @param count the new count
     */
    public void setMines(int count) {
        minesLeft = count;
    }

    /**
     * Prints the board to the console used for debugging
     */
    public void printBoard() {
        for(int i = 0; i < board.length; i++) {
            System.out.print("row " + (i + 1) + " [");
            for(int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j].value);
                if (j < board[i].length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
    }

    /**
     * Allows the user to manually input the board state from the console.
     * Used for debugging
     */
    public void fillBoardManually() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the board state. Use -2 for mines, " +
                "-1 for unrevealed cells, " +
                "and 0-8 for number of adjacent mines:");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.printf("Enter value for cell (%d, %d): ",
                        i + 1, j + 1);
                int cellValue = scanner.nextInt();
                board[i][j].setValue(cellValue);
                checkActive(board[i][j]);
            }
        }
    }

    /**
     * Fills the entire board with values from a single string input.
     * The input should contain rows of the board separated by new lines,
     * and each value in the row should be separated by spaces.
     * used for debugging
     *
     * @param input The entire board configuration as a string.
     */
    public void fillBoard(String input) {
        String[] rows = input.split("\n");
        for (int i = 0; i < rows.length; i++) {
            String[] values = rows[i].trim().split("\\s+");
            for (int j = 0; j < values.length; j++) {
                int cellValue = Integer.parseInt(values[j]);
                board[i][j].setValue(cellValue);
                checkActive(board[i][j]);
            }
        }
    }

    /**
     * Checks if the square is active(has a number1-8) if true adds it to
     * list activeSquares
     * @param curr the square to be checked
     */
    private void checkActive(GridSquare curr) {
        if (curr.value > 0) {
            activeSquares.add(curr);
        }
    }

    /**
     * Attempts to solve the current board
     */
    public void attemptSolve() {
        boolean change;
        do {
            change = false;
            for(GridSquare curr : activeSquares) {
                change = solveHelper(curr.getNeighbors(board), curr);
            }
        }while(change);
    }

    /**
     * helper method for attmept solve processes individual GridSquares
     */
    private boolean solveHelper(List<GridSquare> neighbors, GridSquare curr) {

        boolean change = false;
        int numMines = countType(neighbors, MINE);
        int unrevealed = countType(neighbors, UNREVEALED);

        // case where # of mines = square value
        // in this case all remaining unrevealed are safe
        if(numMines == curr.value) {
            for(GridSquare square : neighbors) {
                if(square.value == UNREVEALED) {
                    square.setValue(-3); // value for cells to be clicked
                    // click on cell value
                    change = true;
                }
            }
        }

        // case where #mines + #unrevealed = cell value
        if(numMines + unrevealed == curr.value) {
            for(GridSquare square : neighbors) {
                if(square.value == UNREVEALED) {
                    square.setValue(MINE); // value for mine
                    change = true;
                    setMines(getMines() - 1); // updated mine count
                }
            }
        }
        return change;
    }

    /**
     * counts and returns the number of GridSquares that are of the
     * passed type list
     * @param list the list to be counted
     * @param type the type of square to be counted
     */
    private int countType(List<GridSquare> list, int type){
        int count = 0;
        for(GridSquare curr : list) {
            if(curr.value == type) {count++;}
        }
        return count;
    }

    /**
     * represents a grid square on a minesweeper board
     */
    private class GridSquare {
        /*
        Represents what the square contains
            -2 for mine
            -1 for unrevealed
            0 for 0 neighbors
            positive int x for a number value in the cell
         */
        private int value;

        //stores column index
        private int row;

        // stores row index
        private int column;

        public GridSquare() {}

        /**
         * GridSquare value setter method
         * @param val new value
         */
        public void setValue(int val) {
            this.value = val;
        }

        /**
         * Returns a list of all bordering neighbors
         * @param board the board
         */
        public List<GridSquare> getNeighbors(GridSquare[][] board) {
            List<GridSquare> neighbors = new ArrayList<>();

            int numRows = board.length;
            int numCols = board[0].length;

            // Define the relative positions of the 8 neighboring squares
            int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
            int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 };

            for (int i = 0; i < 8; i++) {
                int newRow = this.row + dx[i];
                int newCol = this.column + dy[i];

                // Check if the new position is within bounds
                if (newRow >= 0 && newRow < numRows && newCol >= 0
                        && newCol < numCols) {
                    neighbors.add(board[newRow][newCol]);
                }
            }
            return neighbors;
        }
    }
}