import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws IOException {
        Gradebook gradebook = new Gradebook(); 
        gradebook.load();
        App app = new App();
        // app.init(stage, gradebook);
        System.out.println(gradebook.toString());
    }

}
