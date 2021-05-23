package com.game.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name", length = 12, nullable = false)
    public String name;

    @Column(name = "title", length = 30, nullable = false)
    public String title;

    @Column(name = "race", nullable = false)
    @Enumerated(EnumType.STRING)
    public Race race;

    @Column(name = "profession", nullable = false)
    @Enumerated(EnumType.STRING)
    public Profession profession;

    @Column(name = "experience", nullable = false)
    public Integer experience;

    @Column(name = "level")
    public Integer level;

    @Column(name = "untilNextLevel")
    public Integer untilNextLevel;

    @Column(name = "birthday", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat
    public Date birthday;

    @Column(name = "banned")
    public Boolean banned;

    public Player() {
    }

    public Player(String name, String title, Race race, Profession profession, Integer experience, Date birthday, Boolean banned) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.birthday = birthday;
        if (banned == null) {
            this.banned = false;
        } else {
            this.banned = banned;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @PrePersist
    public void prePersistUpdate() {
        this.level = Math.toIntExact((long) ((Math.sqrt(2500 + 200 * this.experience) - 50) / 100));
        this.untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
    }

    public void update(Player player) {
        if (player.getName() != null) {
            this.setName(player.getName());
        }
        if (player.getTitle() != null) {
            this.setTitle(player.getTitle());
        }
        if (player.getExperience() != null) {
            this.setExperience(player.getExperience());
        }
        if (player.getBanned() != null) {
            this.setBanned(player.getBanned());
        }
        if (player.getBirthday() != null) {
            this.setBirthday(player.getBirthday());
        }
        if (player.getProfession() != null) {
            this.setProfession(player.getProfession());
        }
        if (player.getRace() != null) {
            this.setRace(player.getRace());
        }
        if (player.getLevel() != null) {
            this.setLevel(player.getLevel());
        }
        if (player.getUntilNextLevel() != null) {
            this.setUntilNextLevel(player.getUntilNextLevel());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) && Objects.equals(name, player.name) && Objects.equals(title, player.title) && race == player.race && profession == player.profession && Objects.equals(experience, player.experience) && Objects.equals(level, player.level) && Objects.equals(untilNextLevel, player.untilNextLevel) && Objects.equals(birthday, player.birthday) && Objects.equals(banned, player.banned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, race, profession, experience, level, untilNextLevel, birthday, banned);
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
