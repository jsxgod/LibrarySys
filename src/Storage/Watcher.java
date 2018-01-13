package Storage;

/**
 * Interface for Watcher, part of the Observer pattern
 * @author katjon
 */
public interface Watcher<T> {
    /**
     * Notify watcher, that property under {@code key} has changed, and has value {@code value}
     * @param key key of property changed
     * @param value new value of property
     */
    void update(String key, T value);
}
