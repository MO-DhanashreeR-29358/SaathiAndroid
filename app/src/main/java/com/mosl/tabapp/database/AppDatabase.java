package com.mosl.tabapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { LocationData.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
