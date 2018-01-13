package util;

public enum ReaderStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED;

    @Override
    public String toString() {
        return this.name();
    }
}
