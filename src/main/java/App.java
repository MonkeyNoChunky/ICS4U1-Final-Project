import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App {

    // importing FXML GUI components from GUI.fxml 
    @FXML private BorderPane appBorder;
    @FXML private VBox leftPanel;
    @FXML private Label studentsLabel;
    @FXML private ListView<Student> studentsList;
    @FXML private TextField newStudentField;
    @FXML private Button newStudentButton;
    @FXML private Label totalGradeAvgLabel;
    @FXML private Label totalGradeAvgTitleLabel;

    @FXML private VBox middlePanel;
    @FXML private Label subjectsLabel;
    @FXML private ListView<Subject> subjectsList;
    @FXML private TextField newSubjectField;
    @FXML private TextField assignmentWeightField;
    @FXML private TextField testWeightField;
    @FXML private TextField examWeightField;
    @FXML private Button newSubjectButton;

    @FXML private VBox rightPanel;
    @FXML private ComboBox<String> assessmentTypeBox;
    @FXML private TextField newAssessmentField;
    @FXML private TextField scoreField;
    @FXML private TextField maxScoreField;
    @FXML private Button newAssessmentButton;
    @FXML private ListView<Assessment> assessmentsList;

    /**
    * Loads the FXML file, matches a MusicApp instance with the FXML loader, 
    * and starts the app
    *
    * @param stage the main JavaFX stage
    * @param gradebook the libraryData instance used to store songs and playists
    * @throws IOException if GUI.fxml can't be loaded
    */
    public void init(Stage stage, Gradebook gradebook) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("Gradebook GUI.fxml"));
        Parent scene = loader.load();

        // Gets the controller attached to the FXML to setup the app
        App app = loader.getController();
        app.setup(gradebook);

        stage.setTitle("gradebook app");
        stage.setScene(new Scene(scene, 1100, 600));
        stage.setResizable(false);
        stage.show();
    }

    /**
    * Setup the GUI by loading data into ListViews, assigning icons,
    * adding contextMenus, and coding all event listeners
    *
    * @param libraryData libraryData instance used for songs and playlists
    * @param musicPlayer musicPlayer instance used for playback
    */
    public void setup(Gradebook gradebook) {
        // Fill the library panel with the saved student data
        studentsList.getItems().addAll(gradebook.getStudents());
        // Automatically default to the song library on load
        studentsList.getSelectionModel().select(0);
        // subjectsList.getItems().setAll(allSongsPlaylist.getSongs());

        // Fill the songs panel with all the songs in the library
        // songsList.getItems().addAll(libraryData.getSongs());

        // Creating the right click menu for students
        ContextMenu studentMenu = new ContextMenu();
        MenuItem deleteStudentItem = new MenuItem("delete student");
        studentMenu.getItems().add(deleteStudentItem);
        studentsList.setContextMenu(studentMenu);

        // Creating a right-click menu for the subjects
        ContextMenu subjectMenu = new ContextMenu();
        MenuItem deleteSubjectItem = new MenuItem("delete subject");
        subjectsList.setContextMenu(subjectMenu);

        // Right-click actions on playlists
        studentsList.setOnContextMenuRequested(e -> {
            Student selectedStudent = studentsList.getSelectionModel().getSelectedItem();
            if(selectedStudent == null) return;

            deleteStudentItem.setOnAction(actionEvent -> {
                // Removes the selected playlist from the GUI and backend data
                studentsList.getItems().remove(selectedStudent);
                gradebook.removeStudent(selectedStudent);

                // Updates the GUI
                Student newSelectedStudent = studentsList.getSelectionModel().getSelectedItem();
                // currentPlaylistLabel.setText(newSelectedPlaylist.toString());
                subjectsList.getItems().setAll(newSelectedStudent.getSubjects());

                // Shows the action feedback to the user
                // actionDoneLabel.setText("Deleted playlist: " + selectedPlaylist.toString());
            });
        });

        // When a playlist is clicked, update the song list
        studentsList.setOnMouseClicked(e -> {
            Student selectedStudent = studentsList.getSelectionModel().getSelectedItem();

            if (selectedStudent == null) return;

            // currentPlaylistLabel.setText(selectedPlaylist.toString());
            subjectsList.getItems().setAll(selectedStudent.getSubjects());
        });

        // When a song is clicked, play the song and add the rest on the list to queue 
        subjectsList.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                Subject selectedSubject = subjectsList.getSelectionModel().getSelectedItem();
                int selectedIndex = subjectsList.getSelectionModel().getSelectedIndex();

                if(selectedSubject == null) return;

                // // Play the remaining songs in order from the playlist
                // java.util.ArrayList<Song> currentSongs = new java.util.ArrayList<>(songsList.getItems());
                // musicPlayer.playFromList(currentSongs, selectedIndex); // Plays the current song and adds the rest to the queue
                // pauseOrPlayButton.setGraphic(pauseIcon);
            }
        });

        // Creates a playlist with a name using the specified text in the text box
        newStudentButton.setOnAction(e -> {
            String studentName = newStudentField.getText().trim();
            newStudentField.clear();

            if(studentName.isEmpty()) return;

            Student student = new Student(studentName);
            gradebook.addStudent(student);
            studentsList.getItems().add(student);
            // actionDoneLabel.setText("Created playlist: " + playlistName);
        });
    }
}