package libsys;

import Controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LibrarySystem extends Application {

    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/scenes/login.fxml"));
        Parent loginRoot = loginLoader.load();

        Scene loginScene = new Scene(loginRoot,600,300);

        LoginController loginController = loginLoader.getController();

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}
