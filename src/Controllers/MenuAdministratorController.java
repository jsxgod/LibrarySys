package Controllers;

import javafx.event.ActionEvent;

public class MenuAdministratorController extends MenuSupervisorController {

    public void handleBackup(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("backup"));
        window.setTitle("Backup");
        window.centerOnScreen();
    }

    public void handleLogOut(ActionEvent actionEvent){
        parameterStorage.remove("currentEmployee");
        parameterStorage.remove("currentUser");
        sceneStorage.remove("currentMenu");

        window.setScene(sceneStorage.get("login"));
        window.setTitle("Login");
        window.centerOnScreen();
    }
}
