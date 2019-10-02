package de.dkutzer.buggy.developer.control;

import de.dkutzer.buggy.developer.entity.DeveloperDto;
import de.dkutzer.buggy.developer.entity.DeveloperEntity;
import de.dkutzer.buggy.errorhandling.NotAllowedException;
import de.dkutzer.buggy.issue.control.IssueRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DeveloperService {

    private DeveloperRepository developerRepository;
    private IssueRepository issueRepository;

    public DeveloperService(DeveloperRepository developerRepository,
        IssueRepository issueRepository) {
        this.developerRepository = developerRepository;
        this.issueRepository = issueRepository;
    }

    public Optional<DeveloperDto> findByName(final String name){
        final Optional<DeveloperEntity> developerEntityOptional = developerRepository.findByName(name);
        return developerEntityOptional.map(DeveloperEntity::toDto);
    }


    public void upsert(final DeveloperDto dev){
        DeveloperEntity developerEntity = developerRepository.findByName(dev.getName()).orElse(new DeveloperEntity());
        developerEntity.setName(dev.getName());
        developerRepository.save(developerEntity);
    }


    public void delete(String name) {
        if(issueRepository.existsByAssignee(name)){
            throw new NotAllowedException("You can not delete a developer which has assignments to issues.");
        }
        developerRepository.deleteByName(name);
    }

    public List<DeveloperDto> findAll() {
        return developerRepository.findAll().stream().map(DeveloperEntity::toDto).collect(Collectors.toList());
    }
}
