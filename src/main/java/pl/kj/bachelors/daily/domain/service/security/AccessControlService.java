package pl.kj.bachelors.daily.domain.service.security;

import pl.kj.bachelors.daily.domain.exception.AccessDeniedException;

public interface AccessControlService<S, A> {
    void ensureThatUserHasAccess(S subject, A action) throws AccessDeniedException;
}
