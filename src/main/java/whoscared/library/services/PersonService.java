package whoscared.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whoscared.library.models.Book;
import whoscared.library.models.Person;
import whoscared.library.repositories.PersonRepository;

import java.util.List;

@Service
//readOnly for all methods without Annotation @Transaction
//Annotation for a particular method has higher precedence
@Transactional(readOnly = true)
//transactions start and end in service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findOne(int id) {
        return personRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        //can set id in html form
        person.setId(id);
        //if in database had person with the same id
        //update command is executed
        personRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    public Person getOwnerByBook(Book book) {
        return personRepository.getPersonByBooksContaining(book);
    }
    public List<Book> getBookByPersonId(int id){
        Person person = personRepository.getOne(id);
        // lazy loading -> initialize list independently
        Hibernate.initialize(person.getBooks());
        return person.getBooks();
    }
}
