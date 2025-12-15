package com.levelup.levelup_academy.Service;

import com.levelup.levelup_academy.Api.ApiException;
import com.levelup.levelup_academy.Model.Booking;
import com.levelup.levelup_academy.Model.Session;
import com.levelup.levelup_academy.Model.Subscription;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Repository.AuthRepository;
import com.levelup.levelup_academy.Repository.BookingRepository;
import com.levelup.levelup_academy.Repository.SessionRepository;
import com.levelup.levelup_academy.Repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.levelup.levelup_academy.DTOOut.BookingWithDetailsDTO;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SessionRepository sessionRepository;
    private final AuthRepository authRepository;
    private final SubscriptionRepository subscriptionRepository;


    public List<Booking> getMyBookings(Integer userId) {
        User user = authRepository.findUserById(userId);
        if(user == null) throw new ApiException("User not found");

        return bookingRepository.findMyBookings(userId);
    }

    public void addBooking(Integer userId, Integer sessionId, Integer subscriptionId){
        User user = authRepository.findUserById(userId);
        if(user == null) throw new ApiException("User not found");

        Session session = sessionRepository.findSessionById(sessionId);
        if(session == null) throw new ApiException("Session not found");

        if (session.getAvailableSets() <= 0) {
            throw new ApiException("No available seats for this session.");
        }


        List<Booking> existingBookings = bookingRepository.findByUserId(userId);
        for (Booking b : existingBookings) {
            if (b.getSession().getStartDate().isEqual(session.getStartDate()) &&
                    b.getSession().getTime().equals(session.getTime())) {
                throw new ApiException("You already have a booking at this time.");
            }
        }

        Subscription subscription = subscriptionRepository.findSubscriptionById(subscriptionId);
        if(subscription == null) throw new ApiException("Subscription not found");
        if(subscription.getSessionCount() <= 0) throw new ApiException("You have no remaining sessions in your subscription");

        Booking booking = new Booking();
        booking.setBookDate(LocalDateTime.now());
        booking.setUser(user);
        booking.setSession(session);
        booking.setSubscription(subscription);

        session.setAvailableSets(session.getAvailableSets() - 1);
        subscription.setSessionCount(subscription.getSessionCount() - 1);

        authRepository.save(user);
        sessionRepository.save(session);
        bookingRepository.save(booking);
        subscriptionRepository.save(subscription);

    }

    public void checkBookState(Integer userId, Integer bookingId) {
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null) throw new ApiException("Booking not found");

        Session session = sessionRepository.findSessionById(booking.getSession().getId());
        if (session == null) throw new ApiException("Session not found");

        User user = authRepository.findUserById(userId);
        if (user == null) throw new ApiException("User not found");

        if (LocalDateTime.now().isAfter(session.getStartDate())) {
            booking.setStatus("ACTIVE");
            bookingRepository.save(booking);
        }
    }
    public void cancelPendingBooking(Integer userId, Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException("Booking not found"));

        User user = booking.getUser();

        if (!user.getId().equals(userId)) {
            throw new ApiException("You are not authorized to cancel this booking.");
        }

        String role = user.getRole();
        if (!(role.equals("PLAYER") || role.equals("PRO") || role.equals("PARENT"))) {
            throw new ApiException("Only players, pros, or parents can cancel bookings.");
        }

        if (!booking.getStatus().equalsIgnoreCase("PENDING")) {
            throw new ApiException("Only bookings with status PENDING can be cancelled.");
        }

        LocalDateTime sessionStart = booking.getSession().getStartDate();
        LocalDateTime now = LocalDateTime.now();

        if (!now.isBefore(sessionStart.minusHours(1))) {
            throw new ApiException("Cannot cancel booking less than 1 hour before the session.");
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }
    
    // Get all bookings with details for admin dashboard
    public List<BookingWithDetailsDTO> getAllBookingsWithDetails() {
        List<Booking> bookings = bookingRepository.findAllWithDetails();
        return bookings.stream()
            .map(booking -> {
                User user = booking.getUser();
                Subscription subscription = booking.getSubscription();
                Session session = booking.getSession();
                
                return new BookingWithDetailsDTO(
                    booking.getId(),
                    user != null ? user.getId() : null,
                    user != null ? user.getUsername() : "N/A",
                    user != null ? user.getFirstName() : "N/A",
                    user != null ? user.getLastName() : "N/A",
                    user != null ? user.getEmail() : "N/A",
                    user != null ? user.getRole() : "N/A",
                    subscription != null ? subscription.getId() : null,
                    subscription != null ? subscription.getPackageType() : "N/A",
                    subscription != null ? subscription.getPrice() : null,
                    subscription != null ? subscription.getStatus() : "N/A",
                    session != null ? session.getId() : null,
                    session != null ? session.getName() : "N/A",
                    session != null && session.getGame() != null ? session.getGame().getName() : "N/A",
                    session != null ? session.getStartDate() : null,
                    session != null ? session.getEndDate() : null,
                    session != null ? session.getTime() : "N/A",
                    booking.getBookDate(),
                    booking.getStatus()
                );
            })
            .collect(Collectors.toList());
    }

}
