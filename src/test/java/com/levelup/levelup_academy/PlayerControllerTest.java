package com.levelup.levelup_academy;

import com.levelup.levelup_academy.Controller.PlayerController;
import com.levelup.levelup_academy.DTOOut.PlayerDTOOut;
import com.levelup.levelup_academy.Model.Moderator;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.PlayerService;
import com.levelup.levelup_academy.Service.StatisticPlayerService;
import java.util.List;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private StatisticPlayerService statisticPlayerService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private User createModeratorUser(Integer id) {
        User user = new User();
        user.setId(id);
        Moderator moderator = new Moderator();
        moderator.setId(id);
        user.setModerator(moderator);
        return user;
    }

    @Test
    void testGetAllPlayers() throws Exception {
        User moderatorUser = createModeratorUser(1);
        PlayerDTOOut mockPlayer = new PlayerDTOOut(
                "Abdullah",
                "Abdullah",
                "Ali",
                "Abdullah@gmail.com",
                null
        );
        Mockito.when(playerService.getAllPlayers(1)).thenReturn(List.of(mockPlayer));

        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(moderatorUser, null));

        mockMvc.perform(get("/api/v1/player/get").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Abdullah"));
    }

    @Test
    void testDeletePlayer() throws Exception {
        User playerUser = new User();
        playerUser.setId(2);

        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(playerUser, null));
        Mockito.doNothing().when(playerService).deletePlayer(2);

        mockMvc.perform(delete("/api/v1/player/delete"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Player deleted successfully\"}"));
    }
}
