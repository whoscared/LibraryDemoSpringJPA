package whoscared.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whoscared.library.models.Book;
import whoscared.library.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person getPersonByBooksContaining(Book book);

}
