package antifraud.persistence;

import antifraud.model.ip.IPAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpAddressRepository extends CrudRepository<IPAddress, Long> {

    List<IPAddress> findAll();

    boolean existsByIp(String ip);

    IPAddress findByIp(String ip);

}
