package ru.serafimodin.app.file;

import java.util.TreeSet;

public class FileLineStarts {

    private final int size;
    private final TreeSet<Long> indexes = new TreeSet<>();

    FileLineStarts(int size) {
        if (size < 1L) {
            throw new IllegalArgumentException("Size must be more than zero!");
        }
        this.size = size;
    }

    void addFirst( long index ) {
        indexes.add( index );
        trim( false );
    }

    void addLast( long index ) {
        indexes.add( index );
        trim( true );
    }

    void clear() {
        indexes.clear();
    }

    long getFirst() {
        if ( indexes.isEmpty() ) {
            return 0L;
        } else {
            return indexes.first();
        }
    }

    long getLast() {
        if ( indexes.isEmpty() ) {
            return 0L;
        } else {
            return indexes.last();
        }
    }

    private void trim( boolean fromBeginning ) {
        Runnable remove = fromBeginning ?
                () -> indexes.remove( getFirst() ) :
                () -> indexes.remove( getLast() );

        while ( indexes.size() > size ) {
            remove.run();
        }
    }

    @Override
    public String toString() {
        return "FileLineStarts{" +
                "indexes=" + indexes +
                '}';
    }
}
