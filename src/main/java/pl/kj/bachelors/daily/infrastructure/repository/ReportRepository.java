package pl.kj.bachelors.daily.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kj.bachelors.daily.domain.model.embeddable.ReportIdentity;
import pl.kj.bachelors.daily.domain.model.entity.Report;
import pl.kj.bachelors.daily.domain.model.remote.Team;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, ReportIdentity> {
    @Query("select r from Report r where r.id.teamId = :teamId and r.id.day = :day")
    List<Report> findByTeamIdAndDay(int teamId, String day);

    @Query("select distinct r.id.day from Report r where r.id.teamId = :teamId")
    Page<String> findDaysWithReports(int teamId, Pageable pageable);
}
