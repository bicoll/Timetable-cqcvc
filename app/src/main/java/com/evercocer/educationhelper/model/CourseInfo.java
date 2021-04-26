package com.evercocer.educationhelper.model;

public class CourseInfo {
    //课程名称
    private String course;
    //授课教师
    private String teacher;
    //授课地点
    private String classRoom;
    //时间信息：第几节
    private int[] chapterInfo;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int[] getChapterInfo() {
        return chapterInfo;
    }

    public void setChapterInfo(int[] chapterInfo) {
        this.chapterInfo = chapterInfo;
    }
}
