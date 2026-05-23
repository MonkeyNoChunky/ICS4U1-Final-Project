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

    public double getAverage() {
        if (subjects.isEmpty()) return 0;
        double total = 0;
        for (Subject subject : subjects) {
            total += subject.getAverage();
        }
        return Math.round(total / subjects.size() * 10000.0) / 100.0;
    }

    @Override
    public String toString() {
        return name;
    }
}