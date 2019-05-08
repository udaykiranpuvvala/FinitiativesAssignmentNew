package com.uday.finitiativesassignment.utilites;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.uday.finitiativesassignment.dao.DaoAccess;
import com.uday.finitiativesassignment.entities.CoinsEntity;
import com.uday.finitiativesassignment.entities.EventsEntity;

@Database(entities = {CoinsEntity.class, EventsEntity.class}, version = 1)
public abstract class FinitDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();


}
