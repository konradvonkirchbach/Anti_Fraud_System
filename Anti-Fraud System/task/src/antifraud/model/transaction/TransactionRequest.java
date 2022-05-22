package antifraud.model.transaction;

import antifraud.model.feedback.FeedbackEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity(name = "transaction")
public class TransactionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    @NotNull(message = "amount must be set")
    private Long amount;

    @Column
    @NotNull(message = "ip adress must be set")
    private String ip;

    @Column
    @NotNull(message = "number must be set")
    private String number;

    @Column
    @NotNull(message = "region must be set")
    private RegionCode region;

    @Column
    @NotNull(message = "date must be set")
    private LocalDateTime date;

    @Column
    private TransactionResponseStatus result;

    @Column
    private FeedbackEnum feedback = FeedbackEnum.NONE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setIpAddress(String ip) {
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

    public FeedbackEnum getFeedback() {
        return feedback;
    }

    public void setFeedback(FeedbackEnum feedback) {
        this.feedback = feedback;
    }
}
