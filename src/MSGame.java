import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Class used to simulate a minesweeper game used to test MSolver
 */
public class MSGame {

    private int[][] actualBoard;

    private int[][] userBoard;

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
        this.rows = rows;
        this.columns = columns;
        actualBoard = new int[rows][columns];
        generateBoard(firstX, firstY, mineCount);
    }

    /**
     * Genrates the board based on the users first click
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

        // fill out board for non-mine squares
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                // case where we have a none mine square
                int count  = 0;
                if(actualBoard[i][j] != MINE) {
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
}
