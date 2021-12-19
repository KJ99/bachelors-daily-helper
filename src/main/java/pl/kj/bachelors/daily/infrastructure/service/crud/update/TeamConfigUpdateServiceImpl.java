package pl.kj.bachelors.daily.infrastructure.service.crud.update;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.daily.domain.model.entity.TeamConfig;
import pl.kj.bachelors.daily.domain.model.update.TeamConfigUpdateModel;
import pl.kj.bachelors.daily.domain.service.crud.update.TeamConfigUpdateService;
import pl.kj.bachelors.daily.infrastructure.repository.TeamConfigRepository;
import pl.kj.bachelors.daily.infrastructure.service.ValidationService;
import pl.kj.bachelors.daily.infrastructure.user.RequestHolder;

import java.util.TimeZone;

@Service
public class TeamConfigUpdateServiceImpl
        extends BaseEntityUpdateService<TeamConfig, Integer, TeamConfigUpdateModel, TeamConfigRepository>
        implements TeamConfigUpdateService {
    @Autowired
    public TeamConfigUpdateServiceImpl(TeamConfigRepository repository, ValidationService validationService, ModelMapper modelMapper, ObjectMapper objectMapper) {
        super(repository, validationService, modelMapper, objectMapper);
    }

    @Override
    protected void preUpdate(TeamConfig original, TeamConfigUpdateModel updateModel) {
        TimeZone timeZone = RequestHolder.getRequestTimeZone().orElse(TimeZone.getDefault());
        original.setTimeZone(timeZone.getID());
    }
}
