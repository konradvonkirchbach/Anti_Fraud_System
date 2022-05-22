package antifraud.controller;

import antifraud.model.ip.DeleteIpAddressResponse;
import antifraud.utils.IpAddressValidator;
import antifraud.model.ip.IPAddress;
import antifraud.service.IpAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IpAddressController {

    private IpAddressService ipAddressService;

    @Autowired
    public IpAddressController(IpAddressService ipAddressService) {
        this.ipAddressService = ipAddressService;
    }

    @PostMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<IPAddress> postIpAddress(@RequestBody IPAddress ipAddress) {
        if (ipAddressService.ipAddressExists(ipAddress.getIp())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (!IpAddressValidator.validate(ipAddress.getIp())) {
            return ResponseEntity.badRequest().build();
        }
        IPAddress savedAddress = ipAddressService.saveIpAddress(ipAddress);
        return ResponseEntity.ok(savedAddress);
    }

    @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
    public ResponseEntity<DeleteIpAddressResponse> deleteIpAddress(@PathVariable String ip) {
        if (!IpAddressValidator.validate(ip)) {
            return ResponseEntity.badRequest().build();
        }
        if (!ipAddressService.ipAddressExists(ip)) {
            return ResponseEntity.notFound().build();
        }
        IPAddress deletedAddress = ipAddressService.delete(ip);
        DeleteIpAddressResponse response = new DeleteIpAddressResponse(deletedAddress);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<List<IPAddress>> getAllSuspiciousIpAddresses() {
        return ResponseEntity.ok(ipAddressService.getAllIpAddresses());
    }
}
