package pl.kj.bachelors.daily.domain.model.entity;

import pl.kj.bachelors.daily.domain.model.embeddable.ReportIdentity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "reports")
public class Report {
    @EmbeddedId
    private ReportIdentity id;
    @Column(name = "last_time")
    private String lastTime;
    private String today;
    private String problem;

    public ReportIdentity getId() {
        return id;
    }

    public void setId(ReportIdentity id) {
        this.id = id;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
