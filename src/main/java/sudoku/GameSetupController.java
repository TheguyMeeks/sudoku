package sudoku;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class GameSetupController {

    // NORMAL FIELDS
    @FXML private Button easyButton;
    @FXML private Button mediumButton;
    @FXML private Button hardButton;

    // FXML FIELDS
    @FXML public Label titleLabel;
    @FXML private Label welcomeText;

    // ============================
    // FXML METHODS RAHHH
    // ============================
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    // ============================
    // NON-FXML METHODS RAHHH
    // ============================
    @FXML
    private void launchGame (ActionEvent event) {
       Button difficulty = (Button) event.getSource();

       // launch the game of sudoku with playGame(difficulty)
        try {
            FXMLLoader loader = new FXMLLoader(SudokuApp.class.getResource("SudokuAppView.fxml"));
            Stage gameStage = (Stage) titleLabel.getScene().getWindow();
            Scene gameScene = new Scene(loader.load()); // read SudokuAppView.fxml and draw out whatever scene the fxml file specifies, then store it in this variable.
            gameStage.setScene(gameScene); // take that empty window and dump this scene into it

            SudokuAppController controller = loader.getController();
            controller.playGame(difficulty);

            // handle key presses in the scene later that was a pain last time. hopefully we learned something


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
