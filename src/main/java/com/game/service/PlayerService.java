package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findPlayersByAttributes(String name, String title,
                                                Date after, Date before,
                                                Integer minExperience, Integer maxExperience,
                                                Integer minLevel, Integer maxLevel,
                                                Race race, Profession profession,
                                                Boolean banned,
                                                Integer pageNumber, Integer pageSize, PlayerOrder order) {
        return playerRepository.findAllByAttributes(name, title,
                after, before, minExperience, maxExperience,
                minLevel, maxLevel, race, profession, banned,
                PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName())));
    }

    public Long getCount(String name, String title,
                         Date after, Date before,
                         Integer minExperience, Integer maxExperience,
                         Integer minLevel, Integer maxLevel,
                         Race race, Profession profession,
                         Boolean banned) {

        return (long) playerRepository.findAllByAttributes(name, title,
                after, before, minExperience, maxExperience,
                minLevel, maxLevel, race, profession, banned,
                PageRequest.of(0, Integer.MAX_VALUE)).size();
    }

    public void createPlayer(Player player) {
        playerRepository.save(player);
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    public Optional<Player> updatePlayerById(Long id, Player player) {
        if (player == null) return Optional.empty();

        if (player.getExperience() != null && player.getExperience() != 0) {
            int level = Math.toIntExact((long) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100.0));
            player.setLevel(level);
            player.setUntilNextLevel(50 * (level + 1) * (level + 2) - player.getExperience());
        }

        Optional<Player> optional = playerRepository.findById(id);
        if (optional.isPresent()) {
            Player updatedPlayer = optional.get();
            updatedPlayer.update(player);
            playerRepository.save(updatedPlayer);
        }
        return optional;
    }

    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }

    public boolean existById(Long id) {
        return playerRepository.existsById(id);
    }
}
