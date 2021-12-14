package me.yoon.atoresearch.host.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import lombok.Getter;
import me.yoon.atoresearch.host.Host;
import me.yoon.atoresearch.host.api.HostApi;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class HostResponse extends RepresentationModel<HostResponse> {

    private Integer id;
    private String name;
    private String address;
    private LocalDateTime createdDateTime;
    private LocalDateTime lastModifiedDateTime;
    private LocalDateTime lastAliveDateTime;

    public HostResponse(Host host) {
        if (host.getId() == null) {
            this.id = 0;
        } else {
            this.id = host.getId();
        }
        this.name = host.getName();
        this.address = host.getAddress();
        this.createdDateTime = host.getCreatedDateTime();
        this.lastModifiedDateTime = host.getLastModifiedDateTime();
        this.lastAliveDateTime = host.getLastAliveDateTime();
    }

    public void toSelfModel(HostRequest dto) {
        this.add(linkTo(methodOn(HostApi.class).addHost(dto)).slash(this.getId()).withSelfRel());
    }

    public void toSelfModel(Integer id) {
        this.add(linkTo(methodOn(HostApi.class).getHost(id)).slash(this.getId()).withSelfRel());
    }
}
