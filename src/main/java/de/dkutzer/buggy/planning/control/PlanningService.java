package de.dkutzer.buggy.planning.control;

import de.dkutzer.buggy.developer.control.DeveloperRepository;
import de.dkutzer.buggy.issue.control.IssueRepository;
import de.dkutzer.buggy.issue.entity.IssueDto;
import de.dkutzer.buggy.issue.entity.IssueEntitiy;
import de.dkutzer.buggy.issue.entity.Status;
import de.dkutzer.buggy.issue.entity.Type;
import de.dkutzer.buggy.planning.entity.PlanningDto;
import de.dkutzer.buggy.planning.entity.Summary;
import de.dkutzer.buggy.planning.entity.Week;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class PlanningService {

    private IssueRepository issueRepository;
    private DeveloperRepository developerRepository;

    @Value("${buggy.developers.defaultcap:10}")
    private Integer capacityPerDev;

    public PlanningService(IssueRepository issueRepository,
        DeveloperRepository developerRepository) {
        this.issueRepository = issueRepository;
        this.developerRepository = developerRepository;
    }

    public PlanningDto doPlanning() {

        PlanningDto planningDto = new PlanningDto();

        final List<IssueEntitiy> allEstimatedStories = issueRepository
            .findAllByTypeAndStatusOrderByPriorityCreatedAt(Type.STORY.name(), Status.Estimated.name());
        Summary summary = calcSummary(allEstimatedStories);
        planningDto.setSummary(summary);

        final long developers = summary.getDevelopers();
        long capacity = developers * capacityPerDev;

        MultiValueMap<Integer, IssueDto> issuesPerWeek = new LinkedMultiValueMap<>();
        List<IssueEntitiy> remaningIssues = new ArrayList<>(allEstimatedStories);
        List<IssueEntitiy> issues = new ArrayList<>(remaningIssues);
        while (!issues.isEmpty()){
            int currentWeek = 1;
            for (IssueEntitiy  issueEntitiy : issues){
                final int points = issueEntitiy.getPoints();
                if(points <= capacity){
                    issuesPerWeek.add(currentWeek, issueEntitiy.toDto());
                    capacity-=points;
                    remaningIssues.remove(issueEntitiy);
                }
                else if (capacity > 0) {
                    long remainingPoints = points - capacity;
                    issueEntitiy.setPoints((int)remainingPoints);
                    issuesPerWeek.add(currentWeek,issueEntitiy.toDto());
                    break;
                }
            }
            issues.clear();
            issues.addAll(remaningIssues);
            ++currentWeek;
            capacity = developers * capacityPerDev;
        }

        List<Week> weeks = new ArrayList<>();
        issuesPerWeek.forEach((weekNr, issueDtos) -> {
            Week week = new Week();
            week.setIssueDtos(issueDtos);
            week.setNumber(weekNr);
            weeks.add(week);
        });
        planningDto.setWeeks(weeks);

        planningDto.getSummary().calcIssuesPerWeek();

        return planningDto;
    }

    private Summary calcSummary(List<IssueEntitiy> allEstimatedStories) {

        Summary summary  = new Summary();
        final long count = developerRepository.count();
        summary.setDevelopers(count);
        summary.setIssues(allEstimatedStories.size());
        return summary;
    }
}
