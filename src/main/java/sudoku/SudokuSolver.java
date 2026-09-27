package sudoku;

import java.util.*;

public class SudokuSolver {
    //fields
    private SudokuAppController controller;
    private Cell[][] cellMatrix;
    private ArrayList<Cell> blankableCells = new ArrayList<>();

    public final static int MINIMUM_PAIRS_FOR_DIFFICULTY = 15;


    public SudokuSolver(SudokuAppController controller) {
        this.controller = controller;
        this.cellMatrix = controller.getCells();
    }


    // Checks if the number that we supposedly want to add is valid
    private boolean isValid(int row, int col, int num) {
        // first set the number we want to test
        Cell targetCell = cellMatrix[row][col];
        String numStr = String.valueOf(num);

//        targetCell.setText(String.valueOf(num));


        // check by row
        Cell[] targetRow = cellMatrix[row]; // pluck out the row we want to search
        for (Cell currentCell : targetRow) {
            if (currentCell == targetCell) {
                continue;
            }

            if (currentCell.getValue().isEmpty()) {
                continue;
            }

            if (currentCell.getValue().equals(numStr)) {
                return false;
            }
        }

        // check by column
        for (Cell[] rowArray : cellMatrix) {
            Cell currentCell = rowArray[col]; // pulls the column element from each row, essentially the same as looping through a column

            if (currentCell == targetCell) {
                continue;
            }

            if (currentCell.getValue().isEmpty()) {
                continue;
            }

            if (currentCell.getValue().equals(numStr)) {
                return false;
            }
        }




        // check each 3x3 grid
        Coordinate startingCoord = locate3x3(row,col);
        int startingRow = startingCoord.row;
        int startingCol = startingCoord.col;


        for (int i = startingRow; i < startingRow + 3; i++) {
            for (int j = startingCol; j < startingCol + 3; j++) {
                if (cellMatrix[i][j] == targetCell) {
                    continue;
                }

                if (cellMatrix[i][j].getValue().isEmpty()) {
                    continue;
                }

                if (cellMatrix[i][j].getValue().equals(numStr)) {
                    return false;
                }
            }
        }

        return true;
    }

    public record Coordinate(int row, int col) {}


    protected Coordinate locate3x3(int row, int col) {
        // Given any random labels coordinates, find its band and stack, then find the 3x3 box it belongs to

        // Bands are 3 horizontal 3x3 boxes, stacks are 3 vertical
        int currentBand = switch (row) {
            case 0,1,2 -> 0;
            case 3,4,5 -> 1;
            case 6,7,8 -> 2;
            default -> throw new IllegalArgumentException("Invalid row " + row);
        };

        int currentStack = switch (col) {
            case 0,1,2 -> 0;
            case 3,4,5 -> 1;
            case 6,7,8 -> 2;
            default -> throw new IllegalArgumentException("Invalid column " + col);

        };

        return getRootCoords(currentBand, currentStack);
    }


    private static Coordinate getRootCoords(int Band, int Stack) {
        // given a band and a stack, return the coordinates of the label in the top left of the 3x3 area

        Coordinate coords = null;

        switch (Band) {
            case 0 -> {
               switch (Stack) {
                   case 0 -> coords = new Coordinate(0,0);
                   case 1 -> coords = new Coordinate(0,3);
                   case 2 -> coords = new Coordinate(0,6);
               }
            }

            case 1 -> {
                switch (Stack) {
                    case 0 -> coords = new Coordinate(3,0);
                    case 1 -> coords = new Coordinate(3,3);
                    case 2 -> coords = new Coordinate(3,6);
                }
            }

            case 2 -> {
                switch (Stack) {
                    case 0 -> coords = new Coordinate(6,0);
                    case 1 -> coords = new Coordinate(6,3);
                    case 2 -> coords = new Coordinate(6,6);
                }
            }
        }

        return coords;
    }

    public boolean solveBoard(int row, int col) {
        // checks if the entire board is valid

        // base case
        if (row == 9) {
            return true;
        }

        int nextRow = (col == 8) ? row + 1 : row;
        int nextCol = (col == 8) ? 0 : col + 1;

        List<Integer> numBank = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(numBank);

        Cell targetCell = cellMatrix[row][col];

        if (!targetCell.getValue().isEmpty()) {
            return solveBoard(nextRow, nextCol);
        }

        for (Integer num : numBank) {
            boolean validNumber = isValid(row, col, num);

            if (validNumber) {
                boolean setValue = targetCell.trySetValue(String.valueOf(num)); // remember to use the boolean trysetvalue returns eventually
                boolean solvedNextBox = solveBoard(nextRow, nextCol);
                if (solvedNextBox) {
                    return true;
                } else {
                    targetCell.trySetValue("");
                }
            }

        }
        return false;
    }

    private boolean attemptRemovePair(int row, int col) {

        // we only need on map because if a cell does not make it in here, it means after emptying it solveBoard()
        // failed to solve the puzzle, and we NEED to  what was originally in there to stay
        // if solveBoard can solve the board after emptying, we save whatever cells are safe to empty here
        // ArrayList<Coordinate> tracker = new ArrayList<>();

        int pairRow = 4 + (4 - row);
        int pairCol = 4 + (4 - col);

        Cell currCell = cellMatrix[row][col];
        Cell pairCell = cellMatrix[pairRow][pairCol];

        String trackedValueInCell = currCell.getValue();
        String trackedValueInPair = pairCell.getValue();

        currCell.trySetValue("");
        pairCell.trySetValue("");

        boolean boardStillSolvable = solveBoard(0, 0);

        if (boardStillSolvable) {
            // solveBoard() does it's job by filling the board so we have to wipe the cells again
            blankableCells.add(currCell);
            blankableCells.add(pairCell);
            for (Cell cell : blankableCells) {cell.trySetValue("");}
            return true;
        } else {
            currCell.trySetValue(trackedValueInCell);
            pairCell.trySetValue(trackedValueInPair);
        }
        return false;
    }

    public void removePairs(int amount) {
        int counter = 0;
        int enforcedMin = (amount < 14) ? MINIMUM_PAIRS_FOR_DIFFICULTY : amount;

        outer:
        for (int row = 0; row <= 4; row++) {
            int maxCol = (row == 4) ? 4 : 8;
            for (int col = 0; col <= maxCol; col++) {
                if (attemptRemovePair(row, col)) {
                    counter++;
                }
                if (counter == enforcedMin) {
                    break outer;
                }
            }
        }
    }

    public void lockCells() {
        for (Cell[] matrix : cellMatrix) {
            for (Cell cell : matrix) {
                if (!cell.getValue().isEmpty()) {cell.lock();}
                // grey out the cell upon locking in Cell.lock()
            }
        }
    }

    public void printLabelMatrix() {
        System.out.println(Arrays.deepToString(cellMatrix));
    }
}
