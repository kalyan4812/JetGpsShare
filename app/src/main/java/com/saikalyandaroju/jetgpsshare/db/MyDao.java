package com.saikalyandaroju.jetgpsshare.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface MyDao {
    @Insert
    public void addFriend(FriendEntity friendEntity);
    @Query("SELECT * FROM friends")
    List<FriendEntity> getFriends();
    @Delete
    public void deleteFriend(FriendEntity friendEntity);


}
