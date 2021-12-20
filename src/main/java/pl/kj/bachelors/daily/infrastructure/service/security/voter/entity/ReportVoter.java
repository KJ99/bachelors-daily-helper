package pl.kj.bachelors.daily.infrastructure.service.security.voter.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kj.bachelors.daily.domain.model.entity.Report;
import pl.kj.bachelors.daily.domain.model.entity.TeamConfig;
import pl.kj.bachelors.daily.domain.model.extension.AccessVote;
import pl.kj.bachelors.daily.domain.model.extension.action.ReportAction;
import pl.kj.bachelors.daily.infrastructure.TimeUtil;
import pl.kj.bachelors.daily.infrastructure.repository.TeamConfigRepository;

import java.util.Optional;

@Component
public class ReportVoter extends BaseEntityVoter<Report, ReportAction> {
    private final TeamConfigRepository configRepository;

    @Autowired
    public ReportVoter(TeamConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    protected AccessVote voteInternal(Report subject, ReportAction action, String userId) {
        Optional<TeamConfig> config = this.configRepository.findById(subject.getId().getTeamId());
        return subject.getId().getUserId().equals(userId) &&
                (
                        config.isEmpty() ||
                                TimeUtil.isHourInFuture(config.get().getHourlyDeadline(), config.get().getTimeZone())
                ) ? AccessVote.ALLOW : AccessVote.DENY;
    }

    @Override
    protected ReportAction[] getSupportedActions() {
        return ReportAction.values();
    }
}
