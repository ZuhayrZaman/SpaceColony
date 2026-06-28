package com.oop.spacecolony.model;

import com.oop.spacecolony.R;

public class Pilot extends Crew {
    public Pilot(int id, String name, String gender) {
        super(id, name, "Pilot", gender, 90, 10);
    }

    @Override
    public int attack() {
        return skill + 3;
    }

    @Override
    public int specialAbility() {
        return skill + 6;
    }

    @Override
    public int getImageResId() {
        return gender.equalsIgnoreCase("Male") ? R.drawable.pilot_m : R.drawable.pilot_f;
    }
}