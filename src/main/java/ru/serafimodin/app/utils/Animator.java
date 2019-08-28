package ru.serafimodin.app.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

public class Animator {

    public static void playTransitionAnimation(@NotNull Parent newScreenRoot,
                                               @NotNull StackPane stack,
                                               @NotNull KeyValue kv) {
        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.seconds(1.2), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

}
