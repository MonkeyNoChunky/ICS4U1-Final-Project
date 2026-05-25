public class Assessment {
    private String name;
    private double score;
    private double maxScore;
    private String type; // either "exam", "test", or "assignment"

    /**
    * Constructor for the Assessment object 
    *
    * @param name the name of the assessment
    * @param score the amount of marks acheived
    * @param maxScore the total amount of marks possible
    * @param type the String defining the type of assessment. Can be "exam", "test", or "assignment"
    */
    public Assessment(String name, double score, double maxScore, String type) {
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
        this.type = type;
    }

    /**
    * @return returns the String type. Either "test", "assignment", or "exam"
    */
    public String getType() {
        return type;
    }

    /**
    * @return returns the String assessment name
    */
    public String getName() {
        return name;
    }

    /**
    * @return returns the percentage amount acheived on the assessment. score / maxScore
    */
    public double getScore() {
        return score/maxScore * 100;
    }

    /**
    * @return returns the raw mark amount acheived on the assessment
    */
    public double getRawScore() {
        return score;
    }

    /**
    * @return returns the maximum amount of marks on the assessment
    */
    public double getMaxScore() {
        return maxScore;
    }

    @Override
    public String toString() {
        return name;
    }
}