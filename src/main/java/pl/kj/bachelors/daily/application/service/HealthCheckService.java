package pl.kj.bachelors.daily.application.service;

import org.springframework.stereotype.Service;
import pl.kj.bachelors.daily.application.model.HealthCheckResult;
import pl.kj.bachelors.daily.application.model.SingleCheckResult;

@Service
public class HealthCheckService {

    public HealthCheckResult check() {
        HealthCheckResult report = new HealthCheckResult();

        report.addResult(new SingleCheckResult("Main Database", true));

        return report;
    }
}
