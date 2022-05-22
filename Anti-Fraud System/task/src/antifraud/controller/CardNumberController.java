package antifraud.controller;

import antifraud.model.cardnumber.CardNumber;
import antifraud.model.cardnumber.DeleteCardNumberResponse;
import antifraud.service.CardNumberService;
import antifraud.utils.CardNumberValidator;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardNumberController {

    private CardNumberService cardNumberService;

    @Autowired
    public CardNumberController(CardNumberService cardNumberService) {
        this.cardNumberService = cardNumberService;
    }

    @PostMapping("/api/antifraud/stolencard")
    public ResponseEntity<CardNumber> postCardNumber(@RequestBody CardNumber cardNumber) {
        if (cardNumberService.numberExists(cardNumber.getNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (!CardNumberValidator.validateNumber(cardNumber.getNumber())) {
            return ResponseEntity.badRequest().build();
        }
        CardNumber savedNumber = cardNumberService.saveCardNumber(cardNumber);
        return ResponseEntity.ok(savedNumber);
    }

    @DeleteMapping("/api/antifraud/stolencard/{number}")
    public ResponseEntity<DeleteCardNumberResponse> deleteCardNumber(@PathVariable String number) {
        if (!CardNumberValidator.validateNumber(number)) {
            return ResponseEntity.badRequest().build();
        }
        if (!cardNumberService.numberExists(number)) {
            return ResponseEntity.notFound().build();
        }
        CardNumber deletedNumber = cardNumberService.delete(number);
        DeleteCardNumberResponse response = new DeleteCardNumberResponse(deletedNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/antifraud/stolencard")
    public ResponseEntity<List<CardNumber>> getAllStolenCardNumbers() {
        return ResponseEntity.ok(cardNumberService.getAllCardNumbers());
    }

}
