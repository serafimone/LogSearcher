package ru.serafimodin.app.file;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class FileInformation {
    private Path path;
    private long firstLineWithNeededText;

    public FileInformation(@NotNull Path path, long firstLineWithNeededText) {
        this.path = path;
        this.firstLineWithNeededText = firstLineWithNeededText;
    }

    public Path getPath() {
        return path;
    }

    public long getFirstLineWithNeededText() {
        return firstLineWithNeededText;
    }
}
