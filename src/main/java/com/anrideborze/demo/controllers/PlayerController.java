package com.anrideborze.demo.controllers;

import com.anrideborze.demo.entities.Player;
import com.anrideborze.demo.repositories.PlayerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        return playerRepository.save(player);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        return playerRepository.findById(id)
                .map(player -> {
                    player.setName(updatedPlayer.getName());
                    player.setCharacterClass(updatedPlayer.getCharacterClass());
                    player.setLevel(updatedPlayer.getLevel());
                    player.setStrength(updatedPlayer.getStrength());
                    player.setDexterity(updatedPlayer.getDexterity());
                    player.setConstitution(updatedPlayer.getConstitution());
                    return playerRepository.save(player);
                })
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerRepository.deleteById(id);
    }
}