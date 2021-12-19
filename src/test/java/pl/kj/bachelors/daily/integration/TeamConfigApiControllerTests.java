package pl.kj.bachelors.daily.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import pl.kj.bachelors.daily.application.dto.response.error.ValidationErrorResponse;
import pl.kj.bachelors.daily.domain.exception.ApiError;
import pl.kj.bachelors.daily.domain.model.create.TeamConfigCreateModel;
import pl.kj.bachelors.daily.domain.model.extension.Role;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.model.remote.TeamMember;
import pl.kj.bachelors.daily.domain.service.TeamProvider;
import pl.kj.bachelors.daily.domain.service.user.MemberProvider;
import pl.kj.bachelors.daily.model.PatchOperation;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamConfigApiControllerTests extends BaseIntegrationTest {
    @MockBean
    private MemberProvider memberProvider;
    @MockBean
    private TeamProvider teamProvider;

    @BeforeEach
    public void setUp() {
        Team team1 = new Team();
        team1.setId(1);
        team1.setName("Team 1");
        Team team2 = new Team();
        team2.setId(2);
        team2.setName("Team 2");
        
        TeamMember scrumMaster = new TeamMember();
        scrumMaster.setUserId("uid-1");
        scrumMaster.setRoles(List.of(Role.SCRUM_MASTER));

        TeamMember member = new TeamMember();
        member.setUserId("uid-100");
        member.setRoles(List.of(Role.TEAM_MEMBER));

        given(teamProvider.get(1)).willReturn(Optional.of(team1));
        given(teamProvider.get(2)).willReturn(Optional.of(team2));
        given(memberProvider.get(1, "uid-1")).willReturn(Optional.of(scrumMaster));
        given(memberProvider.get(1, "uid-100")).willReturn(Optional.of(member));
        given(memberProvider.get(2, "uid-1")).willReturn(Optional.of(scrumMaster));
        given(memberProvider.get(2, "uid-100")).willReturn(Optional.of(member));
    }

    @Test
    public void testPost_Created() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        TeamConfigCreateModel model = new TeamConfigCreateModel();
        model.setTeamId(1);
        model.setHourlyDeadline("22:15");

        this.mockMvc.perform(
                post("/v1/configurations")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.serialize(model))
        ).andExpect(status().isCreated());
    }

    @Test
    public void testPost_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        TeamConfigCreateModel model = new TeamConfigCreateModel();
        model.setTeamId(1);
        model.setHourlyDeadline("22:sq");

        this.mockMvc.perform(
                post("/v1/configurations")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.serialize(model))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testPost_Unauthorized() throws Exception {
        TeamConfigCreateModel model = new TeamConfigCreateModel();
        model.setTeamId(1);
        model.setHourlyDeadline("22:15");

        this.mockMvc.perform(
                post("/v1/configurations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.serialize(model))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPost_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        TeamConfigCreateModel model = new TeamConfigCreateModel();
        model.setTeamId(1);
        model.setHourlyDeadline("22:15");

        this.mockMvc.perform(
                post("/v1/configurations")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.serialize(model))
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testPatch_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/hourly_deadline", "20:00")
        });

        this.mockMvc.perform(
                patch("/v1/configurations/2")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testPatch_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/hourly_deadline", "fake")
        });

        this.mockMvc.perform(
                patch("/v1/configurations/2")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testPatch_Unauthorized() throws Exception {
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/hourly_deadline", "20:00")
        });

        this.mockMvc.perform(
                patch("/v1/configurations/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPatch_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/hourly_deadline", "20:00")
        });

        this.mockMvc.perform(
                patch("/v1/configurations/2")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGet_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                get("/v1/configurations/2")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGet_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/configurations/2")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGet_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-300"));
        this.mockMvc.perform(
                get("/v1/configurations/2")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testDelete_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                delete("/v1/configurations/2")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testDelete_isUnauthorized() throws Exception {
        this.mockMvc.perform(
                delete("/v1/configurations/2")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testDelete_isForbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                delete("/v1/configurations/2")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }
}
