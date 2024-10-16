package training.system.core.domain.user;

public class Role {
    private int id;
    private RoleEnum role;

    public Role(RoleEnum role) {
        this.role = role;
    }

    public Role(int id, RoleEnum role) {
        this.id = id;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", role=" + role + '}';
    }
}

