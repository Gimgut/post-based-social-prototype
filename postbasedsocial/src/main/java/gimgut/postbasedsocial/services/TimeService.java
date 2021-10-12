package gimgut.postbasedsocial.services;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TimeService {

    private final ZoneId utcZone = ZoneId.of("Etc/GMT");

    public LocalDateTime getUtcNowLDT() {
        return LocalDateTime.now(utcZone);
    }

    public Timestamp getUtcNowTS() {
        return Timestamp.valueOf(LocalDateTime.now(utcZone));
    }

    public ZoneId getUtcZone() {
        return utcZone;
    }
}

