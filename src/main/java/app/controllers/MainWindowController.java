package app.controllers;

import app.data.SearchParams;
import app.ui.FileTreeItem;
import app.ui.FileTreeView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {
    private final Logger log = Logger.getLogger(getClass().getName());

    @FXML
    private AnchorPane mainWindowRoot;
    @FXML
    private FileTreeView filesTreeView;
    @FXML
    private TabPane tabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filesTreeView.setOnMouseClicked((this::onFileItemClick));
    }

    private @NotNull Tab buildTabWithFile(@NotNull FileTreeItem item) {
        Tab tabData = new Tab();
        Label tabLabel = new Label(item.getValue());
        tabData.setGraphic(tabLabel);
        tabData.setContent(buildTextArea(getFileData(item.getFullPath().toFile())));
        return tabData;
    }

    private @NotNull TextArea buildTextArea(@NotNull List<String> lines) {
        TextArea text = new TextArea();
        text.clear();
        for (String line : lines) {
            text.appendText(line + "/n");
        }
        return text;
    }

    private @NotNull List<String> getFileData(File file) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Exception:", ex);
        }

        return lines;
    }

    private void onFileItemClick(MouseEvent event) {
        if (event.getClickCount() != 2) {
            return;
        }
        FileTreeItem clickedItem = (FileTreeItem) filesTreeView.getSelectionModel().getSelectedItem();
        if (clickedItem == null) {
            return;
        }
        Tab tab = buildTabWithFile(clickedItem);
        tabPane.getTabs().add(tab);
    }

    private List<Path> getFilesPaths(@NotNull SearchParams searchParams) throws IOException {
        return Files.walk(Paths.get(searchParams.getDirectoryPath()))
                .filter(p -> filterFile(p, searchParams))
                .collect(Collectors.toList());
    }

    public void setFilePathsToTreeView(@NotNull SearchParams searchParams) throws IOException, URISyntaxException {
        filesTreeView.setRoot(new FileTreeItem(searchParams.getDirectoryPath(), searchParams.getDirectoryPath()));
        for (Path path : getFilesPaths(searchParams)) {
            filesTreeView.addPath(path);
        }
    }

    private boolean filterFile(@NotNull Path path, @NotNull SearchParams searchParams) {
        if (FilenameUtils.getExtension(path.toString()).equals(searchParams.getFileExtension())) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
                String nextString;
                while ((nextString = reader.readLine()) != null) {
                    if (nextString.contains(searchParams.getSearchText())) {
                        return true;
                    }
                }
                reader.close();
            } catch (IOException ex) {
                log.log(Level.SEVERE, "Exception:", ex);
            }
        }
        return false;
    }
}
