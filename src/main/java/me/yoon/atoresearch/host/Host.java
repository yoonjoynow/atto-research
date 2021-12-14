package me.yoon.atoresearch.host;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yoon.atoresearch.host.dto.HostRequest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
public class Host {

    @Id @GeneratedValue
    @Column(name = "host_id")
    private Integer id;

    @Column(name = "host_name", nullable = false, unique = true)
    private String name;

    @Column(name = "host_adress", nullable = false, unique = true)
    private String address;

    @CreatedDate
    @Column(name = "createdDateTime", updatable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "lastModifiedDateTime")
    private LocalDateTime lastModifiedDateTime;

    @Column(name = "lastAliveDateTime")
    private LocalDateTime lastAliveDateTime;

    public Host(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void update(HostRequest dto) {
        this.name = dto.getName();
        this.address = dto.getAddress();
    }

    public void updateAliveTime() {
        this.lastAliveDateTime = LocalDateTime.now();
    }
}
