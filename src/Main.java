public class Main {
    public static void main(String[] args) {

        MSolver board = new MSolver(3,8,3);
        board.fillBoard(
                "-1 1 0 0 0 0 0 0\n" +
                        "-1 2 0 0 0 1 1 1 \n" +
                        "-1 2 0 0 0 1 -1 -1");
        while(board.getMines() != 0) {
            board.attemptSolve();
            board.printBoard();
            board.fillBoardManually();
        }
    }
}