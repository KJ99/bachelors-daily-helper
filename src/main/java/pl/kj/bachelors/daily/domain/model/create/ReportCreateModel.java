package pl.kj.bachelors.daily.domain.model.create;

import io.swagger.v3.oas.annotations.Hidden;
import org.codehaus.jackson.annotate.JsonIgnore;
import pl.kj.bachelors.daily.domain.model.embeddable.ReportIdentity;

public class ReportCreateModel {
    @JsonIgnore
    @Hidden
    private ReportIdentity id;
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
