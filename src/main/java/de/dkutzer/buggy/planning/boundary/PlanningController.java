package de.dkutzer.buggy.planning.boundary;

import de.dkutzer.buggy.planning.control.PlanningService;
import de.dkutzer.buggy.planning.entity.PlanningDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PlanningController {

    private PlanningService planningService;

    public PlanningController(PlanningService planningService) {
        this.planningService = planningService;
    }

    @GetMapping(path = "/planning")
    public PlanningDto planning(){

        return planningService.doPlanning();

    }

}
