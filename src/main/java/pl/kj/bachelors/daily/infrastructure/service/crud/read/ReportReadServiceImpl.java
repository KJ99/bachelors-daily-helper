package pl.kj.bachelors.daily.infrastructure.service.crud.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.daily.domain.model.entity.Report;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.model.remote.UserProfile;
import pl.kj.bachelors.daily.domain.model.result.ReportWithMemberResult;
import pl.kj.bachelors.daily.domain.service.crud.read.ReportReadService;
import pl.kj.bachelors.daily.domain.service.user.ProfileProvider;
import pl.kj.bachelors.daily.infrastructure.repository.ReportRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportReadServiceImpl implements ReportReadService {
    private final ReportRepository repository;
    private final ProfileProvider profileProvider;

    @Autowired
    public ReportReadServiceImpl(ReportRepository repository, ProfileProvider profileProvider) {
        this.repository = repository;
        this.profileProvider = profileProvider;
    }

    @Override
    public List<ReportWithMemberResult> getForTeamAndDay(Team team, String day) {
        List<Report> reports = this.repository.findByTeamIdAndDay(team.getId(), day);
        return reports.stream().map(this::createResult).collect(Collectors.toList());
    }

    private ReportWithMemberResult createResult(Report report) {
        UserProfile userProfile = this.profileProvider.get(report.getId().getUserId()).orElse(null);
        ReportWithMemberResult result = new ReportWithMemberResult();
        result.setUser(userProfile);
        result.setReport(report);

        return result;
    }
}
