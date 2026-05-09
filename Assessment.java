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
}