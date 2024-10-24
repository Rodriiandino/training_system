package training.system.core.domain.user;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return role == role1.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", role=" + role + '}';
    }
}

