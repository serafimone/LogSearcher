package app.file;

public enum OutsideRangeQueryResult implements IFileQueryResult {
    BEFORE( true ), AFTER( false );

    private final boolean before;

    OutsideRangeQueryResult( boolean before ) {
        this.before = before;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public int fileLineNumber() {
        return this == BEFORE ? -1 : -2;
    }

    @Override
    public boolean isBeforeRange() {
        return before;
    }

    @Override
    public boolean isAfterRange() {
        return !before;
    }
}
