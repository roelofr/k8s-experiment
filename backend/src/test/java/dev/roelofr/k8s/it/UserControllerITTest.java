package dev.roelofr.k8s.it;

import dev.roelofr.k8s.dto.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static dev.roelofr.k8s.domain.User.userFromName;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerITTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnEmptyList() throws Exception {
        userRepository.save(userFromName("Alice"));
        userRepository.save(userFromName("Bob"));

        this.mockMvc.perform(get("/user"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(allOf(
                containsString("Alice"),
                containsString("Bob")
            )));
    }

    @Test
    void fullUserFlow() throws Exception {
        userRepository.save(userFromName("Alice"));
        userRepository.save(userFromName("Bob"));

        this.mockMvc.perform(get("/user"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(allOf(
                containsString("Alice"),
                containsString("Bob"),
                not(containsString("Charlie"))
            )));

        this.mockMvc.perform(get("/user/{id}", 3))
            .andDo(print())
            .andExpect(status().isNotFound());

        this.mockMvc.perform(
                post("/user")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Charlie\"}")
            )
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(3),
                jsonPath("$.name").value("Charlie")
            );

        this.mockMvc.perform(get("/user"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(allOf(
                containsString("Alice"),
                containsString("Bob"),
                containsString("Charlie")
            )));

        this.mockMvc.perform(get("/user/{id}", 3))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("Charlie"));

        this.mockMvc.perform(
                put("/user/{id}", 3)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Becky\"}")
            )
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(3),
                jsonPath("$.name").value("Becky")
            );

        this.mockMvc.perform(get("/user/{id}", 3))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("Becky"));

        this.mockMvc.perform(delete("/user/{id}", 3))
            .andDo(print())
            .andExpect(status().isResetContent());

        this.mockMvc.perform(get("/user/{id}", 3))
            .andDo(print())
            .andExpect(status().isNotFound());

        this.mockMvc.perform(get("/user"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(allOf(
                containsString("Alice"),
                containsString("Bob"),
                not(containsString("Charlie")),
                not(containsString("Becky"))
            )));
    }
}
