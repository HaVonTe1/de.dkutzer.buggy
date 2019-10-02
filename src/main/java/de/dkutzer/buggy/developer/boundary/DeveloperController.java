package de.dkutzer.buggy.developer.boundary;

import de.dkutzer.buggy.developer.control.DeveloperService;
import de.dkutzer.buggy.developer.entity.DeveloperDto;
import de.dkutzer.buggy.errorhandling.NotFoundException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DeveloperController {

    private DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @GetMapping(path = "/developers/{name}")
    public DeveloperDto getDevByName(@PathVariable @NonNull String name){
        final Optional<DeveloperDto> developerDtoOptional = developerService.findByName(name);
        return developerDtoOptional.orElseThrow(NotFoundException::new);
    }
    @GetMapping(path = "/developers/")
    public List<DeveloperDto> getAll(){
        List<DeveloperDto> developerDtoList =  developerService.findAll();
        return developerDtoList;
    }

    @PostMapping(path = "/developers/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeveloperDto> create(@Validated @RequestBody final DeveloperDto dto){
        developerService.upsert(dto);
        return ResponseEntity.created(
            new DefaultUriBuilderFactory().builder().path(String.format("/developers/%s",dto.getName())).build()).body(dto);
    }
    @DeleteMapping(path = "/developers/{name}")
    public ResponseEntity<Void> update(@PathVariable @NonNull String name){
        developerService.delete(name);
        return ResponseEntity.noContent().build();
    }
}
