package com.anrideborze.demo.repositories;

import com.anrideborze.demo.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}