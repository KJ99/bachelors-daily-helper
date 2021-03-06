package pl.kj.bachelors.daily.application.controller;

import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.daily.application.dto.response.TeamConfigResponse;
import pl.kj.bachelors.daily.domain.annotation.Authentication;
import pl.kj.bachelors.daily.domain.exception.AccessDeniedException;
import pl.kj.bachelors.daily.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.daily.domain.model.create.TeamConfigCreateModel;
import pl.kj.bachelors.daily.domain.model.entity.TeamConfig;
import pl.kj.bachelors.daily.domain.model.extension.action.TeamDailyAction;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.model.update.TeamConfigUpdateModel;
import pl.kj.bachelors.daily.domain.service.TeamProvider;
import pl.kj.bachelors.daily.domain.service.crud.create.TeamConfigCreateService;
import pl.kj.bachelors.daily.domain.service.crud.update.TeamConfigUpdateService;
import pl.kj.bachelors.daily.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.daily.infrastructure.repository.TeamConfigRepository;

@RestController
@RequestMapping("/v1/configurations")
@Authentication
@Tag(name = "Configurations")
public class TeamConfigApiController extends BaseApiController {
    private final TeamConfigCreateService createService;
    private final EntityAccessControlService<Team> teamAccessControl;
    private final TeamProvider teamProvider;
    private final TeamConfigRepository repository;
    private final TeamConfigUpdateService updateService;

    @Autowired
    public TeamConfigApiController(
            TeamConfigCreateService createService,
            EntityAccessControlService<Team> teamAccessControl,
            TeamProvider teamProvider,
            TeamConfigRepository repository,
            TeamConfigUpdateService updateService) {
        this.createService = createService;
        this.teamAccessControl = teamAccessControl;
        this.teamProvider = teamProvider;
        this.repository = repository;
        this.updateService = updateService;
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeamConfigResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<TeamConfigResponse> post(@RequestBody TeamConfigCreateModel model) throws Exception {
        Team team = this.teamProvider.get(model.getTeamId()).orElseThrow(ResourceNotFoundException::new);
        this.teamAccessControl.ensureThatUserHasAccess(team, TeamDailyAction.CONFIGURE);
        TeamConfig config = this.createService.create(model, TeamConfig.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.map(config, TeamConfigResponse.class));
    }

    @PatchMapping("/{teamId}")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> patch(@PathVariable int teamId, @RequestBody JsonPatch patch) throws Exception {
        Team team = this.teamProvider.get(teamId).orElseThrow(ResourceNotFoundException::new);
        this.teamAccessControl.ensureThatUserHasAccess(team, TeamDailyAction.CONFIGURE);
        TeamConfig config = this.repository.findById(team.getId()).orElseThrow(ResourceNotFoundException::new);

        this.updateService.processUpdate(config, patch, TeamConfigUpdateModel.class);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{teamId}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeamConfigResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<TeamConfigResponse> get(@PathVariable int teamId)
            throws ResourceNotFoundException, AccessDeniedException {
        Team team = this.teamProvider.get(teamId).orElseThrow(ResourceNotFoundException::new);
        this.teamAccessControl.ensureThatUserHasAccess(team, TeamDailyAction.READ_CONFIGURATION);
        TeamConfig config = this.repository.findById(team.getId()).orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.ok(this.map(config, TeamConfigResponse.class));
    }

    @DeleteMapping("/{teamId}")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> delete(@PathVariable int teamId)
            throws ResourceNotFoundException, AccessDeniedException {
        Team team = this.teamProvider.get(teamId).orElseThrow(ResourceNotFoundException::new);
        this.teamAccessControl.ensureThatUserHasAccess(team, TeamDailyAction.CONFIGURE);
        TeamConfig config = this.repository.findById(team.getId()).orElseThrow(ResourceNotFoundException::new);

        this.repository.delete(config);

        return ResponseEntity.noContent().build();
    }
}
