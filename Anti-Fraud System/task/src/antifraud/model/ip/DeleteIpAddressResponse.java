package antifraud.model.ip;

public class DeleteIpAddressResponse {

    private String status;

    public DeleteIpAddressResponse(IPAddress ipAddress) {
        status = String.format("IP %s successfully removed!", ipAddress.getIp());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
