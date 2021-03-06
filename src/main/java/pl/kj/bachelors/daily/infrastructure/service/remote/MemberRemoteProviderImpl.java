package pl.kj.bachelors.daily.infrastructure.service.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.daily.domain.config.JwtConfig;
import pl.kj.bachelors.daily.domain.model.remote.TeamMember;
import pl.kj.bachelors.daily.domain.service.security.JwtGenerator;
import pl.kj.bachelors.daily.domain.service.user.MemberRemoteProvider;
import pl.kj.bachelors.daily.infrastructure.config.TeamsApiConfig;
import pl.kj.bachelors.daily.infrastructure.user.RequestHolder;

import java.util.HashMap;
import java.util.Optional;

@Service
public class MemberRemoteProviderImpl extends BaseRemoteEntityProvider<TeamMember> implements MemberRemoteProvider {
    private final TeamsApiConfig config;
    private final JwtGenerator jwtGenerator;
    @Autowired
    public MemberRemoteProviderImpl(JwtConfig jwtConfig, ObjectMapper objectMapper, TeamsApiConfig config, JwtGenerator jwtGenerator) {
        super(jwtConfig, objectMapper);
        this.config = config;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public Optional<TeamMember> get(Integer teamId, String uid) {
        String url = String.format("%s/v1/teams/%s/roles/%s", this.config.getHost(), teamId, uid);
        return this.fetch(url);
    }

    @Override
    protected Class<TeamMember> getModelClass() {
        return TeamMember.class;
    }

    @Override
    protected String createAuthorization() {
        String uid = RequestHolder.getCurrentUserId().orElse("");
        String token = this.jwtGenerator.generateToken(uid, this.jwtConfig.getSecret(), new HashMap<>());
        return String.format("%s %s", this.jwtConfig.getType(), token);
    }
}
