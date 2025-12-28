package com.levelup.levelup_academy.Repository;

import com.levelup.levelup_academy.Model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Integer> {
    Player findPlayerById(Integer id);
    Player findPlayerByUser_Id(Integer userId);
    
    @Query("SELECT p FROM Player p JOIN FETCH p.user")
    List<Player> findAllWithUser();
}
