package pl.kj.bachelors.daily.unit.infrastructure.service.crud.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.daily.domain.exception.AggregatedApiError;
import pl.kj.bachelors.daily.domain.model.create.TeamConfigCreateModel;
import pl.kj.bachelors.daily.domain.model.entity.TeamConfig;
import pl.kj.bachelors.daily.infrastructure.service.crud.create.TeamConfigCreateServiceImpl;
import pl.kj.bachelors.daily.infrastructure.user.RequestHolder;
import pl.kj.bachelors.daily.unit.BaseUnitTest;

import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class TeamConfigCreateServiceTests extends BaseUnitTest {
    @Autowired
    private TeamConfigCreateServiceImpl service;

    @BeforeEach
    public void setUp() {
        when(RequestHolder.getRequestTimeZone()).thenReturn(Optional.of(TimeZone.getTimeZone("Europe/Amsterdam")));
    }

    @Test
    public void testCreate() throws Exception {
        TeamConfigCreateModel model = new TeamConfigCreateModel();
        model.setTeamId(1);
        model.setHourlyDeadline("22:15");

        TeamConfig config = this.service.create(model, TeamConfig.class);

        assertThat(config).isNotNull();
        assertThat(config.getTeamId()).isEqualTo(1);
        assertThat(config.getHourlyDeadline()).isEqualTo("22:15");
        assertThat(config.getTimeZone()).isEqualTo("Europe/Amsterdam");
    }

    @Test
    public void testCreate_ValidationFails() {
        TeamConfigCreateModel model = new TeamConfigCreateModel();
        model.setTeamId(1);
        model.setHourlyDeadline("26:90");

        Throwable thrown = catchThrowable(() -> this.service.create(model, TeamConfig.class));

        assertThat(thrown).isInstanceOf(AggregatedApiError.class);
        assertThat(((AggregatedApiError)thrown).getErrors().stream().anyMatch(error -> error.getCode().equals("DH.001")))
                .isTrue();
    }
}
