package training.system.viewController;

public interface IViewModal {
    void create();
    void edit();
    void createForClient();
    <T> void setParentController(T controller);
    void updateModalMode();
}
