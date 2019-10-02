package de.dkutzer.buggy.issue.entity;

import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@TypeAlias("Developer")
@Document(collection = "developer")
@CompoundIndexes({
    @CompoundIndex(name = "status_type", def = "{'status' : 1, 'type': 1}"),
    @CompoundIndex(name = "priority_createdAt", def = "{'priority' : 1, 'createdAt': 1}")
})
public class IssueEntitiy {

    @Id
    private String id;

    @NotNull
    private Type type;

    @NotNull
    private String title;

    private String description;

    @NotNull
    private LocalDate createdAt;

    @Indexed
    private String assignee;

    @Min(0)
    private int points;

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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
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
        final StringBuilder sb = new StringBuilder("Issue{");
        sb.append("id='").append(id).append('\'');
        sb.append(", type=").append(type);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", assignee='").append(assignee).append('\'');
        sb.append(", points=").append(points);
        sb.append(", status=").append(status);
        sb.append(", priority=").append(priority);
        sb.append('}');
        return sb.toString();
    }

    public IssueDto toDto() {

        IssueDto issueDto = new IssueDto();
        issueDto.setPoints(this.points);
        issueDto.setPriority(this.priority);
        issueDto.setStatus(this.status);
        issueDto.setAssignee(this.assignee);
        issueDto.setCreatedAt(this.createdAt);
        issueDto.setDescription(this.description);
        issueDto.setId(this.id);
        issueDto.setTitle(this.title);
        issueDto.setType(this.type);
        return issueDto;
    }
}
