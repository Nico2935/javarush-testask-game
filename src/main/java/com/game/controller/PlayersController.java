package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.service.PlayerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@Validated
@RequestMapping("/rest/players")
public class PlayersController {


    private final PlayerService playerService;
    private final PlayerValidator playerValidator;

    @Autowired
    public PlayersController(PlayerService playerService, PlayerValidator playerValidator) {
        this.playerService = playerService;
        this.playerValidator = playerValidator;
    }

    @GetMapping()
    public ResponseEntity<List<Player>> getPlayers(@RequestParam(name = "name", required = false) String name,
                                                   @RequestParam(name = "title", required = false) String title,
                                                   @RequestParam(name = "race", required = false) Race race,
                                                   @RequestParam(name = "profession", required = false) Profession prof,
                                                   @RequestParam(name = "after", required = false) Long after,
                                                   @RequestParam(name = "before", required = false) Long before,
                                                   @RequestParam(name = "banned", required = false) Boolean banned,
                                                   @RequestParam(name = "minExperience", required = false) Integer minExp,
                                                   @RequestParam(name = "maxExperience", required = false) Integer maxExp,
                                                   @RequestParam(name = "minLevel", required = false) Integer minLevel,
                                                   @RequestParam(name = "maxLevel", required = false) Integer maxLevel,
                                                   @RequestParam(name = "order", defaultValue = "ID") PlayerOrder order,
                                                   @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {
        List<Player> result = playerService.findPlayersByAttributes(name, title,
                convertToDate(after), convertToDate(before), minExp, maxExp,
                minLevel, maxLevel, race, prof, banned, pageNumber, pageSize, order);


        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getPlayersCount(@RequestParam(name = "name", required = false) String name,
                                                @RequestParam(name = "title", required = false) String title,
                                                @RequestParam(name = "race", required = false) Race race,
                                                @RequestParam(name = "profession", required = false) Profession profession,
                                                @RequestParam(name = "after", required = false) Long after,
                                                @RequestParam(name = "before", required = false) Long before,
                                                @RequestParam(name = "banned", required = false) Boolean banned,
                                                @RequestParam(name = "minExperience", required = false) Integer minExperience,
                                                @RequestParam(name = "maxExperience", required = false) Integer maxExperience,
                                                @RequestParam(name = "minLevel", required = false) Integer minLevel,
                                                @RequestParam(name = "maxLevel", required = false) Integer maxLevel,
                                                @RequestParam(name = "order", defaultValue = "ID") PlayerOrder order,
                                                @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                                @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {

        return new ResponseEntity<>(playerService.getCount(name, title,
                convertToDate(after), convertToDate(before), minExperience, maxExperience,
                minLevel, maxLevel, race, profession, banned), HttpStatus.OK);

    }

    @PostMapping()
    public ResponseEntity<Player> createPlayer(@Validated @RequestBody Player player, BindingResult bindingResult) {

        playerValidator.validate(player, bindingResult);

        if (bindingResult.hasErrors())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        playerService.createPlayer(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") long id) {
        Player player = playerService.getPlayerById(id);
        if (id <= 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (!playerService.existById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (player == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") long id, @Validated @RequestBody Player player,
                                               BindingResult bindingResult) {
        if (id <= 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else if (!playerService.existById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Player> updatedPlayer = playerService.updatePlayerById(id, player);
        if (updatedPlayer.isPresent()) {
            playerValidator.validate(updatedPlayer.get(), bindingResult);
            if (bindingResult.hasErrors())
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return updatedPlayer.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(()
                -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable("id") Long id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.existById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Player player = playerService.getPlayerById(id);
        playerService.deleteById(id);
        return new ResponseEntity<>(player, HttpStatus.OK);

    }

    public Date convertToDate(Long l) {
        if (l != null) {
            return new Date(l);
        } else {
            return null;
        }
    }

}
