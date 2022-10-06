package whoscared.library.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id_person")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Full name should not be empty!")
    @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+ [A-Z]\\w+",
            message = "You need to enter the full name according to the example:" +
                    "Senatorova Sofya Stanislavovna")
    @Column(name = "fullname")
    private String fullName;
    @Min(value = 1900, message = "Year of birth should not < 1900")
    @Column(name = "year")
    private int year;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(int id, String fullName, int yearOfBirth) {
        this.id = id;
        this.fullName = fullName;
        this.year = yearOfBirth;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getYear() {
        return year;
    }

    public int getAge() {
        return (2022 - this.year);
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(Book book) {
        if (this.books == null) {
            this.books = new ArrayList<>();
        }
        this.books.add(book);
        book.setOwner(this);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
