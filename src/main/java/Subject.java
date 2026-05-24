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

    public boolean addAssessment(Assessment assessment) {
        if (assessment.getType().equals("exam")) {
            if (exam != null) {
                return false;
            }
            exam = assessment;
            return true;
        }
        if (assessment.getType().equals("test")) {
            tests.add(assessment);
            return true;
        }
        if (assessment.getType().equals("assignment")) {
            assignments.add(assessment);
            return true;
        }
        return false;
    }

    public void removeAssessment(Assessment assessment) {
        if (assessment.getType().equals("exam") && exam == assessment) {
            exam = null;
        } else if (assessment.getType().equals("test")) {
            tests.remove(assessment);
        } else if (assessment.getType().equals("assignment")) {
            assignments.remove(assessment);
        }
    }

    public double getAverage() {
        double total = 0;
        double activeWeight = 0;

        if (!tests.isEmpty()) {
            double testAvg = 0;
            for (Assessment test : tests) testAvg += test.getScore();
            total += (testAvg / tests.size()) * testsWeight;
            activeWeight += testsWeight;
        }

        if (!assignments.isEmpty()) {
            double assignAvg = 0;
            for (Assessment assignment : assignments) assignAvg += assignment.getScore();
            total += (assignAvg / assignments.size()) * assignmentsWeight;
            activeWeight += assignmentsWeight;
        }

        if (exam != null) {
            total += exam.getScore() * examWeight;
            activeWeight += examWeight;
        }

        if (activeWeight == 0) return 0;
        return total / activeWeight;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Assessment> getTests() {
        return tests;
    }

    public ArrayList<Assessment> getAssignments() {
        return assignments;
    }

    public Assessment getExam() {
        return exam;
    }

    public double getTestsWeight() {
        return testsWeight;
    }

    public double getAssignmentsWeight() {
        return assignmentsWeight;
    }

    public double getExamWeight() {
        return examWeight;
    }

    @Override
    public String toString() {
        return name;
    }
}
