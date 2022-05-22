package antifraud.model.user;

public class DeleteUserResponse {

    public DeleteUserResponse(User user) {
        this.username = user.getUsername();
    }

    private String username;

    private String status = "Deleted successfully!";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
