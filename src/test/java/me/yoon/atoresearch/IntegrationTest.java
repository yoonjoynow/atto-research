package me.yoon.atoresearch;

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
import me.yoon.atoresearch.host.Host;
import me.yoon.atoresearch.host.HostRepository;
import me.yoon.atoresearch.host.dto.HostRequest;
import me.yoon.atoresearch.host.dto.HostResponse;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class IntegrationTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    HostRepository hostRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(this.context)
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @Test
    @DisplayName("호스트 조회 성공 테스트")
    void getHost_success() throws Exception {
        //given
        Host savedHost = saveHost(buildHostRequest());

        //when
        ResultActions resultActions = requestGetHost(savedHost.getId());

        //then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(savedHost.getName()))
            .andExpect(jsonPath("$.address").value(savedHost.getAddress()));
    }

    @Test
    @DisplayName("호스트 조회 실패 테스트")
    void getHost_fail() throws Exception {
        //given
        Integer id = 2000;
        saveHost(buildHostRequest());

        //when
        ResultActions resultActions = requestGetHost(id);

        //then
        resultActions
            .andExpect(status().isNotFound());
    }

    @Test
    @Disabled
    @DisplayName("호스트 조회 후 최근 조회 시간 확인 테스트")
    void getHost_checkLastAliveTime() throws Exception {
        //given
        Host savedHost = saveHost(buildHostRequest());

        //when
        ResultActions result1 = requestGetHost(savedHost.getId());
        Thread.sleep(1000);
        ResultActions result2 = requestGetHost(savedHost.getId());

        //then
        result2
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("호스트 등록 성공 테스트")
    void addHost_success() throws Exception {
        //given
        HostRequest dto = buildHostRequest();

        //when
        ResultActions resultActions = requestAddHost(dto);

        //then
        resultActions
            .andExpect(status().isCreated())
            .andExpect(jsonPath("name").value(dto.getName()))
            .andExpect(jsonPath("address").value(dto.getAddress()))
            .andExpect(jsonPath("_links.self.href").exists());
    }

    @Test
    @DisplayName("호스트 등록 실패 테스트 (호스트명 중복)")
    void addHost_failByHostNameDuplicate() throws Exception {
        //given
        requestAddHost(buildHostRequest());
        HostRequest dto = buildHostRequest();

        //when
        ResultActions resultActions = requestAddHost(dto);

        //then
        resultActions
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("호스트 수정 성공 테스트")
    void editHost_success() throws Exception {
        //given
        Host savedHost = saveHost(buildHostRequest());
        String name = "google.com";
        String address = "142.250.207.14";
        HostRequest updateDto = buildHostRequest(name, address);

        //when
        ResultActions resultActions = this.mockMvc.perform(put("/api/hosts/{id}", savedHost.getId())
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
        Host savedHost = saveHost(buildHostRequest());

        //when
        ResultActions resultActions = requestDeleteHost(savedHost.getId());

        //then
        resultActions
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("호스트 삭제 실패 테스트")
    void deleteHost_fail() throws Exception {
        //given
        saveHost(buildHostRequest());

        //when
        ResultActions resultActions = requestDeleteHost(100);

        //then
        resultActions
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(ErrorCode.HOST_NOT_FOUND.getMessage()));
    }


    private ResultActions requestGetHost(Integer id) throws Exception {
        return this.mockMvc.perform(get("/api/hosts/{id}", id)
            .accept(MediaTypes.HAL_JSON));
    }

    private ResultActions requestAddHost(HostRequest dto) throws Exception {
        return this.mockMvc.perform(post("/api/hosts")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
            .content(this.objectMapper.writeValueAsString(dto)));
    }

    private ResultActions requestDeleteHost(Integer id) throws Exception {
        return this.mockMvc.perform(delete("/api/hosts/{id}", id)
            .accept(MediaTypes.HAL_JSON));
    }

    private HostRequest buildHostRequest() {
        String name = "atto-research.com";
        String address = "210.114.6.150";
        return new HostRequest(name, address);
    }

    private HostRequest buildHostRequest(String name, String address) {
        return new HostRequest(name, address);
    }

    private Host saveHost(HostRequest dto) {
        return this.hostRepository.save(dto.toEntity());
    }

}
