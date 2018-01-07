package Storage;

import Controllers.SceneController;

import java.util.concurrent.ConcurrentHashMap;

public class ControllerStorage implements Storage<SceneController> {
//    private Map<String, SceneController>
    private static ControllerStorage instance = new ControllerStorage();
    private final ConcurrentHashMap<String, SceneController> controllers;

    private ControllerStorage() {
        controllers = new ConcurrentHashMap<>();
    }

    public static ControllerStorage getInstance() {
        return instance;
    }

    @Override
    public SceneController get(String key) {
        return controllers.get(key);
    }

    @Override
    public void put(String key, SceneController object) {
        controllers.put(key,object);
    }
}
