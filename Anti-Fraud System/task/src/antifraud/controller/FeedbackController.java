package antifraud.controller;

import antifraud.model.feedback.FeedbackException;
import antifraud.model.feedback.FeedbackRequest;
import antifraud.model.feedback.FeedbackResponse;
import antifraud.model.transaction.TransactionProcessResult;
import antifraud.model.transaction.TransactionRequest;
import antifraud.model.transaction.TransactionResponse;
import antifraud.persistence.TransactionRequestRepository;
import antifraud.service.FeedbackService;
import antifraud.utils.CardNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FeedbackController {

    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PutMapping("/api/antifraud/transaction")
    public ResponseEntity<FeedbackResponse> putFeedback(@RequestBody FeedbackRequest feedback) {
        if (!feedbackService.transactionExists(feedback.getTransactionId())) {
            return ResponseEntity.notFound().build();
        }

        if (feedbackService.isTransactionSet(feedback.getTransactionId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        try {
            feedbackService.processFeedback(feedback);
        } catch (FeedbackException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        TransactionRequest transaction = feedbackService.getTransaction(feedback.getTransactionId());
        return ResponseEntity.ok(new FeedbackResponse(transaction));
    }

    @GetMapping("/api/antifraud/history")
    public ResponseEntity<List<FeedbackResponse>> getHistory() {
        List<FeedbackResponse> history = feedbackService.getAllTransactions().stream()
                .map(transaction -> new FeedbackResponse(transaction))
                .sorted(Comparator.comparing(FeedbackResponse::getTransactionId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(history);
    }

    @GetMapping("/api/antifraud/history/{number}")
    public ResponseEntity<List<FeedbackResponse>> getHistoryByNumber(@PathVariable String number) {
        if (!CardNumberValidator.validateNumber(number)) {
            return ResponseEntity.badRequest().build();
        }

        if (!feedbackService.existsByNumber(number)) {
            return ResponseEntity.notFound().build();
        }

        List<FeedbackResponse> history = feedbackService.getAllByCardNumber(number).stream()
                .map(transaction -> new FeedbackResponse(transaction))
                .collect(Collectors.toList());
        return ResponseEntity.ok(history);
    }
}
