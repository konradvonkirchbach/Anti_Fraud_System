package antifraud.model.user;

public class UserSetLockResponse {

    private String status;

    public UserSetLockResponse() {

    }

    public UserSetLockResponse(User user) {
        status = String.format("User %s %s!", user.getUsername(), user.getLockEnum().getStatus() );
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
