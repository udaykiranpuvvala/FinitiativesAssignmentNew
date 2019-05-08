package com.uday.finitiativesassignment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.uday.finitiativesassignment.entities.CoinsEntity;
import com.uday.finitiativesassignment.entities.EventsEntity;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DaoAccess {
    @Insert
    void insertCoins(List<CoinsEntity> coinsEntityList);

    @Query("SELECT * FROM coins")
    List<CoinsEntity> fetchAllCoinsData();

    @Query("DELETE FROM coins")
    public void deleteCoinsData();

    @Insert
    void insertEvents(List<EventsEntity> eventsEntityList);

    @Query("SELECT * FROM events")
    List<EventsEntity> fetchAllEventsData();

    @Query("DELETE FROM events")
    public void deleteEventsData();
}
