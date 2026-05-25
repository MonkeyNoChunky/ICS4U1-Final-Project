import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;

public class Gradebook {
    private ArrayList<Student> students = new ArrayList<>(); // ArrayList to track the students in the gradebook
    private static Gson gson = new Gson(); // Used to serialize the gradebook class into the JSON file

    /**
    * Adds a Student to the ArrayList and saves the data
    */
    public void addStudent(Student student) {
        students.add(student);
        save();
    }

    /**
    * removes a Student from the students ArrayList and saves the data
    */
    public void removeStudent(Student removedStudent) {
        students.remove(removedStudent);
        save();
    }

    /**
    * Uses gson to serialize the current object data and write it to the JSON file
    */
    public void save() {
        try (Writer writer = new FileWriter("studentData.json")) {
            writer.write(gson.toJson(this));
        } catch (IOException e) {

        }
    }

    /**
    * Uses gson to deseralize the object data from the JSON file and update the students list
    */
    public void load() {
        try (Reader reader = new FileReader("studentData.json")) {
            Gradebook loadedGradebook = gson.fromJson(reader, Gradebook.class);

            if (loadedGradebook == null) return;

            // Using the loaded data to update the students list
            students = loadedGradebook.getStudents();
        } catch (IOException e) {

        }
    }

    /**
    * @return returns the ArrayList of students in the current gradebook object
    */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
    * Returns the student object associated with a name
    */
    public Student getStudentByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        return null;
    }
}