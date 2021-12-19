package pl.kj.bachelors.daily.domain.service.crud.create;

public interface CreateService<E, C> {
    E create(C model, Class<E> entityClass) throws Exception;
}
