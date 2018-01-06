package Storage;

import Controllers.SceneController;

public class ControllerStorage implements Storage<SceneController> {
//    private Map<String, SceneController>
    private static ControllerStorage instance = new ControllerStorage();

    private ControllerStorage() {

    }

    @Override
    public SceneController get(String key) {
        return null;
    }

    @Override
    public void put(String key, SceneController object) {

    }
}
