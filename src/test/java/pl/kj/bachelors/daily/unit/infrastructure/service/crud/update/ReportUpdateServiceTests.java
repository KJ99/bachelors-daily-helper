package pl.kj.bachelors.daily.unit.infrastructure.service.crud.update;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.daily.domain.exception.AggregatedApiError;
import pl.kj.bachelors.daily.domain.model.embeddable.ReportIdentity;
import pl.kj.bachelors.daily.domain.model.entity.Report;
import pl.kj.bachelors.daily.domain.model.update.ReportUpdateModel;
import pl.kj.bachelors.daily.domain.model.update.TeamConfigUpdateModel;
import pl.kj.bachelors.daily.infrastructure.repository.ReportRepository;
import pl.kj.bachelors.daily.infrastructure.service.crud.update.ReportUpdateServiceImpl;
import pl.kj.bachelors.daily.model.PatchOperation;
import pl.kj.bachelors.daily.unit.BaseUnitTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class ReportUpdateServiceTests extends BaseUnitTest {
    @Autowired
    private ReportUpdateServiceImpl service;
    @Autowired
    private ReportRepository repository;

    @Test
    public void testProcessUpdate() throws IOException {
        ReportIdentity id = new ReportIdentity();
        id.setDay("2030-01-01");
        id.setTeamId(1);
        id.setUserId("uid-100");

        Report original = this.repository.findById(id).orElseThrow();
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("replace", "/last_time", "Hello"),
                new PatchOperation("replace", "/problem", null),
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));
        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, ReportUpdateModel.class));
        assertThat(thrown).isNull();
        assertThat(original.getLastTime()).isEqualTo("Hello");
        assertThat(original.getProblem()).isNull();
    }
}
