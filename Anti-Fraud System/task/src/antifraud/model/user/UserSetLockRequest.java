package antifraud.model.user;

public class UserSetLockRequest {

    private String username;

    private LockEnum operation;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LockEnum getOperation() {
        return operation;
    }

    public void setOperation(LockEnum operation) {
        this.operation = operation;
    }
}
