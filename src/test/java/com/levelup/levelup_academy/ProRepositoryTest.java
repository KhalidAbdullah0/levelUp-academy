package com.levelup.levelup_academy;

import com.levelup.levelup_academy.Model.Pro;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Repository.ProRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProRepositoryTest {

    @Mock
    private ProRepository proRepository;

    @Test
    void findProById_returnsEntity() {
        Pro pro = new Pro();
        pro.setId(3);
        pro.setCvPath("cv1.pdf");
        when(proRepository.findProById(3)).thenReturn(pro);

        Pro result = proRepository.findProById(3);
        assertNotNull(result);
        assertEquals("cv1.pdf", result.getCvPath());
    }

    @Test
    void findByIsApproved_returnsMatches() {
        Pro pro = new Pro();
        pro.setId(4);
        pro.setIsApproved(false);
        User user = new User();
        user.setId(7);
        pro.setUser(user);

        when(proRepository.findByIsApproved(false)).thenReturn(List.of(pro));

        List<Pro> result = proRepository.findByIsApproved(false);
        assertFalse(result.isEmpty());
        assertEquals(4, result.get(0).getId());
    }
}
