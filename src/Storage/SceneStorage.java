package Storage;

import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of {@link Storage} interface for {@link Scene} class
 * It also uses the singleton pattern hence the private constructor
 *
 */
@SuppressWarnings("unchecked")
public class SceneStorage implements Storage<Scene>, Watchable<Scene> {
    private static SceneStorage instance = new SceneStorage();
    private ConcurrentMap<String,Scene> scenes;
    private List<WatchEntry<Scene>> watchers;

    private SceneStorage() {
        scenes = new ConcurrentHashMap<>();
        watchers = new ArrayList<>();
    }

    /**
     * @return SceneStorage instance
     */
    public static SceneStorage getInstance() {
        return instance;
    }

    /**
     * Get {@link Scene} object stored under the name {@code key}
     * @param key key
     * @return {@link Scene}
     */
    @Override
    public Scene get(String key) {
        return scenes.get(key);
    }

    /**
     * Put {@link Scene} object into storage
     * @param key key
     * @param object new value
     */
    @Override
    public void put(String key, Scene object) {
        scenes.put(key, object);
    }

    /**
     * Remove {@link Scene} object stored under the name {@code key}
     * @param key key
     */
    public void remove(String key) {
        scenes.remove(key);
    }

    /**
     * Register new watcher on {@code key}
     * @param key key
     * @param watcher watcher
     */
    @Override
    public void watch(String key, Watcher<Scene> watcher) {
        watchers.add(new WatchEntry(key, watcher));
    }

    /**
     * Notify watchers that the object under the name {@code key} has new value
     * @param key key
     * @param value new value
     */
    @Override
    public void notifyWatchers(String key, Scene value) {
        for (WatchEntry watchEntry : watchers) {
            if (watchEntry.getKey().equals(key)) {
                watchEntry.getWatcher().update(key, value);
            }
        }
    }

}
