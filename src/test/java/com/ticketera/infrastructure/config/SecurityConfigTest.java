package com.ticketera.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "security.jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970",
    "security.jwt.expiration-ms=86400000"
})
@DisplayName("Security Config")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String registerAndGetToken(String email) throws Exception {
        String body = """
            {"email": "%s", "fullName": "Admin", "password": "secret123"}""".formatted(email);
        String resp = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(resp).get("token").asText();
    }

    @Test
    @DisplayName("GET /api/v1/events is public")
    void getEventsIsPublic() throws Exception {
        mockMvc.perform(get("/api/v1/events"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/cities is public")
    void getCitiesIsPublic() throws Exception {
        mockMvc.perform(get("/api/v1/cities"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /healthcheck is public")
    void healthcheckIsPublic() throws Exception {
        mockMvc.perform(get("/healthcheck"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/orders is public")
    void postOrderIsPublic() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                .contentType("application/json")
                .content("{\"eventId\":1,\"quantity\":1}"))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /api/v1/auth/register returns 201 with token")
    void registerReturns201() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"email": "user@example.com", "fullName": "Jane", "password": "secret123"}
                    """))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login rejects wrong credentials with 401")
    void loginRejectsWrongCredentials() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"email": "nobody@example.com", "password": "wrongpass"}
                    """))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/v1/events returns 403 without token")
    void postEventWithoutTokenReturns403() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                .contentType("application/json")
                .content("{}"))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/v1/events is allowed with a valid token")
    void postEventWithValidTokenAllowed() throws Exception {
        String token = registerAndGetToken("registered@example.com");
        mockMvc.perform(post("/api/v1/events")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content("{}"))
            .andExpect(status().is4xxClientError());
    }
}
