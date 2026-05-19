package sudoku;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Arrays;

public class SudokuSolver {
    //fields
    private SudokuAppController controller;
    private Label[][] labelMatrix;


    public SudokuSolver(SudokuAppController controller) {
        this.controller = controller;
        this.labelMatrix = controller.getLabels();
    }


    // Checks if the number that we supposedly want to add is valid
    public boolean isValid(GridPane grid, int row, int col, int num) {
        // first set the number we want to test
        Label targetLabel = labelMatrix[row][col];
        String numStr = String.valueOf(num);

//        targetLabel.setText(String.valueOf(num));


        // check by row
        Label[] targetRow = labelMatrix[row]; // pluck out the row we want to search
        for (Label currentLabel : targetRow) {
            if (currentLabel == targetLabel) {
                continue;
            }

            if (currentLabel.getText().isEmpty()) {
                continue;
            }

            if (currentLabel.getText().equals(numStr)) {
                return false;
            }
        }

        // check by column
        for (Label[] rowArray : labelMatrix) {
            Label currentLabel = rowArray[col]; // pulls the column element from each row, essentially the same as looping through a column

            if (currentLabel == targetLabel) {
                continue;
            }

            if (currentLabel.getText().isEmpty()) {
                continue;
            }

            if (currentLabel.getText().equals(numStr)) {
                return false;
            }
        }




        // check each 3x3 grid
        Coordinate startingCoord = locate3x3(grid, row,col);
        int startingRow = startingCoord.row;
        int startingCol = startingCoord.col;


        for (int i = startingRow; i < startingRow + 3; i++) {
            for (int j = startingCol; j < startingCol + 3; j++) {
                if (labelMatrix[i][j] == targetLabel) {
                    continue;
                }

                if (labelMatrix[i][j].getText().isEmpty()) {
                    continue;
                }

                if (labelMatrix[i][j].getText().equals(numStr)) {
                    return false;
                }
            }
        }

        return true;
    }

    public record Coordinate(int row, int col) {}


    protected Coordinate locate3x3(GridPane grid, int row, int col) {
        // Given any random labels coordinates, find the 3x3 box it belongs to

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

    public boolean isValidBoard (GridPane grid) {
        // checks if the entire board is valid
        return false;
    }

    public void printLabelMatrix() {
        System.out.println(Arrays.deepToString(labelMatrix));
    }
}
