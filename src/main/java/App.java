import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class App {
    private Gradebook gradebook;
    private Stage stage;

    // UI Components
    private ListView<String> studentListView;
    private ListView<String> subjectListView;
    private TextArea gradeDisplayArea;
    private ComboBox<String> studentSelector;
    private ComboBox<String> studentSelectorAssess;
    private ComboBox<String> subjectSelector;

    public void init(Stage stage, Gradebook gradebook) throws IOException {
        this.stage = stage;
        this.gradebook = gradebook;

        stage.setTitle("Gradebook Application");
        stage.setScene(createMainScene());
        stage.setWidth(900);
        stage.setHeight(700);
        stage.setResizable(true);
        stage.show();
    }

    private Scene createMainScene() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(
                createStudentTab(),
                createSubjectTab(),
                createAssessmentTab(),
                createGradeViewTab()
        );

        return new Scene(tabPane);
    }

    private Tab createStudentTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        // Student List
        Label listLabel = new Label("Students:");
        studentListView = new ListView<>();
        updateStudentList();

        // Add Student Section
        HBox addBox = new HBox(10);
        TextField studentNameField = new TextField();
        studentNameField.setPromptText("Enter student name");
        Button addButton = new Button("Add Student");

        addButton.setOnAction(e -> {
            String name = studentNameField.getText().trim();
            if (!name.isEmpty()) {
                Student newStudent = new Student(name);
                gradebook.addStudent(newStudent);
                gradebook.save();
                studentNameField.clear();
                updateStudentList();
                updateStudentSelectors();
            }
        });

        addBox.getChildren().addAll(studentNameField, addButton);

        // Remove Student Section
        HBox removeBox = new HBox(10);
        Button removeButton = new Button("Remove Selected Student");
        removeButton.setOnAction(e -> {
            String selected = studentListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                gradebook.removeStudent(selected);
                gradebook.save();
                updateStudentList();
                updateStudentSelectors();
            }
        });
        removeBox.getChildren().add(removeButton);

        vbox.getChildren().addAll(
                listLabel,
                studentListView,
                new Separator(),
                new Label("Add New Student:"),
                addBox,
                new Separator(),
                removeBox
        );

        Tab tab = new Tab("Manage Students", vbox);
        return tab;
    }

    private Tab createSubjectTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        // Student Selector
        Label selectLabel = new Label("Select Student:");
        studentSelector = new ComboBox<>();
        updateStudentCombo(studentSelector);

        HBox selectorBox = new HBox(10);
        selectorBox.getChildren().addAll(selectLabel, studentSelector);

        // Subjects List
        Label subjectLabel = new Label("Subjects for Student:");
        subjectListView = new ListView<>();
        studentSelector.setOnAction(e -> updateSubjectList());

        // Add Subject Section
        VBox addSubjectBox = new VBox(10);
        addSubjectBox.setStyle("-fx-border-color: #cccccc; -fx-padding: 10;");

        Label addLabel = new Label("Add New Subject:");
        TextField subjectNameField = new TextField();
        subjectNameField.setPromptText("Subject name");

        HBox weightsBox = new HBox(10);
        TextField testsWeightField = new TextField();
        testsWeightField.setPromptText("Tests weight (0-1)");
        testsWeightField.setPrefWidth(120);

        TextField assignWeightField = new TextField();
        assignWeightField.setPromptText("Assignment weight (0-1)");
        assignWeightField.setPrefWidth(140);

        TextField examWeightField = new TextField();
        examWeightField.setPromptText("Exam weight (0-1)");
        examWeightField.setPrefWidth(120);

        weightsBox.getChildren().addAll(testsWeightField, assignWeightField, examWeightField);

        Button addSubjectButton = new Button("Add Subject");
        addSubjectButton.setOnAction(e -> {
            String studentName = studentSelector.getValue();
            String subjectName = subjectNameField.getText().trim();

            if (studentName != null && !studentName.isEmpty() && !subjectName.isEmpty()) {
                try {
                    double testsW = Double.parseDouble(testsWeightField.getText());
                    double assignW = Double.parseDouble(assignWeightField.getText());
                    double examW = Double.parseDouble(examWeightField.getText());

                    Student student = gradebook.getStudentByName(studentName);
                    if (student != null) {
                        Subject subject = new Subject(subjectName, testsW, assignW, examW);
                        student.addSubject(subject);
                        gradebook.save();
                        subjectNameField.clear();
                        testsWeightField.clear();
                        assignWeightField.clear();
                        examWeightField.clear();
                        updateSubjectList();
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Input", "Weights must be decimal numbers");
                }
            }
        });

        addSubjectBox.getChildren().addAll(
                addLabel,
                new Label("Subject Name:"),
                subjectNameField,
                new Label("Weights (should sum to ~1.0):"),
                weightsBox,
                addSubjectButton
        );

        vbox.getChildren().addAll(
                selectorBox,
                subjectLabel,
                subjectListView,
                new Separator(),
                addSubjectBox
        );

        return new Tab("Manage Subjects", vbox);
    }

    private Tab createAssessmentTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        // Student Selector
        Label selectLabel = new Label("Select Student:");
        studentSelectorAssess = new ComboBox<>();
        updateStudentCombo(studentSelectorAssess);

        // Subject Selector
        Label subjectLabel = new Label("Select Subject:");
        subjectSelector = new ComboBox<>();
        studentSelectorAssess.setOnAction(e -> updateSubjectCombo());

        HBox selectors = new HBox(20);
        selectors.getChildren().addAll(
                selectLabel, studentSelectorAssess,
                subjectLabel, subjectSelector
        );

        // Assessment Input Section
        VBox assessmentBox = new VBox(10);
        assessmentBox.setStyle("-fx-border-color: #cccccc; -fx-padding: 10;");

        Label typeLabel = new Label("Assessment Type:");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("test", "assignment", "exam");

        TextField assessNameField = new TextField();
        assessNameField.setPromptText("Assessment name");

        TextField scoreField = new TextField();
        scoreField.setPromptText("Score");
        scoreField.setPrefWidth(100);

        TextField maxScoreField = new TextField();
        maxScoreField.setPromptText("Max Score");
        maxScoreField.setPrefWidth(100);

        HBox scoreBox = new HBox(10);
        scoreBox.getChildren().addAll(new Label("Score:"), scoreField, new Label("Max:"), maxScoreField);

        Button addAssessButton = new Button("Add Assessment");
        addAssessButton.setOnAction(e -> {
            String studentName = studentSelectorAssess.getValue();
            String subjectName = subjectSelector.getValue();
            String type = typeCombo.getValue();
            String name = assessNameField.getText().trim();

            if (studentName != null && subjectName != null && type != null && !name.isEmpty()) {
                try {
                    double score = Double.parseDouble(scoreField.getText());
                    double maxScore = Double.parseDouble(maxScoreField.getText());

                    Student student = gradebook.getStudentByName(studentName);
                    if (student != null) {
                        Subject subject = null;
                        for (Subject s : student.getSubjects()) {
                            if (s.getName().equals(subjectName)) {
                                subject = s;
                                break;
                            }
                        }

                        if (subject != null) {
                            Assessment assessment = new Assessment(name, score, maxScore, type);
                            subject.addAssessment(assessment);
                            gradebook.save();
                            assessNameField.clear();
                            scoreField.clear();
                            maxScoreField.clear();
                            typeCombo.setValue(null);
                            showAlert("Success", "Assessment added successfully");
                        }
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Input", "Score and Max Score must be numbers");
                }
            } else {
                showAlert("Error", "Please fill all fields");
            }
        });

        assessmentBox.getChildren().addAll(
                typeLabel,
                typeCombo,
                new Label("Assessment Name:"),
                assessNameField,
                scoreBox,
                addAssessButton
        );

        vbox.getChildren().addAll(
                selectors,
                new Separator(),
                assessmentBox
        );

        return new Tab("Manage Assessments", vbox);
    }

    private Tab createGradeViewTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Label titleLabel = new Label("Student Grades Summary:");

        gradeDisplayArea = new TextArea();
        gradeDisplayArea.setWrapText(true);
        gradeDisplayArea.setEditable(false);
        gradeDisplayArea.setPrefHeight(400);

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> updateGradeDisplay());

        updateGradeDisplay();

        vbox.getChildren().addAll(titleLabel, gradeDisplayArea, refreshButton);
        return new Tab("View Grades", vbox);
    }

    private void updateStudentList() {
        ArrayList<String> names = new ArrayList<>();
        for (Student s : gradebook.getStudents()) {
            names.add(s.getName());
        }
        studentListView.getItems().setAll(names);
    }

    private void updateStudentSelectors() {
        updateStudentCombo(studentSelector);
        updateStudentCombo(studentSelectorAssess);
    }

    private void updateStudentCombo(ComboBox<String> combo) {
        ArrayList<String> names = new ArrayList<>();
        for (Student s : gradebook.getStudents()) {
            names.add(s.getName());
        }
        combo.getItems().setAll(names);
    }

    private void updateSubjectList() {
        ArrayList<String> names = new ArrayList<>();
        String selectedStudent = studentSelector.getValue();

        if (selectedStudent != null) {
            Student student = gradebook.getStudentByName(selectedStudent);
            if (student != null) {
                for (Subject s : student.getSubjects()) {
                    names.add(s.getName());
                }
            }
        }
        subjectListView.getItems().setAll(names);
    }

    private void updateSubjectCombo() {
        ArrayList<String> names = new ArrayList<>();
        String selectedStudent = studentSelectorAssess.getValue();

        if (selectedStudent != null) {
            Student student = gradebook.getStudentByName(selectedStudent);
            if (student != null) {
                for (Subject s : student.getSubjects()) {
                    names.add(s.getName());
                }
            }
        }
        subjectSelector.getItems().setAll(names);
    }

    private void updateGradeDisplay() {
        StringBuilder sb = new StringBuilder();

        for (Student student : gradebook.getStudents()) {
            sb.append("=== ").append(student.getName()).append(" ===\n");

            if (student.getSubjects().isEmpty()) {
                sb.append("  No subjects\n");
            } else {
                for (Subject subject : student.getSubjects()) {
                    double average = subject.getAverage();
                    String letterGrade = getLetterGrade(average);
                    sb.append(String.format("  %s: %.2f%% (%s)\n", subject.getName(), average * 100, letterGrade));
                }
            }
            sb.append("\n");
        }

        gradeDisplayArea.setText(sb.toString().isEmpty() ? "No students" : sb.toString());
    }

    private String getLetterGrade(double percentage) {
        if (percentage >= 0.90) return "A";
        if (percentage >= 0.80) return "B";
        if (percentage >= 0.70) return "C";
        if (percentage >= 0.60) return "D";
        return "F";
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setup(Gradebook gradebook) {
    }
}