package com.game.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // ID игрока

    @Column(nullable = false, length = 12)
    private String name;            // Имя персонажа (до 12 знаков включительно)

    @Column(nullable = false, length = 30)
    private String title;           // Титул персонажа (до 30 знаков включительно)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Race race;              // Расса персонажа

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Profession profession;  // Профессия персонажа

    @Column(nullable = false)
    private Integer experience;     // Опыт персонажа. Диапазон значений 0..10,000,000
    @Column(nullable = false)
    private Integer level;          // Уровень персонажа
    @Column(nullable = false)
    private Integer untilNextLevel; // Остаток опыта до следующего уровня
    @Column(nullable = false)
    private Date birthday;          // Дата регистрации Диапазон значений года 2000..3000 включительно
    @Column(nullable = false)
    private Boolean banned;         // Забанен / не забанен

    public Player() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.isEmpty() || name.length() > 12)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.isEmpty() || title.length() > 30)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public void setRace(String raceString) {
        try {
            Race race = Race.valueOf(raceString);
            setRace(race);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public void setProfession(String professionString) {
        try {
            Profession profession = Profession.valueOf(professionString);
            setProfession(profession);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
        this.level = Math.toIntExact((Math.round(Math.sqrt(2500 + 200 * this.experience)) - 50) / 100);
        this.untilNextLevel = 50 * (this.level + 1) * (this.level + 2) - this.experience;
    }

    public void setExperience(String experienceString) {
        try {
            int experience = Integer.parseInt(experienceString);
            if (experience < 0 || experience > 10_000_000)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            System.out.println(experience);
            setExperience(experience);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthday);
        if (cal.get(Calendar.YEAR) < 2000 || cal.get(Calendar.YEAR) > 3000)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        this.birthday = birthday;
    }

    public void setBirthday(String birthdayString) {
        try {
            setBirthday(new Date(Long.parseLong(birthdayString)));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public void setBanned(String bannedString) {
        Boolean banned;
        try {
            banned = Boolean.valueOf(bannedString);
        } catch (Exception e) {
            banned = false;
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        setBanned(banned);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + birthday +
                ", banned=" + banned +
                '}';
    }
}
