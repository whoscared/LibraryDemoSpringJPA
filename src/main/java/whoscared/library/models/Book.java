package whoscared.library.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;


@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id_book")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Name of book should not be empty!")
    @Size(min = 1, max = 30)
    @Column(name = "name")
    private String name;
    @NotEmpty(message = "Author name should not be empty")
    @Size(min = 1, max = 30)
    @Pattern(regexp = "[A-Z]\\w+ [A-Z].([A-Z].|)",
    message = "The author's name must contain the last name and initials with a dot")
    @Column(name = "author")
    private String author;
    @Min(value = 1000, message = "Year should not be < 1000")
    @Column(name = "year")
    private int year;

    @Column(name = "time")
    private Date time;

    @Transient
    private boolean arrears;

    //owning side
    @ManyToOne
    @JoinColumn(name = "id_person", referencedColumnName = "id_person")
    private Person owner;

    public Book(int id, String name, String author, int year) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
        this.time = new Date();
    }

    public Book(){}
    public int getId() {
        return id;
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
    public Person getOwner() {
        return owner;
    }
    public void setId(int id) {
        this.id = id;
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

    public void setOwner(Person owner){
        this.owner = owner;
    }

    public void release(){
        this.owner.getBooks().remove(this);
        this.owner = null;
    }

    public boolean getArrears(){
        Date now = new Date();
        long difference = now.getTime() - this.time.getTime();
        int days =  (int)(difference / (24 * 60 * 60 * 1000));
        return days > 10;
    }
}
