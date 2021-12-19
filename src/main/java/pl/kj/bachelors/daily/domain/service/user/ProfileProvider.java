package pl.kj.bachelors.daily.domain.service.user;

import pl.kj.bachelors.daily.domain.model.remote.UserProfile;

import java.util.Optional;

public interface ProfileProvider {
    Optional<UserProfile> get(String uid);
}
