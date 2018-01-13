package Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of {@link Storage} interface for Object, to store parameters of application
 * It also uses the singleton pattern hence the private constructor
 *
 */
@SuppressWarnings("unchecked")
public class ParameterStorage implements Storage<Object>, Watchable<Object> {
    private ConcurrentMap<String, Object> parameters;
    private List<WatchEntry<Object>> watchers;

    private static ParameterStorage instance = new ParameterStorage();

    /**
     * @return ParameterStorage instance
     */
    public static ParameterStorage getInstance() {
        return instance;
    }

    private ParameterStorage() {
        parameters = new ConcurrentHashMap<>();
        watchers = new ArrayList<>();
    }

    /**
     * Get the object stored under the name {@code key} from {@code parameters}
     * @param key key
     * @return desired Object
     */
    public Object get(String key) {
        return parameters.get(key);
    }

    /**
     * Store the object under the name {@code key}
     * @param key key
     * @param object Object to be stored
     * Note that this method also calls {@link #notifyWatchers(String, Object)}
     */
    public void put(String key, Object object) {
        System.out.println("Putting " + key);
        parameters.put(key, object);
        notifyWatchers(key, object);
    }

    /**
     * Remove the object under the name {@code key}
     * @param key key
     */
    public void remove(String key) {
        System.out.println("removing " + key);
        parameters.remove(key);
    }

    /**
     * Register a new watcher
     * @param key key
     * @param watcher watcher
     */
    @Override
    public void watch(String key, Watcher<Object> watcher) {
        watchers.add(new WatchEntry(key, watcher));
    }

    /**
     * Notify all watchers that the object under the name {@code key} has changed
     * @param key key
     * @param object new object
     */
    @Override
    public void notifyWatchers(String key, Object object) {
        for (WatchEntry watchEntry : watchers) {
            if (watchEntry.getKey().equals(key)) {
                watchEntry.getWatcher().update(key, object);
            }
        }
    }

}
