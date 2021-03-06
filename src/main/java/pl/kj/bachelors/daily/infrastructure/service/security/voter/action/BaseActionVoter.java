package pl.kj.bachelors.daily.infrastructure.service.security.voter.action;

import pl.kj.bachelors.daily.domain.model.extension.AccessVote;
import pl.kj.bachelors.daily.domain.service.security.voter.ActionVoter;

import java.util.Arrays;

public abstract class BaseActionVoter<S, A> implements ActionVoter<S, A> {
    @Override
    public AccessVote vote(S subject, A action, String userId) {
        return Arrays.asList(this.getSupportedActions()).contains(action)
                ? voteInternal(subject, (A)action, userId)
                : AccessVote.OMIT;
    }
    protected abstract AccessVote voteInternal(S subject, A action, String userId);
}
