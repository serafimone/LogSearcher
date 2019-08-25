package app.ui;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class FileTreeView extends TreeView<String> {

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

    public void addPath(@NotNull Path path) {
        String[] items = path.toString().substring(1).split("/");
        if (currentNode == null) {
            setCurrentNodeToRoot();
        }
        for (String item : items) {
            TreeItem<String> found = null;
            for (TreeItem<String> child : currentNode.getChildren()) {
                if (child.getValue().equals(item)) {
                    found = child;
                    break;
                }
            }
            if (found == null) {
                found = new TreeItem<>(item);
                currentNode.getChildren().add(found);
            }
            currentNode = found;
        }
        setCurrentNodeToRoot();
    }
}
