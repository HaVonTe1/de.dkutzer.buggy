package de.dkutzer.buggy.issue.control;

import de.dkutzer.buggy.developer.control.DeveloperRepository;
import de.dkutzer.buggy.developer.entity.DeveloperEntity;
import de.dkutzer.buggy.errorhandling.NotAllowedException;
import de.dkutzer.buggy.errorhandling.NotFoundException;
import de.dkutzer.buggy.issue.entity.IssueDto;
import de.dkutzer.buggy.issue.entity.IssueEntitiy;
import de.dkutzer.buggy.issue.entity.Status;
import de.dkutzer.buggy.issue.entity.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class IssueService {

    private IssueRepository issueRepository;
    private DeveloperRepository developerRepository;

    public IssueService(IssueRepository issueRepository,
        DeveloperRepository developerRepository) {
        this.issueRepository = issueRepository;
        this.developerRepository = developerRepository;
    }

    public Optional<IssueDto> findById(String id) {
        final Optional<IssueEntitiy> optionalIssueEntitiy = issueRepository.findById(id);
        return optionalIssueEntitiy.map(IssueEntitiy::toDto);
    }

    public List<IssueDto> findAll() {
        return issueRepository.findAll().stream().map(IssueEntitiy::toDto).collect(Collectors.toList());
    }

    public  IssueDto upsert(IssueDto dto) {
        IssueEntitiy entity = dto.getId()!=null ? issueRepository.findById(dto.getId()).orElse(new IssueEntitiy()) : new IssueEntitiy();
        if(entity.getId()==null){
            entity.setId(UUID.randomUUID().toString());
        }
        checkCreatedAt(dto, entity);
        checkAssignee(dto, entity);
        entity.setAssignee(dto.getAssignee());
        entity.setDescription(dto.getDescription());
        checkId(dto, entity);
        checkBugProperties(dto, entity);
        checkStoryProperties(dto, entity);
        entity.setTitle(dto.getTitle());
        entity.setType(dto.getType());

        final IssueEntitiy savedEntity = issueRepository.save(entity);
        return savedEntity.toDto();
    }

    private void checkStoryProperties(IssueDto dto, IssueEntitiy entity) {
        if (dto.getType() == Type.STORY) {
            entity.setPoints(dto.getPoints());
            if (dto.getStatus() == Status.Estimated) {
                if (dto.getPoints() == null || dto.getPoints() == 0) {
                    throw new NotAllowedException(
                        "You can not set the Status of a Story to 'Estimated' without giving an estimation.");
                }
            }
            if (dto.getStatus() == Status.New || dto.getStatus() == Status.Estimated
                || dto.getStatus() == Status.Completed) {

                entity.setStatus(dto.getStatus());
            } else {
                throw new NotAllowedException(
                    String.format("Invalid Status for an issue of type Story: %s", dto.getStatus()));
            }
        }
    }

    private  void checkBugProperties(IssueDto dto, IssueEntitiy entity) {
        if(dto.getType() == Type.BUG){
            entity.setPriority(dto.getPriority());
            if (dto.getStatus() == Status.New || dto.getStatus() == Status.Verified
                || dto.getStatus() == Status.Completed) {

                entity.setStatus(dto.getStatus());
            } else {
                throw new NotAllowedException(
                    String.format("Invalid Status for an issue of type Bug: %s", dto.getStatus()));
            }

        }
    }

    private  void checkId(IssueDto dto, IssueEntitiy entity) {
        if(dto.getId()!=null) {
            entity.setId(dto.getId());
        }
    }

    private  void checkCreatedAt(IssueDto dto, IssueEntitiy entity) {
        if(entity.getCreatedAt()==null) {
            entity.setCreatedAt(LocalDate.now());
        } else {
            entity.setCreatedAt(dto.getCreatedAt());
        }
    }

    private  void checkAssignee(IssueDto dto, IssueEntitiy entity) {
        if(dto.getAssignee()!=null && !dto.getAssignee().isEmpty()){
            final Optional<DeveloperEntity> developerEntityOptional = developerRepository.findByName(dto.getAssignee());
            if(developerEntityOptional.isPresent()){
                entity.setAssignee(dto.getAssignee());
            }
            else{
                throw new NotFoundException(String.format("The requested Assignee does not exists: %s",dto.getAssignee()));
            }
        }
    }


}
