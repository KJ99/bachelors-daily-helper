package pl.kj.bachelors.daily.infrastructure.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.daily.domain.model.extension.CacheTag;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.service.TeamProvider;
import pl.kj.bachelors.daily.domain.service.TeamRemoteProvider;
import pl.kj.bachelors.daily.infrastructure.service.cache.CacheManagementService;

import java.util.Optional;

@Service
public class TeamProviderService
        extends BaseCacheableRemoteEntityProvider<Team, TeamRemoteProvider>
        implements TeamProvider {
    @Autowired
    protected TeamProviderService(TeamRemoteProvider remoteEntityProvider, CacheManagementService cacheManagementService) {
        super(remoteEntityProvider, cacheManagementService);
    }

    @Override
    public Optional<Team> get(Integer teamId) {
        Optional<Team> result;
        if(this.noCache || this.shouldSkipCache()) {
            result = this.remoteEntityProvider.get(teamId);
        } else {
            result = this.getFromCache(String.valueOf(teamId)).or(() -> {
                Optional<Team> team = this.remoteEntityProvider.get(teamId);
                team.ifPresent(data -> this.saveToCache(String.valueOf(teamId), data));
                return team;
            });
        }

        return result;
    }

    @Override
    protected CacheTag getCacheTag() {
        return CacheTag.TEAM;
    }

    @Override
    protected Class<Team> getModelClass() {
        return Team.class;
    }
}
