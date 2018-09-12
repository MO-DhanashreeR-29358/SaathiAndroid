package com.mosl.tabapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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
