package me.yoon.atoresearch.host.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.yoon.atoresearch.error.ErrorCode;
import me.yoon.atoresearch.error.ErrorController;
import me.yoon.atoresearch.error.exception.DuplicatedHostException;
import me.yoon.atoresearch.error.exception.ExceedMaxLimitException;
import me.yoon.atoresearch.error.exception.HostNotFoundException;
import me.yoon.atoresearch.error.exception.UnregisteredHostException;
import me.yoon.atoresearch.host.service.HostService;
import me.yoon.atoresearch.host.dto.HostRequest;
import me.yoon.atoresearch.host.dto.HostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(MockitoExtension.class)
class HostApiTest {

    MockMvc mockMvc;

    @InjectMocks
    HostApi hostApi;

    @Mock
    HostService hostService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(this.hostApi)
            .setControllerAdvice(new ErrorController())
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @Test
    @DisplayName("호스트 조회 성공 테스트")
    void getHost_success() throws Exception {
        //given
        HostRequest dto = buildHostRequest();
        given(this.hostService.getHost(anyInt()))
            .willReturn(buildHostResponse(dto));

        //when
        ResultActions resultActions = this.mockMvc.perform(get("/api/hosts/{id}", 0)
            .accept(MediaTypes.HAL_JSON));

        //then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(dto.getName()))
            .andExpect(jsonPath("$.address").value(dto.getAddress()));
    }

    @Test
    @DisplayName("호스트 조회 실패 테스트")
    void getHost_fail() throws Exception {
        //given
        given(this.hostService.getHost(anyInt()))
            .willThrow(HostNotFoundException.class);

        //when
        ResultActions resultActions = this.mockMvc.perform(get("/api/hosts/{id}", 0)
            .accept(MediaTypes.HAL_JSON));

        //then
        resultActions
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(ErrorCode.HOST_NOT_FOUND.getHttpStatus().name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.HOST_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("호스트 등록 성공 테스트")
    void addHost_success() throws Exception {
        //given
        HostRequest dto = buildHostRequest();
        given(this.hostService.addHost(any(HostRequest.class)))
            .willReturn(buildHostResponse(dto));

        //when
        ResultActions resultActions = requestAddHost(dto);

        //then
        resultActions
            .andExpect(status().isCreated())
            .andExpect(jsonPath("name").value(dto.getName()))
            .andExpect(jsonPath("address").value(dto.getAddress()));
    }

    @Test
    @DisplayName("호스트 등록 실패 테스트 (존재하지 않는 도메인)")
    void addHost_fail() throws Exception {
        //given
        HostRequest dto = buildHostRequest();
        given(this.hostService.addHost(any(HostRequest.class)))
            .willThrow(UnregisteredHostException.class);

        //when
        ResultActions resultActions = requestAddHost(dto);

        //then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(ErrorCode.UNREGISTERED_HOST.getHttpStatus().name()));
    }

    @Test
    @DisplayName("호스트 등록 실패 테스트 (호스트명 중복)")
    void addHost_failByHostNameDuplicate() throws Exception {
        //given
        HostRequest dto = buildHostRequest();
        given(this.hostService.addHost(any(HostRequest.class)))
            .willThrow(DuplicatedHostException.class);

        //when
        ResultActions resultActions = requestAddHost(dto);

        //then
        resultActions
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(ErrorCode.DUPLICATED_HOST.getHttpStatus().name()));
    }

    @Test
    @DisplayName("호스트 등록 실패 테스트 (등록 최대 한도 초과)")
    void addHost_failByExceedMaxLimit() throws Exception {
        //given
        HostRequest dto = buildHostRequest();
        given(this.hostService.addHost(any(HostRequest.class)))
            .willThrow(ExceedMaxLimitException.class);

        //when
        ResultActions resultActions = requestAddHost(dto);

        //then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(ErrorCode.EXCEED_MAX_LIMIT.getHttpStatus().name()));
    }

    @Test
    @DisplayName("호스트 수정 성공 테스트")
    void editHost_success() throws Exception {
        //given
        String name = "google.com";
        String address = "142.250.207.14";
        HostRequest updateDto = buildHostRequest(name, address);
        given(this.hostService.updateHost(anyInt(), any(HostRequest.class)))
            .willReturn(buildHostResponse(updateDto));

        //when
        ResultActions resultActions = this.mockMvc.perform(put("/api/hosts/{id}", 0)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
            .content(this.objectMapper.writeValueAsString(updateDto)));

        //then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(updateDto.getName()))
            .andExpect(jsonPath("$.address").value(updateDto.getAddress()));
    }

    @Test
    @DisplayName("호스트 삭제 성공 테스트")
    void deleteHost_success() throws Exception {
        //given
        given(this.hostService.deleteHost(anyInt()))
            .willReturn(1);

        //when
        ResultActions resultActions = this.mockMvc.perform(delete("/api/hosts/{id}", anyInt())
            .accept(MediaTypes.HAL_JSON));

        //then
        resultActions
            .andExpect(status().isNoContent());
    }

    private ResultActions requestAddHost(HostRequest dto) throws Exception {
        return this.mockMvc.perform(post("/api/hosts")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
            .content(this.objectMapper.writeValueAsString(dto)));
    }

    private HostRequest buildHostRequest() {
        String name = "atto-research.com";
        String address = "210.114.6.150";
        return new HostRequest(name, address);
    }

    private HostRequest buildHostRequest(String name, String address) {
        return new HostRequest(name, address);
    }

    private HostResponse buildHostResponse(HostRequest dto) {
        return new HostResponse(dto.toEntity());
    }
}