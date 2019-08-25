package app.data;

import org.jetbrains.annotations.NotNull;

public class SearchParams {
    private String directoryPath;
    private String searchText;
    private String fileExtension;

    public SearchParams(@NotNull String directoryPath,
                        @NotNull String searchText,
                        @NotNull String fileExtension) {
        this.directoryPath = directoryPath;
        this.searchText = searchText;
        this.fileExtension = fileExtension;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
