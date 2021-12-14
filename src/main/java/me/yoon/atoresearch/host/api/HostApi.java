package me.yoon.atoresearch.host.api;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import me.yoon.atoresearch.host.service.HostService;
import me.yoon.atoresearch.host.dto.HostRequest;
import me.yoon.atoresearch.host.dto.HostResponse;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping(value = "/api/hosts", produces = MediaTypes.HAL_JSON_VALUE)
@Controller
public class HostApi {

    private final HostService hostService;

    @GetMapping("/{id}")
    public ResponseEntity<HostResponse> getHost(@PathVariable Integer id) {
        HostResponse model = this.hostService.getHost(id);
        model.toSelfModel(id);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<HostResponse> addHost(@RequestBody HostRequest dto) {
        validInetAddress(dto);
        HostResponse model = this.hostService.addHost(dto);
        model.toSelfModel(dto);
        return ResponseEntity.created(getSelfUri(model)).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HostResponse> editHost(@PathVariable Integer id, @RequestBody HostRequest dto) {
        validInetAddress(dto);
        HostResponse model = this.hostService.updateHost(id, dto);
        model.toSelfModel(dto);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHost(@PathVariable Integer id) {
        this.hostService.deleteHost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void validInetAddress(HostRequest dto) {
        try {
            InetAddress.getByName(dto.getName()).isReachable(1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private URI getSelfUri(HostResponse model) {
        return model.getRequiredLink(IanaLinkRelations.SELF).toUri();
    }

}
