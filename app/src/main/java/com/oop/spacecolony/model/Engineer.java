package com.oop.spacecolony.model;

import com.oop.spacecolony.R;

public class Engineer extends Crew {
    public Engineer(int id, String name, String gender) {
        super(id, name, "Engineer", gender, 80, 11);
    }

    @Override
    public int attack() {
        return skill + 1;
    }

    @Override
    public int specialAbility() {
        heal(8);
        return skill + 4;
    }

    @Override
    public int getImageResId() {
        return gender.equalsIgnoreCase("Male") ? R.drawable.engineer_m : R.drawable.engineer_f;
    }
}