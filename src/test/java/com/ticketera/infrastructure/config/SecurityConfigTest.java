package com.ticketera.infrastructure.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "admin.username=admin",
    "admin.password=testpass",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DisplayName("Security Config")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private String basicAuth(String user, String pass) {
        return "Basic " + java.util.Base64.getEncoder()
            .encodeToString((user + ":" + pass).getBytes());
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
    @DisplayName("POST /api/v1/events returns 401 without credentials")
    void postEventWithoutCredentialsReturns401() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                .contentType("application/json")
                .content("{}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/v1/events returns 401 with wrong credentials")
    void postEventWithWrongCredentialsReturns401() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                .header("Authorization", basicAuth("admin", "wrongpass"))
                .contentType("application/json")
                .content("{}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/v1/events returns 400 with correct credentials")
    void postEventWithCorrectCredentialsReturns400() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                .header("Authorization", basicAuth("admin", "testpass"))
                .contentType("application/json")
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/v1/events/1 returns 401 without credentials")
    void putEventWithoutCredentialsReturns401() throws Exception {
        mockMvc.perform(put("/api/v1/events/1")
                .contentType("application/json")
                .content("{}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("DELETE /api/v1/events/1 returns 401 without credentials")
    void deleteEventWithoutCredentialsReturns401() throws Exception {
        mockMvc.perform(delete("/api/v1/events/1"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/v1/cities returns 401 without credentials")
    void postCityWithoutCredentialsReturns401() throws Exception {
        mockMvc.perform(post("/api/v1/cities")
                .contentType("application/json")
                .content("{}"))
            .andExpect(status().isUnauthorized());
    }
}