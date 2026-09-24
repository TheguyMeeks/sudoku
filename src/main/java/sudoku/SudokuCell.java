package sudoku;

import javafx.scene.control.Label;

public class SudokuCell {
    private Label label;
    private boolean isLocked;

    public SudokuCell(Label label) {
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
    }

    public boolean trySetValue(String s) {
        if (isLocked) {
            return false;
        }
        this.label.setText(s);
        return true;
    }
}
