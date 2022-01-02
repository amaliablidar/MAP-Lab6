package com.Model;

import java.util.List;
import java.util.Objects;


public class Teacher extends Person {
    private int teacherId;
    private List<Course>courses;

    public Teacher (int teacherID,String firstName,String lastName,int personId,List<Course>courses)
    {
        this.teacherId = teacherID;
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setPersonalId(personId);
        this.courses=courses;
    }
    public Teacher (){};

    public int getId() {
        return teacherId;
    }

    public void setId(int teacherId) {
        this.teacherId = teacherId;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}