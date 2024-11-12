package training.system.core.domain.exercise;

import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.core.exception.DAOException;
import training.system.core.exception.DatabaseConnectionException;
import training.system.core.factory.ConnectionFactory;
import training.system.core.generic.GenericController;

import java.util.Set;

public class ExerciseController implements GenericController<Exercise, Long>, IExercise {

    private final ExerciseDAO exerciseDAO;

    public ExerciseController() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            this.exerciseDAO = new ExerciseDAO(connectionFactory.getConnection());
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el controlador de ejercicio", e);
        }
    }

    @Override
    public Exercise create(Exercise entity) throws ControllerException {
        try {
            return exerciseDAO.create(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear el ejercicio", e);
        }
    }

    @Override
    public Exercise update(Exercise entity) throws ControllerException {
        try {
            return exerciseDAO.update(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al actualizar el ejercicio", e);
        }
    }

    @Override
    public Set<Exercise> list() throws ControllerException {
        try {
            return exerciseDAO.list();
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los ejercicios", e);
        }
    }

    @Override
    public Exercise search(Long aLong) throws ControllerException {
        try {
            return exerciseDAO.search(aLong);
        } catch (DAOException e) {
            throw new ControllerException("Error al buscar el ejercicio", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ControllerException {
        try {
            return exerciseDAO.delete(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar el ejercicio", e);
        }
    }

    @Override
    public Exercise createExerciseForClient(Exercise exercise) throws ControllerException {
        try {
            return exerciseDAO.createExerciseForClient(exercise);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear el ejercicio para el cliente", e);
        }
    }

    @Override
    public Set<Exercise> listUserExercises(User user) throws ControllerException {
        try {
            return exerciseDAO.listUserExercises(user);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los ejercicios del usuario", e);
        }
    }
}