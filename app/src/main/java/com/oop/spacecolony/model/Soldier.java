package com.oop.spacecolony.model;

import com.oop.spacecolony.R;

public class Soldier extends Crew {
    public Soldier(int id, String name, String gender) {
        super(id, name, "Soldier", gender, 120, 8);
    }

    @Override
    public int attack() {
        return skill + 2;
    }

    @Override
    public int specialAbility() {
        heal(10);
        return skill + 1;
    }

    @Override
    public int getImageResId() {
        return gender.equalsIgnoreCase("Male") ? R.drawable.soldier_m : R.drawable.soldier_f;
    }
}