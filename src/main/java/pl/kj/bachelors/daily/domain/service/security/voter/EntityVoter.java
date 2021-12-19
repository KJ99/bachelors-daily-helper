package pl.kj.bachelors.daily.domain.service.security.voter;

import pl.kj.bachelors.daily.domain.model.extension.AccessVote;

public interface EntityVoter<T> {
    AccessVote vote(T subject, Object action, String userId);
}
