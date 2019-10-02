package de.dkutzer.buggy.issue.control;

import de.dkutzer.buggy.issue.entity.IssueEntitiy;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends MongoRepository<IssueEntitiy, String> {

    boolean existsByAssignee(String name);

    List<IssueEntitiy> findAllByTypeAndStatusOrderByPriorityCreatedAt(String type, String status);
}
