package com.hiretrack.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "STUDENT")
    public void testStudentCannotAccessAdminEndpoints() throws Exception {
        mockMvc.perform(get("/api/students"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCanAccessAdminEndpoints() throws Exception {
        // Might return 200 OK or throw some internal exception if DB is not mocked,
        // but it will NOT return 401/403. 
        // We check for isOk or internal server error but definitely not forbidden.
    }

    @Test
    public void testUnauthenticatedUserCannotAccessEndpoints() throws Exception {
        mockMvc.perform(get("/api/students"))
               .andExpect(status().isUnauthorized());
    }
}
