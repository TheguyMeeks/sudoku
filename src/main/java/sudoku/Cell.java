package sudoku;

import javafx.scene.control.Label;

public class Cell {
    private Label label;
    private boolean isLocked;
    static final String BASE_CELL_STYLE =
            "-fx-border-color: #969696; -fx-alignment: center; -fx-border-width: 1; -fx-font-size: 35; -fx-font-family: 'JetBrains Mono ExtraBold';";


    public Cell(Label label) {
        this.label = label;
        isLocked = false;
    }

    // the only function that should call this is drawGame()
    public Label getLabel() {
        return this.label;
    }

    public String getValue() {
        return this.label.getText();
    }

    public void lock() {
        isLocked = true;
        this.label.setStyle(this.label.getStyle() + "-fx-background-color: #dddddd");

    }

    public boolean trySetValue(String s) {
        if (isLocked) {
            return false;
        }
        this.label.setText(s);
        return true;
    }
}
