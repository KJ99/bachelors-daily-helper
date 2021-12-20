package pl.kj.bachelors.daily.application.dto.response;

public class UserReportResponse {
    private ProfileResponse user;
    private ReportResponse report;

    public ProfileResponse getUser() {
        return user;
    }

    public void setUser(ProfileResponse user) {
        this.user = user;
    }

    public ReportResponse getReport() {
        return report;
    }

    public void setReport(ReportResponse report) {
        this.report = report;
    }
}
