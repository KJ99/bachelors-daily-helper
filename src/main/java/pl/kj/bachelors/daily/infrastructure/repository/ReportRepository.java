package pl.kj.bachelors.daily.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.daily.domain.model.embeddable.ReportIdentity;
import pl.kj.bachelors.daily.domain.model.entity.Report;

public interface ReportRepository extends JpaRepository<Report, ReportIdentity> {
}
