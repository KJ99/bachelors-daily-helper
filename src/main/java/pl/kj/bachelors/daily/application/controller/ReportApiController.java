package pl.kj.bachelors.daily.application.controller;

import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.daily.application.dto.request.PagingQuery;
import pl.kj.bachelors.daily.application.dto.response.ReportResponse;
import pl.kj.bachelors.daily.application.dto.response.TeamConfigResponse;
import pl.kj.bachelors.daily.application.dto.response.UserReportResponse;
import pl.kj.bachelors.daily.application.dto.response.page.PageResponse;
import pl.kj.bachelors.daily.application.example.DayPageExample;
import pl.kj.bachelors.daily.domain.annotation.Authentication;
import pl.kj.bachelors.daily.domain.exception.AccessDeniedException;
import pl.kj.bachelors.daily.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.daily.domain.model.create.ReportCreateModel;
import pl.kj.bachelors.daily.domain.model.embeddable.ReportIdentity;
import pl.kj.bachelors.daily.domain.model.entity.Report;
import pl.kj.bachelors.daily.domain.model.extension.action.ReportAction;
import pl.kj.bachelors.daily.domain.model.extension.action.TeamDailyAction;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.model.result.ReportWithMemberResult;
import pl.kj.bachelors.daily.domain.model.update.ReportUpdateModel;
import pl.kj.bachelors.daily.domain.service.TeamProvider;
import pl.kj.bachelors.daily.domain.service.crud.create.ReportCreateService;
import pl.kj.bachelors.daily.domain.service.crud.read.ReportReadService;
import pl.kj.bachelors.daily.domain.service.crud.update.ReportUpdateService;
import pl.kj.bachelors.daily.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.daily.infrastructure.TimeUtil;
import pl.kj.bachelors.daily.infrastructure.repository.ReportRepository;
import pl.kj.bachelors.daily.infrastructure.user.RequestHolder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/reports/{teamId}")
@Authentication
@Tag(name = "Reports")
public class ReportApiController extends BaseApiController {
    private final ReportCreateService createService;
    private final ReportUpdateService updateService;
    private final TeamProvider teamProvider;
    private final EntityAccessControlService<Team> teamAccessControl;
    private final EntityAccessControlService<Report> reportAccessControl;
    private final ReportRepository repository;
    private final ReportReadService readService;

    @Autowired
    public ReportApiController(
            ReportCreateService createService,
            ReportUpdateService updateService,
            TeamProvider teamProvider,
            EntityAccessControlService<Team> teamAccessControl,
            EntityAccessControlService<Report> reportAccessControl,
            ReportRepository repository,
            ReportReadService readService) {
        this.createService = createService;
        this.updateService = updateService;
        this.teamProvider = teamProvider;
        this.teamAccessControl = teamAccessControl;
        this.reportAccessControl = reportAccessControl;
        this.repository = repository;
        this.readService = readService;
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReportResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
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

    @PatchMapping
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> patch(@PathVariable int teamId, @RequestBody JsonPatch patch)
            throws Exception {
        String userId = RequestHolder.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        Report report = this.getReport(teamId, userId).orElseThrow(ResourceNotFoundException::new);
        this.reportAccessControl.ensureThatUserHasAccess(report, ReportAction.DELETE);

        this.updateService.processUpdate(report, patch, ReportUpdateModel.class);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReportResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ReportResponse> get(@PathVariable int teamId)
            throws AccessDeniedException, ResourceNotFoundException {
        String userId = RequestHolder.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        Report report = this.getReport(teamId, userId).orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.ok(this.map(report, ReportResponse.class));
    }

    @GetMapping("/{day}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = UserReportResponse.class))
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Collection<UserReportResponse>> getForDay(@PathVariable int teamId, @PathVariable String day)
            throws AccessDeniedException, ResourceNotFoundException {
        Team team = this.teamProvider.get(teamId).orElseThrow(ResourceNotFoundException::new);
        this.teamAccessControl.ensureThatUserHasAccess(team, TeamDailyAction.READ_REPORTS);

        List<ReportWithMemberResult> results = this.readService.getForTeamAndDay(team, day);

        return ResponseEntity.ok(this.mapCollection(results, UserReportResponse.class));
    }

    @GetMapping("/archive/days")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DayPageExample.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<PageResponse<String>> getDays(
            @PathVariable int teamId,
            @RequestParam Map<String, String> params) throws AccessDeniedException, ResourceNotFoundException {
        Team team = this.teamProvider.get(teamId).orElseThrow(ResourceNotFoundException::new);
        this.teamAccessControl.ensureThatUserHasAccess(team, TeamDailyAction.READ_REPORTS);
        PagingQuery query = this.parseQueryParams(params, PagingQuery.class);
        Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize());
        Page<String> page = this.repository.findDaysWithReports(team.getId(), pageable);

        return ResponseEntity.ok(this.createPageResponse(page, String.class));
    }

    @DeleteMapping
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> delete(@PathVariable int teamId)
            throws ResourceNotFoundException, AccessDeniedException {
        String userId = RequestHolder.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        Report report = this.getReport(teamId, userId).orElseThrow(ResourceNotFoundException::new);
        this.reportAccessControl.ensureThatUserHasAccess(report, ReportAction.DELETE);

        this.repository.delete(report);

        return ResponseEntity.noContent().build();
    }

    private Optional<Report> getReport(int teamId, String uid) {
        ReportIdentity identity = new ReportIdentity();
        identity.setTeamId(teamId);
        identity.setUserId(uid);
        identity.setDay(TimeUtil.getToday());
        return this.repository.findById(identity);
    }
}
