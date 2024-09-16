package com.example.flightmanager.model;

import androidx.annotation.NonNull;

// naming convention: PascalCase
// reason: class names should be in PascalCase to match the database table names and retrieveing data from the database snapshot
public class Location {
    private int Id;
    private String Name;

    public Location() {}

    public int getId() { return Id; }
    public void setId(int Id) { this.Id = Id; }

    public String getName() { return Name; }
    public void setName(String Name) { this.Name = Name; }

    @NonNull
    @Override
    public String toString() { return Name; }
}
