package pl.kj.bachelors.daily.domain.model.update;

import io.swagger.v3.oas.annotations.Hidden;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.validation.constraints.Pattern;

public class TeamConfigUpdateModel {
    @Pattern(regexp = "^((2[0-3])|([0-1][0-9])):[0-5][0-9]$", message = "DH.001")
    private String hourlyDeadline;

    public String getHourlyDeadline() {
        return hourlyDeadline;
    }

    public void setHourlyDeadline(String hourlyDeadline) {
        this.hourlyDeadline = hourlyDeadline;
    }
}
