package de.dkutzer.buggy.issue.boundary;

import de.dkutzer.buggy.errorhandling.NotAllowedException;
import de.dkutzer.buggy.errorhandling.NotFoundException;
import de.dkutzer.buggy.issue.control.IssueService;
import de.dkutzer.buggy.issue.entity.IssueDto;
import java.util.List;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class IssueController {

    private IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping(path = "/issues/{id}")
    public IssueDto getIssueById(@PathVariable @NonNull String id){
        final Optional<IssueDto> developerDtoOptional = issueService.findById(id);
        return developerDtoOptional.orElseThrow(NotFoundException::new);
    }
    @GetMapping(path = "/issues/")
    public List<IssueDto> getAll(){
        return issueService.findAll();
    }

    @PostMapping(path = "/issues/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<IssueDto> create(@Validated @RequestBody  IssueDto dto){
        IssueDto result = issueService.upsert(dto);
        return ResponseEntity.created(
            new DefaultUriBuilderFactory().builder().path(String.format("/issues/%s",dto.getId())).build()).body(result);
    }

    @PutMapping(path = "/issues/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<IssueDto> create(@PathVariable @NonNull String id, @Validated @RequestBody  IssueDto dto){
        IssueDto result = issueService.upsert(dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "/issues/")
    public ResponseEntity<Void> update(){
        throw new NotAllowedException();
    }
}
