import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);  // launches the JavaFX program and calls start()
    }

    /**
    * Starts the application by creating the backend objects,
    * loading saved data, and starting the GUI window
    *
    * @param stage the auto created JavaFX stage
    * @throws IOException used if the FXML file can't be loaded
    */
    @Override
    public void start(Stage stage) throws IOException {
        Gradebook gradebook = new Gradebook(); 
        gradebook.load(); // Loads the saved student data into the current gradebook object
        
        App app = new App();
        app.init(stage, gradebook); // Launches the GUI for the app
    }
}
