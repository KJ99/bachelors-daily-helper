package pl.kj.bachelors.daily.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.daily.domain.model.entity.TeamConfig;

public interface TeamConfigRepository extends JpaRepository<TeamConfig, Integer> {
}
