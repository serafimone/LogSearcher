package ru.serafimodin.app.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import ru.serafimodin.app.data.SearchParams;
import ru.serafimodin.app.file.FileVisitor;
import ru.serafimodin.app.ui.FileTreeItem;
import ru.serafimodin.app.ui.FileTreeView;
import ru.serafimodin.app.ui.TabWithText;
import ru.serafimodin.app.utils.Animator;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWindowController implements Initializable {

    @FXML
    private AnchorPane mainWindowRoot;
    @FXML
    private FileTreeView filesTreeView;
    @FXML
    private TabPane tabPane;

    private SearchParams searchParams;

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
        TabWithText tab = new TabWithText(clickedItem.getFullPath().toFile());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML
    private void onBackButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getClassLoader()
                .getResource("searchParamsWindow.fxml"));
        Parent paramsRoot = loader.load();
        Scene scene = mainWindowRoot.getScene();
        StackPane stack = (StackPane) scene.getRoot();
        paramsRoot.translateXProperty().set(scene.getWidth());
        stack.getChildren().setAll(paramsRoot);
        Animator.playTransitionAnimation(
                paramsRoot,
                (StackPane) scene.getRoot(),
                new KeyValue(paramsRoot.translateXProperty(), 0, Interpolator.EASE_OUT));
    }

    private @NotNull ObservableList<Path> buildPathsObservableList() {
        ObservableList<Path> paths = FXCollections.observableList(new ArrayList<>());
        paths.addListener((ListChangeListener.Change<? extends Path> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Path path : c.getAddedSubList()) {
                        filesTreeView.addPath(path);
                    }
                }
            }
        });
        return paths;
    }

    void setFilePathsToTreeView() {
        FileVisitor visitor = new FileVisitor(searchParams, buildPathsObservableList());
        filesTreeView.setRoot(new FileTreeItem(searchParams.getDirectoryPath(), searchParams.getDirectoryPath()));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Files.walkFileTree(Path.of(searchParams.getDirectoryPath()), visitor);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }
}
