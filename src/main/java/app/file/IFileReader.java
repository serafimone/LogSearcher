package app.file;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IFileReader {

    void setLineFilter(Predicate<String> lineFilter);

    Optional<? extends List<String>> moveUp(int lines);

    Optional<? extends List<String>> moveDown(int lines);

    IFileQueryResult moveTo(ZonedDateTime dateTime,
                           Function<String, Optional<ZonedDateTime>> dateExtractor);

    void top();

    void tail();

    Optional<? extends List<String>> refresh();

    File getFile();

}
