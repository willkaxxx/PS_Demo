package ua.oleksii.shchetinin.ps.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ua.oleksii.shchetinin.ps.demo.dto.request.RegisterUserDto;
import ua.oleksii.shchetinin.ps.demo.dto.responce.LoginResponseDto;
import ua.oleksii.shchetinin.ps.demo.model.User;
import ua.oleksii.shchetinin.ps.demo.repository.UserRepository;
import ua.oleksii.shchetinin.ps.demo.service.security.JwtService;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepositoryMock;

    @Test
    void testLoginGeneratesValidToken() throws Exception {
        doReturn(Optional.of(User.builder()
                .username("Oleksii")
                .password("{bcrypt}$2a$10$Aoyh8t8osPrkGM4WnVtxMuz0GgG36fx76Q3VKy6BE.TbavXP6m8Cu")
                .email("newEmail@gmail.com")
                .favoritePosts(Set.of("663fb04c5f4ab024e84ed1ad"))
                .subscriptions(Set.of("Oleksii2"))
                .build()))
                .when(userRepositoryMock).findByUsername("Oleksii");
        doReturn(Optional.of(User.builder()
                .username("Oleksii2")
                .password("{bcrypt}$2a$10$Wr1020KEYEKcaPH.SmOguugbEGgjvZrm.f/QYFaZkS5cGaEo0duLu")
                .email("newEmail@gmail.com")
                .subscriptions(Set.of("Oleksii"))
                .build()))
                .when(userRepositoryMock).findByUsername("Oleksii2");

        ResultMatcher resultMatcher = matcher -> {
            LoginResponseDto responseDto = objectMapper.reader().readValue(matcher.getResponse().getContentAsString());
            String token = responseDto.getToken();
            assertThat(jwtService.isTokenValid(token, User.builder().username("Oleksii").build())).isTrue();
        };
        mockMvc.perform(post("/v1/auth/login")
                        .header("authorization", "Basic T2xla3NpaTpwYXNz"))
                .andExpect(status().isOk()).andExpect(resultMatcher);
    }

    @Test
    void testSignupCreatesNewRecord() throws Exception {
        RegisterUserDto requestBody = RegisterUserDto.builder()
                .username("User")
                .password("pass")
                .email("email@gmail.com")
                .build();

        mockMvc.perform(post("/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk()).andExpect(w -> {
                });

        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryMock, times(1)).save(ac.capture());

        assertThat(ac.getValue()).matches(actual -> actual.getUsername().equals("User") &&
                actual.getEmail().equals("email@gmail.com"));
    }
}