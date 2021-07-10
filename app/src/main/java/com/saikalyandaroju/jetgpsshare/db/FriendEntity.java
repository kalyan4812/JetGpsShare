package com.saikalyandaroju.jetgpsshare.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "friends")
public class FriendEntity {
    private int id;
    @ColumnInfo(name = "friendname")
    private String name;
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "friendphone")
    private String phone;

    public FriendEntity() {

    }

    public FriendEntity(String name, String phone) {

        this.name = name;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
