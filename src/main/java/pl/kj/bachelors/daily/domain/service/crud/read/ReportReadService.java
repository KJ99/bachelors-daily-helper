package pl.kj.bachelors.daily.domain.service.crud.read;

import pl.kj.bachelors.daily.domain.model.remote.Team;
import pl.kj.bachelors.daily.domain.model.result.ReportWithMemberResult;

import java.util.List;

public interface ReportReadService {
    List<ReportWithMemberResult> getForTeamAndDay(Team team, String day);
}
