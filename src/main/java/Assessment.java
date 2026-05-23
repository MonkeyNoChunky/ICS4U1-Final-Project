public class Assessment {
    private String name;
    private double score;
    private double maxScore;
    private String type; // either "exam", "test", or "assignment"

    public Assessment(String name, double score, double maxScore, String type) {
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score/maxScore;
    }

    public double getRawScore() {
        return score;
    }

    public double getMaxScore() {
        return maxScore;
    }

    @Override
    public String toString() {
        return name;
    }
}