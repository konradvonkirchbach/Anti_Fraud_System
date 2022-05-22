package antifraud.model.feedback;

public class FeedbackRequest {

    private Long transactionId;

    private FeedbackEnum feedback;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public FeedbackEnum getFeedback() {
        return feedback;
    }

    public void setFeedback(FeedbackEnum feedback) {
        this.feedback = feedback;
    }
}
