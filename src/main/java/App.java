import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App {

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
    @FXML private Label subjectAverageLabel;
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
    @FXML private Label assessmentDetailsTitleLabel;
    @FXML private Label assessmentTypeLabel;
    @FXML private Label assessmentScoreLabel;
    @FXML private Label assessmentMaxScoreLabel;
    @FXML private Label assessmentPercentLabel;

    public void init(Stage stage, Gradebook gradebook) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/Gradebook GUI.fxml"));
        Parent scene = loader.load();

        App app = loader.getController();
        app.setup(gradebook);

        stage.setTitle("Gradebook App");
        stage.setScene(new Scene(scene, 1100, 600));
        stage.setResizable(false);
        stage.show();
    }

    public void setup(Gradebook gradebook) {
        // Populate assessment type dropdown
        assessmentTypeBox.getItems().addAll("assignment", "test", "exam");
        assessmentTypeBox.setValue("assignment");

        // Load saved students
        studentsList.getItems().addAll(gradebook.getStudents());
        studentsList.getSelectionModel().select(0);

        // Load subjects for the initially selected student
        Student initialStudent = studentsList.getSelectionModel().getSelectedItem();
        if (initialStudent != null) {
            subjectsList.getItems().setAll(initialStudent.getSubjects());
            totalGradeAvgLabel.setText(String.format("%.2f%%", initialStudent.getAverage()));
        } else {
            totalGradeAvgLabel.setText("-");
        }
        subjectAverageLabel.setText("Subject Avg: -");

        // Right-click menus
        ContextMenu studentMenu = new ContextMenu();
        MenuItem deleteStudentItem = new MenuItem("delete student");
        studentMenu.getItems().add(deleteStudentItem);
        studentsList.setContextMenu(studentMenu);

        ContextMenu subjectMenu = new ContextMenu();
        MenuItem deleteSubjectItem = new MenuItem("delete subject");
        subjectMenu.getItems().add(deleteSubjectItem);
        subjectsList.setContextMenu(subjectMenu);

        ContextMenu assessmentMenu = new ContextMenu();
        MenuItem deleteAssessmentItem = new MenuItem("delete assessment");
        assessmentMenu.getItems().add(deleteAssessmentItem);
        assessmentsList.setContextMenu(assessmentMenu);

        // Delete student
        studentsList.setOnContextMenuRequested(e -> {
            Student selectedStudent = studentsList.getSelectionModel().getSelectedItem();
            if (selectedStudent == null) return;

            deleteStudentItem.setOnAction(actionEvent -> {
                studentsList.getItems().remove(selectedStudent);
                gradebook.removeStudent(selectedStudent);
                subjectsList.getItems().clear();
                assessmentsList.getItems().clear();

                Student newSelected = studentsList.getSelectionModel().getSelectedItem();
                if (newSelected != null) {
                    subjectsList.getItems().setAll(newSelected.getSubjects());
                }
            });
        });

        // Click student to load their subjects
        studentsList.setOnMouseClicked(e -> {
            Student selectedStudent = studentsList.getSelectionModel().getSelectedItem();
            if (selectedStudent == null) {
                totalGradeAvgLabel.setText("-");
                subjectsList.getItems().clear();
                assessmentsList.getItems().clear();
                subjectAverageLabel.setText("Subject Avg: -");
                return;
            }

            subjectsList.getItems().setAll(selectedStudent.getSubjects());
            assessmentsList.getItems().clear();
            totalGradeAvgLabel.setText(String.format("%.2f%%", selectedStudent.getAverage()));
            subjectAverageLabel.setText("Subject Avg: -");
        });

        // Delete subject
        subjectsList.setOnContextMenuRequested(e -> {
            Subject selectedSubject = subjectsList.getSelectionModel().getSelectedItem();
            if (selectedSubject == null) return;

            deleteSubjectItem.setOnAction(actionEvent -> {
                Student selectedStudent = studentsList.getSelectionModel().getSelectedItem();
                if (selectedStudent == null) return;

                selectedStudent.getSubjects().remove(selectedSubject);
                subjectsList.getItems().remove(selectedSubject);
                assessmentsList.getItems().clear();
            });
        });

        // Click subject to load its assessments
        subjectsList.setOnMouseClicked(e -> {
            Subject selectedSubject = subjectsList.getSelectionModel().getSelectedItem();
            if (selectedSubject == null) {
                assessmentsList.getItems().clear();
                subjectAverageLabel.setText("Subject Avg: -");
                return;
            }

            assessmentsList.getItems().clear();
            assessmentsList.getItems().addAll(selectedSubject.getAssignments());
            assessmentsList.getItems().addAll(selectedSubject.getTests());
            if (selectedSubject.getExam() != null) {
                assessmentsList.getItems().add(selectedSubject.getExam());
            }
            subjectAverageLabel.setText(String.format("Subject Avg: %.2f%%", selectedSubject.getAverage()));
        });

        // Delete assessment
        assessmentsList.setOnContextMenuRequested(e -> {
            Assessment selectedAssessment = assessmentsList.getSelectionModel().getSelectedItem();
            if (selectedAssessment == null) return;

            deleteAssessmentItem.setOnAction(actionEvent -> {
                Subject selectedSubject = subjectsList.getSelectionModel().getSelectedItem();
                if (selectedSubject == null) return;

                selectedSubject.removeAssessment(selectedAssessment);
                assessmentsList.getItems().remove(selectedAssessment);
            });
        });

        // Show selected assessment details
        assessmentsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedAssessment) -> {
            if (selectedAssessment == null) {
                assessmentTypeLabel.setText("Type: -");
                assessmentScoreLabel.setText("Score: -");
                assessmentMaxScoreLabel.setText("Max Score: -");
                assessmentPercentLabel.setText("Percent: -");
                return;
            }

            assessmentTypeLabel.setText("Type: " + selectedAssessment.getType());
            assessmentScoreLabel.setText("Score: " + selectedAssessment.getRawScore());
            assessmentMaxScoreLabel.setText("Max Score: " + selectedAssessment.getMaxScore());
            assessmentPercentLabel.setText(String.format("Percent: %.2f%%", selectedAssessment.getScore() * 100));
        });

        // Add new student
        newStudentButton.setOnAction(e -> {
            String studentName = newStudentField.getText().trim();
            newStudentField.clear();
            if (studentName.isEmpty()) return;

            if (gradebook.getStudentByName(studentName) != null) {
                showError("Duplicate student", "A student with that name already exists.");
                return;
            }

            Student student = new Student(studentName);
            gradebook.addStudent(student);
            studentsList.getItems().add(student);
        });

        // Add new subject to selected student
        newSubjectButton.setOnAction(e -> {
            Student selectedStudent = studentsList.getSelectionModel().getSelectedItem();
            if (selectedStudent == null) return;

            String subjectName = newSubjectField.getText().trim();
            newSubjectField.clear();
            if (subjectName.isEmpty()) return;

            for (Subject subject : selectedStudent.getSubjects()) {
                if (subject.getName().equalsIgnoreCase(subjectName)) {
                    showError("Duplicate subject", "This student already has a subject with that name.");
                    return;
                }
            }

            double assignmentWeight = Double.parseDouble(assignmentWeightField.getText());
            double testWeight = Double.parseDouble(testWeightField.getText());
            double examWeight = Double.parseDouble(examWeightField.getText());
            assignmentWeightField.clear();
            testWeightField.clear();
            examWeightField.clear();

            Subject subject = new Subject(subjectName, testWeight, assignmentWeight, examWeight);
            selectedStudent.addSubject(subject);
            subjectsList.getItems().add(subject);
        });

        // Add new assessment to selected subject
        newAssessmentButton.setOnAction(e -> {
            Subject selectedSubject = subjectsList.getSelectionModel().getSelectedItem();
            if (selectedSubject == null) return;

            String assessmentName = newAssessmentField.getText().trim();
            String type = assessmentTypeBox.getValue();
            newAssessmentField.clear();
            if (assessmentName.isEmpty() || type == null) return;

            double score, maxScore;
            try {
                score = Double.parseDouble(scoreField.getText().trim());
                maxScore = Double.parseDouble(maxScoreField.getText().trim());
            } catch (NumberFormatException ex) {
                showError("Invalid score", "Score and max score must be valid numbers.");
                return;
            }

            if (score < 0 || maxScore <= 0) {
                showError("Invalid score", "Score must be non-negative and max score must be greater than 0.");
                return;
            }

            if (score > maxScore) {
                showError("Invalid score", "Score cannot be greater than max score.");
                return;
            }

            if (type.equals("exam") && selectedSubject.getExam() != null) {
                showError("Exam already exists", "Each subject can have only one exam.");
                return;
            }

            scoreField.clear();
            maxScoreField.clear();

            Assessment assessment = new Assessment(assessmentName, score, maxScore, type);
            boolean added = selectedSubject.addAssessment(assessment);
            if (!added) {
                showError("Cannot add assessment", "This assessment could not be added.");
                return;
            }
            assessmentsList.getItems().add(assessment);
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
