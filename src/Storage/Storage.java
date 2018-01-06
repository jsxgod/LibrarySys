package Storage;

/**
 * Interface for storage, which stores objects under the key.
 */
public interface Storage<T> {
    /**
     * Get object under the name {@code key} from the Storage
     * @param key key
     * @return object stored under {@code key} or null
     */
    T get(String key);

    /**
     * Store the object in the storage
     * @param key key
     * @param object object to be stored
     */
    void put(String key, T object);
}