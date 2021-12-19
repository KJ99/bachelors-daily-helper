package pl.kj.bachelors.daily.domain.service.crud.delete;

public interface DeleteService<E> {
    void delete(E entity) throws Exception;
}
