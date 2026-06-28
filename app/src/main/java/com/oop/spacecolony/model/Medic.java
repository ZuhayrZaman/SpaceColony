package com.oop.spacecolony.model;

import com.oop.spacecolony.R;

public class Medic extends Crew {
    public Medic(int id, String name, String gender) {
        super(id, name, "Medic", gender, 100, 7);
    }

    @Override
    public int attack() {
        heal(5);
        return skill;
    }

    @Override
    public int specialAbility() {
        heal(20);
        return 0;
    }

    @Override
    public int getImageResId() {
        return gender.equalsIgnoreCase("Male") ? R.drawable.medic_m : R.drawable.medic_f;
    }
}