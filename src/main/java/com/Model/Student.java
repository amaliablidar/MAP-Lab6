package com.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Student extends Person {
    private int studentId;
    private int totalCredits;
    private ArrayList<Course>enrolledCourses;


    public Student (int studentId,String firstname,String name ,int personalId,int totalCredits,ArrayList<Course>enrolledCourses)
    {
        this.studentId=studentId;
        super.setLastName(name);
        super.setFirstName(firstname);
        super.setPersonalId(personalId);
        this.totalCredits=totalCredits;
        this.enrolledCourses=enrolledCourses;
    }

    public Student (){};

    public void addCourse(Course newCourse)
    {
        enrolledCourses.add(newCourse);
        int newTotalCredits=totalCredits+newCourse.getCredits();
        setTotalCredits(newTotalCredits);
    }

    public void delete(Course course)
    {
        ArrayList<Course>newCourseList=new ArrayList<>();
        for (Course course1:enrolledCourses)
        {
            if (course.getId()!=course1.getId())
            {
                newCourseList.add(course1);
            }
        }
        setEnrolledCourses(newCourseList);
    }

    public boolean compareTo( Student s2){
        if(this.getId()!=s2.getId()){
            return false;
        }
        if(this.getFirstName()!= s2.getFirstName())
            return false;
        if(this.getLastName()!=s2.getLastName())
            return false;
        if(this.getPersonalId()!=s2.getPersonalId())
            return false;
        if(this.totalCredits!=s2.getTotalCredits())
            return false;
        for(int i=0;i<enrolledCourses.size()-1;i++)
            if(this.enrolledCourses.get(i)!=s2.enrolledCourses.get(i))
                return false;

            return true;

    }
    public int getId() {
        return studentId;
    }

    public StringProperty lastNameProperty() {

        return new SimpleStringProperty((String) super.getLastName());
    }
    public StringProperty firstNameProperty() {

        return new SimpleStringProperty((String) super.getFirstName());
    }
    public IntegerProperty studIdProperty() {

        return new SimpleIntegerProperty((int) getId());
    }
    public IntegerProperty creditsProperty() {

        return new SimpleIntegerProperty((int) totalCredits);
    }


    public void setId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalCredits, enrolledCourses);
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(ArrayList<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }


}