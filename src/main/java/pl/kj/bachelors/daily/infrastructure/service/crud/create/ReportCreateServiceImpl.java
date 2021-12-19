package pl.kj.bachelors.daily.infrastructure.service.crud.create;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.daily.domain.model.create.ReportCreateModel;
import pl.kj.bachelors.daily.domain.model.embeddable.ReportIdentity;
import pl.kj.bachelors.daily.domain.model.entity.Report;
import pl.kj.bachelors.daily.domain.service.ModelValidator;
import pl.kj.bachelors.daily.domain.service.crud.create.ReportCreateService;
import pl.kj.bachelors.daily.infrastructure.repository.ReportRepository;

@Service
public class ReportCreateServiceImpl
        extends BaseEntityCreateService<Report, ReportIdentity, ReportRepository, ReportCreateModel>
        implements ReportCreateService {
    @Autowired
    public ReportCreateServiceImpl(ModelMapper modelMapper, ReportRepository repository, ModelValidator validator) {
        super(modelMapper, repository, validator);
    }
}
