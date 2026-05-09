public class Assessment {
    private String name;
    private double score;
    private double maxScore;
    private double weight;
    private String type; // either "exam", "test", or "assignment"

    public Assessment(String name, double score, double maxScore, double weight, String type) {
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
        this.weight = weight;
        this.type = type;
    }

    public String getType() {
        return type;
    }
}