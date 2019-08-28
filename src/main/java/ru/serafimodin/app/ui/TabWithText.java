package ru.serafimodin.app.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.input.ScrollEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.serafimodin.app.file.FileReader;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class TabWithText extends Tab {
    private static final Logger log = LoggerFactory.getLogger(TabWithText.class);

    private static final double DELTA_FACTOR = 10.0;

    private ObservableList<String> fileLines;
    private FileReader fileReader;

    public TabWithText(@NotNull File file) {
        super();
        this.fileLines = FXCollections.observableArrayList();
        this.fileReader = new FileReader(file, 2048, 300);
        ListView<String> fileLinesView = new ListView<>(fileLines);
        fileReader.moveDown(100)
                .ifPresent(list -> fileLines.setAll(FXCollections.observableList(list)));
        setGraphic(new Label(file.getName()));
        setClosable(true);
        setContent(fileLinesView);
        fileLinesView.setOnScroll(this::onScrollTextEvent);
    }

    private void move(double deltaY) {
        int lines = Double.valueOf(deltaY / DELTA_FACTOR).intValue();
        Platform.runLater( () -> {
            Optional<? extends List<String>> result;
            if (deltaY > 0.0) {
                result = moveUp(lines);
                result.ifPresent(this::addTopLines);
            } else {
                result = moveDown(-lines);
                result.ifPresent(this::addBottomLines);
            }
        });
    }

    private void onScrollTextEvent(@NotNull ScrollEvent event) {
        double deltaY = event.getDeltaY();
        switch (event.getTextDeltaYUnits()) {
            case NONE:
                // do nothing
                break;
            case LINES:
                deltaY *= 10.0;
                break;
            case PAGES:
                deltaY *= 50.0;
                break;
        }
        move(deltaY);
    }

    private void addTopLines(@NotNull List<String> lines) {
        if (lines.size() >= fileLines.size()) {
            fileLines.clear();
        } else {
            fileLines.remove( fileLines.size() - lines.size(), fileLines.size());
        }
        fileLines.addAll(0, FXCollections.observableList(lines));
    }

    private void addBottomLines(@NotNull List<String> lines) {
        if (lines.size() >= fileLines.size()) {
            fileLines.clear();
        } else {
            fileLines.remove(0, lines.size());
        }
        fileLines.addAll(FXCollections.observableList(lines));
    }

    private Optional<List<String>> moveDown(int lines) {
       return fileReader.moveDown(lines);
    }

    private Optional<List<String>> moveUp(int lines) {
        return fileReader.moveUp(lines);
    }
}
