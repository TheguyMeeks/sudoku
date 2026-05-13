package sudoku;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class SudokuAppController {

    //FXML FIELDS
    @FXML private GridPane sudokuGrid;
    @FXML private HBox numPadRowOne;
    @FXML private HBox numPadRowTwo;
    @FXML private HBox numPadRowThree;

    //NON-FXML FIELDS
    private Label[][] labels;
    private int boxSize = 68;

    // ============================
    // FXML METHODS RAHHH
    // ============================
    @FXML
    public void initialize() {

    }

    @FXML
    private void menuQuit(ActionEvent event) {
    }

    // ============================
    // NON-FXML METHODS RAHHH
    // ============================
    private void drawGame() {
        labels = new Label[9][9]; // initialize an empty 9x9 2d array board of labels

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                StackPane stackPane =  new StackPane();
                Label numberBox = new Label();

                numberBox.setStyle("-fx-border-color: #d1d4d8;-fx-alignment: center; -fx-border-width: 1; -fx-font-size: 10; -fx-font-family: 'Arial Rounded MT Bold'");
                labels[row][col] = numberBox;

                stackPane.getChildren().add(numberBox);
                stackPane.setPrefSize(boxSize, boxSize);
                numberBox.setPrefSize(boxSize, boxSize);

                sudokuGrid.add(stackPane, col, row);
            }
        }
    }

    public void playGame(Button difficulty) {
      // plays the game of sudoku with the selected difficulty
        drawGame();
    }



}
