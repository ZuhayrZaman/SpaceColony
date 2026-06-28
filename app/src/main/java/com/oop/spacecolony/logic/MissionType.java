package com.oop.spacecolony.logic;

import com.oop.spacecolony.model.Crew;

public enum MissionType {
    COMBAT,
    REPAIR_STATION,
    MEDICAL_OUTPOST,
    RESEARCH_LAB;

    public int getSkillBonus(Crew crew) {
        String type = crew.getCrewClass();

        switch (this) {
            case COMBAT:
                if (type.equals("Soldier") || type.equals("Pilot")) return 2;
                break;

            case REPAIR_STATION:
                if (type.equals("Engineer") || type.equals("Pilot")) return 2;
                break;

            case MEDICAL_OUTPOST:
                if (type.equals("Medic") || type.equals("Scientist")) return 2;
                break;

            case RESEARCH_LAB:
                if (type.equals("Scientist") || type.equals("Engineer")) return 2;
                break;
        }

        return 0;
    }

    public String getBonusDescription() {
        switch (this) {
            case COMBAT: return "Bonus: Soldier, Pilot";
            case REPAIR_STATION: return "Bonus: Engineer, Pilot";
            case MEDICAL_OUTPOST: return "Bonus: Medic, Scientist";
            case RESEARCH_LAB: return "Bonus: Scientist, Engineer";
            default: return "No specific class bonuses";
        }
    }
}