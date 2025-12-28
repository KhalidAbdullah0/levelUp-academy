package com.levelup.levelup_academy;

import com.levelup.levelup_academy.Controller.BookingController;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.BookingService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetAllBookings() throws Exception {
        User user = new User();
        user.setId(5);
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(user, null));

        Mockito.when(bookingService.getMyBookings(5)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/booking/get-all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelBooking() throws Exception {
        User user = new User();
        user.setId(3);
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(user, null));

        Mockito.doNothing().when(bookingService).cancelPendingBooking(3, 9);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/v1/booking/cancel/9")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Booking cancelled successfully.\"}"));
    }
}
