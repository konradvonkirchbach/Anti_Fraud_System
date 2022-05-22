package antifraud.model.feedback;

import antifraud.model.transaction.RegionCode;
import antifraud.model.transaction.TransactionRequest;
import antifraud.model.transaction.TransactionResponseStatus;

import java.time.LocalDateTime;

public class FeedbackResponse {

    public FeedbackResponse() {
        // empty constructor
    }

    public FeedbackResponse(TransactionRequest transaction) {
        this.transactionId = transaction.getId();
        this.amount = transaction.getAmount();
        this.ip = transaction.getIp();
        this.number = transaction.getNumber();
        this.region = transaction.getRegion();
        this.date = transaction.getDate();
        this.result = transaction.getResult();
        this.feedback = transaction.getFeedback().getText();
    }

    private Long transactionId;

    private Long amount;

    private String ip;

    private String number;

    private RegionCode region;

    private LocalDateTime date;

    private TransactionResponseStatus result;

    private String feedback;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RegionCode getRegion() {
        return region;
    }

    public void setRegion(RegionCode region) {
        this.region = region;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionResponseStatus getResult() {
        return result;
    }

    public void setResult(TransactionResponseStatus result) {
        this.result = result;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
