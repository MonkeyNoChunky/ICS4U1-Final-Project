import java.util.ArrayList;

public class Student {
    private String name;
    private ArrayList<Subject> subjects = new ArrayList<>(); // The ArrayList to organize the subjects a student has

    /**
    * Constructor for the Student object 
    *
    * @param name the name of the student
    */
    public Student(String name) {
        this.name = name;
    }

    /**
     * Adds a subject object to the student
     * 
    * @param subject the subject wanting to be added
    */
    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    /**
    * @return returns the String student name
    */
    public String getName() {
        return name;
    }

    /**
    * @return returns the ArrayList of subjects the student has
    */
    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    /**
    * Returns the average of the student across all subjects
    *
    * @return the average in percentage format
    */
    public double getAverage() {
        if (subjects.isEmpty()) return 0;
        double total = 0;
        for (Subject subject : subjects) {
            total += subject.getAverage();
        }
        return total / subjects.size();
    }

    @Override
    public String toString() {
        return name;
    }
}