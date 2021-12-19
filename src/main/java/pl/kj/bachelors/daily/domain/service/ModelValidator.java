package pl.kj.bachelors.daily.domain.service;

import pl.kj.bachelors.daily.domain.exception.ApiError;

import java.util.Collection;

public interface ModelValidator {
    <T> Collection<ApiError> validateModel(T model);
}
