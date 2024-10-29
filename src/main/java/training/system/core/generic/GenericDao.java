package training.system.core.generic;

import java.util.Set;

public interface GenericDao<T, ID> {
    T create(T entity);
    T update(T entity);
    Set<T> list();
    boolean delete(ID id);
}
