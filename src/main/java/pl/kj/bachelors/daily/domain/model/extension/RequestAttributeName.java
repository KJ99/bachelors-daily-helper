package pl.kj.bachelors.daily.domain.model.extension;

public enum RequestAttributeName {
    USER_ID("uid");

    public final String value;

    RequestAttributeName(String value) {
        this.value = value;
    }
}
