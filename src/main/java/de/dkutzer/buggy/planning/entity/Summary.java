package de.dkutzer.buggy.planning.entity;

public class Summary {

    private int weeks;
    private long issues;
    private long developers;
    private double issuesPerWeek;


    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public long getIssues() {
        return issues;
    }

    public void setIssues(long issues) {
        this.issues = issues;
    }

    public double getIssuesPerWeek() {
        return issuesPerWeek;
    }

    public void setIssuesPerWeek(double issuesPerWeek) {
        this.issuesPerWeek = issuesPerWeek;
    }

    public long getDevelopers() {
        return developers;
    }

    public void setDevelopers(long developers) {
        this.developers = developers;
    }

    public void calcIssuesPerWeek() {
        this.issuesPerWeek = (double)(issues / getWeeks());
    }
}
