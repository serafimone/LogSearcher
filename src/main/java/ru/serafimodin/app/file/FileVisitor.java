package ru.serafimodin.app.file;

import javafx.collections.ObservableList;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.serafimodin.app.data.SearchParams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileVisitor extends SimpleFileVisitor<Path> {
    private static final Logger log = LoggerFactory.getLogger(FileVisitor.class.getName());

    private final SearchParams searchParams;
    private final ObservableList<Path> foundFiles;

    public FileVisitor(@NotNull SearchParams searchParams, @NotNull ObservableList<Path> foundFiles) {
        super();
        this.searchParams = searchParams;
        this.foundFiles = foundFiles;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (FilenameUtils.getExtension(file.toString()).equals(searchParams.getFileExtension())) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file.toFile()));
                String nextString;
                while ((nextString = reader.readLine()) != null) {
                    if (nextString.contains(searchParams.getSearchText())) {
                        foundFiles.add(file);
                        return FileVisitResult.CONTINUE;
                    }
                }
                reader.close();
            } catch (IOException ex) {
                log.error("Exception: ", ex);
                return FileVisitResult.CONTINUE;
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        log.warn("Failed visit file at path: {}", file.toAbsolutePath().toString());
        return FileVisitResult.SKIP_SUBTREE;
    }

    private @NotNull ObservableList<Path> getFoundFiles() {
        return this.foundFiles;
    }
}
