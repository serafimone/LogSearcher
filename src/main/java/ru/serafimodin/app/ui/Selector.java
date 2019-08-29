package ru.serafimodin.app.ui;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import org.jetbrains.annotations.NotNull;

public class Selector {
    public enum Direction {
        UP{
            @Override
            void action(Selector selector) {
                selector.selectUp();
            }
        },
        DOWN {
            @Override
            void action(Selector selector) {
                selector.selectDown();
            }
        };

        abstract void action(Selector selector);
    }

    private String searchText;
    private ListView<String> lines;
    private int currentSelectedRow;

    Selector(@NotNull ListView<String> lines, @NotNull String searchText) {
        this.searchText = searchText;
        this.lines = lines;
        currentSelectedRow = -1;
    }

    int getCurrentSelectedRow() {
        return currentSelectedRow;
    }

    private void selectUp() {
        if (currentSelectedRow == 0) {
            return;
        }
        int iteratorIndex = currentSelectedRow;
        while (iteratorIndex > 0) {
            iteratorIndex--;
            if (lines.getItems().get(iteratorIndex).contains(searchText)) {
                setCurrentSelectedRow(iteratorIndex);
                return;
            }
        }
    }

    private void selectDown() {
        if (currentSelectedRow == lines.getItems().size() - 1) {
            return;
        }
        int itemsSize = lines.getItems().size();
        int iteratorIndex = currentSelectedRow;
        while (iteratorIndex < itemsSize - 2) {
            iteratorIndex++;
            if (lines.getItems().get(iteratorIndex).contains(searchText)) {
                setCurrentSelectedRow(iteratorIndex);
                return;
            }
        }
    }

    private void setCurrentSelectedRow(int index) {
        this.currentSelectedRow = index;
        Platform.runLater(() -> {
            lines.getSelectionModel().select(index);
            lines.getFocusModel().focus(index);
            lines.scrollTo(index);});
    }
}
