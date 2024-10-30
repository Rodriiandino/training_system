package training.system.core.generic;

import training.system.core.exception.ControllerException;

import java.util.Set;

public interface GenericController<T, ID> {
    T create(T entity) throws ControllerException;
    T update(T entity) throws ControllerException;
    Set<T> list() throws ControllerException;
    boolean delete(ID id) throws ControllerException;
}