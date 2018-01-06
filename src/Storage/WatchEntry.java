package Storage;

/**
 * Class storing the pair of key and {@link Watcher}
 * @author katjon
 */
public final class WatchEntry<T> {
    private String key;
    private Watcher<T> watcher;

    /**
     * @param key key, that will be listened on
     * @param watcher object, that watches
     */
    public WatchEntry(String key, Watcher<T> watcher) {
        this.key = key;
        this.watcher = watcher;
    }

    /**
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return {@link Watcher}
     */
    public Watcher<T> getWatcher() {
        return watcher;
    }
}
