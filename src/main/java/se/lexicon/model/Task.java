package se.lexicon.model;

public class Task {

    // in SQL - task is a table, but in Java we can define it as a class with fields

    private Integer id;
    private String title;
    private String description;
    private Person person;
    // in SQL - we have only person_id, but in Java we can define directly an object (class) Person


    //constructor without task id
    public Task(String title, String description, Person person) {
        this.title = title;
        this.description = description;
        this.person = person;
    }
    // another constructor in order to introduce a task without assigned Person
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // constructor with all fields
    public Task(Integer id, String title, String description, Person person) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.person = person;
    }
    //constructor without person
    public Task(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    //getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    //toString

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", person=" + person +
                '}';
    }
}
