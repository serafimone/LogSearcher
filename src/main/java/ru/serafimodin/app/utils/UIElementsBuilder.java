package ru.serafimodin.app.utils;

import ru.serafimodin.app.ui.FileTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class UIElementsBuilder {
    public static @NotNull Tab buildTabWithFile(@NotNull FileTreeItem item) {
        Tab tabData = new Tab();
        Label tabLabel = new Label(item.getValue());
        tabData.setGraphic(tabLabel);
        tabData.setContent(buildTextArea(getFileData(item.getFullPath().toFile())));
        tabData.setClosable(true);
        return tabData;
    }

    private static @NotNull TextArea buildTextArea(@NotNull List<String> lines) {
        TextArea text = new TextArea();
        text.setEditable(false);
        text.clear();
        for (String line : lines) {
            text.appendText(line + System.lineSeparator());
        }
        return text;
    }

    private static @NotNull List<String> getFileData(File file) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lines;
    }
}
