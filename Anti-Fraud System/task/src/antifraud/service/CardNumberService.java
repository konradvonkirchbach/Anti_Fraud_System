package antifraud.service;

import antifraud.model.cardnumber.CardNumber;
import antifraud.persistence.CardNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardNumberService {

    private CardNumberRepository cardNumberRepository;

    @Autowired
    public CardNumberService(CardNumberRepository cardNumberRepository) {
        this.cardNumberRepository = cardNumberRepository;
    }

    public CardNumber saveCardNumber(CardNumber cardNumber) {
        return cardNumberRepository.save(cardNumber);
    }

    public boolean numberExists(String number) {
        return cardNumberRepository.existsByNumber(number);
    }

    public List<CardNumber> getAllCardNumbers() {
        return cardNumberRepository.findAll();
    }

    public CardNumber delete(String number) {
        CardNumber cardNumber = cardNumberRepository.findByNumber(number);
        cardNumberRepository.delete(cardNumber);
        return cardNumber;
    }

}