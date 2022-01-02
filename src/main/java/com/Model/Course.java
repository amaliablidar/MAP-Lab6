//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.Model;


import com.Repository.CrudRepo;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Iterator;

public class Course implements CrudRepo<Student> {
    private int id;
    private String title;
    private int credits;
    private int teacherId;
    private int maxStudents;
    private ArrayList<Student> studentsEnrolled = new ArrayList();

    public Course(int id, String title, int credits, int teacherId, int maxStudents, ArrayList<Student> studentsList) {
        this.id = id;
        this.title = title;
        this.credits = credits;
        this.teacherId = teacherId;
        this.maxStudents = maxStudents;
        this.studentsEnrolled = studentsList;
    }

    public Course() {

    }
    public StringProperty nameProperty() {

        return new SimpleStringProperty((String) title);
    }
    public IntegerProperty courseIdProperty() {

        return new SimpleIntegerProperty((Integer) id);
    }
    public IntegerProperty creditsProperty() {

        return new SimpleIntegerProperty((int) credits);
    }
    public boolean compareTo(Course course) {
        return this.getId() == (course.getId());
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return this.credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getTeacher() {
        return this.teacherId;
    }

    public void setTeacher(int teacher) {
        this.teacherId = teacher;
    }

    public int getMaxStudents() {
        return this.maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public ArrayList<Student> getStudentsList() {
        return this.studentsEnrolled;
    }

    public void setStudentsList(ArrayList<Student> studentsList) {
        this.studentsEnrolled = studentsList;
    }

    public Student findOne(int id) {
        Iterator var2 = this.studentsEnrolled.iterator();

        Student student;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            student = (Student)var2.next();
        } while(student.getId()!=id);

        return student;
    }

    public ArrayList<Student> findAll() {
        return this.studentsEnrolled;
    }

    public Student save(Student var1) {
        Iterator iterator = this.studentsEnrolled.iterator();

        while(iterator.hasNext()) {
            Student student = (Student)iterator.next();
            if (student.equals(var1)) {
                return student;
            }
        }

        this.studentsEnrolled.add(var1);
        return null;
    }

    public Student update(Student entity) {
        Iterator var2 = this.studentsEnrolled.iterator();

        Student student;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            student = (Student)var2.next();
        } while(student.getId()!=entity.getId());

        student.setFirstName(entity.getFirstName());
        student.setLastName(entity.getLastName());
        student.setEnrolledCourses(entity.getEnrolledCourses());
        return student;
    }

    public Student delete(int id) {
        Iterator var2 = this.studentsEnrolled.iterator();

        Student student;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            student = (Student)var2.next();
        } while(student.getId()!=(id));

        this.studentsEnrolled.remove(student);
        return student;
    }
}