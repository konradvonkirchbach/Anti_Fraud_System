package antifraud.model.user;

public enum LockEnum {
    LOCK(true, "locked"),
    UNLOCK(false, "unlocked");

    private boolean isLocked;

    private String status;

    LockEnum(boolean isLocked, String status) {
        this.isLocked = isLocked;
        this.status = status;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public String getStatus() {
        return status;
    }
}
