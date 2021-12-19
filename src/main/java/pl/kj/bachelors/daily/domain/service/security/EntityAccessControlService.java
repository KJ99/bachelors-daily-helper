package pl.kj.bachelors.daily.domain.service.security;

import pl.kj.bachelors.daily.domain.exception.AccessDeniedException;

public interface EntityAccessControlService <T> {
    void ensureThatUserHasAccess(T subject, Object action) throws AccessDeniedException;
}
