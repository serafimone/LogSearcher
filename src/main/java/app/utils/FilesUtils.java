package app.utils;

import app.data.SearchParams;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FilesUtils {

    private static boolean filterFile(@NotNull Path path, @NotNull SearchParams searchParams) {
        if (FilenameUtils.getExtension(path.toString()).equals(searchParams.getFileExtension())) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
                String nextString;
                while ((nextString = reader.readLine()) != null) {
                    if (nextString.contains(searchParams.getSearchText())) {
                        return true;
                    }
                }
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public static @NotNull List<Path> getFilesPaths(@NotNull SearchParams searchParams) throws IOException {
        return Files.walk(Paths.get(searchParams.getDirectoryPath()))
                .filter(p -> filterFile(p, searchParams))
                .collect(Collectors.toList());
    }

}
