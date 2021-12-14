package me.yoon.atoresearch.host.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.yoon.atoresearch.error.ErrorController;
import me.yoon.atoresearch.host.Host;
import me.yoon.atoresearch.host.HostRepository;
import me.yoon.atoresearch.host.dto.HostRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class HostAliveApiTest {

    MockMvc mockMvc;

    @Autowired
    HostAliveApi hostAliveApi;

    @Autowired
    HostRepository hostRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(this.hostAliveApi)
            .setControllerAdvice(new ErrorController())
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @Test
    @DisplayName("호스트 Alive 상태 확인 성공 테스트")
    void checkAlive_success() throws Exception {
        //given
        saveHosts();

        this.mockMvc.perform(get("/api/hosts/alive")
                .accept(MediaTypes.HAL_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @Rollback(value = false)
    void hello() {
        saveHosts();
    }

    private void saveHosts() {
        this.hostRepository.save(buildAttoRequest().toEntity());
        this.hostRepository.save(buildGoogleRequest().toEntity());
        this.hostRepository.save(buildNaverRequest().toEntity());
    }

    private HostRequest buildAttoRequest() {
        String name = "atto-research.com";
        String address = "210.114.6.150";
        return new HostRequest(name, address);
    }

    private HostRequest buildNaverRequest() {
        String name = "naver.com";
        String address = "223.130.195.95";
        return new HostRequest(name, address);
    }

    private HostRequest buildGoogleRequest() {
        String name = "google.com";
        String address = "142.250.207.14";
        return new HostRequest(name, address);
    }

}