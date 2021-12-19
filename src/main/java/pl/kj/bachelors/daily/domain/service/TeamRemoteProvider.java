package pl.kj.bachelors.daily.domain.service;

import pl.kj.bachelors.daily.domain.model.remote.Team;

import java.util.Optional;

public interface TeamRemoteProvider {
    Optional<Team> get(Integer teamId);
}
