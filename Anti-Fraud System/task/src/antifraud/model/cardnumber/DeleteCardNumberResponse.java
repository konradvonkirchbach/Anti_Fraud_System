package antifraud.model.cardnumber;

public class DeleteCardNumberResponse {

    private String status;

    public DeleteCardNumberResponse(CardNumber cardNumber) {
        status = String.format("Card %s successfully removed!", cardNumber.getNumber());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
