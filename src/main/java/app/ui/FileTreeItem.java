package app.ui;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileTreeItem extends TreeItem<String> {

    private static final Image FOLDER_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("icons/folder.png")));
    private static final Image FILE_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("icons/text-x-generic.png")));

    private Path fullPath;
    private boolean isDirectory;

    public FileTreeItem(String value, String path) {
        super(value);
        this.fullPath = Paths.get(path);
        this.isDirectory = Files.isDirectory(fullPath);
        setGraphic(new ImageView(isDirectory ? FOLDER_ICON : FILE_ICON));
    }

    public Path getFullPath() {
        return fullPath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
