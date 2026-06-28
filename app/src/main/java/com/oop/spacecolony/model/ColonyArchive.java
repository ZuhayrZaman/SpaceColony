package com.oop.spacecolony.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColonyArchive implements Serializable {
    private HashMap<Integer, Crew> crewMap = new HashMap<>();

    public void addCrew(Crew crew) {
        crewMap.put(crew.getId(), crew);
    }

    public Crew getCrew(int id) {
        return crewMap.get(id);
    }

    public List<Crew> getAllCrew() {
        return new ArrayList<>(crewMap.values());
    }

    public List<Crew> getAvailableCrew() {
        List<Crew> available = new ArrayList<>();
        for (Crew crew : crewMap.values()) {
            if (!crew.isInMedbay()) {
                available.add(crew);
            }
        }
        return available;
    }

    public List<Crew> getMedbayCrew() {
        List<Crew> medbay = new ArrayList<>();
        for (Crew crew : crewMap.values()) {
            if (crew.isInMedbay()) {
                medbay.add(crew);
            }
        }
        return medbay;
    }

    public void restoreAllAvailableCrewEnergy() {
        for (Crew crew : crewMap.values()) {
            if (!crew.isInMedbay()) {
                crew.restoreFullEnergy();
            }
        }
    }

    public void releaseAllFromMedbay() {
        for (Crew crew : crewMap.values()) {
            if (crew.isInMedbay()) {
                crew.releaseFromMedbay();
            }
        }
    }

    public void progressMedbayRecoveryForAll() {
        for (Crew crew : crewMap.values()) {
            crew.progressMedbayRecovery();
        }
    }

    public int size() {
        return crewMap.size();
    }

    public boolean isEmpty() {
        return crewMap.isEmpty();
    }
}