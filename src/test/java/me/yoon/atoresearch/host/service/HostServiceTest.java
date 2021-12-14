package me.yoon.atoresearch.host.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import me.yoon.atoresearch.error.ErrorCode;
import me.yoon.atoresearch.error.exception.DuplicatedHostException;
import me.yoon.atoresearch.error.exception.ExceedMaxLimitException;
import me.yoon.atoresearch.error.exception.HostNotFoundException;
import me.yoon.atoresearch.host.Host;
import me.yoon.atoresearch.host.HostRepository;
import me.yoon.atoresearch.host.dto.HostRequest;
import me.yoon.atoresearch.host.dto.HostResponse;
import me.yoon.atoresearch.host.service.HostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HostServiceTest {

    @InjectMocks
    HostService hostService;

    @Mock
    HostRepository hostRepository;

    HostRequest dto;

    @BeforeEach
    void setUp() {
        String name = "atto-research.com";
        String address = "210.114.6.150";
        this.dto = new HostRequest(name, address);
    }

    @Test
    @DisplayName("호스트 조회 성공 테스트")
    void getHost_success() {
        //given
        given(this.hostRepository.findById(anyInt()))
            .willReturn(Optional.of(this.dto.toEntity()));

        //when
        HostResponse response = this.hostService.getHost(1);

        //then
        assertThat(this.dto.getName()).isEqualTo(response.getName());
        assertThat(this.dto.getAddress()).isEqualTo(response.getAddress());
    }

    @Test
    @DisplayName("호스트 조회 실패 테스트")
    void getHost_fail() {
        //given
        given(this.hostRepository.findById(anyInt()))
            .willReturn(Optional.empty());

        //when
        HostNotFoundException exception = assertThrows(
            HostNotFoundException.class, () -> this.hostService.getHost(1));

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.HOST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("호스트 등록 성공 테스트")
    void addHost_success() {
        //given
        given(this.hostRepository.count())
            .willReturn(99L);
        given(this.hostRepository.save(any(Host.class)))
            .willReturn(this.dto.toEntity());

        //when
        HostResponse response = this.hostService.addHost(this.dto);

        //then
        assertThat(this.dto.getName()).isEqualTo(response.getName());
        assertThat(this.dto.getAddress()).isEqualTo(response.getAddress());
    }

    @Test
    @DisplayName("호스트 등록 실패 테스트 (호스트명 중복)")
    void addHost_failByHostNameDuplicate() {
        //given
        given(this.hostRepository.existsHostByNameOrAddress(anyString(), anyString()))
            .willReturn(true);

        //when
        DuplicatedHostException exception = assertThrows(
            DuplicatedHostException.class,
            () -> this.hostService.addHost(this.dto));

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.DUPLICATED_HOST.getMessage());
    }

    @Test
    @DisplayName("호스트 등록 실패 테스트 (등록 최대 한도 초과)")
    void addHost_failByExceedMaxLimit() {
        //given
        given(this.hostRepository.count())
            .willReturn(100L);

        //when
        ExceedMaxLimitException exception = assertThrows(
            ExceedMaxLimitException.class,
            () -> this.hostService.addHost(this.dto));

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.EXCEED_MAX_LIMIT.getMessage());
    }

    @Test
    @DisplayName("호스트 수정 성공 테스트")
    void editHost_success() {
        //given
        String name = "google.com";
        String address = "142.250.207.14";
        HostRequest updateDto = buildHostRequest(name, address);
        given(this.hostRepository.findById(anyInt()))
            .willReturn(Optional.of(this.dto.toEntity()));

        //when
        HostResponse response = this.hostService.updateHost(1, updateDto);

        //then
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getAddress()).isEqualTo(address);
    }

    @Test
    @DisplayName("호스트 삭제 성공 테스트")
    void deleteHost_success() {
        //given
        int id = 1;
        given(this.hostRepository.findById(id))
            .willReturn(Optional.of(this.dto.toEntity()));

        //when
        Integer deletedId = this.hostService.deleteHost(id);

        //then
        assertThat(deletedId).isEqualTo(id);
    }

    @Test
    @DisplayName("호스트 삭제 실패 테스트 (등록되지 않은 호스트)")
    void deleteHost_fail() {
        //given

        //when

        //then

    }

    private HostRequest buildHostRequest(String name, String address) {
        return new HostRequest(name, address);
    }

    private HostResponse buildHostResponse(HostRequest dto) {
        return new HostResponse(dto.toEntity());
    }

}