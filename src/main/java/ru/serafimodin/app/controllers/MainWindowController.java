package ru.serafimodin.app.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import ru.serafimodin.app.data.SearchParams;
import ru.serafimodin.app.file.FileInformation;
import ru.serafimodin.app.file.FileVisitor;
import ru.serafimodin.app.ui.FileTreeItem;
import ru.serafimodin.app.ui.FileTreeView;
import ru.serafimodin.app.ui.Selector;
import ru.serafimodin.app.ui.TabWithText;
import ru.serafimodin.app.utils.Animator;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

public class MainWindowController implements Initializable {

    @FXML
    private AnchorPane mainWindowRoot;
    @FXML
    private FileTreeView filesTreeView;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button goBottomBtn;
    @FXML
    private Button goTopBtn;

    private SearchParams searchParams;
    private TabWithText currentFocusedTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.currentFocusedTab = null;
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        filesTreeView.setOnMouseClicked((this::onFileItemClick));
        tabPane.setOnMouseClicked(this::onTabMouseMiddleButtonClick);
        tabPane.getTabs().addListener(this::tabsChangeListener);
        tabPane.getSelectionModel().selectedItemProperty().addListener(this::tabSelectionChangeListener);
    }

    @FXML
    private void onGoDownBtnClick() {
        if (currentFocusedTab == null) {
            return;
        }
        currentFocusedTab.selectNext(Selector.Direction.DOWN);
    }

    @FXML
    private void onGoTopBtnClick() {
        if (currentFocusedTab == null) {
            return;
        }
        currentFocusedTab.selectNext(Selector.Direction.UP);
    }

    private void tabsChangeListener(@NotNull ListChangeListener.Change<? extends Tab> change) {
        while (change.next()) {
            if (change.wasAdded() && change.getList().size() == 1) {
                goTopBtn.setDisable(false);
                goBottomBtn.setDisable(false);
            } else if (change.getList().size() == 0) {
                goTopBtn.setDisable(true);
                goBottomBtn.setDisable(true);
            }
        }
    }

    private void tabSelectionChangeListener (ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
        this.currentFocusedTab = (TabWithText) newTab;
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
        TabWithText tab = new TabWithText(clickedItem.getInformation(), searchParams.getSearchText());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML
    private void onBackButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getClassLoader()
                .getResource("searchParamsWindow.fxml"));
        Parent paramsRoot = loader.load();
        Scene scene = mainWindowRoot.getScene();
        StackPane stack = (StackPane) scene.getRoot();
        paramsRoot.translateXProperty().set(scene.getWidth());
        stack.getChildren().setAll(paramsRoot);
        Animator.playTransitionAnimation(new KeyValue(paramsRoot.translateXProperty(), 0, Interpolator.EASE_OUT));
    }

    private @NotNull ObservableList<FileInformation> buildPathsObservableList() {
        ObservableList<FileInformation> paths = FXCollections.observableList(new ArrayList<>());
        paths.addListener((ListChangeListener.Change<? extends FileInformation> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (FileInformation information : c.getAddedSubList()) {
                        filesTreeView.addPath(information);
                    }
                }
            }
        });
        return paths;
    }

    void setFilePathsToTreeView() {
        FileVisitor visitor = new FileVisitor(searchParams, buildPathsObservableList());
        filesTreeView.setRoot(new FileTreeItem(searchParams.getDirectoryPath(),
                new FileInformation(Path.of(searchParams.getDirectoryPath()), 0)));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Files.walkFileTree(Path.of(searchParams.getDirectoryPath()), visitor);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void expandTreeView( TreeItem selectedItem )
    {
        if ( selectedItem != null )
        {
            expandTreeView( selectedItem.getParent() );

            if ( ! selectedItem.isLeaf() )
            {
                selectedItem.setExpanded( true );
            }
        }
    }

    void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }
}
