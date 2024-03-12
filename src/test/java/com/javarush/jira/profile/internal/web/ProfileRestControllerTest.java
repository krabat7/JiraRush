package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ProfileTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetProfile() throws Exception {
        mockMvc.perform(get("/api/profile")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "admin"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.mailNotifications").isArray())
                .andExpect(jsonPath("$.mailNotifications[0]").value("overdue"))
                .andExpect(jsonPath("$.contacts").isArray())
                .andExpect(jsonPath("$.contacts[0].code").value("github"))
                .andExpect(jsonPath("$.contacts[0].value").value("adminGitHub"));
    }

    @Test
    void testGetProfile_SuccessPath() throws Exception {
        mockMvc.perform(get("/api/profile")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "admin"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.mailNotifications").isArray())
                .andExpect(jsonPath("$.mailNotifications[0]").value("one_day_before_deadline"))
                .andExpect(jsonPath("$.contacts").isArray())
                .andExpect(jsonPath("$.contacts[0].code").value("github"))
                .andExpect(jsonPath("$.contacts[0].value").value("adminGitHub"));
    }

    @Test
    void testGetProfile_UnsuccessPath() throws Exception {
        mockMvc.perform(get("/api/profile")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("guest@gmail.com", "guest"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


}