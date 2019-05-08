package com.uday.finitiativesassignment.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "coins")
public class CoinsEntity {

    @PrimaryKey
    @NonNull
    private String coinId;

    @NonNull
    private String coinName;

    @NonNull
    private String coinSymbol;


    @NonNull
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(@NonNull String coinId) {
        this.coinId = coinId;
    }

    @NonNull
    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(@NonNull String coinName) {
        this.coinName = coinName;
    }

    @NonNull
    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(@NonNull String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }
}
