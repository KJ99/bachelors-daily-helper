package pl.kj.bachelors.daily.integration.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pl.kj.bachelors.daily.domain.model.create.ReportCreateModel;
import pl.kj.bachelors.daily.domain.model.entity.TeamConfig;
import pl.kj.bachelors.daily.domain.model.extension.Role;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.model.remote.TeamMember;
import pl.kj.bachelors.daily.domain.service.TeamProvider;
import pl.kj.bachelors.daily.domain.service.user.MemberProvider;
import pl.kj.bachelors.daily.infrastructure.repository.TeamConfigRepository;
import pl.kj.bachelors.daily.integration.BaseIntegrationTest;
import pl.kj.bachelors.daily.model.PatchOperation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReportApiControllerTests extends BaseIntegrationTest {
    @Autowired
    private TeamConfigRepository configRepository;
    @MockBean
    private TeamProvider teamProvider;
    @MockBean
    private MemberProvider memberProvider;

    @BeforeEach
    private void setUp() {
        given(this.teamProvider.get(3)).willReturn(Optional.of(this.getTeam(3)));
        given(this.teamProvider.get(4)).willReturn(Optional.of(this.getTeam(4)));
        given(this.memberProvider.get(3, "uid-100"))
                .willReturn(Optional.of(this.getMember("uid-100", List.of(Role.TEAM_MEMBER))));
        given(this.memberProvider.get(3, "uid-1"))
                .willReturn(Optional.of(this.getMember("uid-1", List.of(Role.SCRUM_MASTER))));

        given(this.teamProvider.get(4)).willReturn(Optional.of(this.getTeam(4)));
        given(this.memberProvider.get(4, "uid-100"))
                .willReturn(Optional.of(this.getMember("uid-100", List.of(Role.TEAM_MEMBER))));
        given(this.memberProvider.get(4, "uid-1"))
                .willReturn(Optional.of(this.getMember("uid-1", List.of(Role.SCRUM_MASTER))));

        this.createConfig(3, this.getHourInFuture());
        this.createConfig(4, this.getHourInPast());
    }

    @Test
    public void testPost_Created() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        ReportCreateModel model = new ReportCreateModel();
        model.setToday("Hello");
        model.setLastTime("World");

        this.mockMvc.perform(
                post("/v1/reports/3")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.serialize(model))
        ).andExpect(status().isCreated());
    }

    @Test
    public void testPost_Unauthorized() throws Exception {
        ReportCreateModel model = new ReportCreateModel();
        model.setToday("Hello");
        model.setLastTime("World");

        this.mockMvc.perform(
                post("/v1/reports/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.serialize(model))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPost_Forbidden_DeadlinePast() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        ReportCreateModel model = new ReportCreateModel();
        model.setToday("Hello");
        model.setLastTime("World");

        this.mockMvc.perform(
                post("/v1/reports/4")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.serialize(model))
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testPost_Forbidden_WrongRole() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        ReportCreateModel model = new ReportCreateModel();
        model.setToday("Hello");
        model.setLastTime("World");

        this.mockMvc.perform(
                post("/v1/reports/3")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.serialize(model))
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testPatch_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/today", "Edited")
        });

        this.mockMvc.perform(
                patch("/v1/reports/3")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testPatch_Unauthorized() throws Exception {
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/today", "Edited")
        });

        this.mockMvc.perform(
                patch("/v1/reports/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPatch_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/today", "Edited")
        });

        this.mockMvc.perform(
                patch("/v1/reports/4")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetForMember_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                get("/v1/reports/3")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testDelete_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                delete("/v1/reports/3")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    private TeamMember getMember(String uid, List<Role> roles) {
        TeamMember member = new TeamMember();
        member.setUserId(uid);
        member.setRoles(roles);

        return member;
    }

    private Team getTeam(int id) {
        Team team = new Team();
        team.setName("Team");
        team.setId(id);

        return team;
    }

    private void createConfig(int id, String deadlineHour) {
        TeamConfig config = new TeamConfig();
        config.setTeamId(id);
        config.setHourlyDeadline(deadlineHour);
        config.setTimeZone(TimeZone.getDefault().getID());

        this.configRepository.saveAndFlush(config);
    }

    private String getHourInFuture() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);

        return dateFormat.format(calendar.getTime());
    }

    private String getHourInPast() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);

        return dateFormat.format(calendar.getTime());
    }
}
