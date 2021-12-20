package pl.kj.bachelors.daily.unit.infrastructure.service.crud.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.model.remote.UserProfile;
import pl.kj.bachelors.daily.domain.model.result.ReportWithMemberResult;
import pl.kj.bachelors.daily.domain.service.user.ProfileProvider;
import pl.kj.bachelors.daily.infrastructure.service.crud.read.ReportReadServiceImpl;
import pl.kj.bachelors.daily.unit.BaseUnitTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class ReportReadServiceTests extends BaseUnitTest {
    @Autowired
    private ReportReadServiceImpl service;
    @MockBean
    private ProfileProvider profileProvider;

    @BeforeEach
    public void setUp() {
        UserProfile profile = new UserProfile();
        profile.setId("uid-101");
        profile.setFirstName("John");
        profile.setLastName("Doe");

        given(this.profileProvider.get(anyString())).willReturn(Optional.of(profile));
    }

    @Test
    public void testGetForTeamAndDay() {
        Team team = new Team();
        team.setId(3);
        List<ReportWithMemberResult> results = this.service.getForTeamAndDay(team, "2020-01-01");

        assertThat(results).isNotEmpty();
        assertThat(results.stream().anyMatch(res -> res.getUser().getFirstName().equals("John"))).isTrue();
    }
}
