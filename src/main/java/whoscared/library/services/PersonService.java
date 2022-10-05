package whoscared.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whoscared.library.models.Book;
import whoscared.library.models.Person;
import whoscared.library.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
//readOnly for all methods without Annotation @Transaction
//Annotation for a particular method has higher precedence
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll () {
        return personRepository.findAll();
    }

    public Person findOne(int id) {
        return personRepository.findById(id).orElse(null);
        //TODO:Exception if person not found
    }

    @Transactional
    public void save(Person person){
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person){
        //can set id in html form
        person.setId(id);
        //if in database had person with the same id
        //update command is executed
        personRepository.save(person);
    }

    @Transactional
    public void delete(int id){
        personRepository.deleteById(id);
    }

    public Person getOwnerByBook(Book book) {
        return personRepository.getPersonByBooksContaining(book);
    }
}
