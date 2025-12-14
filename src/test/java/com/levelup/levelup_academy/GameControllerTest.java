package com.levelup.levelup_academy;

import com.levelup.levelup_academy.Controller.GameController;
import com.levelup.levelup_academy.Model.Game;
import com.levelup.levelup_academy.Model.Moderator;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.GameService;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import com.levelup.levelup_academy.Config.JwtAuthenticationFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
@AutoConfigureMockMvc(addFilters = false)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private User moderatorUser() {
        User user = new User();
        user.setId(1);
        Moderator moderator = new Moderator();
        moderator.setId(1);
        user.setModerator(moderator);
        return user;
    }

    @Test
    void testGetGames() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(moderatorUser(), null));
        Mockito.when(gameService.getAllGames(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/game/get").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddGame() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(moderatorUser(), null));
        Mockito.doNothing().when(gameService).addGame(Mockito.eq(1), Mockito.any(Game.class));

        String payload = """
                {
                  "name": "Team A",
                  "age": 12,
                  "category": "Action"
                }
                """;

        mockMvc.perform(post("/api/v1/game/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Game Added\"}"));
    }
}
