package pl.kj.bachelors.daily.infrastructure.service.crud.update;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.daily.domain.model.embeddable.ReportIdentity;
import pl.kj.bachelors.daily.domain.model.entity.Report;
import pl.kj.bachelors.daily.domain.model.update.ReportUpdateModel;
import pl.kj.bachelors.daily.domain.service.crud.update.ReportUpdateService;
import pl.kj.bachelors.daily.infrastructure.repository.ReportRepository;
import pl.kj.bachelors.daily.infrastructure.service.ValidationService;

@Service
public class ReportUpdateServiceImpl
        extends BaseEntityUpdateService<Report, ReportIdentity, ReportUpdateModel, ReportRepository>
        implements ReportUpdateService {

    @Autowired
    public ReportUpdateServiceImpl(ReportRepository repository, ValidationService validationService, ModelMapper modelMapper, ObjectMapper objectMapper) {
        super(repository, validationService, modelMapper, objectMapper);
    }
}
