package ru.serafimodin.app.ui;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.serafimodin.app.file.FileInformation;

import java.nio.file.Files;
import java.util.Objects;

public class FileTreeItem extends TreeItem<String> {

    private static final Image FOLDER_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("icons/folder.png")));
    private static final Image FILE_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("icons/text-x-generic.png")));

    private FileInformation information;
    private boolean isDirectory;

    public FileTreeItem(String value, FileInformation information) {
        super(value);
        this.information = information;
        this.isDirectory = Files.isDirectory(this.information.getPath());
        setGraphic(new ImageView(isDirectory ? FOLDER_ICON : FILE_ICON));
        setExpanded(true);
    }

    public FileInformation getInformation() {
        return information;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
