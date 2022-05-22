package antifraud.model.ip;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity(name = "ip_address")
public class IPAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    @NotNull(message = "IP adress cannot be null")
    private String ip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IPAddress ipAdress1 = (IPAddress) o;
        return Objects.equals(id, ipAdress1.id) && Objects.equals(ip, ipAdress1.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ip);
    }
}
