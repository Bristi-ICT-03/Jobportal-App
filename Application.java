public class Application {
    private String userName;
    private String jobTitle;
    private int score;
    private String status;

    public Application(String userName, String jobTitle, int score) {
        this.userName = userName;
        this.jobTitle = jobTitle;
        this.score = score;
        this.status = "Applied";
    }

    public String getUserName() {
        return userName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public int getScore() {
        return score;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return userName + " applied to " + jobTitle + " | Match: " + score + "% | Status: " + status;
    }
}