package de.dkutzer.buggy.planning.entity;

import de.dkutzer.buggy.issue.entity.IssueDto;
import java.util.List;
import java.util.StringJoiner;

public class Week {

    private Integer number;
    private List<IssueDto> issueDtos;


    public List<IssueDto> getIssueDtos() {
        return issueDtos;
    }

    public void setIssueDtos(List<IssueDto> issueDtos) {
        this.issueDtos = issueDtos;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Week.class.getSimpleName() + "[", "]")
            .add("number=" + number)
            .add("issueDtos=" + issueDtos)
            .toString();
    }
}
