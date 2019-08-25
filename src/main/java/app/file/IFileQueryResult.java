package app.file;

public interface IFileQueryResult {
    boolean isSuccess();

    int fileLineNumber();

    boolean isBeforeRange();

    boolean isAfterRange();
}
