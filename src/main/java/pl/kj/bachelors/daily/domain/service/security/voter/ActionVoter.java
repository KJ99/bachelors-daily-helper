package pl.kj.bachelors.daily.domain.service.security.voter;

import pl.kj.bachelors.daily.domain.model.extension.AccessVote;

public interface ActionVoter<S, A> {
    AccessVote vote(S subject, A action, String userId);
    A[] getSupportedActions();
}
