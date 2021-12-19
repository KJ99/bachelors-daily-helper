package pl.kj.bachelors.daily.infrastructure.service.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.daily.domain.config.JwtConfig;
import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.service.TeamRemoteProvider;
import pl.kj.bachelors.daily.domain.service.security.JwtGenerator;
import pl.kj.bachelors.daily.infrastructure.config.TeamsApiConfig;

import java.util.Optional;

@Service
public class TeamRemoteProviderImpl extends BaseRemoteEntityProvider<Team> implements TeamRemoteProvider {
    private final TeamsApiConfig config;

    @Autowired
    protected TeamRemoteProviderImpl(
            JwtConfig jwtConfig,
            ObjectMapper objectMapper,
            TeamsApiConfig config,
            JwtGenerator jwtGenerator) {
        super(jwtConfig, objectMapper);
        this.config = config;
    }

    @Override
    public Optional<Team> get(Integer teamId) {
        String url = String.format("%s/v1/teams/%s", this.config.getHost(), teamId);
        return this.fetch(url);
    }

    @Override
    protected Class<Team> getModelClass() {
        return Team.class;
    }
}
