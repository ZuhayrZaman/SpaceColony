package com.oop.spacecolony.model;

import com.oop.spacecolony.R;
import java.io.Serializable;
import java.util.Random;

public class Alien implements Serializable {
    private String name;
    private int energy;
    private int maxEnergy;
    private int damage;
    private int imageResId;

    public Alien(String name, int maxEnergy, int damage, int imageResId) {
        this.name = name;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.damage = damage;
        this.imageResId = imageResId;
    }

    public static Alien generateAlien(int floor) {
        int hp = 40 + (floor * 10);
        int dmg = 5 + (floor * 2);
        
        int[] images = {
            R.drawable.alien_01,
            R.drawable.alien_02,
            R.drawable.alien_03,
            R.drawable.alien_04
        };
        int randomImage = images[new Random().nextInt(images.length)];
        
        return new Alien("Alien Specimen " + floor, hp, dmg, randomImage);
    }

    public int attack() {
        return damage + new Random().nextInt(4);
    }

    public void takeDamage(int dmg) {
        energy -= dmg;
        if (energy < 0) energy = 0;
    }

    public boolean isAlive() {
        return energy > 0;
    }

    public String getName() { return name; }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
    public int getImageResId() { return imageResId; }
}