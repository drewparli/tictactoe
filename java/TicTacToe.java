import java.util.Scanner;

/**
 * This is a traditional TicTacToe game.
 * By default, the board is set to 3x3, but the class is generalized for other
 * reasonable sizes. Simply change the BOARD_SIZE variable below.
 *
 * @author Andrew Tristan Parli {@literal<qcm239@alumni.ku.dk>}
 * @version 1.0
 * @since 2014-11-25
 */

public class TicTacToe {
    /* Constansts */
    public static final int BOARD_SIZE = 3;
    public static final char EMPTY = ' ';

    /* Static Variables */
    private static Scanner in = new Scanner(System.in);
    private static char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
    private static char currentPlayer;
    private static boolean isWin;
    private static int moveCount;

    /* Update variables used to check for wins */
    private static int[] rowSums = new int[BOARD_SIZE];
    private static int[] colSums = new int[BOARD_SIZE];
    private static int diag;
    private static int antidiag;

    /**
     * Creates a new playing board
     */
    public static void newBoard() {
        isWin = false;
        currentPlayer = 'O';
        moveCount = 0;
        diag = 0;
        antidiag = 0;

        for(int i = 0; i < BOARD_SIZE; i++) {
            rowSums[i] = 0;
            colSums[i] = 0;

            for(int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    /**
     * Prints the current board with formatting
     */
    public static void printBoard() {
        printRow("\n    ", " %d  ", true);    // column index
        printRow("\n   +", "---+", false);    // top border

        for(int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("\n%d  |", i);  // row index

            for(int j = 0; j < BOARD_SIZE; j++) {
                System.out.printf(" " + board[i][j] + " |");  // existing moves
            }

            printRow("\n   +", "---+", false); // bottom border
        }

        System.out.printf("\n\n");
    }

    /**
     * Prints a row of the board
     */
    public static void printRow(String padding,
                                String repeatMe,
                                boolean formatRow) {
        // System.out.println("\n");
        System.out.printf(padding);
        for(int row = 0; row < BOARD_SIZE; row++) {
            if (formatRow) {
                System.out.printf(repeatMe, row);
            } else {
                System.out.printf(repeatMe);
            }
        }
    }

    /**
     * Returns a box's symbol value
     * @param row the number of the desired row
     * @param column the number of the desired column
     */
    public static char get(int row, int column) {
        return board[row][column];
    }

    /**
     * Assigns a box on the board with symbol value
     * @param row the number of the desired row
     * @param column the number of the desired column
     * @param val the symbol value to be assigned to the board
     */
    public static void set(int row, int column, char val) {
        board[row][column] = val;
    }

    /**
     * Returns true for a win or draw after all legal moves have been made.
     * Otherwise returns false, and the game continues.
     * @return a boolean
     */
    public static boolean isGameOver() {
        if (checkDiagonals() || checkRowsColumns()) {
            isWin = true;
            return true;
        }
        if (moveCount == (BOARD_SIZE * BOARD_SIZE)) {
            return true;
        }
        return false;
    }

    /**
     * Returns an integer value for each player's symbol value
     * @return 1 for 'X' || -1 for 'O'
     */
    public static int playerValue() {
        if (currentPlayer == 'X') {
            return 1;
        }
        if (currentPlayer == 'O') {
            return -1;
        }
        return 0;
    }

    /**
     * Increments the update variables based on the current move
     */
    public static void record(int row, int column) {
        /* Increment boxes in the corresponding row and column */
        rowSums[row] += playerValue();
        colSums[column] += playerValue();

        /* Increment for boxes on the diagonal */
        if (row == column) {
            diag += playerValue();
        }

        /* Increment for boxes on the anti-diagonal */
        if (row + column == BOARD_SIZE - 1) {
            antidiag += playerValue();
        }
    }

    /**
     * Changes the symbol value of the current player
     */
    public static void changePlayer() {
        if (currentPlayer == 'X') {
            currentPlayer = 'O';
        } else {
            currentPlayer = 'X';
        }
    }

    /**
     * Returns the row number given as input by the player.
     * Checks for valid input.
     * @return the row number
     */
    public static int getRow() {
        int row = -1;
        while (row < 0 || row > BOARD_SIZE - 1) {
            System.out.printf("Enter a row number from [0-%d]: ", (BOARD_SIZE - 1));

            while (!in.hasNextInt()) {
                System.out.printf("Not valid input, please try again!\n");
                System.out.printf("Enter a row number from [0-%d]: ",
                                  (BOARD_SIZE - 1));
                in.next();
            }

            row = in.nextInt();
        }
        return row;
    }

    /**
     * Returns the column number given as input by the player.
     * Checks for valid input.
     * @return the column number
     */
    public static int getColumn() {
        int column = -1;
        while (column < 0 || column > BOARD_SIZE - 1) {
            System.out.printf("Enter a column number from [0-%d]: ",
                              (BOARD_SIZE - 1));
            while (!in.hasNextInt()) {
                System.out.printf("Not valid input, please try again!\n");
                System.out.printf("Enter a column number from [0-%d]: ",
                                  (BOARD_SIZE - 1));
                in.next();
            }
            column = in.nextInt();
        }
        return column;
    }

    /**
     * Asks which box the player would like to occupy and checks if box is
     * already occupied. If empty, updates corresponding variables and prints
     * the resulting board. If occupied, prints error message and asks for new
     * input.
     */
    public static void move() {
        System.out.println("Your move " + currentPlayer + ":");
        int row = getRow();
        int column = getColumn();
        if (get(row,column) == EMPTY) {
            set(row, column, currentPlayer);
            record(row, column);
            moveCount++;
            System.out.printf(currentPlayer + " takes (%d,%d)\n", row, column);
            changePlayer();
            printBoard();
        } else {
            System.out.printf("Invalid move!\n");
            printBoard();
        }
    }

    /**
     * Checks for a win on the diagonal or anti-diagonal
     * @return tthe column number
     */
    public static boolean checkDiagonals() {
        if (Math.abs(diag) == BOARD_SIZE  || Math.abs(antidiag) == BOARD_SIZE) {
            return true;
        }
        return false;
    }


    // Check rows and columns for win
    public static boolean checkRowsColumns() {
        for(int i = 0; i < BOARD_SIZE; i++) {
            if (Math.abs(rowSums[i]) == BOARD_SIZE  || Math.abs(colSums[i]) == BOARD_SIZE) {
                return true;
            }
        }
        return false;
    }


    // Preamble
    public static void intro() {
        System.out.printf("\n\n\n" +
               "*********************************\n" +
               "*   ______  __   ______         *\n" +
               "*  /\\__  _\\/\\ \\ /\\  ___\\        *\n" +
               "*  \\/_/\\ \\/\\ \\ \\\\ \\ \\____       *\n" +
               "*     \\ \\_\\ \\ \\_\\\\ \\_____\\      *\n" +
               "*      \\/_/  \\/_/ \\/_____/      *\n" +
               "*   ______  ______   ______     *\n" +
               "*  /\\__  _\\/\\  __ \\ /\\  ___\\    *\n" +
               "*  \\/_/\\ \\/\\ \\  __ \\\\ \\ \\____   *\n" +
               "*     \\ \\_\\ \\ \\_\\ \\_\\\\ \\_____\\  *\n" +
               "*      \\/_/  \\/_/\\/_/ \\/_____/  *\n" +
               "*   ______  ______   ______     *\n" +
               "*  /\\__  _\\/\\  __ \\ /\\  ___\\    *\n" +
               "*  \\/_/\\ \\/\\ \\ \\/\\ \\\\ \\  __\\    *\n" +
               "*     \\ \\_\\ \\ \\_____\\\\ \\_____\\  *\n" +
               "*      \\/_/  \\/_____/ \\/_____/  *\n" +
               "*                               *\n" +
               "*********************************\n");
        System.out.printf("Created by: Andrew Tristan Parli\n");
    }


    // End Game
    public static void endGame() {
        changePlayer();
        if (isWin == true) {
            System.out.printf("--- " + currentPlayer + " WINS! ---\n");
        } else {
            System.out.printf("--- DRAW ---\n");
        }
    }


    public static void main(String[] args) {
        intro();
        newBoard();
        printBoard();
        while (!isGameOver()) {
            move();
        }
        endGame();
    }

}
