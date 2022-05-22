package antifraud.persistence;

import antifraud.model.cardnumber.CardNumber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardNumberRepository extends CrudRepository<CardNumber, Long> {

    List<CardNumber> findAll();

    boolean existsByNumber(String number);

    CardNumber findByNumber(String number);

    CardNumber deleteByNumber(String number);

}
