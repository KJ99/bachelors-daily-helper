package pl.kj.bachelors.daily.unit.infrastructure.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.daily.model.ExampleValidatableModel;
import pl.kj.bachelors.daily.infrastructure.service.ValidationService;
import pl.kj.bachelors.daily.unit.BaseUnitTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationServiceTests extends BaseUnitTest {
    @Autowired
    private ValidationService service;

    @Test
    public void testValidateModel_NoErrors() {
        var model = ExampleValidatableModel.getValidInstance();

        var result = this.service.validateModel(model);

        assertThat(result).isEmpty();
    }

    @Test
    public void testValidateModel_WithErrors() {
        var model = ExampleValidatableModel.getInvalidInstance();

        var result = this.service.validateModel(model);

        assertThat(result)
                .isNotEmpty()
                .hasSize(3);
        assertThat(result.stream().allMatch(item -> item.getPath() != null)).isTrue();
    }
}
