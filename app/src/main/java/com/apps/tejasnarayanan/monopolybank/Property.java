package com.apps.tejasnarayanan.monopolybank;

/**
 * Created by Tejas Narayanan on 6/13/17.
 */

public class Property {
    String name;
    int numOfHouses;

    public Property(String name, int numOfHouses) {
        this.name = name;
        this.numOfHouses = numOfHouses;
    }

    public void addHouses(int h) {
        numOfHouses += h;
        numOfHouses = numOfHouses <= 5 ? numOfHouses : 5;
    }

    @Override
    public String toString() {
        switch (numOfHouses) {
            case 5:
                return name + " (Hotel)";
            case 1:
                return name + " (1 house)";
            case 0:
                return name + " (Rent)";
            default:
                return name + " (" + String.valueOf(numOfHouses) + " houses)";
        }
    }
}
