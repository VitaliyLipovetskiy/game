package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class PlayerServiceImpl implements PlayerService{

    private Page<Player> players = Page.empty();

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Page<Player> fetchPlayerList(PageRequest page, Map<String, String> filter) {
        if (filter.size() == 0)
            players = playerRepository.findAll(page);
        else
            players = Page.empty();
        return players;
    }

    @Override
    public long countPlayers() {
        return players.getTotalElements();
    }

    @Override
    public Player findById(Long id) {
        if (id <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent())
            return player.get();
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    @Modifying
    public Player save(Player player) {
        return playerRepository.save(player);
    }


    @Override
    public void delete(Long id) {
        if (id <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!playerRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        playerRepository.deleteById(id);
    }
}
