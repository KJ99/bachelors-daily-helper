package pl.kj.bachelors.daily.unit.infrastructure.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.service.TeamRemoteProvider;
import pl.kj.bachelors.daily.infrastructure.service.TeamProviderService;
import pl.kj.bachelors.daily.infrastructure.user.RequestHolder;
import pl.kj.bachelors.daily.unit.BaseUnitTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class TeamProviderServiceTests extends BaseUnitTest {
    @Autowired
    private TeamProviderService service;
    @MockBean
    private TeamRemoteProvider remoteProvider;

    @BeforeEach
    public void setUp() {
        Team team = new Team();
        team.setId(100);
        team.setName("First remote team");
        Team team2 = new Team();
        team2.setId(200);
        team2.setName("Second remote team");

        given(this.remoteProvider.get(100)).willReturn(Optional.of(team));
        given(this.remoteProvider.get(200)).willReturn(Optional.of(team2));
    }
    @Test
    public void testGet_FromCache() {
        Optional<Team> result = this.service.get(100);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(100);
        assertThat(result.get().getName()).isEqualTo("First Local Team");
    }

    @Test
    public void testGet_NoCache_Field() {
        this.service.setNoCache(true);
        Optional<Team> result = this.service.get(100);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(100);
        assertThat(result.get().getName()).isEqualTo("First remote team");
    }

    @Test
    public void testGet_NoCache_Header() {
        this.requestHandlerMock.when(() -> RequestHolder.getHeaderValue(HttpHeaders.CACHE_CONTROL))
                .thenReturn(Optional.of("no-cache"));

        Optional<Team> result = this.service.get(100);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(100);
        assertThat(result.get().getName()).isEqualTo("First remote team");
    }

    @Test
    public void testGet_NotFoundInCache() {
        Optional<Team> result = this.service.get(200);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(200);
        assertThat(result.get().getName()).isEqualTo("Second remote team");
    }

    @Test
    public void testGet_NotFoundAnywhere() {
        Optional<Team> result = this.service.get(-10);

        assertThat(result).isEmpty();
    }
}
