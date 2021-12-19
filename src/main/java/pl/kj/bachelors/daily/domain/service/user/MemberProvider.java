package pl.kj.bachelors.daily.domain.service.user;

import pl.kj.bachelors.daily.domain.model.remote.TeamMember;

import java.util.Optional;

public interface MemberProvider {
    Optional<TeamMember> get(Integer teamId, String uid);
}
