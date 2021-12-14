package me.yoon.atoresearch.host.api;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.yoon.atoresearch.host.Host;
import me.yoon.atoresearch.host.HostRepository;
import me.yoon.atoresearch.host.dto.HostAliveStatus;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping(value = "/api/hosts/alive", produces = MediaTypes.HAL_JSON_VALUE)
@Controller
public class HostAliveApi {

    // service 대체
    private final HostRepository hostRepository;

    @GetMapping
    public ResponseEntity<?> getAliveStatuses() {
        List<Host> hosts = this.hostRepository.findAll();
        List<HostAliveStatus> statuses = checkAllHostAlive(hosts);
        return null;
    }

    private List<HostAliveStatus> checkAllHostAlive(List<Host> hosts) {
        List<HostAliveStatus> statuses = new ArrayList<>();
        for (Host host : hosts) {
            HostAliveStatus status = new HostAliveStatus(host);
            boolean alive = isAlive(status);
            status.updateAlive(alive);
            host.updateAliveTime(); //실패시에도 변경해주어야하나?
            statuses.add(status);
        }

        return statuses;
    }

    private boolean isAlive(HostAliveStatus status) {
        try {
            return InetAddress.getByName(status.getName()).isReachable(2_000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        InetAddress inetAddress = null;
        String name = "naver.com";
        try {
            InetAddress.getByName(name).isReachable(2_000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
