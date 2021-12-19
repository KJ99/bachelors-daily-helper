package pl.kj.bachelors.daily.infrastructure.service.security.voter.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kj.bachelors.daily.domain.model.entity.TeamConfig;
import pl.kj.bachelors.daily.domain.model.extension.AccessVote;
import pl.kj.bachelors.daily.domain.model.extension.Role;
import pl.kj.bachelors.daily.domain.model.extension.action.TeamDailyAction;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.model.remote.TeamMember;
import pl.kj.bachelors.daily.domain.service.user.MemberProvider;
import pl.kj.bachelors.daily.infrastructure.TimeUtil;
import pl.kj.bachelors.daily.infrastructure.repository.TeamConfigRepository;

import java.util.Optional;

@Component
public class TeamVoter extends BaseEntityVoter<Team, TeamDailyAction> {
    private final MemberProvider memberProvider;
    private final TeamConfigRepository configRepository;

    @Autowired
    public TeamVoter(MemberProvider memberProvider, TeamConfigRepository configRepository) {
        this.memberProvider = memberProvider;
        this.configRepository = configRepository;
    }

    @Override
    protected AccessVote voteInternal(Team subject, TeamDailyAction action, String userId) {
        Optional<TeamMember> member = this.memberProvider.get(subject.getId(), userId);
        AccessVote vote;
        switch (action) {
            case CONFIGURE:
                vote = member.isPresent() && member.get().getRoles().contains(Role.SCRUM_MASTER)
                        ? AccessVote.ALLOW
                        : AccessVote.DENY;
                break;
            case SEND_REPORT:
                Optional<TeamConfig> config = this.configRepository.findById(subject.getId());
                vote = member.isPresent() &&
                        member.get().getRoles().contains(Role.TEAM_MEMBER) &&
                        (
                                config.isEmpty() ||
                                        TimeUtil.isHourInFuture(
                                                config.get().getHourlyDeadline(),
                                                config.get().getTimeZone()
                                        )
                        )
                        ? AccessVote.ALLOW
                        : AccessVote.DENY;
                break;
            case READ_CONFIGURATION:
                vote = member.isPresent() ? AccessVote.ALLOW : AccessVote.DENY;
                break;
            default:
                vote = AccessVote.OMIT;
                break;
        }

        return vote;
    }

    @Override
    protected TeamDailyAction[] getSupportedActions() {
        return TeamDailyAction.values();
    }
}
