package Storage;

/**
 * Observer pattern
 */
public interface Watchable<T> {
    /**
     * Register new {@link Watcher}, watching on {@code key}
     * @param key key
     * @param watcher watcher
     */
    void watch(String key, Watcher<T> watcher);

    /**
     * Notify watchers who watch {@code key} that it has changed value
     * @param key key
     * @param value new value
     */
    void notifyWatchers(String key, T value);
}
