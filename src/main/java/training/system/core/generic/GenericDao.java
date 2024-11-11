package training.system.core.generic;

import training.system.core.exception.DAOException;

import java.util.Set;

public interface GenericDao<T, ID> {
    T create(T entity) throws DAOException;
    T update(T entity) throws DAOException;
    Set<T> list() throws DAOException;
    T search(ID id) throws DAOException;
    boolean delete(ID id) throws DAOException;
}
