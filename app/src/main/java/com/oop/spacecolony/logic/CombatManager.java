package com.oop.spacecolony.logic;

import com.oop.spacecolony.model.Crew;
import com.oop.spacecolony.model.Alien;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatManager {

    private Crew lead;
    private Crew reserve1;
    private Crew reserve2;
    private Alien alien;
    private final Random random = new Random();
    private final List<Crew> defeatedCrew = new ArrayList<>();

    private MissionType missionType;

    public CombatManager(Crew lead, Crew reserve1, Crew reserve2, int floor, MissionType missionType) {
        this.lead = lead;
        this.reserve1 = reserve1;
        this.reserve2 = reserve2;
        this.alien = Alien.generateAlien(floor);
        this.missionType = missionType;
    }

    public String attack() {
        if (lead == null) return "No lead crew.";
        int bonus = missionType.getSkillBonus(lead);
        int dmg = lead.attack() + bonus + random.nextInt(4);
        alien.takeDamage(dmg);
        return lead.getName() + " dealt " + dmg + " damage.";
    }

    public String defend() {
        if (lead == null) return "No lead crew.";
        lead.defend();
        return lead.getName() + " is defending.";
    }

    public String useSpecial() {
        if (lead == null) return "No lead crew.";
        int bonus = missionType.getSkillBonus(lead);
        int dmg = lead.specialAbility() + bonus;
        if (dmg > 0) {
            alien.takeDamage(dmg);
            return lead.getName() + " used special ability for " + dmg + " damage.";
        }
        return lead.getName() + " used a support special ability.";
    }

    public String swapToReserve1() {
        if (reserve1 == null) return "No first reserve available.";
        Crew temp = lead;
        lead = reserve1;
        reserve1 = temp;
        return "Swapped with reserve 1.";
    }

    public String swapToReserve2() {
        if (reserve2 == null) return "No second reserve available.";
        Crew temp = lead;
        lead = reserve2;
        reserve2 = temp;
        return "Swapped with reserve 2.";
    }

    public String enemyTurn() {
        if (lead == null || !alien.isAlive()) return "No enemy action.";
        int damage = alien.attack();
        if (lead.getEnergy() < lead.getMaxEnergy() / 3) {
            damage += 3;
        }
        lead.takeDamage(damage);
        return alien.getName() + " attacks " + lead.getName() + " for " + damage + " damage.";
    }

    public String checkDeaths() {
        StringBuilder log = new StringBuilder();

        if (lead != null && !lead.isAlive()) {
            defeatedCrew.add(lead);
            log.append(lead.getName()).append(" has fallen.\n");
            lead = getNextAvailableReserve();
            if (lead != null) {
                log.append(lead.getName()).append(" becomes the new Lead.\n");
            }
        }

        if (reserve1 != null && !reserve1.isAlive()) {
            defeatedCrew.add(reserve1);
            log.append(reserve1.getName()).append(" has fallen.\n");
            reserve1 = null;
        }

        if (reserve2 != null && !reserve2.isAlive()) {
            defeatedCrew.add(reserve2);
            log.append(reserve2.getName()).append(" has fallen.\n");
            reserve2 = null;
        }

        return log.toString();
    }

    private Crew getNextAvailableReserve() {
        if (reserve1 != null && reserve1.isAlive()) {
            Crew next = reserve1;
            reserve1 = reserve2;
            reserve2 = null;
            return next;
        }
        if (reserve2 != null && reserve2.isAlive()) {
            Crew next = reserve2;
            reserve2 = null;
            return next;
        }
        return null;
    }

    public boolean isVictory() {
        return !alien.isAlive();
    }

    public boolean isGameOver() {
        return lead == null && reserve1 == null && reserve2 == null;
    }

    public Crew getLead() { return lead; }
    public Crew getReserve1() { return reserve1; }
    public Crew getReserve2() { return reserve2; }
    public Alien getAlien() { return alien; }
    public List<Crew> getDefeatedCrew() { return defeatedCrew; }

    public void clearDefeatedCrew() {
        defeatedCrew.clear();
    }

    public List<Crew> getAllCrew() {
        List<Crew> list = new ArrayList<>();
        if (lead != null) list.add(lead);
        if (reserve1 != null) list.add(reserve1);
        if (reserve2 != null) list.add(reserve2);
        return list;
    }
}