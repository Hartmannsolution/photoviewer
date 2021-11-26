package datafacades;
import java.util.List;

import entities.Photo;
import entities.Tag;
import errorhandling.API_Exception;
import errorhandling.EntityNotFoundException;

public interface IDataFacade<T> {
    T create(T t) throws API_Exception;
    T getById(String fileName) throws EntityNotFoundException;
    List<T> getAll();
    T update(T t) throws EntityNotFoundException;
    T delete(String id) throws EntityNotFoundException;
    List<T> findByProperty(String property, String propValue) throws EntityNotFoundException;
}