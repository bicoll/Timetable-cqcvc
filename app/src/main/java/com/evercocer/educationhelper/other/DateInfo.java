package com.evercocer.educationhelper.other;

public class DateInfo {
    private int Year;
    private int Month;
    private int day;

    public DateInfo(int year, int month, int day) {
        Year = year;
        Month = month;
        this.day = day;
    }

    public DateInfo(int month, int day) {
        Month = month;
        this.day = day;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
