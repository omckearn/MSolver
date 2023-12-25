import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Class used to simulate a minesweeper game used to test MSolver
 */
public class MSGame {

    // represents the actual game board
    private int[][] actualBoard;

    // represents what the user will see
    private int[][] userBoard;

    private boolean gameOver;

    private int rows;
    private int columns;
    private final int MINE = -1;

    /**
     * Constructor
     * @param firstX x cord of first click, must be within board bounds
     * @param firstY cord of first click, must be within board bounds
     * @param mineCount the number of mines on the board
     */
    public void MSGame(int firstX, int firstY, int mineCount,
                       int rows, int columns) {
        this.gameOver = false;
        this.rows = rows;
        this.columns = columns;
        actualBoard = new int[rows][columns];
        generateBoard(firstX, firstY, mineCount);
    }

    /**
     * Generates the game boards based on the users first click
     * @param firstX x cord of first click, must be within board bounds
     * @param firstY cord of first click, must be within board bounds
     * @param mineCount the number of mines on the board
     */
    private void generateBoard(int firstX, int firstY, int mineCount){
        int[] firstClick = {firstX, firstY};

        // get list of mines and place them
        HashSet<int[]> mineList = generateMines(firstClick, mineCount);
        for(int[] curr : mineList) {
            actualBoard[curr[0]][curr[1]] = MINE;
        }

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                // populate user board
                userBoard[i][j] = -1;

                // populate game board
                int count  = 0;
                if(actualBoard[i][j] != MINE) { // populate game board
                    ArrayList<int[]> neighbors = getNeighbors(i, j);
                    for(int[] curr: neighbors) {
                        if(actualBoard[curr[0]][curr[1]] == MINE) {
                            count++;
                        }
                    }
                }
                actualBoard[i][j] = count;
            }
        }
        // get user click somehow userBoard[firstX][firstY] =
    }

    /**
     * Returns a list of unique random x y pairs within the boards bounds
     * @param firstClick the users first click, excluded from the selection
     * @param mineCount the number of pairs to be generated
     */
    private HashSet<int[]> generateMines(int[] firstClick, int mineCount) {
        // list of pairs
        HashSet<int[]> minePairs = new HashSet<>();

        // add the user input to the set now so it can't add randomly later
        minePairs.add(firstClick);

        Random random = new Random();
        while(minePairs.size() >= mineCount) {
            int x = random.nextInt(rows + 1);
            int y = random.nextInt(columns + 1);
            int[] curr = {x, y};
            minePairs.add(curr);
        }

        // remove the first click you added before
        minePairs.remove(firstClick);
        return minePairs;
    }

    /**
     * Returns a list of neighboring squares from a passed array index
     * @param x x index
     * @param y y index
     */
    private ArrayList<int[]> getNeighbors(int x, int y) {
        ArrayList<int[]> neighbors = new ArrayList<>();

        // Define the relative positions of the 8 neighboring squares
        int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 };

        for (int i = 0; i < 8; i++) {
            int newRow = this.rows + dx[i];
            int newCol = this.columns + dy[i];

            // Check if the new position is within bounds
            if (newRow >= 0 && newRow < rows && newCol >= 0
                    && newCol < columns) {
                int[] toAdd = {newRow, newCol};
                neighbors.add(toAdd);
            }
        }
        return neighbors;
    }

    /**
     *
     */
    private void userSolve() {
        while(!gameOver) {
            //userClick();
            if(userBoard == actualBoard) {
                gameOver = true;
            }
        }
    }


    /**
     *
     * @param x x coordinate of click
     * @param y y coordinate of click
     */
    private void userClick(int x, int y) {
        if(actualBoard[x][y] != MINE) {
            userBoard[x][y] = actualBoard[x][y];
            if(userBoard[x][y] == 0) {
                revealZeros(x,y);
            }
        }else {
            gameOver = true;
        }
    }

    /**
     * In Minesweeper, a single click on an empty square
     * (a square without adjacent mines) can reveal a contiguous area of empty
     * squares, extending until it reaches squares adjacent to mines. This
     * method does this for a empty square
     * @param x y index of square
     * @param y x index of square
     */
    private void revealZeros(int x, int y) {
        ArrayList<int[]> neighbors = getNeighbors(x,y);
        for(int[] curr : neighbors) {
            int row = curr[0];
            int column = curr[1];
            if(actualBoard[row][column] == 0) {
                userBoard[row][column] = 0;
                revealZeros(row, column);
            }
        }
    }
}
