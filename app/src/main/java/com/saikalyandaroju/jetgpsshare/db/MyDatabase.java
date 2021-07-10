package com.saikalyandaroju.jetgpsshare.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FriendEntity.class},version = 2)
public abstract class MyDatabase extends RoomDatabase {
    public abstract MyDao myDao();
}
