package com.Model;


public class Person {
    private String firstName;
    private String lastName;
    private int ID;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(){}


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPersonalId() {
        return ID;
    }

    public void setPersonalId(int ID) {
        this.ID = ID;
    }

}