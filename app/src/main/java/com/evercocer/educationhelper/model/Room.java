package com.evercocer.educationhelper.model;

public class Room {
    private String classRoom;
    private String number;
    private String volume;
    private String campus;

    @Override
    public String toString() {
        return "Room{" +
                "classRoom='" + classRoom + '\'' +
                ", number='" + number + '\'' +
                ", volume='" + volume + '\'' +
                ", campus='" + campus + '\'' +
                '}';
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }
}
