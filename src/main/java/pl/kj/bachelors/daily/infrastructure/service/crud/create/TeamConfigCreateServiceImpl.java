package pl.kj.bachelors.daily.infrastructure.service.crud.create;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.daily.domain.model.create.TeamConfigCreateModel;
import pl.kj.bachelors.daily.domain.model.entity.TeamConfig;
import pl.kj.bachelors.daily.domain.service.ModelValidator;
import pl.kj.bachelors.daily.domain.service.crud.create.TeamConfigCreateService;
import pl.kj.bachelors.daily.infrastructure.repository.TeamConfigRepository;
import pl.kj.bachelors.daily.infrastructure.user.RequestHolder;

import java.util.TimeZone;

@Service
public class TeamConfigCreateServiceImpl
        extends BaseEntityCreateService<TeamConfig, Integer, TeamConfigRepository, TeamConfigCreateModel>
        implements TeamConfigCreateService {
    @Autowired
    public TeamConfigCreateServiceImpl(ModelMapper modelMapper, TeamConfigRepository repository, ModelValidator validator) {
        super(modelMapper, repository, validator);
    }

    @Override
    protected void preCreate(TeamConfigCreateModel model) {
        TimeZone timeZone = RequestHolder.getRequestTimeZone().orElse(TimeZone.getDefault());

        model.setTimeZone(timeZone.getID());
    }
}
