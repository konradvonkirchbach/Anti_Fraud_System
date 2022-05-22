package antifraud.model.user;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    @NotNull(message = "Name cannot be null")
    private String name;

    @Column
    @NotNull(message = "username cannot be null")
    private String username;

    @Column
    @NotNull(message = "password cannot be null")
    private String password;

    @Column
    private RoleEnum role;

    @Column
    private LockEnum lockEnum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public LockEnum getLockEnum() {
        return lockEnum;
    }

    public void setLockEnum(LockEnum locked) {
        lockEnum = locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.name.equalsIgnoreCase(user.getName())
                && this.username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, username, password, role, lockEnum);
    }
}
