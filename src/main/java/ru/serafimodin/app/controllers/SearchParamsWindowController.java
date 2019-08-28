package ru.serafimodin.app.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import org.jetbrains.annotations.NotNull;
import ru.serafimodin.app.data.SearchParams;
import ru.serafimodin.app.utils.Animator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class SearchParamsWindowController implements Initializable {

    @FXML
    private StackPane paramsRoot;
    @FXML
    private Button searchBtn;
    @FXML
    private TextField pathToDirectory;
    @FXML
    private TextField textForSearch;
    @FXML
    private TextField fileExtensionType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileExtensionType.setTextFormatter(new TextFormatter<String>(getTextFieldFileExtensionFilter()));
    }

    private @NotNull UnaryOperator<TextFormatter.Change> getTextFieldFileExtensionFilter() {
        return change -> {
            String newText = change.getText();
            if (!change.isContentChange()) {
                return change;
            }
            if (newText.matches("[a-z]*") || newText.isEmpty()) {
                return change;
            }

            return null;
        };
    }

    private @NotNull DirectoryChooser buildDirectoryChooser() {
        final DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose a directory for search");
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        return dc;
    }

    @FXML
    private void chooseFolderBtnClick(ActionEvent event) {
        File directory = buildDirectoryChooser().showDialog(paramsRoot.getScene().getWindow());
        if (directory != null) {
            pathToDirectory.setText(directory.getAbsolutePath());
            searchBtn.setDisable(false);
            textForSearch.setDisable(false);
            fileExtensionType.setDisable(false);
        }
    }

    @FXML
    private void searchBtnClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getClassLoader()
                .getResource("main.fxml"));
        Parent newScreenRoot = loader.load();
        Scene scene = paramsRoot.getScene();
        StackPane stack = (StackPane) scene.getRoot();
        newScreenRoot.translateXProperty().set(scene.getWidth());
        stack.getChildren().setAll(newScreenRoot);
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setSearchParams(new SearchParams(
                pathToDirectory.getText(),
                textForSearch.getText(),
                fileExtensionType.getText().isEmpty() ? "log" : fileExtensionType.getText()));
        mainWindowController.setFilePathsToTreeView();
        Animator.playTransitionAnimation(new KeyValue(newScreenRoot.translateXProperty(), 0, Interpolator.EASE_IN));
    }
}
