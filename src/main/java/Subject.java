import java.util.ArrayList;

public class Subject {
    private String name;
    private double testsWeight;
    private double assignmentsWeight;
    private double examWeight;
    private ArrayList<Assessment> tests = new ArrayList<>();
    private ArrayList<Assessment> assignments = new ArrayList<>();
    private Assessment exam; 

    /**
    * Constructor for the Subject object 
    *
    * @param name the String name
    * @param testsWeight the decimal amount that the tests weigh in a subject. Ex. 30% = 0.3
    * @param assignmentsWeight the decimal amount that the assignments weigh in a subject
    * @param examWeight the decimal amount that the exam weighs in a subject
    */
    public Subject(String name, double testsWeight, double assignmentsWeight, double examWeight) {
        this.name = name;
        this.testsWeight = testsWeight;
        this.assignmentsWeight = assignmentsWeight;
        this.examWeight = examWeight;
    }

    /**
    * Adds an assessment object to the Subject 
    *
    * @param assessment The assessment object, carrying its score and type of assessment
    * @return returns if adding the assessment was successful
    */
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

    /**
    * Removes an assessment currently loaded in the subject
    *
    * @param assessment Removed assessment
    */
    public void removeAssessment(Assessment assessment) {
        if (assessment.getType().equals("exam") && exam == assessment) {
            exam = null;
        } else if (assessment.getType().equals("test")) {
            tests.remove(assessment);
        } else if (assessment.getType().equals("assignment")) {
            assignments.remove(assessment);
        }
    }

    /**
    * Returns the current average in the subject. 
    * Calculates using the tests, assignments, and exams loaded
    *
    * @return the current grade in percentage of the subject
    */
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

    /**
    * @return returns the name of the subject
    */
    public String getName() {
        return name;
    }

    /**
    * @return returns the ArrayList of the tests
    */
    public ArrayList<Assessment> getTests() {
        return tests;
    }

    /**
    * @return returns the ArrayList of assignments
    */
    public ArrayList<Assessment> getAssignments() {
        return assignments;
    }

    /**
    * @return returns the exam object
    */
    public Assessment getExam() {
        return exam;
    }

    /**
    * @return returns the weight of the tests in decimal format (Ex. 30% = 0.3)
    */
    public double getTestsWeight() {
        return testsWeight;
    }

    /**
    * @return returns the weight of the assignments in decimal format (Ex. 30% = 0.3)
    */
    public double getAssignmentsWeight() {
        return assignmentsWeight;
    }

    /**
    * @return returns the weight of the exam in decimal format (Ex. 30% = 0.3)
    */
    public double getExamWeight() {
        return examWeight;
    }

    @Override
    public String toString() {
        return name;
    }
}
