package de.dkutzer.buggy.developer.control;

import de.dkutzer.buggy.developer.entity.DeveloperEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends MongoRepository<DeveloperEntity, String> {

    Optional<DeveloperEntity> findByName(String name);

    void deleteByName(String name);

    List<DeveloperEntity> findAllByOrderByNameAsc();
}
