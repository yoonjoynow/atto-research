package me.yoon.atoresearch.host.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yoon.atoresearch.host.Host;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HostRequest {

    private String name;

    private String address;

    @Builder
    public HostRequest(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Host toEntity() {
        return new Host(this.name, this.address);
    }
}
