package app.controllers;

import app.data.SearchParams;
import app.ui.FileTreeItem;
import app.ui.FileTreeView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {

    @FXML
    private AnchorPane mainWindowRoot;
    @FXML
    private FileTreeView filesTreeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: implement me
    }

    private List<Path> getFilesPaths(@NotNull SearchParams searchParams) throws IOException {
        return Files.walk(Paths.get(searchParams.getDirectoryPath()))
                .filter(p -> filterFile(p, searchParams))
                .collect(Collectors.toList());
    }

    public void setFilePathsToTreeView(@NotNull SearchParams searchParams) throws IOException, URISyntaxException {
        filesTreeView.setRoot(new FileTreeItem(searchParams.getDirectoryPath(), searchParams.getDirectoryPath()));
        for (Path path : getFilesPaths(searchParams)) {
            filesTreeView.addPath(path);
        }
    }

    private boolean filterFile(@NotNull Path path, @NotNull SearchParams searchParams) {
        if (FilenameUtils.getExtension(path.toString()).equals(searchParams.getFileExtension())) {
            try {
                FileReader fr = new FileReader(path.toFile());
                BufferedReader br = new BufferedReader(fr);
                String nextString;
                while ((nextString = br.readLine()) != null) {
                    if (nextString.contains(searchParams.getSearchText())) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
