package training.system.viewController.interfaces;

public interface IViewControllerManipulation<T> {
    void createColumn();
    void edit(T entity);
    void create(T entity);
    void list();
    void showCreateModal();
    void showEditModal();
    void showCreateForClientModal();
    void createForClient(T entity);
}
