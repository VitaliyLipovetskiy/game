package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface PlayerService {

    Page<Player> fetchPlayerList(PageRequest page, Map<String, String> filter);

    long countPlayers();

    Player findById(Long id);

    Player save(Player player);

    void delete(Long id);
}
