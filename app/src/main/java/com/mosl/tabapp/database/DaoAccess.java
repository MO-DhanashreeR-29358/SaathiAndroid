package com.mosl.tabapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoAccess {
    @Insert
    void insertLocationList(LocationData locationDataList);

    @Query("SELECT * FROM locationdata")
    List<LocationData> getLocationList();

    @Query("DELETE FROM locationdata")
    void deleteAll();
}
