package com.uday.finitiativesassignment.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "events")
public class EventsEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int eventsId;


    @NonNull
    private String type, title, description, organizer, start_date, end_date,
            website, email, venue, address, city, country, screenshot;

    @NonNull
    public int getEventsId() {
        return eventsId;
    }

    public void setEventsId(@NonNull int eventsId) {
        this.eventsId = eventsId;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(@NonNull String organizer) {
        this.organizer = organizer;
    }

    @NonNull
    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(@NonNull String start_date) {
        this.start_date = start_date;
    }

    @NonNull
    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(@NonNull String end_date) {
        this.end_date = end_date;
    }

    @NonNull
    public String getWebsite() {
        return website;
    }

    public void setWebsite(@NonNull String website) {
        this.website = website;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getVenue() {
        return venue;
    }

    public void setVenue(@NonNull String venue) {
        this.venue = venue;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    @NonNull
    public String getCountry() {
        return country;
    }

    public void setCountry(@NonNull String country) {
        this.country = country;
    }

    @NonNull
    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(@NonNull String screenshot) {
        this.screenshot = screenshot;
    }

}
