package de.dkutzer.buggy.issue.entity;

import java.time.LocalDate;
import java.util.StringJoiner;

public class IssueDto  {
    protected String id;
    protected Type type;
    protected String title;
    protected String description;
    protected LocalDate createdAt;
    protected String assignee;
    private Integer points;

    private Status status;

    private Priority priority;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }



    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", IssueDto.class.getSimpleName() + "[", "]")
            .add("id='" + id + "'")
            .add("type=" + type)
            .add("title='" + title + "'")
            .add("description='" + description + "'")
            .add("createdAt=" + createdAt)
            .add("assignee='" + assignee + "'")
            .add("points=" + points)
            .add("status=" + status)
            .add("priority=" + priority)
            .toString();
    }
}
