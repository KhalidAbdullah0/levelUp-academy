package com.levelup.levelup_academy;

import com.levelup.levelup_academy.Model.Player;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Repository.PlayerRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerRepositoryTest {

    @Mock
    private PlayerRepository playerRepository;

    @Test
    void findPlayerById_returnsPlayer() {
        User user = new User(null, "player1", "password", "player1@example.com", "John", "Doe", "PLAYER",
                LocalDate.now(), null, null, null, null, null, null, null, null);
        Player player = new Player(null, user, null);

        when(playerRepository.findPlayerById(1)).thenReturn(player);

        Player result = playerRepository.findPlayerById(1);

        assertNotNull(result);
        assertEquals("player1", result.getUser().getUsername());
    }
}
