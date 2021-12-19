package pl.kj.bachelors.daily.unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.kj.bachelors.daily.BaseTest;
import pl.kj.bachelors.daily.infrastructure.user.RequestHolder;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class BaseUnitTest extends BaseTest {
    protected MockedStatic<RequestHolder> requestHandlerMock;

    @BeforeEach
    public void beforeEach() {
        this.requestHandlerMock = Mockito.mockStatic(RequestHolder.class);

        when(RequestHolder.getCurrentUserId()).thenReturn(Optional.ofNullable(this.getCurrentUserId()));
    }

    @AfterEach
    public void afterEach() {
        this.requestHandlerMock.close();
    }
}
