package pl.kj.bachelors.daily.domain.model.result;

import pl.kj.bachelors.daily.domain.model.entity.Report;
import pl.kj.bachelors.daily.domain.model.remote.TeamMember;
import pl.kj.bachelors.daily.domain.model.remote.UserProfile;

public class ReportWithMemberResult {
    private UserProfile user;
    private Report report;

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
