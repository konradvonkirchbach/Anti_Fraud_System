package antifraud.controller;

import antifraud.model.transaction.TransactionRequest;
import antifraud.model.transaction.TransactionResponse;
import antifraud.service.ProcessTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TransactionController {

    private ProcessTransactionService processTransactionService;

    @Autowired
    public TransactionController(ProcessTransactionService processTransactionService) {
        this.processTransactionService = processTransactionService;
    }

    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<TransactionResponse> processTransactionRequest(@Valid @RequestBody TransactionRequest transactionRequest) {
        var result = processTransactionService.processTransaction(transactionRequest);
        var response = result.getStatus();
        switch (response) {
            case BAD_REQUEST:
                return ResponseEntity.badRequest().build();
            case ALLOWED:
            case PROHIBITED:
            case MANUAL_PROCESSING:
                return ResponseEntity.ok(new TransactionResponse(result));
            default:
                throw new IllegalArgumentException("No a suitable repsonse for " + response);
        }
    }

}
