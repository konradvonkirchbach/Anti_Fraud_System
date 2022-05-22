package antifraud.service;

import antifraud.model.feedback.FeedbackEnum;
import antifraud.model.feedback.FeedbackException;
import antifraud.model.feedback.FeedbackRequest;
import antifraud.model.transaction.Limits;
import antifraud.model.transaction.TransactionRequest;
import antifraud.model.transaction.TransactionResponseStatus;
import antifraud.persistence.TransactionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private TransactionRequestRepository transactionRequestRepository;

    @Autowired
    public FeedbackService(TransactionRequestRepository transactionRequestRepository) {
        this.transactionRequestRepository = transactionRequestRepository;
    }

    public boolean transactionExists(Long transactionId) {
        return transactionRequestRepository.existsById(transactionId);
    }

    public TransactionRequest getTransaction(Long transactionId) {
        return transactionRequestRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalStateException(String.format("Transaction with id %d not found", transactionId)));
    }

    public boolean isTransactionSet(Long transactionId) {
        return transactionRequestRepository.findById(transactionId)
                .map(TransactionRequest::getFeedback)
                .map(result -> FeedbackEnum.validFeedback().contains(result))
                .orElseGet(() -> false);
    }

    public boolean existsByNumber(String number) {
        return transactionRequestRepository.existsByNumber(number);
    }

    public List<TransactionRequest> getAllTransactions() {
        return transactionRequestRepository.findAll();
    }

    public List<TransactionRequest> getAllByCardNumber(String number) {
        return transactionRequestRepository.findAllByNumberOrderById(number);
    }

    public void processFeedback(FeedbackRequest feedback) {
        TransactionRequest transactionRequest = getTransaction(feedback.getTransactionId());

        if (feedback.getFeedback().getText().equals(transactionRequest.getResult().name())) {
            throw new FeedbackException("Same feedback as result");
        }

        switch (feedback.getFeedback()) {
            case ALLOWED:
                processAllowed(transactionRequest);
                break;
            case MANUAL_PROCESSING:
                processManualProcessing(transactionRequest);
                break;
            case PROHIBITED:
                processProhibited(transactionRequest);
                break;
            default:
                throw new IllegalArgumentException(String.format("Feedback %s not recognized",
                        feedback.getFeedback().getText()));
        }

        transactionRequest.setFeedback(feedback.getFeedback());
        transactionRequestRepository.save(transactionRequest);
    }

    private void processAllowed(TransactionRequest transactionRequest) {
        if (transactionRequest.getResult() == TransactionResponseStatus.MANUAL_PROCESSING) {
            Limits.upgradeManualProcessingLimit(transactionRequest.getAmount());
        } else if (transactionRequest.getResult() == TransactionResponseStatus.PROHIBITED) {
            Limits.upgradeManualProcessingLimit(transactionRequest.getAmount());
            Limits.upgradeProhibitedLimit(transactionRequest.getAmount());
        }
    }

    private void processManualProcessing(TransactionRequest transactionRequest) {
        if (transactionRequest.getResult() == TransactionResponseStatus.ALLOWED) {
            Limits.downgradManualProcessingLimit(transactionRequest.getAmount());
        } else if (transactionRequest.getResult() == TransactionResponseStatus.PROHIBITED) {
            Limits.upgradeProhibitedLimit(transactionRequest.getAmount());
        }
    }

    private void processProhibited(TransactionRequest transactionRequest) {
        if (transactionRequest.getResult() == TransactionResponseStatus.ALLOWED) {
            Limits.downgradManualProcessingLimit(transactionRequest.getAmount());
            Limits.downgradProhibitedLimit(transactionRequest.getAmount());
        } else if (transactionRequest.getResult() == TransactionResponseStatus.MANUAL_PROCESSING) {
            Limits.downgradProhibitedLimit(transactionRequest.getAmount());
        }
    }
}
