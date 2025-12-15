package com.levelup.levelup_academy.Controller;

import com.levelup.levelup_academy.Api.ApiResponse;
import com.levelup.levelup_academy.DTO.PlayerDTO;
import com.levelup.levelup_academy.DTOOut.PlayerDTOOut;
import com.levelup.levelup_academy.DTOOut.PlayerRegistrationResponse;
import com.levelup.levelup_academy.Model.Player;
import com.levelup.levelup_academy.Model.StatisticPlayer;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.PlayerService;
import com.levelup.levelup_academy.Service.StatisticPlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;
    private final StatisticPlayerService statisticPlayerService;


     //GET
    @GetMapping("/get")
    public ResponseEntity getAllPlayers(@AuthenticationPrincipal User user){
        // Check if user is admin (no moderator) or moderator
        if (user.getRole().equals("ADMIN")) {
            // Admin can see all players without moderator filter
            return ResponseEntity.status(200).body(playerService.getAllPlayersForAdmin());
        } else {
            // Moderator needs their moderator ID
            return ResponseEntity.status(200).body(playerService.getAllPlayers(user.getModerator().getId()));
        }
    }

    //get player by moderator
    @GetMapping("/get-player/{playerId}")
    public ResponseEntity getPlayer(@AuthenticationPrincipal User moderatorId,@PathVariable Integer playerId){
        return ResponseEntity.status(200).body(playerService.getPlayer(moderatorId.getModerator().getId(), playerId));
    }

    //Register
    @PostMapping("/register")
    public ResponseEntity registerPlayer(@RequestBody @Valid PlayerDTO playerDTO){
        PlayerRegistrationResponse response = playerService.registerPlayer(playerDTO);
        return ResponseEntity.status(201).body(response);
    }
    @PutMapping("/edit")
    public ResponseEntity edit(@AuthenticationPrincipal User playerId, @RequestBody PlayerDTO playerDTO) {
        playerService.updatePlayer(playerId.getId(), playerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Player updated successfully"));
    }

    // Delete player by ID
    @DeleteMapping("/delete")
    public ResponseEntity delete(@AuthenticationPrincipal User playerId) {
        playerService.deletePlayer(playerId.getId());
        return ResponseEntity.ok(new ApiResponse("Player deleted successfully"));
    }

    @GetMapping("/player")
    public ResponseEntity<StatisticPlayer> getPlayerStatistics(@AuthenticationPrincipal User playerId) {
        return ResponseEntity.ok(playerService.getMyStatisticsByPlayerId(playerId.getId()));
    }

}
