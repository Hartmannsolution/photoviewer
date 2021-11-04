package datafacades;
import java.util.List;

import entities.Photo;
import entities.Tag;
import errorhandling.EntityNotFoundException;

public interface IDataFacade<T> {
    T create(T t);
    T getById(String fileName) throws EntityNotFoundException;
    List<T> getAll();
    T update(T t) throws EntityNotFoundException;
    T delete(String id) throws EntityNotFoundException;
    List<T> findByProperty(String property, String propValue);
}