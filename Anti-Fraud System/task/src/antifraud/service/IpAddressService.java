package antifraud.service;

import antifraud.model.ip.IPAddress;
import antifraud.persistence.IpAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpAddressService {

    private IpAddressRepository ipAddressRepository;

    @Autowired
    public IpAddressService(IpAddressRepository ipAddressRepository) {
        this.ipAddressRepository = ipAddressRepository;
    }

    public IPAddress saveIpAddress(IPAddress address) {
        return ipAddressRepository.save(address);
    }

    public boolean ipAddressExists(String ipAddress) {
        return ipAddressRepository.existsByIp(ipAddress);
    }

    public List<IPAddress> getAllIpAddresses() {
        return ipAddressRepository.findAll();
    }

    public IPAddress delete(String ipAddress) {
        IPAddress address = ipAddressRepository.findByIp(ipAddress);
        ipAddressRepository.delete(address);
        return address;
    }

}
