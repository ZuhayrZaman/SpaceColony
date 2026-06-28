package com.oop.spacecolony.model;

import com.oop.spacecolony.R;

public class Scientist extends Crew {
    public Scientist(int id, String name, String gender) {
        super(id, name, "Scientist", gender, 70, 14);
    }

    @Override
    public int attack() {
        return skill + 5;
    }

    @Override
    public int specialAbility() {
        return skill + 10;
    }

    @Override
    public int getImageResId() {
        return gender.equalsIgnoreCase("Male") ? R.drawable.scientist_m : R.drawable.scientist_f;
    }
}