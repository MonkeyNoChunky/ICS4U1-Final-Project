import java.util.ArrayList;
public class Student {
    private String name;
    private ArrayList<Subject> subjects = new ArrayList<>();

    public Student(String name) {
        this.name = name;
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    @Override
    public String toString() {
        return name;
    }
}