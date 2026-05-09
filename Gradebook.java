import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;

public class Gradebook {
    private ArrayList<Student> students = new ArrayList<>();
    private static Gson gson = new Gson();

    public void addStudent(Student student) {
        students.add(student);
    }

    public void save() {
        try (Writer writer = new FileWriter("studentData.json")) {
            writer.write(gson.toJson(this));
        } catch (IOException e) {

        }
    }

    public void load() {
        try (Reader reader = new FileReader("studentData.json")) {
            Gradebook loadedGradebook = gson.fromJson(reader, Gradebook.class);

            if (loadedGradebook == null) return;

            students = loadedGradebook.getStudents();
        } catch (IOException e) {

        }
    }

    public void clear() {
        students.clear();
        save();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        String formattedString = "";

        for(Student student : students) {
            formattedString += student.getName() + "\n";
        }

        return formattedString;
    }
}