package ru.serafimodin.app.ui;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;
import ru.serafimodin.app.file.FileInformation;
import ru.serafimodin.app.utils.OsUtils;

import java.nio.file.Path;

public class FileTreeView extends TreeView<String> {

    private final boolean isWindows = OsUtils.isWindows();

    private TreeItem<String> currentNode;

    public FileTreeView() {
        super();
        currentNode = null;
    }

    public FileTreeView(TreeItem<String> item) {
        super(item);
        currentNode = item;
    }

    private void setCurrentNode(TreeItem<String> node) {
        currentNode = node;
    }

    private void setCurrentNodeToRoot() {
        setCurrentNode(getRoot());
    }

    public void addPath(@NotNull FileInformation information) {
        Path path = information.getPath();
        Platform.runLater(() -> {
            String stringRepresentOfPath = path.toString().substring(path.getRoot().toString().length());
            if (isWindows) {
                stringRepresentOfPath = stringRepresentOfPath.replace("\\", "/");
            }
            String[] items = stringRepresentOfPath.split("/");
            if (currentNode == null) {
                setCurrentNodeToRoot();
            }
            for (int i = 0; i < items.length; i++) {
                FileTreeItem found = null;
                for (TreeItem<String> child : currentNode.getChildren()) {
                    if (child.getValue().equals(items[i])) {
                        found = (FileTreeItem) child;
                        break;
                    }
                }
                if (found == null) {
                    String subPath = path.getRoot() + path.subpath(0, i + 1).toString();
                    found = new FileTreeItem(items[i], new FileInformation(Path.of(subPath), information.getFirstLineWithNeededText()));
                    currentNode.getChildren().add(found);
                }
                currentNode = found;
            }
            setCurrentNodeToRoot();
        });
    }
}
