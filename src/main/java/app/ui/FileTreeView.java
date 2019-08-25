package app.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
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
        for (int i = 0; i < items.length; i++) {
            FileTreeItem found = null;
            for (TreeItem<String> child : currentNode.getChildren()) {
                if (child.getValue().equals(items[i])) {
                    found = (FileTreeItem) child;
                    break;
                }
            }
            if (found == null) {
                String subpath = "/" + path.subpath(0, i + 1).toString();
                found = new FileTreeItem(items[i], subpath);
                currentNode.getChildren().add(found);
            }
            currentNode = found;
        }
        setCurrentNodeToRoot();
    }
}
