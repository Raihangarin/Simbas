package sembako.dao;

import java.util.List;

public abstract class BaseDAO<T> implements IBaseDAO<T> {
    protected BaseDAO() {
    }

    @Override
    public abstract List<T> getAll();

    @Override
    public abstract boolean create(T entity);

    @Override
    public abstract boolean update(T entity);

    @Override
    public abstract boolean delete(int id);

    @Override
    public abstract List<T> search(String keyword);

    protected void logError(String methodName, String errorMessage) {
        System.err.println("Error " + methodName + ": " + errorMessage);
    }
}
