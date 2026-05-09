import java.util.ArrayList;

public class Subject {
    private String name;
    private int testsWeight;
    private int assignmentsWeight;
    private int examWeight;
    private ArrayList<Assessment> tests;
    private ArrayList<Assessment> assignments;
    private Assessment exam; 

    public Subject(String name, int testsWeight, int assignmentsWeight, int examWeight) {
        this.name = name;
        this.testsWeight = testsWeight;
        this.assignmentsWeight = assignmentsWeight;
        this.examWeight = examWeight;
    }

    public void addAssessment(Assessment assessment) {
        if(assessment.getType().equals("exam")) {
            exam = assessment;
        }
        if(assessment.getType().equals("test")) {
            tests.add(assessment);
        }
        if(assessment.getType().equals("assignment")) {
            assignments.add(assessment);
        }
    }

    public void getAverage() {
        
    }
}
