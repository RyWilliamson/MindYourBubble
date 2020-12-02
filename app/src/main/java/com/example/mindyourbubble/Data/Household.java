package com.example.mindyourbubble.Data;

import java.io.Serializable;
import java.util.List;

public class Household implements Serializable {
    private List<String> peopleInHouse;

    public List<String> getPeopleInHouse() {
        return peopleInHouse;
    }

    @Override
    public String toString() {
        return "Household{" +
                "peopleInHouse=" + peopleInHouse +
                '}';
    }

    public Household addToHousehold( String person ) {
        this.peopleInHouse.add(person);
        return this;
    }

    public void removeFromHousehold( String person ) {
        this.peopleInHouse.remove(person);
    }
}
