package pl.kj.bachelors.daily.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.daily.application.dto.response.ReportResponse;
import pl.kj.bachelors.daily.domain.annotation.Authentication;
import pl.kj.bachelors.daily.domain.exception.AccessDeniedException;
import pl.kj.bachelors.daily.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.daily.domain.model.create.ReportCreateModel;
import pl.kj.bachelors.daily.domain.model.embeddable.ReportIdentity;
import pl.kj.bachelors.daily.domain.model.entity.Report;
import pl.kj.bachelors.daily.domain.model.extension.action.TeamDailyAction;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.service.TeamProvider;
import pl.kj.bachelors.daily.domain.service.crud.create.ReportCreateService;
import pl.kj.bachelors.daily.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.daily.infrastructure.TimeUtil;
import pl.kj.bachelors.daily.infrastructure.user.RequestHolder;

@RestController
@RequestMapping("/v1/reports/{teamId}")
@Authentication
public class ReportApiController extends BaseApiController {
    private final ReportCreateService createService;
    private final TeamProvider teamProvider;
    private final EntityAccessControlService<Team> teamAccessControl;

    @Autowired
    public ReportApiController(
            ReportCreateService createService,
            TeamProvider teamProvider,
            EntityAccessControlService<Team> teamAccessControl) {
        this.createService = createService;
        this.teamProvider = teamProvider;
        this.teamAccessControl = teamAccessControl;
    }

    @PostMapping
    public ResponseEntity<ReportResponse> post(@PathVariable int teamId, @RequestBody ReportCreateModel model)
            throws Exception {
        Team team = this.teamProvider.get(teamId).orElseThrow(ResourceNotFoundException::new);
        this.teamAccessControl.ensureThatUserHasAccess(team, TeamDailyAction.SEND_REPORT);
        String uid = RequestHolder.getCurrentUserId().orElseThrow(AccessDeniedException::new);

        ReportIdentity identity = new ReportIdentity();
        identity.setTeamId(teamId);
        identity.setUserId(uid);
        identity.setDay(TimeUtil.getToday());

        model.setId(identity);
        Report report = this.createService.create(model, Report.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.map(report, ReportResponse.class));
    }
}
