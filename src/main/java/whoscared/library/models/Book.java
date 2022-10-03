package whoscared.library.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Book {
    private int id;
    private int idPerson;
    @NotEmpty(message = "Name of book should not be empty!")
    @Size(min = 1, max = 30)
    private String name;
    @NotEmpty(message = "Author name should not be empty")
    @Size(min = 1, max = 30)
    @Pattern(regexp = "[A-Z]\\w+ [A-Z].([A-Z].|)",
    message = "The author's name must contain the last name and initials with a dot")
    private String author;
    @Min(value = 1000, message = "Year should not be < 1000")
    private int year;

    public Book(int id, String name, String author, int year) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public Book(){}
    public int getId() {
        return id;
    }
    public int getIdPerson() {
        return idPerson;
    }
    public String getName() {
        return name;
    }
    public String getAuthor() {
        return author;
    }
    public int getYear() {
        return year;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setYear(int year) {
        this.year = year;
    }
}
