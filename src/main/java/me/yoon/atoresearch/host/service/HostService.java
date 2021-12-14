package me.yoon.atoresearch.host.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.yoon.atoresearch.error.ErrorCode;
import me.yoon.atoresearch.error.exception.DuplicatedHostException;
import me.yoon.atoresearch.error.exception.ExceedMaxLimitException;
import me.yoon.atoresearch.error.exception.HostNotFoundException;
import me.yoon.atoresearch.host.Host;
import me.yoon.atoresearch.host.HostRepository;
import me.yoon.atoresearch.host.dto.HostRequest;
import me.yoon.atoresearch.host.dto.HostResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class HostService {

    private static final Integer MAX_SIZE = 100;

    private final HostRepository hostRepository;

    @Transactional(readOnly = true)
    public HostResponse getHost(Integer id) {
        Host host = searchById(id);
        host.updateAliveTime();
        return new HostResponse(host);
    }

    public HostResponse addHost(HostRequest dto) {
        validateHost(dto);
        Host host = this.hostRepository.save(dto.toEntity());
        return new HostResponse(host);
    }

    public HostResponse updateHost(Integer id, HostRequest dto) {
        Host host = searchById(id);
        validateHost(dto);
        host.update(dto);
        return new HostResponse(dto.toEntity());
    }

    public Integer deleteHost(Integer id) {
        Host host = searchHost(this.hostRepository, id);
        this.hostRepository.delete(host);
        return id;
    }

    private Host searchById(Integer id) {
        Optional<Host> optional = this.hostRepository.findById(id);
        optional.orElseThrow(() -> new HostNotFoundException(ErrorCode.HOST_NOT_FOUND.getMessage()));
        return optional.get();
    }

    private void validateHost(HostRequest dto) {
        if (isExceededMaxSize()) {
            throw new ExceedMaxLimitException(ErrorCode.EXCEED_MAX_LIMIT.getMessage());
        }

        if (isDuplicated(dto)) {
            throw new DuplicatedHostException(ErrorCode.DUPLICATED_HOST.getMessage());
        }
    }

    private boolean isExceededMaxSize() {
        return this.hostRepository.count() >= MAX_SIZE;
    }

    private boolean isDuplicated(HostRequest dto) {
        return this.hostRepository.existsHostByNameOrAddress(dto.getName(), dto.getAddress());
    }

    private Host searchHost(HostRepository hostRepository, Integer id) {
        Optional<Host> optional = hostRepository.findById(id);
        optional.orElseThrow(() -> new HostNotFoundException(ErrorCode.HOST_NOT_FOUND.getMessage()));
        return optional.get();
    }

}
