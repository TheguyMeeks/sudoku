package sudoku;

import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SudokuSolver {
    //fields
    private SudokuAppController controller;
    private Cell[][] cellMatrix;


    public SudokuSolver(SudokuAppController controller) {
        this.controller = controller;
        this.cellMatrix = controller.getCells();
    }


    // Checks if the number that we supposedly want to add is valid
    public boolean isValid(GridPane grid, int row, int col, int num) {
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
        Coordinate startingCoord = locate3x3(grid, row,col);
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


    protected Coordinate locate3x3(GridPane grid, int row, int col) {
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

    public boolean solveBoard(GridPane grid, int row, int col) {
        // checks if the entire board is valid

        // base case
        if (row == 9) {
            return true;
        }
        int nextRow = (col == 8) ? row + 1 : row;
        int nextCol = (col == 8) ? 0 : col + 1;
        List<Integer> numBank = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(numBank);

        for (int i = 0; i < numBank.size(); i++) {
            boolean validNumber = isValid(grid, row, col, numBank.get(i));
            Cell targetCell = cellMatrix[row][col];

            if (validNumber) {
                boolean setValue = targetCell.trySetValue(String.valueOf(numBank.get(i))); // remember to use the boolean trysetvalue returns eventually
                boolean solvedNextBox = solveBoard(grid,nextRow, nextCol);
                if (solvedNextBox) {
                    return true;
                } else {
                    targetCell.trySetValue("");
                }
            }

        }
        return false;
    }

    public void printLabelMatrix() {
        System.out.println(Arrays.deepToString(cellMatrix));
    }
}
