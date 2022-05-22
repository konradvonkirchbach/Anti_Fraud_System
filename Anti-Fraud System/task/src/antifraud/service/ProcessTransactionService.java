package antifraud.service;

import antifraud.model.transaction.*;
import antifraud.persistence.TransactionRequestRepository;
import antifraud.utils.CardNumberValidator;
import antifraud.utils.IpAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProcessTransactionService {

    private IpAddressService ipAddressService;

    private CardNumberService cardNumberService;

    private TransactionRequestRepository transactionRequestRepository;

    @Autowired
    public ProcessTransactionService(IpAddressService ipAddressService,
                                     CardNumberService cardNumberService,
                                     TransactionRequestRepository transactionRequestRepository) {
        this.ipAddressService = ipAddressService;
        this.cardNumberService = cardNumberService;
        this.transactionRequestRepository = transactionRequestRepository;
    }

    private final Predicate<TransactionRequest> IS_NEGATIVE = transaction -> transaction.getAmount() <= 0;

    private final Predicate<TransactionRequest> IS_INVALID_IP = transaction -> !IpAddressValidator.validate(transaction.getIp());

    private final Predicate<TransactionRequest> IS_SMALLER_THAN_MANUAL_PROCESSING = transaction -> transaction.getAmount() <= Limits.getLimitManualProcessing();

    private final Predicate<TransactionRequest> IS_SMALLER_PROHIBITED = transaction -> transaction.getAmount() <= Limits.getLimitProhibited();

    private final Predicate<TransactionRequest> IS_IP_ON_BLACKLIST = transaction -> ipAddressService.ipAddressExists(transaction.getIp());

    private final Predicate<TransactionRequest> IS_CARD_ON_BLACKLIST = transaction -> cardNumberService.numberExists(transaction.getNumber());

    private final Predicate<TransactionRequest> IS_INVALID_CARD_NUMBER = transaction -> !CardNumberValidator.validateNumber(transaction.getNumber());

    public TransactionProcessResult processTransaction(TransactionRequest transactionRequest) {
        // transactionRequestRepository.save(transactionRequest);
        TransactionProcessResult result = new TransactionProcessResult();
        if (IS_IP_ON_BLACKLIST.or(IS_INVALID_IP).test(transactionRequest)) {
            result.setStatus(TransactionResponseStatus.PROHIBITED);
            result.getInfos().add(TransactionInfoEnum.IP);
        }
        if (IS_CARD_ON_BLACKLIST.or(IS_INVALID_CARD_NUMBER).test(transactionRequest)) {
            result.setStatus(TransactionResponseStatus.PROHIBITED);
            result.getInfos().add(TransactionInfoEnum.NUMBER);
        }
        if (isMoreThanTwoIpsInLastHour(transactionRequest)) {
            result.setStatus(TransactionResponseStatus.PROHIBITED);
            result.getInfos().add(TransactionInfoEnum.IP_CORRELATION);
        }
        if (isMoreThanTwoRegionsInLastHour(transactionRequest)) {
            result.setStatus(TransactionResponseStatus.PROHIBITED);
            result.getInfos().add(TransactionInfoEnum.REGION_CORRELATION);
        }
        if (IS_NEGATIVE.test(transactionRequest)) {
            result.setStatus(TransactionResponseStatus.BAD_REQUEST);
            result.getInfos().add(TransactionInfoEnum.AMOUNT);
        } else if (!IS_SMALLER_PROHIBITED.test(transactionRequest)) {
            result.setStatus(TransactionResponseStatus.PROHIBITED);
            result.getInfos().add(TransactionInfoEnum.AMOUNT);
        }

        if (result.getStatus() != null) {
            if (result.getStatus() != TransactionResponseStatus.BAD_REQUEST) {
                transactionRequest.setResult(result.getStatus());
                transactionRequestRepository.save(transactionRequest);
            }
            return result;
        }

        if (IS_SMALLER_THAN_MANUAL_PROCESSING.test(transactionRequest)) {
            result.setStatus(TransactionResponseStatus.ALLOWED);
            result.getInfos().add(TransactionInfoEnum.NONE);
        } else if (IS_SMALLER_PROHIBITED.test(transactionRequest)) {
            result.setStatus(TransactionResponseStatus.MANUAL_PROCESSING);
            result.getInfos().add(TransactionInfoEnum.AMOUNT);
        }
        if (isTwoIpsInLastHour(transactionRequest)) {
            result.setStatus(TransactionResponseStatus.MANUAL_PROCESSING);
            result.getInfos().add(TransactionInfoEnum.IP_CORRELATION);
        }
        if (isTwoRegionsInLastHour(transactionRequest)) {
            result.setStatus(TransactionResponseStatus.MANUAL_PROCESSING);
            result.getInfos().add(TransactionInfoEnum.REGION_CORRELATION);
        }

        transactionRequest.setResult(result.getStatus());
        transactionRequestRepository.save(transactionRequest);
        return result;
    }

    private boolean isMoreThanTwoRegionsInLastHour(TransactionRequest transactionRequest) {
        var suspiciousTransactions = getTransactionsForNumberInLastHour(transactionRequest).stream()
                .map(TransactionRequest::getRegion)
                .collect(Collectors.toSet());
        suspiciousTransactions.remove(transactionRequest.getRegion());
        return suspiciousTransactions.size() > 2;
    }

    private boolean isTwoRegionsInLastHour(TransactionRequest request) {
        var suspiciousTransactions = getTransactionsForNumberInLastHour(request).stream()
                .map(TransactionRequest::getRegion)
                .collect(Collectors.toSet());
        suspiciousTransactions.remove(request.getRegion());
        return suspiciousTransactions.size() == 2;
    }

    private List<TransactionRequest> getTransactionsForNumberInLastHour(TransactionRequest request) {
        return transactionRequestRepository.findAllByNumberAndDateAfterAndDateBefore(request.getNumber(), request.getDate().minusHours(1), request.getDate());
    }

    private boolean isMoreThanTwoIpsInLastHour(TransactionRequest request) {
        var suspiciousTransactions = getTransactionsForNumberInLastHour(request).stream()
                .map(TransactionRequest::getIp)
                .collect(Collectors.toSet());
        suspiciousTransactions.remove(request.getIp());
        return suspiciousTransactions.size() > 2;
    }

    private boolean isTwoIpsInLastHour(TransactionRequest request) {
        var suspiciousTransactions = getTransactionsForNumberInLastHour(request).stream()
                .map(TransactionRequest::getIp)
                .collect(Collectors.toSet());
        suspiciousTransactions.remove(request.getIp());
        return suspiciousTransactions.size() == 2;
    }

}
