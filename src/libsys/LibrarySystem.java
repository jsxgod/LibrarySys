package libsys;

import Controllers.*;
import Storage.ControllerStorage;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LibrarySystem extends Application {

    private Stage window;
    private SceneStorage sceneStorage;
    private ParameterStorage parameterStorage;
    private ControllerStorage controllerStorage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;

        sceneStorage = SceneStorage.getInstance();
        parameterStorage = ParameterStorage.getInstance();
        controllerStorage = ControllerStorage.getInstance();

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/scenes/login.fxml"));
        FXMLLoader searchTitleLoader = new FXMLLoader(getClass().getResource("/scenes/browseTitles.fxml"));
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/scenes/menu.fxml"));
        FXMLLoader borrowLoader = new FXMLLoader(getClass().getResource("/scenes/borrow.fxml"));
        FXMLLoader returnLoader = new FXMLLoader(getClass().getResource("/scenes/return.fxml"));
        FXMLLoader addReaderLoader = new FXMLLoader(getClass().getResource("/scenes/addReader.fxml"));
        FXMLLoader browseReadersLoader = new FXMLLoader(getClass().getResource("/scenes/browseReaders.fxml"));
        FXMLLoader addTitleLoader = new FXMLLoader(getClass().getResource("/scenes/addTitle.fxml"));
        FXMLLoader readerInfoLoader = new FXMLLoader(getClass().getResource("/scenes/readerInfo.fxml"));
        FXMLLoader menuSupervisorLoader = new FXMLLoader(getClass().getResource("/scenes/menuSupervisor.fxml"));
        FXMLLoader addEmployeeLoader = new FXMLLoader(getClass().getResource("/scenes/addEmployee.fxml"));
        FXMLLoader browseEmployeesLoader = new FXMLLoader(getClass().getResource("/scenes/browseEmployees.fxml"));

        Parent loginRoot = loginLoader.load();
        Parent searchTitleRoot = searchTitleLoader.load();
        Parent menuRoot = menuLoader.load();
        Parent borrowRoot = borrowLoader.load();
        Parent returnRoot = returnLoader.load();
        Parent addReaderRoot = addReaderLoader.load();
        Parent browseReadersRoot = browseReadersLoader.load();
        Parent addTitleRoot = addTitleLoader.load();
        Parent readerInfoRoot = readerInfoLoader.load();
        Parent menuSupervisorRoot = menuSupervisorLoader.load();
        Parent addEmployeeRoot = addEmployeeLoader.load();
        Parent browseEmployeesRoot = browseEmployeesLoader.load();

        Scene loginScene = new Scene(loginRoot);
        Scene searchTitleScene = new Scene(searchTitleRoot);
        Scene menuScene = new Scene(menuRoot);
        Scene borrowScene = new Scene(borrowRoot);
        Scene returnScene = new Scene(returnRoot);
        Scene addReaderScene = new Scene(addReaderRoot);
        Scene browseReadersScene = new Scene(browseReadersRoot);
        Scene addTitleScene = new Scene(addTitleRoot);
        Scene readerInfoScene = new Scene(readerInfoRoot);
        Scene menuSupervisorScene = new Scene(menuSupervisorRoot);
        Scene addEmployeeScene = new Scene(addEmployeeRoot);
        Scene browseEmployeesScene = new Scene(browseEmployeesRoot);

        sceneStorage.put("login", loginScene);
        sceneStorage.put("browseTitles", searchTitleScene);
        sceneStorage.put("menu", menuScene);
        sceneStorage.put("borrow", borrowScene);
        sceneStorage.put("return", returnScene);
        sceneStorage.put("addReader", addReaderScene);
        sceneStorage.put("browseReaders", browseReadersScene);
        sceneStorage.put("addTitle", addTitleScene);
        sceneStorage.put("readerInfo", readerInfoScene);
        sceneStorage.put("menuSupervisor", menuSupervisorScene);
        sceneStorage.put("addEmployee", addEmployeeScene);
        sceneStorage.put("browseEmployees", browseEmployeesScene);

        LoginController loginController = loginLoader.getController();
        BrowseTitlesController browseTitlesController = searchTitleLoader.getController();
        MenuController menuController = menuLoader.getController();
        BorrowController borrowController = borrowLoader.getController();
        ReturnController returnController = returnLoader.getController();
        AddReaderController addReaderController = addReaderLoader.getController();
        BrowseReadersController browseReadersController = browseReadersLoader.getController();
        AddTitleController addTitleController = addTitleLoader.getController();
        ReaderInfoController readerInfoController = readerInfoLoader.getController();
        MenuSupervisorController menuSupervisorController = menuSupervisorLoader.getController();
        AddEmployeeController addEmployeeController = addEmployeeLoader.getController();
        BrowseEmployeesController browseEmployeesController = browseEmployeesLoader.getController();

        controllerStorage.put("readerInfo", readerInfoController);

        loginController.setupController(this.window);
        browseTitlesController.setupController(this.window);
        menuController.setupController(this.window);
        borrowController.setupController(this.window);
        returnController.setupController(this.window);
        addReaderController.setupController(this.window);
        browseReadersController.setupController(this.window);
        addTitleController.setupController(this.window);
        readerInfoController.setupController(this.window);
        menuSupervisorController.setupController(this.window);
        addEmployeeController.setupController(this.window);
        browseEmployeesController.setupController(this.window);

        primaryStage.setScene(addEmployeeScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
