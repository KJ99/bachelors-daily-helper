package pl.kj.bachelors.daily.unit.infrastructure.service.crud.update;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.daily.domain.exception.AggregatedApiError;
import pl.kj.bachelors.daily.domain.model.entity.TeamConfig;
import pl.kj.bachelors.daily.domain.model.update.TeamConfigUpdateModel;
import pl.kj.bachelors.daily.infrastructure.repository.TeamConfigRepository;
import pl.kj.bachelors.daily.infrastructure.service.crud.update.TeamConfigUpdateServiceImpl;
import pl.kj.bachelors.daily.infrastructure.user.RequestHolder;
import pl.kj.bachelors.daily.model.PatchOperation;
import pl.kj.bachelors.daily.unit.BaseUnitTest;

import java.io.IOException;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

public class TeamConfigUpdateServiceTests extends BaseUnitTest {
    @Autowired
    private TeamConfigUpdateServiceImpl service;
    @Autowired
    private TeamConfigRepository repository;

    @BeforeEach
    public void setUp() {
        when(RequestHolder.getRequestTimeZone()).thenReturn(Optional.of(TimeZone.getTimeZone("Europe/London")));
    }

    @Test
    public void testProcessUpdate() throws IOException {
        TeamConfig original = this.repository.findById(2).orElseThrow();
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("replace", "/hourly_deadline", "17:00")
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));
        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, TeamConfigUpdateModel.class));
        assertThat(thrown).isNull();
        assertThat(original.getHourlyDeadline()).isEqualTo("17:00");
        assertThat(original.getTimeZone()).isEqualTo("Europe/London");
    }

    @Test
    public void testProcessUpdate_ValidationFails() throws IOException {
        TeamConfig original = this.repository.findById(2).orElseThrow();
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("replace", "/hourly_deadline", "23fwe")
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));
        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, TeamConfigUpdateModel.class));
        assertThat(thrown).isInstanceOf(AggregatedApiError.class);
        assertThat(((AggregatedApiError)thrown).getErrors().stream().anyMatch(e -> e.getCode().equals("DH.001")))
                .isTrue();
    }
}
