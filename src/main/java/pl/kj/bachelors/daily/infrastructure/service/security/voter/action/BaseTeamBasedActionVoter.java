package pl.kj.bachelors.daily.infrastructure.service.security.voter.action;

import pl.kj.bachelors.daily.domain.model.extension.AccessVote;
import pl.kj.bachelors.daily.domain.model.extension.Role;
import pl.kj.bachelors.daily.domain.model.remote.TeamMember;
import pl.kj.bachelors.daily.domain.service.user.MemberProvider;

import java.util.List;
import java.util.Optional;

public abstract class BaseTeamBasedActionVoter<A> extends BaseActionVoter<Integer, A> {
    private final MemberProvider memberProvider;


    protected BaseTeamBasedActionVoter(MemberProvider memberProvider) {
        this.memberProvider = memberProvider;
    }

    @Override
    protected AccessVote voteInternal(Integer teamId, A action, String userId) {
        Optional<TeamMember> member = this.memberProvider.get(teamId, userId);
        return member.isPresent() ? this.voteInternal(action, member.get()) : AccessVote.DENY;
    }

    protected abstract AccessVote voteInternal(A action, TeamMember member);

    protected boolean hasRole(TeamMember member, Role role) {
        return member.getRoles().contains(role);
    }

    protected boolean hasRole(TeamMember member, List<Role> roles) {
        return member.getRoles().stream().anyMatch(roles::contains);
    }
}
