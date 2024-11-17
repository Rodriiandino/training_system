package training.system.viewController.interfaces;

public interface IViewModal {
    void create();
    void edit();
    void createForClient();
    <T> void setParentController(T controller);
    void updateModalMode();
}
