package antifraud.persistence;

import antifraud.model.transaction.TransactionRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRequestRepository extends CrudRepository<TransactionRequest, Long> {

    List<TransactionRequest> findAllByNumberAndDateAfterAndDateBefore(String number, LocalDateTime from, LocalDateTime upTp);

    List<TransactionRequest> findAll();

    boolean existsById(Long transactionId);

    List<TransactionRequest> findAllByNumberOrderById(String number);

    boolean existsByNumber(String number);
}
