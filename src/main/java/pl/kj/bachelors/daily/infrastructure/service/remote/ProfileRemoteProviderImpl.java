package pl.kj.bachelors.daily.infrastructure.service.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.daily.domain.config.JwtConfig;
import pl.kj.bachelors.daily.domain.model.remote.UserProfile;
import pl.kj.bachelors.daily.domain.service.user.ProfileRemoteProvider;
import pl.kj.bachelors.daily.infrastructure.config.IdentityServerConfig;

import java.util.Optional;

@Service
public class ProfileRemoteProviderImpl extends BaseRemoteEntityProvider<UserProfile> implements ProfileRemoteProvider {
    private final IdentityServerConfig config;

    @Autowired
    public ProfileRemoteProviderImpl(JwtConfig jwtConfig, IdentityServerConfig config, ObjectMapper objectMapper) {
        super(jwtConfig, objectMapper);
        this.config = config;
    }

    @Override
    protected Class<UserProfile> getModelClass() {
        return UserProfile.class;
    }

    @Override
    public Optional<UserProfile> get(String uid) {
        String url = String.format("%s/v1/profile/%s", this.config.getHost(), uid);
        return this.fetch(url);
    }
}
