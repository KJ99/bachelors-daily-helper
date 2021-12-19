package pl.kj.bachelors.daily.domain.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configurations")
public class TeamConfig {
    @Id
    @Column(name = "team_id")
    private int teamId;
    @Column(name = "hourly_deadline")
    private String hourlyDeadline;
    @Column(name = "timezone")
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
