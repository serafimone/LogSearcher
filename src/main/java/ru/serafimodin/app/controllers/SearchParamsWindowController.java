package ru.serafimodin.app.controllers;

import ru.serafimodin.app.data.SearchParams;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class SearchParamsWindowController implements Initializable {

    @FXML
    private StackPane paramsStackPane;
    @FXML
    private AnchorPane paramsRoot;
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
        File directory = buildDirectoryChooser().showDialog(paramsStackPane.getScene().getWindow());
        if (directory != null) {
            pathToDirectory.setText(directory.getAbsolutePath());
            searchBtn.setDisable(false);
            textForSearch.setDisable(false);
            fileExtensionType.setDisable(false);
        }
    }

    private void playTransitionAnimation(@NotNull Parent root) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            paramsStackPane.getChildren().remove(paramsRoot);
        });
        timeline.play();
    }

    @FXML
    private void searchBtnClick(ActionEvent event) throws IOException, URISyntaxException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getClassLoader()
                .getResource("main.fxml"));
        Parent root = loader.load();
        Scene scene = paramsStackPane.getScene();
        root.translateYProperty().set(scene.getHeight());
        paramsStackPane.getChildren().add(root);
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setSearchParams(new SearchParams(
                pathToDirectory.getText(),
                textForSearch.getText(),
                fileExtensionType.getText().isEmpty() ? "log" : fileExtensionType.getText()));
        mainWindowController.setFilePathsToTreeView();
        playTransitionAnimation(root);
    }


}
