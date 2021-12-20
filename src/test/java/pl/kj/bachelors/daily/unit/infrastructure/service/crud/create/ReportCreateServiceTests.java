package pl.kj.bachelors.daily.unit.infrastructure.service.crud.create;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.daily.domain.model.create.ReportCreateModel;
import pl.kj.bachelors.daily.domain.model.embeddable.ReportIdentity;
import pl.kj.bachelors.daily.domain.model.entity.Report;
import pl.kj.bachelors.daily.infrastructure.service.crud.create.ReportCreateServiceImpl;
import pl.kj.bachelors.daily.unit.BaseUnitTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportCreateServiceTests extends BaseUnitTest {
    @Autowired
    private ReportCreateServiceImpl service;

    @Test
    public void testCreate() throws Exception {
        ReportIdentity identity = new ReportIdentity();
        identity.setTeamId(1);
        identity.setDay("2030-01-01");
        identity.setUserId("uid-100");

        ReportCreateModel model = new ReportCreateModel();
        model.setId(identity);
        model.setLastTime("Last time");
        model.setToday("Today");
        model.setProblem("Problem :-(");

        Report report = this.service.create(model, Report.class);

        assertThat(report.getLastTime()).isEqualTo("Last time");
        assertThat(report.getToday()).isEqualTo("Today");
        assertThat(report.getProblem()).isEqualTo("Problem :-(");
    }
}
