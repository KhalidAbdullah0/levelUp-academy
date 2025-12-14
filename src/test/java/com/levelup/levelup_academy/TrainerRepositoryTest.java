package com.levelup.levelup_academy;

import com.levelup.levelup_academy.Model.Trainer;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerRepositoryTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Test
    void findTrainerById_returnsTrainer() {
        User user = new User();
        user.setUsername("trainer123");
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainer.setUser(user);

        when(trainerRepository.findTrainerById(1)).thenReturn(trainer);

        Trainer found = trainerRepository.findTrainerById(1);
        assertNotNull(found);
        assertEquals("trainer123", found.getUser().getUsername());
    }
}
