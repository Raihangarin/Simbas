package sembako.dao;

import java.util.List;

public interface IBaseDAO<T> {

    List<T> getAll();

    boolean create(T entity);
    boolean update(T entity);
    boolean delete(int id);

    List<T> search(String keyword);
}
