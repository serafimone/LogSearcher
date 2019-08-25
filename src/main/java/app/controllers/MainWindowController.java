package app.controllers;

import app.data.SearchParams;
import app.ui.FileTreeItem;
import app.ui.FileTreeView;
import app.utils.UIElementsBuilder;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import static app.utils.FilesUtils.getFilesPaths;

public class MainWindowController implements Initializable {

    @FXML
    private FileTreeView filesTreeView;
    @FXML
    private TabPane tabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        filesTreeView.setOnMouseClicked((this::onFileItemClick));
        tabPane.setOnMouseClicked(this::onTabMouseMiddleButtonClick);
    }

    private void onTabMouseMiddleButtonClick(@NotNull MouseEvent event) {
        if (event.getButton() != MouseButton.MIDDLE) {
            return;
        }
        EventTarget target = event.getTarget();
        if (target instanceof Tab) {
            Tab tab = (Tab) target;
            tabPane.getTabs().remove(tab);
        }
    }

    private void onFileItemClick(@NotNull MouseEvent event) {
        if (event.getClickCount() != 2) {
            return;
        }
        FileTreeItem clickedItem = (FileTreeItem) filesTreeView.getSelectionModel().getSelectedItem();
        if (clickedItem == null) {
            return;
        }
        if (clickedItem.isDirectory()) {
            return;
        }
        Tab tab = UIElementsBuilder.buildTabWithFile(clickedItem);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    void setFilePathsToTreeView(@NotNull SearchParams searchParams) throws IOException {
        filesTreeView.setRoot(new FileTreeItem(searchParams.getDirectoryPath(), searchParams.getDirectoryPath()));
        for (Path path : getFilesPaths(searchParams)) {
            filesTreeView.addPath(path);
        }
    }


}
