package sudoku;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
        Platform.exit();
    }

    // ============================
    // NON-FXML METHODS RAHHH
    // ============================
    private void drawGame() {
        labels = new Label[9][9]; // initialize an empty 9x9 2d array board of labels

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                StackPane stackPane =  new StackPane();
                Label gridBox = new Label();

                gridBox.setStyle("-fx-border-color: #d1d4d8;-fx-alignment: center; -fx-border-width: 1; -fx-font-size: 10; -fx-font-family: 'Arial Rounded MT Bold'");
                labels[row][col] = gridBox;

                stackPane.getChildren().add(gridBox);
                stackPane.setPrefSize(boxSize, boxSize);
                gridBox.setPrefSize(boxSize, boxSize);

                sudokuGrid.add(stackPane, col, row);

                // add some sort of marker to each label/gridbox so we can do something when its clicked
                int finalRow = row;
                int finalCol = col;

                gridBox.setFocusTraversable(true); // allow the label to take keyboard focus

                // after an annoying amount of time, we find that we have to set up the key listener outside the mouse listener, once per label creation
                gridBox.setOnKeyPressed(keyEvent -> handleKeyPresses(keyEvent, finalRow, finalCol));

                // and then we have the mouse listener request the focus of the listener we defined above
                gridBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        gridBox.requestFocus();

                        System.out.println("Selected cell at: " + finalRow + ", " + finalCol);
                    }
                });
            }
        }
    }

    public void playGame(Button difficulty) {
      // plays the game of sudoku with the selected difficulty
        drawGame();
    }

    private void toggleCell(int row, int col) {
        // tracks what cell is active somehow?
    }

    public void handleKeyPresses(KeyEvent e, int row, int col) {
        KeyCode code = e.getCode();
        if (code.isDigitKey() || code.name().startsWith("NUMPAD") && code.name().length() == 7) {
            String input = e.getText();
            labels[row][col].setText(input);
            System.out.println(input + " should have been written");

        }
    }

    private void handleCellClicked(KeyEvent keyEvent,int row, int col) {
        toggleCell(row, col);
        // this function might not be needed
    }



}
