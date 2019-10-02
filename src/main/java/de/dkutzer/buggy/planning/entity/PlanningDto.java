package de.dkutzer.buggy.planning.entity;


import java.util.List;
import java.util.StringJoiner;

public class PlanningDto {

    private Summary summary;

    private List<Week> weeks;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<Week> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Week> weeks) {
        this.weeks = weeks;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PlanningDto.class.getSimpleName() + "[", "]")
            .add("summary=" + summary)
            .add("weeks=" + weeks)
            .toString();
    }
}
