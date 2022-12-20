package se.lexicon.model;

import java.time.LocalDate;

public class Person {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate regDate;
    private boolean active;

    // full constructor


    public Person(Integer id, String firstName, String lastName, LocalDate regDate, boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.regDate = regDate;
        this.active = active;
    }

    //constructor without id
    public Person(String firstName, String lastName, LocalDate regDate, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.regDate = regDate;
        this.active = active;
    }


    //getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    //toString()

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", regDate=" + regDate +
                ", active=" + active +
                '}';
    }
}
