package pl.kj.bachelors.daily.domain.service.security;

import java.util.Map;

public interface JwtGenerator {
    String generateToken(String sub, String secret, Map<String, Object> additionalClaims);
}
