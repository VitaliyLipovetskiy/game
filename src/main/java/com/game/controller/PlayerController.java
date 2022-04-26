package com.game.controller;

import com.game.entity.*;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    private Player checkParams(Player player, Map<String,String> params) {
        String name = params.get("name");
        if (name != null) player.setName(name);
        String title = params.get("title");
        if (title != null) player.setTitle(title);
        String race = params.get("race");
        if (race != null) player.setRace(race);
        String profession = params.get("profession");
        if (profession != null) player.setProfession(profession);
        String birthday = params.get("birthday");
        if (birthday != null) player.setBirthday(birthday);
        String banned = params.get("banned");
        if (banned != null) player.setBanned(banned);
        String experience = params.get("experience");
        if (experience != null) player.setExperience(experience);
        return player;
    }

    @RequestMapping(value = "/players", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Player> findAllPlayers(@RequestParam Map<String,String> allParams) {

        // Pagination
        int pageSize = Integer.parseInt(allParams.getOrDefault("pageSize", "3"));
        int pageNumber = Integer.parseInt(allParams.getOrDefault("pageNumber", "0"));
        // Sorting
        String order = allParams.getOrDefault("order", "id").toLowerCase();

        // Filter
        String name = allParams.get("name");
        String title = allParams.get("title");
        String race = allParams.get("race");
        String profession = allParams.get("profession");
        String after = allParams.get("after");
        String before = allParams.get("before");
        String banned = allParams.get("banned");
        String minExperience = allParams.get("minExperience");
        String maxExperience = allParams.get("maxExperience");
        String minLevel = allParams.get("minLevel");
        String maxLevel = allParams.get("maxLevel");
        // [name=name, title=title, race=HUMAN, profession=WARRIOR,
        // after=1650499200000, before=1650499200000, banned=false,
        // minExperience=1000, maxExperience=2000, minLevel=0, maxLevel=2]
        Map<String, String> filter = new HashMap<>();
        if (name != null && !name.isEmpty()) filter.put("name", name);
        if (title != null && !title.isEmpty()) filter.put("title", title);
        if (race != null && !race.isEmpty()) filter.put("race", race);
        if (profession != null && !profession.isEmpty()) filter.put("profession", profession);
        if (after != null && !after.isEmpty()) filter.put("after", after);
        if (before != null && !before.isEmpty()) filter.put("before", before);
        if (banned != null && !banned.isEmpty()) filter.put("banned", banned);
        if (minExperience != null && !minExperience.isEmpty()) filter.put("minExperience", minExperience);
        if (maxExperience != null && !maxExperience.isEmpty()) filter.put("maxExperience", maxExperience);
        if (minLevel != null && !minLevel.isEmpty()) filter.put("minLevel", minLevel);
        if (maxLevel != null && !maxLevel.isEmpty()) filter.put("maxLevel", maxLevel);

        return playerService.fetchPlayerList(
                PageRequest.of(pageNumber, pageSize, Sort.by(order)), filter).toList();
    }

    @RequestMapping(value = "/players/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long count(@RequestParam Map<String,String> allParams) {
        findAllPlayers(allParams);
        return playerService.countPlayers();
    }

    @RequestMapping(value = "/players", method = RequestMethod.POST)
//    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Player createPlayer(@RequestBody Map<String,String> dataParams) {
        Player player = new Player();
        try {
            return playerService.save(checkParams(player, dataParams));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/players/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Player editPlayer(@PathVariable("id") Long id) {
        return playerService.findById(id);
    }

    //, produces = "application/json;charset=UTF-8"
    @RequestMapping(value = "/players/{id}", method = RequestMethod.POST)
//    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Player updatePlayer(@PathVariable("id") Long id,
                               @RequestBody Map<String,String> requestParams) {
        Player player = playerService.findById(id);
        if (requestParams.isEmpty())
            return player;
        else
            return playerService.save(checkParams(player, requestParams));
    }

    @RequestMapping(value = "/players/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public String deletePlayer(@PathVariable("id") Long id) {
        playerService.delete(id);
        return "redirect:/";
    }

}
