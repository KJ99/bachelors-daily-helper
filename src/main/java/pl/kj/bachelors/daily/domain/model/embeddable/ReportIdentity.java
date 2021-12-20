package pl.kj.bachelors.daily.domain.model.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ReportIdentity implements Serializable {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "target_day")
    private String day;
    @Column(name = "team_id")
    private int teamId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
