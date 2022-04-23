package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

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
            players = playerRepository.findAll(specification(filter), page);
        return players;
    }

    @Override
    public long countPlayers() {

//        System.out.println("size = " + players.size());
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

    private Specification<Player> specification(Map<String, String> filter) {
        Specification<Player> specifications = new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> criteria = new ArrayList<>();

                if (filter.get("name") != null)
                    criteria.add(cb.like(root.get("name"), "%" + filter.get("name") + "%"));

                if (filter.get("title") != null)
                    criteria.add(cb.like(root.get("title"), "%" + filter.get("title") + "%"));

                if (filter.get("after") != null)
                    criteria.add(cb.greaterThanOrEqualTo(root.get("birthday"), new Date(Long.parseLong(filter.get("after")))));
                if (filter.get("before") != null)
                    criteria.add(cb.lessThanOrEqualTo(root.get("birthday"), new Date(Long.parseLong(filter.get("before")))));

                if (filter.get("minExperience") != null)
                    criteria.add(cb.ge(root.get("experience").as(Integer.class), Integer.parseInt(filter.get("minExperience"))));

                if (filter.get("maxExperience") != null)
                    criteria.add(cb.le(root.get("experience").as(Integer.class), Integer.parseInt(filter.get("maxExperience"))));

                if (filter.get("minLevel") != null)
                    criteria.add(cb.ge(root.get("level").as(Integer.class), Integer.parseInt(filter.get("minLevel"))));

                if (filter.get("maxLevel") != null)
                    criteria.add(cb.le(root.get("level").as(Integer.class), Integer.parseInt(filter.get("maxLevel"))));

                if (filter.get("race") != null)
                    criteria.add(cb.equal(root.get("race").as(Race.class), Race.valueOf(filter.get("race"))));

                if (filter.get("profession") != null)
                    criteria.add(cb.equal(root.get("profession").as(Profession.class), Profession.valueOf(filter.get("profession"))));

                if (filter.get("banned") != null)
                    criteria.add(cb.equal(root.get("banned").as(Boolean.class), Boolean.valueOf(filter.get("banned"))));

                return cb.and(criteria.toArray(new Predicate[0]));

            }
        };
        return specifications;
    }


}
