package pl.kj.bachelors.daily.domain.model.create;

import io.swagger.v3.oas.annotations.Hidden;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.validation.constraints.Pattern;

public class TeamConfigCreateModel {
    private int teamId;
    @Pattern(regexp = "^((2[0-3])|([0-1][0-9])):[0-5][0-9]$", message = "DH.001")
    private String hourlyDeadline;
    @JsonIgnore
    @Hidden
    private String timeZone;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getHourlyDeadline() {
        return hourlyDeadline;
    }

    public void setHourlyDeadline(String hourlyDeadline) {
        this.hourlyDeadline = hourlyDeadline;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
