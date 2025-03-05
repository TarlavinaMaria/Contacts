package com.example.contacts;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
