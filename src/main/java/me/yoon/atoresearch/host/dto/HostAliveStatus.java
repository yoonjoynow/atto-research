package me.yoon.atoresearch.host.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import me.yoon.atoresearch.host.Host;

@Getter
public class HostAliveStatus {

    private String name;
    private String address;
    private LocalDateTime lastAliveDateTime;
    private boolean alive;

    public HostAliveStatus(Host host) {
        this.name = host.getName();
        this.address = host.getAddress();
        this.lastAliveDateTime = host.getLastAliveDateTime();
    }

    public void updateAlive(boolean alive) {
        this.alive = alive;
    }
}
