package pl.kj.bachelors.daily.domain.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import pl.kj.bachelors.daily.domain.model.extension.CacheTag;

import java.util.Optional;

public interface CacheManager {
    Optional<JsonNode> read(CacheTag tag, String key);
    <T> Optional<T> read(CacheTag tag, String key, Class<T> modelClass);
    void save(CacheTag tag, String key, Object value) throws JsonProcessingException;
    void invalidate();
}
