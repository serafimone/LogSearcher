package ru.serafimodin.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LogSearcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent screen = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchParamsWindow.fxml")));
        Scene mainScene = new Scene(screen);
        stage.setScene(mainScene);
        stage.setTitle("LogSearcher");
        stage.setMinWidth(700);
        stage.setMinHeight(450);
        stage.show();
    }
}
