import java.util.ArrayList;

public class Subject {
    private String name;
    private double testsWeight;
    private double assignmentsWeight;
    private double examWeight;
    private ArrayList<Assessment> tests = new ArrayList<>();
    private ArrayList<Assessment> assignments = new ArrayList<>();
    private Assessment exam; 

    public Subject(String name, double testsWeight, double assignmentsWeight, double examWeight) {
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

    public double getAverage() {
        double average = 0;
        for (Assessment test : tests) {
            average += test.getScore() * testsWeight;
        }
        for (Assessment assignment : assignments) {
            average += assignment.getScore() * assignmentsWeight;
        }
        if (exam != null) {
            average += exam.getScore() * examWeight;
        }
        return average;
    }

    
}
